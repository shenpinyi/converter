package com.tpg.mediation.metaswitch.convertor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tpg.mediation.metaswitch.conf.CfsConverterSetting;
import com.tpg.mediation.metaswitch.conf.CfsOutputSetting;

@Component
public class CfsConverter extends XmlConverter {
	
	Logger logger = LoggerFactory.getLogger(CfsConverter.class);
	
	@Autowired
	@Qualifier("cfsConverterSetting")
	private CfsConverterSetting setting;
	
	@Autowired
	@Qualifier("cfsXmlSaxParser")
	private CfsXmlSaxParser parser;
	
	public CfsConverterSetting getSetting() {
		return setting;
	}

	public void setSetting(CfsConverterSetting setting) {
		this.setting = setting;
	}

	
	@Override
	protected List<File> doGetFiles() {
		List <File> allFiles = new ArrayList<>();
		setting.getInput().forEach(input -> {
			logger.info("Get files from input path, input=" + input.getPath() + 
					", filter=" + input.getFilter());
			allFiles.addAll(getFilesByPathAndFilter(input.getPath(), input.getFilter()));
		});
		return allFiles;
	}
	
	@Override
	protected boolean doCheckFile() {
		if (setting.isXsdCheck()) {
			return super.doCheckFile();
		} else {
			return true;
		}
	}

	@Override
	protected File doGetXsdFile() {
		File f = new File(setting.getXsdPath() + setting.getXsdName());
		logger.info("Finding xsd file: " + f.getAbsolutePath());
		return (!f.exists() || f.isDirectory()) ? null : f;
	}

	@Override
	protected boolean doParseFile() {
		this.currentParsedFile = parser.parse(this.currentInFile);
		if (this.currentParsedFile == null) {
			return false;
		}
		return true;
	}

	@Override
	protected boolean doCreateOutputFile() {
		ParsedFile parsedFile = this.currentParsedFile;
		setting.getOutput().forEach(outputSetting -> {
			StringBuffer buff = createCsvContent((CfsParsedFile) parsedFile, outputSetting);
			if (buff == null) {
				logger.error("Creat CSV content error");
				return;
			}
			String targetName = outputSetting.getPath() + outputSetting.getPrefix() + 
					parsedFile.getOriginalName() + outputSetting.getSuffix();
			writeFile(buff, targetName);
		});
		return true;
	}

	private StringBuffer createCsvContent(CfsParsedFile parsed, CfsOutputSetting outputSetting) {
		
		StringBuffer buff = new StringBuffer();
		
		buff.append("HDR" + getLine(parsed.getHeader(), outputSetting.getHeaders()));
		
		buff.append("RecTyp" + getTitles(outputSetting.getFields()));
		
		parsed.getCalls().forEach(call -> {
			buff.append("CDR" + getLine(call, outputSetting.getFields()));
		});
		

		buff.append("TRL" + getLine(parsed.getFooter(), outputSetting.getFooters()));

		return buff;
	}
	
	private String getLine(Map<String, String> record, List <String> fields) {
		StringBuffer line = new StringBuffer();

		
		if (fields != null) {
			fields.forEach(field -> {
				line.append(",");
				String value = record.get(field);
				line.append(value != null ? value : "");
			});
			line.append("\r\n");
		}
		
		return line.toString();
	}

	private String getTitles(List <String> fields) {
		StringBuffer line = new StringBuffer();

		if (fields != null) {
			fields.forEach(field -> {
				line.append(",");
				String value = setting.getFieldsMap().get(field);
				line.append(value != null ? value : field);
			});
			line.append("\r\n");
		}
		
		return line.toString();
	}
	
	private void writeFile(StringBuffer buff, String targetFileName) {
		try {
			Writer writer = new BufferedWriter(new FileWriter(targetFileName));
			writer.write(buff.toString());
			writer.flush();
			writer.close();
			logger.info(String.format("CSV writen %s", targetFileName));
		} catch (IOException e) {
			logger.error(String.format("CSV writen failed %s because of exception", targetFileName));
			logger.error(e.toString());
		}
	}

	@Override
	protected String getBackupPath() {
		return setting.getBackupPath();
	}

	@Override
	protected String getBackupSuffix() {
		return setting.getBackupSuffix();
	}

	@Override
	protected String getErrorPath() {
		return setting.getErrorPath();
	}

	@Override
	protected String getErrorSuffix() {
		return setting.getErrorSuffix();
	}
}
