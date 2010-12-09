package com.github.twitterapi.ui;

import com.github.twitterapi.connection.HttpConnectionFactory;
import com.github.twitterapi.log.AppenderFactory;
import com.github.twitterapiapp.AppSetting;
import com.github.twitterapiapp.ServiceClient;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ActiveFieldCookie;
import net.rim.device.api.ui.component.LabelField;

public class MainTwitterApi extends UiApplication implements ActionListener {

	private static final String twitterapi = "http://search.twitter.com/search.json?q=";

	private HttpConnectionFactory factory;
	private ServiceClient serviceClient;

	private HomeScreen homeScreen;
	private TwitterApiListScreen twitterApiListScreen;

	public MainTwitterApi() {
		initialize();
		if (serviceClient != null) {
			homeScreen = new HomeScreen(serviceClient);
			homeScreen.addActionListener(this);
			pushScreen(homeScreen);
			
		}
		// tes tes = new tes();
		// pushScreen(tes);
	}

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		MainTwitterApi main = new MainTwitterApi();
		main.enterEventDispatcher();

	}

	private void initialize() {
		factory = new HttpConnectionFactory();
		AppSetting setting = new AppSetting(twitterapi);
		serviceClient = new ServiceClient(setting, factory);
	}

	public void onAction(Action event) {
		if (event.getSource() == homeScreen) {
			if (event.getAction().equals(TwitterApiListScreen.ACTION_ENTER)) {
				if (twitterApiListScreen == null) {
					twitterApiListScreen = new TwitterApiListScreen(
							serviceClient);
					twitterApiListScreen.addActionListener(this);
				}
				pushScreen(twitterApiListScreen);
				twitterApiListScreen.loadList();
			}
		}
	}

	public void exit() {
		AppenderFactory.close();
		System.exit(0);
	}

}
