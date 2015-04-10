package slm2015.hey.api;

import android.util.Log;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class JsonObjectPost extends APIBase {
    JSONObject params;

    public JsonObjectPost(String baseUrl, String action, APICallback callback) {
        super(baseUrl, action, callback);

        this.httpRequest = new HttpPost(this.requestUrl);

        params = new JSONObject();
    }

    protected void setParam(String key, String value) {
        try {
            this.params.put(key, value);

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    protected void setStringList(String key, List<String> list) {
        try {
            JSONArray array = new JSONArray();
            for (String value : list) {
                array.put(value);
            }
            this.params.put(key, array);

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    protected void setIntegerList(String key, List<Integer> list) {
        try {
            JSONArray array = new JSONArray();
            for (Integer value : list) {
                array.put(value);
            }
            this.params.put(key, array);

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @Override
    protected void doInit() throws UnsupportedEncodingException {
        this.httpRequest.setHeader("Accept", "application/json");
        this.httpRequest.setHeader("Content-type", "application/json");

        ((HttpPost) this.httpRequest).setEntity(new StringEntity(this.params.toString(), "UTF-8"));
        Log.d("APIBase JSON Post", this.params.toString());
    }
}
