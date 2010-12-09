package com.github.twitterapiapp;

import com.github.twitterapi.connection.HttpClient;
import com.github.twitterapi.connection.HttpConnectionFactory;
import com.github.twitterapi.json.JSONObject;
import com.github.twitterapi.json.JSONTokener;

public class ServiceClient {

	private HttpConnectionFactory factory;

	private HttpClient httpClient;

	private AppSetting appSetting;

	private String parameter;

	private TwitterApi twitterApi;

	public ServiceClient(AppSetting appSetting, HttpConnectionFactory hFactory) {
		this.appSetting = appSetting;
		this.factory = hFactory;
		httpClient = new HttpClient(factory);

	}

	public JSONObject read(String parameter) throws ServiceClientException {
		StringBuffer buffer = new StringBuffer();

		try {
			buffer = httpClient.doGet(appSetting.getUrl(), parameter, factory);
			if (buffer.length() == 0) {
				new ServiceClientException("tidak ada respon");
			}
			return new JSONObject(new JSONTokener(buffer.toString()));
		} catch (ServiceClientException e) {
			throw e;
		} catch (Exception e) {
			new ServiceClientException(e.getMessage());
		}

		return null;

	}

	public HttpConnectionFactory getFactory() {
		return factory;
	}

	public void setFactory(HttpConnectionFactory factory) {
		this.factory = factory;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public AppSetting getAppSetting() {
		return appSetting;
	}

	public void setAppSetting(AppSetting appSetting) {
		this.appSetting = appSetting;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public TwitterApi getTwitterApi() {
		if (twitterApi == null) {
			twitterApi = new TwitterApiImpl(this);
		}
		return twitterApi;
	}

	public void setTwitterApi(TwitterApi twitterApi) {
		this.twitterApi = twitterApi;
	}

}
