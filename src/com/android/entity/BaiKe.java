package com.android.entity;

import java.io.Serializable;

public class BaiKe implements Serializable{

	private static final long serialVersionUID = 1L;

	private String key = "";
	private String wapUrl = "";
	private String copyrights = "";
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getWapUrl() {
		return wapUrl;
	}
	public void setWapUrl(String wapUrl) {
		this.wapUrl = wapUrl;
	}
	public String getCopyrights() {
		return copyrights;
	}
	public void setCopyrights(String copyrights) {
		this.copyrights = copyrights;
	}
	
	
	
}
