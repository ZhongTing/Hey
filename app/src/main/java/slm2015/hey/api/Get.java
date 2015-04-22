package slm2015.hey.api;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Get extends APIBase {
    protected List<NameValuePair> params;

    public Get(String baseUrl, String action, Callback callback) {
        super(baseUrl, action, callback);

        this.httpRequest = new HttpGet(this.requestUrl);

        this.params = new ArrayList<>();
    }

    protected void setParam(String key, String value) {
        this.params.add(new BasicNameValuePair(key, value));
    }

    @Override
    protected void doInit() throws UnsupportedEncodingException {
        String params = URLEncodedUtils.format(this.params, "utf-8");
        String newUrl = this.requestUrl + "?" + params;
        this.httpRequest.setURI(URI.create(newUrl));
        Log.d("APIBase Get", newUrl);
    }
}
