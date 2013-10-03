package ananas.jing.lite.android;

import ananas.jing.lite.android.helper.CoreAgent;
import ananas.jing.lite.android.helper.JingAndroidClient;
import ananas.jing.lite.core.client.JingClient;
import android.app.Activity;
import android.os.Bundle;

public abstract class JingActivityBase extends Activity implements CoreAgent {

	private CoreAgent _agent;
	private JingAndroidClient _jac;

	public JingAndroidClient getJingAndroidClient() {
		return _jac;
	}

	@Override
	protected void onPause() {
		this._jac.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this._jac.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this._jac = JingAndroidClient.Factory.newInstance(this);
		this._agent = CoreAgent.Factory.newInstance();

		this._jac.onCreate();
	}

	@Override
	public JingClient getClient() {
		return this._agent.getClient();
	}

	protected abstract void onServiceStatusChanged(JingAndroidClient jac,
			String newStatus);

}
