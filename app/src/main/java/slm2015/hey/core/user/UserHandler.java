package slm2015.hey.core.user;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import slm2015.hey.api.user.LoginAPI;
import slm2015.hey.api.user.RegisterAPI;
import slm2015.hey.api.user.RegisterGcmAPI;
import slm2015.hey.core.BaseAPIHandler;
import slm2015.hey.util.LocalPreference;

public class UserHandler extends BaseAPIHandler {
    public UserHandler(Context context) {
        super(context);
    }

    public void login(String imei, final LoginListener listener) {
        this.runAPI(new LoginAPI(imei), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                loginSuccess(jsonObject, listener);
            }

            @Override
            public void onFail(String response) {
                listener.onLoginFail();
            }


        });
    }

    public void register(String imei, String registerCode, final LoginListener listener) {
        this.runAPI(new RegisterAPI(imei, registerCode), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                loginSuccess(jsonObject, listener);
            }

            @Override
            public void onFail(String response) {
                listener.onRegisterCodeError();
            }
        });
    }

    public void registerGcm(String gcmToken) {
        this.runAPI(new RegisterGcmAPI(gcmToken));
    }

    private void loginSuccess(JSONObject jsonObject, LoginListener listener) {
        try {
            LocalPreference.instance().setUserToken(jsonObject.getString("token"));
            listener.onLoginSuccess();
        } catch (Exception e) {
            listener.onLoginFail();
        }
    }

    public interface LoginListener {
        void onLoginSuccess();

        void onLoginFail();

        void onRegisterCodeError();
    }
}
