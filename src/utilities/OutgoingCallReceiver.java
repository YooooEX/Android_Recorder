package utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OutgoingCallReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		Log.i(AppInfo.TAG, "OutgoingCall:" + number);
		RecordAction.startRecord(number, RecordAction.CALL_OUTGOING);
	}
}
