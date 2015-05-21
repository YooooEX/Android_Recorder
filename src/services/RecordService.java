package services;

import utilities.PhoneListener;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class RecordService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(getApplication(), "录音服务已创建", 1000).show();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Toast.makeText(getApplicationContext(), "录音服务已启动", 1000).show();
		TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		manager.listen(new PhoneListener(getApplicationContext()),
				PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopSelf();
		Toast.makeText(getApplicationContext(), "录音服务已销毁", 1000).show();
	}

}
