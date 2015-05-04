package slm2015.hey.api;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.net.ssl.SSLPeerUnverifiedException;

public abstract class APIBase implements Runnable {
    protected String TAG = "?????API";

    protected String requestUrl;
    protected HttpRequestBase httpRequest;
    private Runnable successCallback;
    private Runnable failCallback;
    private Runnable errorCallback;
    private String response;

    public APIBase(String baseUrl, String action) {
        this.requestUrl = baseUrl + action;
        Log.d("APIBase", this.requestUrl);
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

    protected abstract void doInit() throws UnsupportedEncodingException;

    private void runOnUiThread(Runnable task) {
        if (task != null)
            new Handler(Looper.getMainLooper()).post(task);
    }

    @Override
    public void run() {
        try {
            this.initConnectionParams();

            this.doInit();

            final HttpResponse res = new DefaultHttpClient().execute(this.httpRequest);
            HttpEntity entity = res.getEntity();
            Integer status = res.getStatusLine().getStatusCode();
            this.response = entity == null ? "" : EntityUtils.toString(entity, "utf-8");

            Log.d(TAG, status.toString());
            Log.d(TAG, response);
            if (status == 200)
                this.runOnUiThread(successCallback);
            else
                this.runOnUiThread(failCallback);

        } catch (SSLPeerUnverifiedException e) {
            Log.d("APIBase", "SSLPeerUnverifiedException");
            run();

        } catch (IOException e) {
            e.printStackTrace();
            this.runOnUiThread(errorCallback);
        }
    }

    public String getResponse() {
        return response;
    }

    public void setSuccessCallback(Runnable onSuccess) {
        this.successCallback = onSuccess;
    }

    public void setFailCallback(Runnable onFail) {
        this.failCallback = onFail;
    }

    public void setErrorCallback(Runnable onError) {
        this.errorCallback = onError;
    }
}

