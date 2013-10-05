package ananas.jing.lite.android;

import ananas.jing.lite.android.helper.JingAndroidClient;
import ananas.jing.lite.core.client.JingClient;
import ananas.jing.lite.core.client.JingMessageManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewMessageActivity extends JingActivityBase {

	private final SendContext _send_context = new SendContext();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_message);

		Button btn = (Button) this.findViewById(R.id.button_send);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				NewMessageActivity.this.onClickSend();
			}
		});

		this.__setup_button_listener(R.id.button_send);
		this.__setup_button_listener(R.id.button_select_contact);

	}

	private void __setup_button_listener(int id) {
		Button btn = (Button) this.findViewById(id);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				NewMessageActivity.this.__onClickButton(v.getId());
			}
		});
	}

	protected void __onClickButton(int id) {
		switch (id) {
		case R.id.button_select_contact: {
			this.onClickSelectContact();
			break;
		}
		case R.id.button_send: {
			this.onClickSend();
			break;
		}
		default:
		}
	}

	class SendContext {

		public String name;
		public String addr;
		public String text;

		public boolean check() {
			if (addr == null)
				return false;
			if (text == null)
				return false;
			if (name == null)
				return false;
			return true;
		}

		public void clear() {
			addr = name = text = null;
		}
	}

	protected void onClickSend() {

		EditText wnd_content = (EditText) this.findViewById(R.id.text_content);

		SendContext sc = this._send_context;
		sc.text = wnd_content.getText().toString();

		AlertDialog dlg = (new AlertDialog.Builder(this))
				.setMessage(sc.addr + " << " + sc.text)
				.setNegativeButton("Cancal",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
						})
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						__do_final_send();
					}
				})

				.create();
		dlg.show();

	}

	private void __do_final_send() {

		SendContext sc = this._send_context;
		if (!sc.check())
			return;

		JingClient client = this.getClient();
		JingMessageManager jmm = client.getMessageManager();
		jmm.sendMessage(sc.addr, sc.text, null);

		sc.clear();

		// this.startActivity(new Intent(this, TaskRunningActivity.class));
		this.startActivity(new Intent(this, MessageListActivity.class));
	}

	final static int CONTACT_PICKER_RESULT = 723824;

	protected void onClickSelectContact() {

		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
				Contacts.CONTENT_URI);
		this.startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if ((requestCode == CONTACT_PICKER_RESULT) && (resultCode == RESULT_OK)) {

			String id = data.getData().getLastPathSegment();

			// query for everything email
			Cursor cursor = this.getContentResolver().query(
					Phone.CONTENT_URI,
					new String[] { Phone._ID, Phone.NUMBER, Phone.TYPE,
							Phone.DISPLAY_NAME, Phone.LABEL },
					Phone.CONTACT_ID + "=?", new String[] { id }, null);

			if (cursor.moveToFirst()) { // True if the cursor is not empty
				String name = this.__getString(cursor, Phone.DISPLAY_NAME);
				String phone = null;
				for (int i = cursor.getColumnCount() - 1; i >= 0; i--) {
					String key = cursor.getColumnName(i);
					String value = this.__getString(cursor, key);
					System.out.println("    cursor::" + key + " = " + value);
					if (this.__is_phone_number(value)) {
						phone = value;
						// break;
					}
				}
				System.out.println("     name = " + name);
				System.out.println("    phone = " + phone);
				this._send_context.name = name;
				this._send_context.addr = phone;
				this.__data_to_ui();
			}
		}
	}

	private boolean __is_phone_number(String value) {
		if (value == null)
			return false;
		char[] chs = value.toCharArray();
		if (chs.length < 6)
			return false;
		for (char c : chs) {
			switch (c) {
			case ' ':
			case '+':
			case '-':
				break;
			default:
				if ('0' <= c && c <= '9') {
					break;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	private void __data_to_ui() {

		String name = this._send_context.name;
		String addr = this._send_context.addr;
		String contact = null;
		if (name != null && addr != null) {
			contact = name + "<" + addr + ">";
		}

		Button btn = (Button) this.findViewById(R.id.button_select_contact);
		btn.setText(contact);

		EditText edit = (EditText) this.findViewById(R.id.text_content);
		edit.setText(this._send_context.text);

	}

	private String __getString(Cursor cursor, String key) {
		int i = cursor.getColumnIndex(key);
		if (i < 0)
			return null;
		return cursor.getString(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onServiceStatusChanged(JingAndroidClient jac,
			String newStatus) {

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		this.__save(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		this.__load(savedInstanceState);
	}

	protected void __save(Bundle b) {
		if (b != null) {
			b.putString(Keys.ADDR, this._send_context.addr);
			b.putString(Keys.NAME, this._send_context.name);
			b.putString(Keys.TEXT, this._send_context.text);
		}
	}

	interface Keys {

		String TEXT = "text";
		String NAME = "name";
		String ADDR = "addr";

	}

	protected void __load(Bundle b) {
		if (b != null) {
			this._send_context.addr = b.getString(Keys.ADDR);
			this._send_context.name = b.getString(Keys.NAME);
			this._send_context.text = b.getString(Keys.TEXT);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.__data_to_ui();
	}

}
