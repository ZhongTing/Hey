package slm2015.hey.api;

import org.json.JSONObject;

public class APIManager {
    // static
    public static final String HEY_SERVER_BASE_URL = "http://140.124.181.195:8000";
    private static APIManager ourInstance = null;

    // private
    private String accessToken = "";
    private JSONObject temp = null;

    private APIManager() {
        this.accessToken = "test";
    }

    public static APIManager getInstance() {
        if (ourInstance == null)
            ourInstance = new APIManager();
        return ourInstance;
    }

    public void run(APIBase task) {
        Thread thread = new Thread(task);
        thread.start();
    }

    public JSONObject getTemp() {
        return this.temp;
    }

    public void setTemp(JSONObject temp) {
        this.temp = temp;
    }

    public String getAccessToken() {
        return this.accessToken;
    }
}
