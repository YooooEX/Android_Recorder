package utilities;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneListener extends PhoneStateListener {
	Context c;
	boolean isCalling = false;

	public PhoneListener(Context context) {
		// TODO Auto-generated constructor stub
		this.c = context;
		isCalling = false;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		// TODO Auto-generated method stub
		super.onCallStateChanged(state, incomingNumber);
		switch (state) {
		case TelephonyManager.CALL_STATE_OFFHOOK:
			Log.i(AppInfo.TAG, "CALL_STATE_OFFHOOK");
			// 去电录音在utilities.OutgoingCallReceiver中实现
			isCalling = true;
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			Log.i(AppInfo.TAG, "CALL_STATE_RINGING");
			Log.i(AppInfo.TAG, "incomingNumber:" + incomingNumber);
			RecordAction
					.startRecord(incomingNumber, RecordAction.CALL_INCOMING);
			isCalling = true;
			break;
		case TelephonyManager.CALL_STATE_IDLE:
			Log.i(AppInfo.TAG, "CALL_STATE_OFFHOOK");
			if (isCalling) {
				RecordAction.stopRecord();
				isCalling = false;
			}
			break;
		}
	}
}
