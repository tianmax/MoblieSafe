package com.txy.mobliesafe;

import com.txy.mobliesafe.utils.SmsUtils;
import com.txy.mobliesafe.utils.SmsUtils.SmsOptions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AdvancedSettingsActivity extends Activity {

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced_settings);
	}

	public void selectNumber(View view) {
		Intent intent = new Intent(AdvancedSettingsActivity.this,
				NumberSelectActivity.class);
		startActivity(intent);
	}

	public void backupSms(View view) {
		dialog = new ProgressDialog(this);
		dialog.setTitle("���ڱ��ݡ�����");
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					SmsUtils.backupSms(AdvancedSettingsActivity.this,
							new SmsOptions() {

								@Override
								public void setProgress(int progress) {
									dialog.setProgress(progress);
								}

								@Override
								public void setMax(int max) {
									dialog.setMax(max);
								}
							});
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(AdvancedSettingsActivity.this,
									"���ݳɹ�", Toast.LENGTH_SHORT).show();
						}
					});

				} catch (Exception e) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(AdvancedSettingsActivity.this,
									"����ʧ��", Toast.LENGTH_SHORT).show();
						}
					});
				} finally {
					dialog.dismiss();
				}
			}
		}).start();

	}

	public void restoreSms(View view) {
		dialog = new ProgressDialog(this);
		dialog.setTitle("���ڻָ�������");
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.show();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					SmsUtils.restoreSms(AdvancedSettingsActivity.this, false,
							new SmsOptions() {

								@Override
								public void setProgress(int progress) {
									dialog.setProgress(progress);
								}

								@Override
								public void setMax(int max) {
									dialog.setMax(max);
								}
							});
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(AdvancedSettingsActivity.this,
									"�ָ��ɹ�", Toast.LENGTH_SHORT).show();

						}
					});

				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(AdvancedSettingsActivity.this,
									"�ָ�ʧ��", Toast.LENGTH_SHORT).show();
						}
					});
				} finally {
					dialog.dismiss();
				}
			}
		}).start();

	}

	public void createShortCut(View view) {

		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));
		// ?��ʱ��Ч
		shortcut.putExtra("duplicate", false);// �����Ƿ��ظ�����
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(this, MainActivity.class);// ���õ�һ��ҳ��
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				this, R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		sendBroadcast(shortcut);

		Toast.makeText(AdvancedSettingsActivity.this, "�����ɹ�",
				Toast.LENGTH_SHORT).show();

	}
}