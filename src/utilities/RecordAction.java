package utilities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import dao.PersonDao;
import android.annotation.SuppressLint;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.audiofx.Equalizer;
import android.os.Environment;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class RecordAction {
	public static MediaRecorder MR;
	public static boolean IS_RECORDING;
	public static int CALL_OUTGOING = 1;
	public static int CALL_INCOMING = 2;
	public static int RECORD_MODE = MediaRecorder.AudioSource.MIC;

	/**
	 * 开始录音
	 */
	public static void startRecord(String number, int mode) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map = PersonDao.get(number);
		String dNumber = String.valueOf(map.get(PersonDao.COLUMN_NUMBER));
		if (dNumber.equals(number)) {

			try {
				String timeStamp = new SimpleDateFormat(
						"yyyy年MM月dd日hh时mm分ss秒的电话录音").format(new Date());
				Log.i(AppInfo.TAG, "start record:" + timeStamp);
				// File file = new
				// File(Environment.getExternalStorageDirectory()
				// .getPath() + "/mediarecorder.amr");
				if (mode == CALL_INCOMING) {
					number = "来电-" + number;
				}
				if (mode == CALL_OUTGOING) {
					number = "去电-" + number;
				}
				File file = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath()
						+ "/"
						+ number
						+ "在"
						+ timeStamp
						+ ".amr/");
				Log.i(AppInfo.TAG, file.getAbsolutePath());
				if (file.exists()) {
					// 如果文件存在，删除它，演示代码保证设备上只有一个录音文件
					file.delete();
				}
				MR = new MediaRecorder();
				// 设置音频录入源
				MR.setAudioSource(RECORD_MODE);
				// 设置录制音频的输出格式
				MR.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
				// 设置音频的编码格式
				MR.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
				// 设置录制音频文件输出文件路径
				MR.setOutputFile(file.getAbsolutePath());

				MR.setOnErrorListener(new OnErrorListener() {

					@Override
					public void onError(MediaRecorder mr, int what, int extra) {
						// 发生错误，停止录制
						MR.stop();
						MR.release();
						MR = null;
						IS_RECORDING = false;
						Log.i(AppInfo.TAG, "录音发生错误");
					}
				});

				// 准备、开始
				MR.prepare();
				MR.start();

				IS_RECORDING = true;
				Log.i(AppInfo.TAG, "开始录音");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 录音结束
	 */
	public static void stopRecord() {
		if (IS_RECORDING) {
			// 如果正在录音，停止并释放资源
			MR.stop();
			MR.reset();
			MR.release();
			MR = null;
			IS_RECORDING = false;
			Log.i(AppInfo.TAG, "录音结束");
		}
	}
}
