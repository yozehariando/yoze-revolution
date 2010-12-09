package com.github.twitterapiapp;

import com.github.twitterapi.json.JSONArray;
import com.github.twitterapi.json.JSONException;
import com.github.twitterapi.json.JSONObject;
import com.github.twitterapi.log.Logger;

public class TwitterApiImpl implements TwitterApi {

	private JSONObject jsonObject = null;
	private ServiceClient serviceClient = null;
	protected Logger log = Logger.getLogger(getClass());

	public TwitterApiImpl(ServiceClient serviceClient) {
		this.serviceClient = serviceClient;
		this.jsonObject = new JSONObject();
	}

	public TwitterApiImpl(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

//	public TwitterApiImpl() {
//		super();
//		// TODO Auto-generated constructor stub
//	}

	public String fromUser() {
		try {
			return jsonObject.getString("from_user");
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public String getAvatar() {
		try {
			return jsonObject.getString("profile_image_url");
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public String getCreateAt() {
		try {
			return jsonObject.getString("created_at");
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public TwitterApi[] getResult() {
		TwitterApi[] results = null;
		try {
			JSONArray array = serviceClient.read(serviceClient.getParameter())
					.getJSONArray("results");
			results = new TwitterApi[array.length()];

			for (int i = 0; i < results.length; i++) {
				JSONObject object = array.getJSONObject(i);
				results[i] = new TwitterApiImpl(object);
				log.debug(results[i].getText());
			}
		} catch (JSONException e) {
			log.error(getClass().getName() + ":" + e.getMessage());
		} catch (ServiceClientException e) {
			log.error(getClass().getName() + ":" + e.getMessage());
		}
		return results;
	}

	public String getText() {
		try {
			return jsonObject.getString("text");
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return null;
	}

}
