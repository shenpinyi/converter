package com.tpg.mediation.metaswitch.conf;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

public class CfsOutputSetting {
	private String path;
	private String prefix;
	private String suffix;
	
	@Value("#{'${fields}'.split(',')}")
	private List <String> fields;

	@Value("#{'${headers}'.split(',')}")
	private List <String> headers;

	@Value("#{'${footers}'.split(',')}")
	private List <String> footers;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public List<String> getFooters() {
		return footers;
	}

	public void setFooters(List<String> footers) {
		this.footers = footers;
	}

	@Override
	public String toString() {
		return "CfsOutputSetting [path=" + path + ", prefix=" + prefix + ", suffix=" + suffix + ", fields=" + fields
				+ ", headers=" + headers + ", footers=" + footers + "]";
	}
}
