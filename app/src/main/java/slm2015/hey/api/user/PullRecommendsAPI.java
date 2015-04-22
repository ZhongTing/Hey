package slm2015.hey.api.user;

import slm2015.hey.api.Get;
import slm2015.hey.manager.APIManager;

public class PullRecommendsAPI extends Get {
    public PullRecommendsAPI(Callback callback) {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/user/pull/recommends", callback);

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.TAG = "PullRecommendsAPI";
    }
}
