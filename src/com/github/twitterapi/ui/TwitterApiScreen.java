package com.github.twitterapi.ui;

import com.github.twitterapiapp.ServiceClient;

public abstract class TwitterApiScreen extends ActionScreen {
	
	protected ServiceClient serviceClient;

	public TwitterApiScreen(ServiceClient serviceClient) {
		super();
		this.serviceClient = serviceClient;
	}

}
