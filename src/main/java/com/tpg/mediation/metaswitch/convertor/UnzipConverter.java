package com.tpg.mediation.metaswitch.convertor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tpg.mediation.metaswitch.conf.UnzipConverterSetting;
import com.tpg.mediation.metaswitch.conf.UnzipOutputSetting;

@Component
public class UnzipConverter extends Converter {

	Logger logger = LoggerFactory.getLogger(UnzipConverter.class);

	@Autowired
	@Qualifier("unzipConverterSetting")
	private UnzipConverterSetting setting;

	@Override
	protected List<File> doGetFiles() {
		List<File> allFiles = new ArrayList<>();
		setting.getInput().forEach(input -> {
			logger.info("Get files from input path, input=" + input.getPath() + 
					", filter=" + input.getFilter());
			allFiles.addAll(getFilesByPathAndFilter(input.getPath(), input.getFilter()));
		});
		return allFiles;
	}

	@Override
	protected boolean doCreateOutputFile() {
		boolean result = true;
		for (int i = 0; i < setting.getOutput().size(); i++) {
			UnzipOutputSetting outputSetting = setting.getOutput().get(i);
			String outputPath = outputSetting.getPath();
			String outputFile = outputPath + this.currentInFile.getName().replaceAll("\\.gz$", "");
			String gzipFile = this.currentInFile.getAbsolutePath();
			try {
				decompressGzipFile(gzipFile, outputFile);
			} catch (IOException e) {
				logger.error("DecompressGzipFile error, got an exception. e=" + e.getMessage());
				result = false;
			}
		}
		return result;
	}

	private void decompressGzipFile(String gzipFile, String outputFile) throws IOException {
		byte[] buffer = new byte[1024];
		GZIPInputStream gzis = null;
		FileOutputStream out = null;
		FileInputStream is = null;
		try {
			is = new FileInputStream(gzipFile);
			gzis = new GZIPInputStream(is);
			out = new FileOutputStream(outputFile);
			int len;
			while ((len = gzis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			out.flush();
		} finally {
			if (is != null) {
				is.close();
			}
			if (gzis != null) {
				gzis.close();
			}
			if (out != null) {
				out.close();
			}
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
