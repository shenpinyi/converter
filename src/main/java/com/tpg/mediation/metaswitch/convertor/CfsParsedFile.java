package com.tpg.mediation.metaswitch.convertor;

import java.util.List;
import java.util.Map;

public class CfsParsedFile extends ParsedFile {

	private Map<String, String> header;
	private List <Map<String, String>> calls;
	private Map<String, String> footer;

	
	public Map<String, String> getHeader() {
		return header;
	}
	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

	public List<Map<String, String>> getCalls() {
		return calls;
	}
	public void setCalls(List<Map<String, String>> calls) {
		this.calls = calls;
	}
	public Map<String, String> getFooter() {
		return footer;
	}
	public void setFooter(Map<String, String> footer) {
		this.footer = footer;
	}
	@Override
	public String toString() {
		return "CfsParsedFile [header=" + header + ", calls=" + calls + ", footer=" + footer + ", originalName="
				+ originalName + "]";
	}
}
