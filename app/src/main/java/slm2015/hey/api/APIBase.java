package slm2015.hey.api;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import junit.framework.Assert;

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
    protected String TAG = "?????API";

    protected Callback callback;

    protected String requestUrl;
    protected HttpRequestBase httpRequest;
    private Activity activity;

    public APIBase(String baseUrl, String action, Callback callback) {
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

    protected void runSuccess(JSONObject object) throws JSONException {
        this.callback.requestSuccess(object);
    }

    protected void runFail(JSONObject object) throws JSONException {
        this.callback.requestFail();
    }

    protected abstract void doInit() throws UnsupportedEncodingException;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        Assert.assertNotNull(activity);

        try {
            this.initConnectionParams();

            this.doInit();

            final HttpResponse res = new DefaultHttpClient().execute(this.httpRequest);
            HttpEntity entity = res.getEntity();
            final int status = res.getStatusLine().getStatusCode();
            final String response = entity == null ? "" : EntityUtils.toString(entity, "utf-8");

            Log.d(TAG, response);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject result = object.has("result") ? object.getJSONObject("result") : null;

                        if (object.getString("status").equals("success")) {
                            APIBase.this.runSuccess(result);

                        } else {
                            APIBase.this.runFail(result);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (SSLPeerUnverifiedException e) {
            Log.d("APIBase", "SSLPeerUnverifiedException");
            run();

        } catch (IOException e) {
            e.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "網路發生錯誤！", Toast.LENGTH_SHORT).show();
                    APIBase.this.callback.requestFail();
                }
            });
        }
    }

    public interface Callback {
        public void requestSuccess(JSONObject result) throws JSONException;

        public void requestFail();
    }
}

