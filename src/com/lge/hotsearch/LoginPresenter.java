package com.lge.hotsearch;

public class LoginPresenter {

	private LoginModel model;
	private LoginView view;
	private boolean canBeLogin = true;

	public LoginPresenter(LoginModel model, LoginView view) {
		this.model = model;
		this.view = view;
	}

	public void initialize() {
		view.setUsername(model.getLastUsername());
		view.disableLoginButton();
	}

	public void verifyLogin() {
		boolean canBeLogin = !view.getUsername().trim().equals("") 
				&& !view.getPassword().equals("");
		if (this.canBeLogin == canBeLogin)
			return;
		
		if (canBeLogin) {
			view.enableLoginButton();
		} else {
			view.disableLoginButton();
		}
		this.canBeLogin = canBeLogin;
	}

	public void login() {
		view.disableLoginButton();
		view.enableProgress();
		model.login(view.getUsername(), view.getPassword(), new LoginModel.Callback() {
			
			@Override
			public void onFail() {
				loginFailed();
			}

			@Override
			public void onSuccess() {
				loginSuccess();
			}
		});
	}

	public void loginFailed() {
		view.disableProgress();
		view.enableLoginButton();
		view.failAlert();
	}

	public void loginSuccess() {
		view.disableProgress();
		view.enableLoginButton();
		view.launchMain();
	}

}
