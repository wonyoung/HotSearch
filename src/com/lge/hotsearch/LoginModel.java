package com.lge.hotsearch;

public interface LoginModel {

	public interface Callback {

		void onFail();

		void onSuccess();

	}

	String getLastUsername();

	void login(String username, String password, LoginModel.Callback callback);

}
