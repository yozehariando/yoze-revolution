package com.github.twitterapi.ui;

import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.github.twitterapi.component.BackgroundManager;
import com.github.twitterapi.component.HeaderField;
import com.github.twitterapi.component.RoundRectField;
import com.github.twitterapi.component.WEditField;
import com.github.twitterapi.component.WLabelField;
import com.github.twitterapi.log.Logger;
import com.github.twitterapiapp.ServiceClient;

public class HomeScreen extends TwitterApiScreen {

	private static final String HEADER_TITLE = "Twitter API Apps";

	private static final String SEARCH_LABEL = "Enter a twitter username : ";
	private static final String SEARCH_BUTTON = "search";

	private BackgroundManager backgroundManager;
	private BitmapField bitmapField;
	private HeaderField headerField;
	private WLabelField searchLabel;
	private WEditField fillKeyword;
	private RoundRectField buttonSearch;

	protected Logger log = Logger.getLogger(getClass());

	public HomeScreen(ServiceClient serviceClient) {
		super(serviceClient);

		backgroundManager = new BackgroundManager();
		headerField = new HeaderField(HEADER_TITLE);
		searchLabel = new WLabelField(SEARCH_LABEL);
		fillKeyword = new WEditField();
		buttonSearch = new RoundRectField(SEARCH_BUTTON) {
			protected void doAction() {
				HomeScreen.this.serviceClient.setParameter(fillKeyword
						.getText());
				fireaction(TwitterApiListScreen.ACTION_ENTER);
				fillKeyword.setText("");

			}
		};

		backgroundManager.add(headerField);
		backgroundManager.add(new SeparatorField());
		backgroundManager.add(searchLabel);
		// vertical.add(new SeparatorField());
		backgroundManager.add(fillKeyword);
		// vertical.add(new SeparatorField());
		backgroundManager.add(buttonSearch);

		// backgroundManager.add(vertical);
		add(backgroundManager);

	}
	
	public boolean onClose() {
		if (Dialog
				.ask("Are You Sure to Exit?", new String[] { "Yes", "No" }, 0) == 0) {
			log.info(getClass().getName() + ": User Exiting Application");
			((MainTwitterApi) getApplication()).exit();
		}
		return true;
	}

}
