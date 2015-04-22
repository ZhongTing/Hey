package slm2015.hey.manager;

import android.util.Log;

import org.json.JSONException;

import slm2015.hey.api.APICallback;
import slm2015.hey.api.user.PullRecommendsAPI;

public class APIManager {
    public static final String HEY_SERVER_BASE_URL = "http://140.124.181.195:8000";
    private static APIManager ourInstance = new APIManager();

    private String accessToken = "test";

    public static APIManager getInstance() {
        return ourInstance;
    }

    private APIManager() {
    }

    public void test(){
        Log.d("APIManager", "start");
        Thread thread = new Thread(new PullRecommendsAPI(new APICallback() {
            @Override
            public void requestCallback(boolean isValid, Object result) throws JSONException {
                Log.d("PullRecommendsAPI", "finish");
            }
        }));
        thread.start();
    }

    public String getAccessToken() {
        return this.accessToken;
    }
}
