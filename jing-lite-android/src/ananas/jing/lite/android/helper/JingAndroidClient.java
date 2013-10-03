package ananas.jing.lite.android.helper;

import android.content.Context;

public interface JingAndroidClient {

	class Factory {
		public static JingAndroidClient newInstance(Context context) {
			return new JingAndroidClientImpl(context);
		}
	}

	void onCreate();

	void onPause();

	void onResume();

	IBinder4JAS getServiceBinder();
}
