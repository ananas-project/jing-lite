package ananas.jing.lite.android;

import java.util.Properties;

import ananas.jing.lite.android.helper.CoreAgent;
import ananas.jing.lite.android.helper.IBinder4JAS;
import ananas.jing.lite.core.JingSMSHandler;
import ananas.jing.lite.core.client.JingClient;
import ananas.jing.lite.core.client.JingMessageManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.SmsManager;

public class JingAndroidService extends Service {

	class MyBinder extends Binder implements IBinder4JAS {

		@Override
		public void fire() {
			JingMessageManager jmm = JingAndroidService.this._ca.getClient()
					.getMessageManager();
			jmm.receiveMessage();
			jmm.sendMessage();
		}

		@Override
		public String getStatus() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	private final MyBinder _binder = new MyBinder();
	private CoreAgent _ca;

	@Override
	public IBinder onBind(Intent arg0) {
		System.out.println(this + ".onBind");
		return this._binder;
	}

	@Override
	public void onCreate() {
		System.out.println(this + ".onCreate");
		super.onCreate();
		this._ca = CoreAgent.Factory.newInstance();
		this._ca.getClient().setSMSHandler(this.__getSmsHandler());
	}

	private JingSMSHandler __getSmsHandler() {
		return new JingSMSHandler() {

			@Override
			public void sendSMS(String to, String msg) {
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage(to, null, msg, null, null);
			}

			@Override
			public void onReceive(JingClient client, Properties overview) {
				// TODO Auto-generated method stub

			}
		};
	}

	@Override
	public void onDestroy() {
		System.out.println(this + ".onDestroy");
		super.onDestroy();
	}

	@Override
	public void onRebind(Intent intent) {
		System.out.println(this + ".onRebind");
		super.onRebind(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println(this + ".onStartCommand");
		this._binder.fire();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		System.out.println(this + ".onUnbind");
		return super.onUnbind(intent);
	}

}
