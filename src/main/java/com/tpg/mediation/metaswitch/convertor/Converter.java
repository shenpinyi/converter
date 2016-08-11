package com.tpg.mediation.metaswitch.convertor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class Converter {
	
	Logger logger = LoggerFactory.getLogger(Converter.class);

	protected ParsedFile currentParsedFile;
	protected File currentInFile;
	
	@Autowired
	@Qualifier("converterContext")
	protected ConverterContext context;
	
	public final void convert() {
		
		if (!doBefore()) {
			logger.error("precheck error");
		}
		
		List <File> inFiles = doGetFiles();
		
		if (inFiles == null || inFiles.size() == 0) {
			logger.info("No input file found in directory");
			return;
		}
		
		try {
			TimeUnit.SECONDS.sleep(3); // wait, in case the file is not transferred complete
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		inFiles.forEach(inFile -> {
			logger.info("Now process file=" + inFile.getAbsolutePath());
			this.currentInFile = inFile;
			this.currentParsedFile = null;
			
			if (!doCheckFile()) {
				logger.error("Input file checked error, inFile=" + inFile.getAbsolutePath());
				context.getFailFiles().add(inFile);
				return;
			}
			
			if (!doParseFile()) {
				logger.error("Input file parsed error, inFile=" + inFile.getAbsolutePath());
				context.getFailFiles().add(inFile);
				return;
			}
			
			if (!doCreateOutputFile()) {
				logger.error("Output file created error, inFile=" + inFile.getAbsolutePath());
				context.getFailFiles().add(inFile);
				return;
			}
			
			context.getSuccFiles().add(inFile);
		});

		doBackupFiles();
		
		doAfter();
	}
	
	protected boolean doBefore() {
		
		return true;
	}
	
	protected abstract List <File> doGetFiles();
	
	protected List<File> getFilesByPathAndFilter(String path, String filter) {
		List <File> allFiles = new ArrayList<>();
		File[] fileArray = new File(path).listFiles(f -> {
    		return f.getName().matches(filter) && f.isFile();
        });
		
		if (fileArray != null) {
			List <File> files = Arrays.asList(fileArray);
			allFiles.addAll(files);
		}
		return allFiles;
	}

	protected boolean doCheckFile() {
		return true;
	}
	
	protected boolean doParseFile() {
		return true;
	}
	
	protected abstract boolean doCreateOutputFile();
	
	protected void doBackupFiles() {

		this.context.getSuccFiles().forEach(f -> {
			
			File sourceFile = new File(f.getAbsolutePath());
			
			File targetFile = new File(getBackupPath() + 
					f.getName() + getBackupSuffix());
			
			if (targetFile.exists()) {
				targetFile = new File(getBackupPath() + 
						f.getName() + "_" +
						new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) +
						getBackupSuffix());
			}
			
			if (sourceFile.renameTo(targetFile)) {
				logger.info("Backup, sourcefile=" + f.getAbsolutePath() + ", targetFile=" + targetFile.getAbsolutePath());
			} else {
				logger.error("Failed backup, sourcefile=" + f.getAbsolutePath() + ", targetFile=" + targetFile.getAbsolutePath());
			}
		});
		
		this.context.getFailFiles().forEach(f -> {
			File sourceFile = new File(f.getAbsolutePath());

			File targetFile = new File(getErrorPath() + 
					f.getName() + getErrorSuffix());
			
			if (targetFile.exists()) {
				targetFile = new File(getErrorPath() + 
						f.getName() + "_" +
						new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) +
						getErrorSuffix());
			}
			if (sourceFile.renameTo(targetFile)) {
				logger.info("Backup to error folder, sourcefile=" + f.getAbsolutePath() + ", targetFile=" + targetFile.getAbsolutePath());
			} else {
				logger.error("Failed backup to error folder, sourcefile=" + f.getAbsolutePath() + ", targetFile=" + targetFile.getAbsolutePath());
			}
		});
	}
	
	protected abstract String getBackupPath();
	protected abstract String getBackupSuffix();
	protected abstract String getErrorPath();
	protected abstract String getErrorSuffix();
	protected boolean doAfter() {
		return true;
	}

}
