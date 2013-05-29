import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.lge.hotsearch.LoginModel;
import com.lge.hotsearch.LoginPresenter;
import com.lge.hotsearch.LoginView;


public class LoginPresenterTest {

	private LoginModel model;
	private LoginView view;
	private LoginPresenter loginPresenter;

	@Before
	public void setUp() throws Exception {
		model = mock(LoginModel.class);
		view = mock(LoginView.class);
		loginPresenter = new LoginPresenter(model, view);
	}
	
	@Test
	public void showsLastLoginSuccessedNameWhenPresenterInit() throws Exception {
		when(model.getLastUsername()).thenReturn("lastname");

		loginPresenter.initialize();
		
		verify(view).setUsername("lastname");
		verify(view).disableLoginButton();
	}

	@Test
	public void changesToEnabledLoginButtonWhenIdAndPasswordEntered() throws Exception {
		when(view.getUsername()).thenReturn("id");
		when(view.getPassword()).thenReturn("");
		loginPresenter.verifyLogin();
		
		when(view.getPassword()).thenReturn("pw");
		loginPresenter.verifyLogin();
		
		InOrder order = inOrder(view);
		order.verify(view).disableLoginButton();
		order.verify(view).enableLoginButton();
	}

	@Test
	public void changesLoginButtonDisabledOnceWhenIdChangedToEmpty() throws Exception {
		when(view.getUsername()).thenReturn("");
		when(view.getPassword()).thenReturn("password");
		loginPresenter.verifyLogin();
		loginPresenter.verifyLogin();
		
		InOrder order = inOrder(view);
		order.verify(view).disableLoginButton();
		order.verify(view, never()).disableLoginButton();
	}

	@Test
	public void showsProgressWhenLogin() throws Exception {
		when(view.getUsername()).thenReturn("id");
		when(view.getPassword()).thenReturn("password");
		
		loginPresenter.login();
		
		verify(view).enableProgress();
		verify(model).login(eq("id"), eq("password"), any(LoginModel.Callback.class));
		verify(view).disableLoginButton();
	}
	
	@Test
	public void showsFailToastWhenLogFailRecieved() throws Exception {
		loginPresenter.loginFailed();
		
		verify(view).failAlert();
		verify(view).enableLoginButton();
		verify(view).disableProgress();
	}
	
	@Test
	public void showsFailToastWhenLoginFailed() throws Exception {
		when(view.getUsername()).thenReturn("id");
		when(view.getPassword()).thenReturn("password");
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object [] args = invocation.getArguments();
				LoginModel.Callback callback = (LoginModel.Callback) args[2];
				if (callback != null) {
					callback.onFail();
				}
				return null;
			}
		}).when(model).login(eq("id"), eq("password"), any(LoginModel.Callback.class));
		
		loginPresenter.login();
		
		verify(view).enableProgress();
		verify(model).login(eq("id"), eq("password"), any(LoginModel.Callback.class));
		verify(view).disableLoginButton();
		
		verify(view).failAlert();
		verify(view).enableLoginButton();
		verify(view).disableProgress();
	}
	
	@Test
	public void launchesMainWhenLogSuccessRecieved() throws Exception {
		loginPresenter.loginSuccess();
		
		verify(view).disableProgress();
		verify(view).enableLoginButton();
		verify(view).launchMain();
	}

	@Test
	public void launchesMainWhenLogSuccess() throws Exception {
		when(view.getUsername()).thenReturn("id");
		when(view.getPassword()).thenReturn("password");
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object [] args = invocation.getArguments();
				LoginModel.Callback callback = (LoginModel.Callback) args[2];
				if (callback != null) {
					callback.onSuccess();
				}
				return null;
			}
		}).when(model).login(eq("id"), eq("password"), any(LoginModel.Callback.class));
		
		loginPresenter.login();
		
		verify(view).enableProgress();
		verify(model).login(eq("id"), eq("password"), any(LoginModel.Callback.class));
		verify(view).disableLoginButton();
		
		verify(view).disableProgress();
		verify(view).enableLoginButton();
		verify(view).launchMain();
	}
}
