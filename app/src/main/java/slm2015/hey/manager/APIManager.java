package slm2015.hey.manager;

import android.app.Activity;

import slm2015.hey.api.APIBase;

public class APIManager {
    // static
    public static final String HEY_SERVER_BASE_URL = "http://140.124.181.195:8000";
    private static APIManager ourInstance = null;

    // private
    private String accessToken = "";

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

    public String getAccessToken() {
        return this.accessToken;
    }
}
