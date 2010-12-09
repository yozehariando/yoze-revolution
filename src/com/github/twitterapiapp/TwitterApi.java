package com.github.twitterapiapp;

public interface TwitterApi {
	
	public String fromUser();
	
	public String getText();
	
	public String getAvatar();
	
	public String getCreateAt();
	
	public TwitterApi[] getResult();

}
