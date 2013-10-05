package ananas.jing.lite.android;

import ananas.jing.lite.android.helper.JingAndroidClient;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends JingActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.__setup_button_listener(R.id.new_message);
		this.__setup_button_listener(R.id.list_message);

		this.startActivity(new Intent(this, MessageListActivity.class));
	}

	private void __setup_button_listener(int id) {
		Button btn = (Button) this.findViewById(id);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				int id = view.getId();
				MainActivity.this.onButtonClick(id);
			}
		});
	}

	private void onButtonClick(int id) {

		switch (id) {
		case R.id.list_message: {
			Intent intent = new Intent(this, MessageListActivity.class);
			this.startActivity(intent);
			break;
		}
		case R.id.new_message: {
			Intent intent = new Intent(this, NewMessageActivity.class);
			this.startActivity(intent);
			break;
		}
		default:
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onServiceStatusChanged(JingAndroidClient jac,
			String newStatus) {
		// TODO Auto-generated method stub

	}

}
