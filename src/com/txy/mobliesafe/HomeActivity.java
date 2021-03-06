package com.txy.mobliesafe;

import com.txy.mobliesafe.utils.MD5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	private GridView gd_function_list;
	private SharedPreferences sp;
	private static String[] functionName = { "防盗", "卫士", "软件", "进程", "流量",
			"杀毒", "缓存", "高级", "设置" };
	private static int[] functionIcon = { R.drawable.lion, R.drawable.defender,
			R.drawable.soft, R.drawable.progress, R.drawable.data,
			R.drawable.kill, R.drawable.cache, R.drawable.advance,
			R.drawable.settings };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		gd_function_list = (GridView) findViewById(R.id.gd_function_list);
		MyAdpter adapter = new MyAdpter();
		gd_function_list.setAdapter(adapter);
		gd_function_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:
					if (isSetPassword()) {
						// 显示检验密码对话框
						showCheckPasswordDialog();
					} else {
						// 显示设置密码界面
						showSetPasswordDialog();
					}
					break;
				case 1:
					Intent intent1 = new Intent(HomeActivity.this,
							ComunicationActivity.class);
					startActivity(intent1);
					break;
				case 2:
					Intent intent2 = new Intent(HomeActivity.this,
							SoftSelectActivity.class);
					startActivity(intent2);
					break;
				case 3:
					Intent intent3 = new Intent(HomeActivity.this,
							ProcessActivity.class);
					startActivity(intent3);
					break;
				case 5:
					Intent intent5 = new Intent(HomeActivity.this,
							KillVirusActivity.class);
					startActivity(intent5);

					break;
				case 6:
					Intent intent6 = new Intent(HomeActivity.this,
							CacheClearActivity.class);
					startActivity(intent6);
					break;

				case 7:
					Intent intent7 = new Intent(HomeActivity.this,
							AdvancedSettingsActivity.class);
					startActivity(intent7);
					break;
				case 8:
					Intent intent8 = new Intent(HomeActivity.this,
							SettingActivity.class);
					startActivity(intent8);
					// finish(); 设置界面不要直接关闭activity
					break;
				default:
					break;
				}

			}
		});

	}

	private EditText et_password, et_password1, et_password2;
	private Button bt_ok, bt_cancel;
	private AlertDialog dialog;

	protected void showSetPasswordDialog() {

		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this,
				R.layout.activity_lion_set_dialog, null);
		et_password1 = (EditText) view.findViewById(R.id.et_password1);
		et_password2 = (EditText) view.findViewById(R.id.et_password2);
		bt_ok = (Button) view.findViewById(R.id.bt_ok);
		bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

		bt_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		bt_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String password1 = et_password1.getText().toString().trim();
				String password2 = et_password2.getText().toString().trim();
				if (TextUtils.isEmpty(password1)
						|| TextUtils.isEmpty(password2)) {
					Toast.makeText(HomeActivity.this, "密码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					if (password1.equals(password2)) {
						Editor editor = sp.edit();
						editor.putString("password",
								MD5Utils.MD5Password(password1));
						editor.commit();
						Toast.makeText(HomeActivity.this, "设置密码成功！",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(HomeActivity.this, "两次输入密码不一致",
								Toast.LENGTH_SHORT).show();
						et_password1.setText("");
						et_password2.setText("");
						return;
					}
				}
				dialog.dismiss();
			}
		});

		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	protected void showCheckPasswordDialog() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this,
				R.layout.activity_lion_check_dialog, null);
		et_password = (EditText) view.findViewById(R.id.et_password);
		bt_ok = (Button) view.findViewById(R.id.bt_ok);
		bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

		bt_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		bt_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String savePassword = sp.getString("password", "");
				String password = et_password.getText().toString().trim();
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(HomeActivity.this, "请输入密码！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (MD5Utils.MD5Password(password).equals(savePassword)) {
					// 进入防盗功能界面
					Toast.makeText(HomeActivity.this, "密码正确",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(HomeActivity.this, SecurityActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(HomeActivity.this, "密码错误",
							Toast.LENGTH_SHORT).show();
					et_password.setText("");
					return;
				}
				dialog.dismiss();
			}
		});

		// builder.setView(view);
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	protected boolean isSetPassword() {
		sp = getSharedPreferences("config", MODE_PRIVATE);
		String password = sp.getString("password", null);
		return !TextUtils.isEmpty(password);
	}

	class MyAdpter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return functionName.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView != null) {
				view = convertView;
			} else {
				view = View
						.inflate(HomeActivity.this, R.layout.item_home, null);
				ImageView iv = (ImageView) view.findViewById(R.id.iv_item_icon);
				TextView tv = (TextView) view.findViewById(R.id.tv_item_name);

				iv.setImageResource(functionIcon[position]);
				tv.setText(functionName[position]);
			}
			return view;
		}

	}
}
