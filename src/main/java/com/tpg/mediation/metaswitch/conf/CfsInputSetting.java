package com.tpg.mediation.metaswitch.conf;

public class CfsInputSetting {
	private String path;
	private String filter;

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}

	@Override
	public String toString() {
		return "CfsInputSetting [path=" + path + ", filter=" + filter + "]";
	}
	
}
