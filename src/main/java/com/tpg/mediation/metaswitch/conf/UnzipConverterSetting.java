package com.tpg.mediation.metaswitch.conf;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component(value = "unzipConverterSetting")
@ConfigurationProperties(prefix="converter.unzip")
public class UnzipConverterSetting {
	String backupPath;
	
	String backupSuffix;
	
	String errorPath;
	
	String errorSuffix;
	
	List <UnzipInputSetting> input;

	List <UnzipOutputSetting> output;

	public String getBackupPath() {
		return backupPath;
	}

	public void setBackupPath(String backupPath) {
		this.backupPath = backupPath;
	}

	public String getBackupSuffix() {
		return backupSuffix;
	}

	public void setBackupSuffix(String backupSuffix) {
		this.backupSuffix = backupSuffix;
	}

	public String getErrorPath() {
		return errorPath;
	}

	public void setErrorPath(String errorPath) {
		this.errorPath = errorPath;
	}

	public String getErrorSuffix() {
		return errorSuffix;
	}

	public void setErrorSuffix(String errorSuffix) {
		this.errorSuffix = errorSuffix;
	}

	public List<UnzipInputSetting> getInput() {
		return input;
	}

	public void setInput(List<UnzipInputSetting> input) {
		this.input = input;
	}

	public List<UnzipOutputSetting> getOutput() {
		return output;
	}

	public void setOutput(List<UnzipOutputSetting> output) {
		this.output = output;
	}

	@Override
	public String toString() {
		return "UnzipConverterSetting [backupPath=" + backupPath + ", backupSuffix=" + backupSuffix + ", errorPath="
				+ errorPath + ", errorSuffix=" + errorSuffix + ", input=" + input + ", output=" + output + "]";
	}
	
}
