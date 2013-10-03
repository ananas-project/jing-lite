package ananas.jing.lite.android.helper;

import ananas.jing.lite.android.JingAndroidService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class JingAndroidClientImpl implements JingAndroidClient,
		ServiceConnection {

	private final Context _context;
	private IBinder4JAS _binder_inner;

	public JingAndroidClientImpl(Context context) {
		this._context = context;
	}

	@Override
	public void onCreate() {
		Intent service = new Intent(_context, JingAndroidService.class);
		_context.startService(service);
	}

	@Override
	public void onPause() {
		_context.unbindService(this);
	}

	@Override
	public void onResume() {
		Intent service = new Intent(_context, JingAndroidService.class);
		_context.bindService(service, this, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onServiceConnected(ComponentName arg0, IBinder arg1) {
		IBinder4JAS binder = (IBinder4JAS) arg1;
		this._binder_inner = binder;
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		this._binder_inner = null;
	}

	private final IBinder4JAS _binder_outer = new IBinder4JAS() {

		private IBinder4JAS __getBinder() {
			return JingAndroidClientImpl.this._binder_inner;
		}

		@Override
		public void fire() {
			IBinder4JAS binder = this.__getBinder();
			if (binder == null)
				return;
			binder.fire();
		}

		@Override
		public String getStatus() {
			IBinder4JAS binder = this.__getBinder();
			if (binder == null)
				return "";
			return binder.getStatus();
		}
	};

	@Override
	public IBinder4JAS getServiceBinder() {
		return this._binder_outer;
	}

}
