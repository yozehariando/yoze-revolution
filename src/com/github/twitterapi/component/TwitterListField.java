package com.github.twitterapi.component;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.amms.control.tuner.TunerControl;

import com.github.twitterapi.log.LogEntryField;
import com.github.twitterapi.log.Logger;
import com.github.twitterapi.ui.TwitterApiListScreen;
import com.github.twitterapi.ui.TwitterApiScreen;
import com.github.twitterapi.util.image.ImageUtil;
import com.github.twitterapiapp.ServiceClient;
import com.github.twitterapiapp.TwitterApi;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class TwitterListField extends ListField implements ListFieldCallback {

	private Vector rows;
	private LabelField twitterText;
	private LabelField userInfo;

	private TableRowManager rowManager;

	private Hashtable thumbnailTwitter = new Hashtable();

	protected Logger log = Logger.getLogger(getClass());

	// protected ServiceClient serviceClient;

	public TwitterListField() {
		super();
		setEmptyString("Nothing fedd found", DrawStyle.HCENTER);
		setCallback(this);
		setRowHeight(70);
		rows = new Vector();
	}

	/*
	 * Starting Implement ListFieldCallBack
	 */

	public void drawListRow(ListField listField, Graphics graphics, int index,
			int y, int width) {

		rowManager = new TableRowManager();
		if (index < rows.size()) {
			int height = listField.getRowHeight();
			ImageUtil imageUtil = new ImageUtil();
			TwitterApi twitterApi = (TwitterApi) rows.elementAt(index);
			Bitmap bitmap = getBitmap(twitterApi.fromUser());

			/*
			 * Draw List Cell every Row
			 */
			int[] upperX_PTS = new int[] { 0, 0, width, width };
			int[] upperY_PTS = new int[] { 0, y + (height / 2),
					y + (height / 2), 0 };

			int[] lowerX_PTS = new int[] { 0, 0, width, width };
			int[] lowerY_PTS = new int[] { y + (height / 2), y + height,
					y + height, y + (height / 2) };

			int[] upperDrawColors = new int[] { 0x5d5e61, 0x3d3f45, 0x3d3f45,
					0x5d5e61 };
			int[] lowerDrawColors = new int[] { 0x2d2f33, 0x1e2126, 0x1e2126,
					0x2d2f33 };
			graphics.drawShadedFilledPath(upperX_PTS, upperY_PTS, null,
					upperDrawColors, null);
			graphics.drawShadedFilledPath(lowerX_PTS, lowerY_PTS, null,
					lowerDrawColors, null);
			/*
			 * End of Draw List Cell
			 */

			/*
			 * Thumbnail function
			 */

			if (bitmap != null) {
				log.info("Bitmap Retrieved : "
						+ getBitmap(twitterApi.getText()) + " Height : ");

				graphics.drawBitmap(0,
						y
								+ ((height - Math.min(bitmap.getHeight(),
										height)) / 2), 50, height, imageUtil
								.bestFit(bitmap, 50, height), 0, 0);
			}

			/*
			 * end of thumbnail function
			 */

			twitterText = new LabelField() {
				protected void paint(Graphics graphics) {
					graphics.setColor(Color.YELLOWGREEN);
					super.paint(graphics);
				}
			};

			userInfo = new LabelField() {
				protected void paint(Graphics graphics) {
					graphics.setColor(Color.YELLOWGREEN);
					super.paint(graphics);
				}
			};

			try {
				twitterText.setFont(FontFamily.forName("System").getFont(
						Font.PLAIN, 7, Ui.UNITS_pt));
				userInfo.setFont(FontFamily.forName("System").getFont(
						Font.BOLD, 5, Ui.UNITS_pt));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			twitterText.setText(twitterApi.getText());
			rowManager.add(twitterText);

			userInfo.setText("From: " + twitterApi.fromUser());
			rowManager.add(userInfo);

			rowManager.drawRow(graphics, 0, y + 1, width, listField
					.getRowHeight());
		}

	}

	/*
	 * function for bitmap
	 */

	public Bitmap getBitmap(String thumbnail) {
		return (Bitmap) thumbnailTwitter.get(thumbnail);
	}

	public void loadBitmap() {
		new BitmapThread().start();
	}

	private class BitmapThread extends Thread {
		protected ServiceClient serviceClient;
		private String avatar;

		public void run() {
			Enumeration enumeration = rows.elements();
			while (enumeration.hasMoreElements()) {
				TwitterApi twitterApi = (TwitterApi) enumeration.nextElement();
				ListField listField = new ListField();
				try {
					avatar = twitterApi.getAvatar();
					log.debug("UrlAvatar : " + avatar);

					StringBuffer response = serviceClient.getHttpClient()
							.doGet(twitterApi.getAvatar());
					byte[] data = avatar.toString().getBytes();
					byte[] data2 = response.toString().getBytes();
					log.debug("testing avatar : " + data);
					log.debug("data2 : " + data2);

					if (data.length > 0) {
						// Bitmap bitmap = Bitmap.createBitmapFromBytes(data, 0,
						// data.length, 1);
						Bitmap bitmap = Bitmap.createBitmapFromBytes(data, 0,
								data.length, 1);
						log.debug("get bitmap : " + bitmap);

						// thumbnailTwitter.put(twitterApi.getText(), bitmap);
						// listField.invalidate();
						// log.debug("testing error AVA : " + thumbnailTwitter);
					}

				} catch (Exception e) {
					log.error(e.getClass() + " : " + e.getMessage());
				}
			}
		}

	}

	/*
	 * end of function
	 */

	public Object get(ListField listField, int index) {
		if (index < rows.size()) {
			return rows.elementAt(index);
		}
		return null;
	}

	public int getPreferredWidth(ListField listField) {

		return Display.getWidth() - 2;
	}

	public int indexOfList(ListField listField, String prefix, int start) {
		for (int i = 0; i < rows.size(); i++) {
			TwitterApi twitterApi = (TwitterApi) rows.elementAt(i);

			if (twitterApi.getText().indexOf(prefix) > 1) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * End Of Implement ListFieldCallBack
	 */

	public void add(TwitterApi twitterApi) {
		rows.addElement(twitterApi);
	}

	public void clear() {
		rows.removeAllElements();
	}

	public void insert(TwitterApi twitterApi, int index) {
		rows.insertElementAt(twitterApi, index);
	}

	private class TableRowManager extends VerticalFieldManager {

		public TableRowManager() {
			super(VERTICAL_SCROLL);
		}

		/*
		 * Cause the fields within this row manager to be layed out then painted
		 */
		public void drawRow(Graphics graphics, int x, int y, int width,
				int height) {
			// arrange the cell with this layout
			layout(width, height);

			// Place this row manager with its enclosing list
			setPosition(x, y);

			// Apply translating/clipping transformation to the graphics
			// context so that this row paints in the right area
			graphics.pushRegion(getExtent());

			// paint this manager
			subpaint(graphics);

			graphics.setColor(0x00CACACA);
			graphics.drawLine(0, 0, getPreferredWidth(), 0);

			// Restore the graphics context
			graphics.popContext();

		}

		/*
		 * Arrange This manager's controlled fields from left to rightwithin the
		 * enclosing table's column
		 */
		protected void sublayout(int arg0, int arg1) {
			// Set the size position of each field
			int fontHeight = Font.getDefault().getHeight();
			int preferredWidth = getPreferredWidth();

			// start with Label Field of Twitter Message
			Field field = getField(0);
			layoutChild(field, getPreferredWidth(), getPreferredHeight());
			setPositionChild(field, 5, 0);

			// set the User sender label Field
			field = getField(1);
			layoutChild(field, getPreferredWidth(), getPreferredHeight());
			setPositionChild(field, 5, getField(0).getHeight() + 1);

			setExtent(preferredWidth, getPreferredHeight());

		}

		public int getPreferredHeight() {
			return Graphics.getScreenHeight();
		}

		public int getPreferredWidth() {
			return Graphics.getScreenWidth();
		}

	}

}
