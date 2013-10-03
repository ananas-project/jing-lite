package ananas.jing.lite.android;

import ananas.jing.lite.android.helper.CoreAgent;
import ananas.jing.lite.android.helper.JingAndroidClient;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class JingSmsReceiver extends BroadcastReceiver {

	private CoreAgent _ca = null;
	private JingAndroidClient _jac;

	public void __onReceive(Context context, Intent intent,
			JingAndroidClient jac) {

		Bundle bundle = intent.getExtras();

		if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			SmsMessage[] mges = new SmsMessage[pdus.length];
			for (int i = 0; i < pdus.length; i++) {
				mges[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			}

			for (SmsMessage sms : mges) {
				// mge.getDisplayOriginatingAddress() ;
				String text = sms.getMessageBody().trim();
				// mge.getTimestampMillis();

				if (text.startsWith("jing:")) {
					this.abortBroadcast();
					this.__on_recv_jing_sms(sms);
					Intent intent2 = new Intent(context, MainActivity.class);
					intent2.setFlags(intent2.getFlags()
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					// context.startActivity(intent2);

					// //////
					Intent service = new Intent(context,
							JingAndroidService.class);
					context.startService(service);

				}

			}

		}

	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {

			JingAndroidClient jac = this._jac;
			if (jac == null) {
				jac = JingAndroidClient.Factory.newInstance(context);
				this._jac = jac;
			}

			try {
				this.__onReceive(context, intent, jac);
			} catch (Exception e) {
				e.printStackTrace();
			}

			jac.onCreate();

		}
	}

	private void __on_recv_jing_sms(SmsMessage sms) {
		CoreAgent ca = _ca;
		if (ca == null) {
			_ca = ca = CoreAgent.Factory.newInstance();
		}

		String addr, text;
		addr = sms.getDisplayOriginatingAddress();
		text = sms.getMessageBody();
		ca.getClient().getMessageManager().receiveMessage(addr, text);
	}

}
