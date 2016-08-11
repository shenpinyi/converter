package com.tpg.mediation.metaswitch.conf;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component(value = "cfsConverterSetting")
@ConfigurationProperties(prefix="converter.cfs")
public class CfsConverterSetting {

	boolean xsdCheck;
	
	String xsdPath;
	
	String xsdName;
	
	String backupPath;
	
	String backupSuffix;
	
	String errorPath;
	
	String errorSuffix;
	
	List <CfsInputSetting> input;

	List <CfsOutputSetting> output;

	Map<String, String> fieldsMap;

	
	public boolean isXsdCheck() {
		return xsdCheck;
	}

	public void setXsdCheck(boolean xsdCheck) {
		this.xsdCheck = xsdCheck;
	}

	public String getXsdPath() {
		return xsdPath;
	}

	public void setXsdPath(String xsdPath) {
		this.xsdPath = xsdPath;
	}

	public String getXsdName() {
		return xsdName;
	}

	public void setXsdName(String xsdName) {
		this.xsdName = xsdName;
	}

	public List<CfsInputSetting> getInput() {
		return input;
	}

	public void setInput(List<CfsInputSetting> input) {
		this.input = input;
	}

	public List<CfsOutputSetting> getOutput() {
		return output;
	}

	public void setOutput(List<CfsOutputSetting> output) {
		this.output = output;
	}

	public Map<String, String> getFieldsMap() {
		return fieldsMap;
	}

	public void setFieldsMap(Map<String, String> fieldsMap) {
		this.fieldsMap = fieldsMap;
	}

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

	@Override
	public String toString() {
		return "CfsConverterSetting [xsdCheck=" + xsdCheck + ", xsdPath=" + xsdPath + ", xsdName=" + xsdName
				+ ", backupPath=" + backupPath + ", backupSuffix=" + backupSuffix + ", errorPath=" + errorPath
				+ ", errorSuffix=" + errorSuffix + ", input=" + input + ", output=" + output + ", fieldsMap="
				+ fieldsMap + "]";
	}
	
}
