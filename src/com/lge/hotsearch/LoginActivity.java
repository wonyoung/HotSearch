package com.lge.hotsearch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class LoginActivity extends Activity implements LoginView {

	private final class OnClickListenerImplementation implements
			OnClickListener {
		@Override
		public void onClick(View v) {
			presenter.login();
		}
	}

	private final class LoginModelImplementation implements LoginModel {
		@Override
		public String getLastUsername() {
			return "lastname";
		}

		@Override
		public void login(String username, String password, Callback callback) {
			// TODO Auto-generated method stub
			if ("pass".equals(username)) {
				callback.onSuccess();
			} else {
				callback.onFail();
			}
		}
	}

	private final class TextWatcherImplementation implements TextWatcher {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable s) {
			presenter.verifyLogin();
		}
	}

	private LoginPresenter presenter;
	private EditText loginId;
	private EditText loginPassword;
	private Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.activity_login);
		
		loginId = (EditText) findViewById(R.id.id_editText);
		loginPassword = (EditText) findViewById(R.id.password_editText);
		loginButton = (Button) findViewById(R.id.login_button);
		
		TextWatcher textWatcher = new TextWatcherImplementation();
		loginId.addTextChangedListener(textWatcher);
		loginPassword.addTextChangedListener(textWatcher);
		loginButton.setOnClickListener(new OnClickListenerImplementation());
		
		LoginModel model =  new LoginModelImplementation();
		
		presenter = new LoginPresenter(model , this);
		presenter.initialize();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	@Override
	public void setUsername(String username) {
		loginId.setText(username);
	}

	@Override
	public void disableLoginButton() {
		loginButton.setEnabled(false);
	}

	@Override
	public void enableLoginButton() {
		loginButton.setEnabled(true);
	}

	@Override
	public String getUsername() {
		return loginId.getText().toString();
	}

	@Override
	public String getPassword() {
		return loginPassword.getText().toString();
	}

	@Override
	public void enableProgress() {
		setProgressBarIndeterminateVisibility(true);
	}

	@Override
	public void failAlert() {
		Toast.makeText(this, "Log in failed.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void disableProgress() {
		setProgressBarIndeterminateVisibility(false);
	}

	@Override
	public void launchMain() {
		Intent intent = new Intent(this, HotSearchActivity.class);
		startActivity(intent);
	}
}
