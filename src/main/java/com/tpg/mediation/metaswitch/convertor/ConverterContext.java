package com.tpg.mediation.metaswitch.convertor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ConverterContext {


	private List <File> succFiles;
	private List <File> failFiles;
	
	
	public ConverterContext() {
		this.succFiles = new ArrayList<>();
		this.failFiles = new ArrayList<>();
	}
	public List<File> getSuccFiles() {
		return succFiles;
	}
	public void setSuccFiles(List<File> succFiles) {
		this.succFiles = succFiles;
	}
	public List<File> getFailFiles() {
		return failFiles;
	}
	public void setFailFiles(List<File> failFiles) {
		this.failFiles = failFiles;
	}
	@Override
	public String toString() {
		return "ConverterContext [succFiles=" + succFiles + ", failFiles=" + failFiles + "]";
	}
}
