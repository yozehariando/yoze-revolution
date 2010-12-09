package com.github.twitterapiapp;

public class AppSetting {

	private String url = "http://search.twitter.com/search.json?q=";

	public AppSetting(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
