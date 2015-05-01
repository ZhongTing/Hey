package slm2015.hey.api.user;

import org.json.JSONException;
import org.json.JSONObject;

import slm2015.hey.api.Get;
import slm2015.hey.api.APIManager;

public class PullRecommendsAPI extends Get {
    public PullRecommendsAPI(Callback callback) {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/user/pull/recommends", callback);

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.TAG = "PullRecommendsAPI";
    }

    @Override
    protected void runSuccess(JSONObject object) throws JSONException {
        APIManager.getInstance().setTemp(object);
        super.runSuccess(object);
    }
}
