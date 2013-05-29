package com.lge.hotsearch;

public interface LoginView {

	void setUsername(String username);

	void disableLoginButton();

	void enableLoginButton();

	String getUsername();

	String getPassword();

	void enableProgress();

	void failAlert();

	void disableProgress();

	void launchMain();

}
