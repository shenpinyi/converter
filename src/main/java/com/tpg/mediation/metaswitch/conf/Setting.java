package com.tpg.mediation.metaswitch.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component(value = "setting")
@ConfigurationProperties(prefix="my")
public class Setting {
	String jdk;

	public String getJdk() {
		return jdk;
	}

	public void setJdk(String jdk) {
		this.jdk = jdk;
	}

	@Override
	public String toString() {
		return "Setting [jdk=" + jdk + "]";
	}
}

