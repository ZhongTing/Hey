package slm2015.hey.core;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import slm2015.hey.R;
import slm2015.hey.api.APIBase;
import slm2015.hey.api.APIManager;
import slm2015.hey.view.util.UiUtility;

public abstract class BaseAPIHandler {
    private Context context;

    public BaseAPIHandler(Context context) {
        this.context = context;
    }

    protected void runAPI(APIBase api) {
        this.runAPI(api, false);
    }

    protected void runAPI(APIBase api, final boolean showLoadingEffect) {
        this.runAPI(api, new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                Log.d("BaseAPIHandler", "No override on Success Method");
            }
        }, showLoadingEffect);
    }

    protected void runAPI(final APIBase api, final Callback callback) {
        this.runAPI(api, callback, false);
    }

    protected void runAPI(final APIBase api, final Callback callback, final boolean showLoadingEffect) {
        if (showLoadingEffect) UiUtility.showLoading(context);
        api.setSuccessCallback(new Runnable() {
            @Override
            public void run() {
                if (showLoadingEffect) UiUtility.stopLoading(context);
                try {
                    JSONObject jsonObject = new JSONObject(api.getResponse());
                    callback.onSuccess(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        api.setFailCallback(new Runnable() {
            @Override
            public void run() {
                if (showLoadingEffect) UiUtility.stopLoading(context);
                callback.onFail(api.getResponse());
            }
        });

        api.setErrorCallback(new Runnable() {
            @Override
            public void run() {
                if (showLoadingEffect) UiUtility.stopLoading(context);
                callback.onError();
            }
        });

        APIManager.getInstance().run(api);
    }

    public abstract class Callback {
        public abstract void onSuccess(JSONObject jsonObject) throws JSONException;

        public void onFail(String response) {
            Toast.makeText(BaseAPIHandler.this.context, R.string.bad_request, Toast.LENGTH_SHORT).show();
        }

        public void onError() {
            Toast.makeText(BaseAPIHandler.this.context, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }
}
