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
	 * ��ʼ¼��
	 */
	public static void startRecord(String number, int mode) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map = PersonDao.get(number);
		String dNumber = String.valueOf(map.get(PersonDao.COLUMN_NUMBER));
		if (dNumber.equals(number)) {

			try {
				String timeStamp = new SimpleDateFormat(
						"yyyy��MM��dd��hhʱmm��ss��ĵ绰¼��").format(new Date());
				Log.i(AppInfo.TAG, "start record:" + timeStamp);
				// File file = new
				// File(Environment.getExternalStorageDirectory()
				// .getPath() + "/mediarecorder.amr");
				if (mode == CALL_INCOMING) {
					number = "����-" + number;
				}
				if (mode == CALL_OUTGOING) {
					number = "ȥ��-" + number;
				}
				File file = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath()
						+ "/"
						+ number
						+ "��"
						+ timeStamp
						+ ".amr/");
				Log.i(AppInfo.TAG, file.getAbsolutePath());
				if (file.exists()) {
					// ����ļ����ڣ�ɾ��������ʾ���뱣֤�豸��ֻ��һ��¼���ļ�
					file.delete();
				}
				MR = new MediaRecorder();
				// ������Ƶ¼��Դ
				MR.setAudioSource(RECORD_MODE);
				// ����¼����Ƶ�������ʽ
				MR.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
				// ������Ƶ�ı����ʽ
				MR.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
				// ����¼����Ƶ�ļ�����ļ�·��
				MR.setOutputFile(file.getAbsolutePath());

				MR.setOnErrorListener(new OnErrorListener() {

					@Override
					public void onError(MediaRecorder mr, int what, int extra) {
						// ��������ֹͣ¼��
						MR.stop();
						MR.release();
						MR = null;
						IS_RECORDING = false;
						Log.i(AppInfo.TAG, "¼����������");
					}
				});

				// ׼������ʼ
				MR.prepare();
				MR.start();

				IS_RECORDING = true;
				Log.i(AppInfo.TAG, "��ʼ¼��");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ¼������
	 */
	public static void stopRecord() {
		if (IS_RECORDING) {
			// �������¼����ֹͣ���ͷ���Դ
			MR.stop();
			MR.reset();
			MR.release();
			MR = null;
			IS_RECORDING = false;
			Log.i(AppInfo.TAG, "¼������");
		}
	}
}
