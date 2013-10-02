package ananas.jing.lite.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewMessageActivity extends Activity {

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

	}

	protected void onClickSend() {

		EditText wnd_content = (EditText) this.findViewById(R.id.text_content);
		EditText wnd_addr = (EditText) this.findViewById(R.id.text_to_address);

		String addr = wnd_addr.getText().toString();
		String text = wnd_content.getText().toString();

		// android.widget ;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}