package slm2015.hey.api;

import slm2015.hey.util.LocalPreference;

public class APIManager {
    // static
    public static final String HEY_SERVER_BASE_URL = "http://140.124.181.195:8000";
    private static APIManager ourInstance = null;

    public static APIManager getInstance() {
        if (ourInstance == null)
            ourInstance = new APIManager();
        return ourInstance;
    }

    public void run(APIBase task) {
        Thread thread = new Thread(task);
        thread.start();
    }

    public String getAccessToken() {
        return LocalPreference.instance().getUserToken();
    }
}
