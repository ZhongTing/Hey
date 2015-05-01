package slm2015.hey.api;

import android.app.Activity;

import org.json.JSONObject;

public class APIManager {
    // static
    public static final String HEY_SERVER_BASE_URL = "http://140.124.181.195:8000";
    private static APIManager ourInstance = null;

    // private
    private String accessToken = "";
    private JSONObject temp = null;

    public static APIManager getInstance() {
        if (ourInstance == null)
            ourInstance = new APIManager();
        return ourInstance;
    }

    private APIManager() {
        this.accessToken = "test";
    }

    public void run(Activity activity, APIBase task) {
        task.setActivity(activity);
        Thread thread = new Thread(task);
        thread.start();
    }

    public void setTemp(JSONObject temp) {
        this.temp = temp;
    }

    public JSONObject getTemp() {
        return this.temp;
    }

    public String getAccessToken() {
        return this.accessToken;
    }
}
