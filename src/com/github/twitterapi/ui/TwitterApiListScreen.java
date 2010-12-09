package com.github.twitterapi.ui;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ListField;

//import com.github.twitterapi.component.BitmapThread;
import com.github.twitterapi.component.HeaderField;
import com.github.twitterapi.component.LoadingPopupScreen;
import com.github.twitterapi.component.TwitterListField;
import com.github.twitterapi.log.Logger;
import com.github.twitterapiapp.ServiceClient;
import com.github.twitterapiapp.TwitterApi;

public class TwitterApiListScreen extends TwitterApiScreen {

	static final String ACTION_ENTER = "Twitter API app";
	static final String ERROR_ENTER = "Error API app";
	static final String REFRESH_ENTER = "Refresh API app";

	private static final String BANNER_TITLE = "Twitter API app";

	private TwitterListField twitterListField;

	private HeaderField banner;

	protected Logger log = Logger.getLogger(getClass());

	private Timer streamTimer = null;

	public TwitterApiListScreen(ServiceClient serviceClient) {
		super(serviceClient);
		banner = new HeaderField(BANNER_TITLE);
		twitterListField = new TwitterListField();
		setBanner(banner);
		add(twitterListField);
	}

	public void loadList() {
		updateList();
	}

	private final void updateList() {
		BackgroundStream backgroundStream = new BackgroundStream(this);
		LoadingPopupScreen.showLoadingScreen(backgroundStream, "loading.....");

		// Update every 10 seconds
		streamTimer = new Timer();
		streamTimer.schedule(new TimerTask() {
			public void run() {
				updateList();
			}
		}, 60000);
	}

	/*
	 * Stop Scheduler Update when Escape this Screen
	 */
	protected boolean keyChar(char c, int status, int time) {
		if (c == Characters.ESCAPE) {
			streamTimer.cancel();
			UiApplication.getUiApplication().popScreen(this);
			return true;
		}
		return false;
	}

	/*
	 * Stop Scheduler Update when close this Screen
	 */
	public void close() {
		streamTimer.cancel();
		UiApplication.getUiApplication().popScreen(this);
	}

	private class BackgroundStream implements Runnable {

		private final TwitterApiListScreen listScreen;

		public BackgroundStream(TwitterApiListScreen listScreen) {
			this.listScreen = listScreen;
		}

		public void run() {
			try {

				// serviceClient.setParameter("yozehariando");
				final TwitterApi[] twitterApis = serviceClient.getTwitterApi()
						.getResult();

				UiApplication.getUiApplication().invokeLater(new Runnable() {

					public void run() {
						while (twitterListField.getSize() > 0) {
							twitterListField.delete(0);
						}

						if (twitterApis.length > 0) {
							twitterListField.clear();

							for (int i = 0; i < twitterApis.length; i++) {
								log.debug("Screen : "
										+ twitterApis[i].getText());
								twitterListField.insert(twitterListField
										.getSize());
								twitterListField.add(twitterApis[i]);
							}
							twitterListField.loadBitmap();	
						}
					}
				});
			} catch (Exception e) {
				log.error(e.getMessage());
				fireaction(ERROR_ENTER, e.getMessage());
			}
		}
	}
}
