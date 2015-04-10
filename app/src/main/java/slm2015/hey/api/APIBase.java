package slm2015.hey.api;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.net.ssl.SSLPeerUnverifiedException;

public abstract class APIBase implements Runnable {
    public static final String NETWORK_ERROR = "network error!!!";
    protected static int NO_CONTENT = 204;

    protected String TAG = "?????API";

    protected APICallback callback;
    protected Activity activity = null;

    protected String requestUrl;
    protected HttpRequestBase httpRequest;

    public APIBase(String baseUrl, String action, APICallback callback) {
        this.requestUrl = baseUrl + action;
        Log.d("APIBase", this.requestUrl);

        this.callback = callback;
    }

    private void initConnectionParams() {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setSoTimeout(httpParams, 30 * 1000);
        HttpConnectionParams.setConnectionTimeout(httpParams, 30 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 30 * 1000);
    }

    protected void setHeader(String key, String value) {
        this.httpRequest.setHeader(key, value);
    }

    protected void doCallback(int status, String result) throws JSONException {
        Log.d(TAG, result);

        JSONObject object = new JSONObject(result);
        if (object.has("error")) {
            this.callback.requestCallback(false, object.getString("error"));
        } else {
            this.callback.requestCallback(true, object);
        }
    }

    protected abstract void doInit() throws UnsupportedEncodingException;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            this.initConnectionParams();

            this.doInit();

            final HttpResponse res = new DefaultHttpClient().execute(this.httpRequest);
            HttpEntity entity = res.getEntity();
            final int status = res.getStatusLine().getStatusCode();
            final String response = entity == null ? "" : EntityUtils.toString(entity, "utf-8");

            final APIBase apiBase = this;
            Runnable callback = new Runnable() {
                @Override
                public void run() {
                    try {
                        apiBase.doCallback(status, response);

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            };

            if (activity == null)
                callback.run();
            else
                activity.runOnUiThread(callback);

        } catch (SSLPeerUnverifiedException e) {
            Log.d("APIBase", "SSLPeerUnverifiedException");
            run();
        } catch (IOException e) {
            e.printStackTrace();
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "請檢查網路是否正常！", Toast.LENGTH_LONG).show();
                        try {
                            callback.requestCallback(false, NETWORK_ERROR);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        }
    }
}

