package slm2015.hey.api.user;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.GetBase;

public class PullRecommendsAPI extends GetBase {
    public PullRecommendsAPI() {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/user/pull/recommends");

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.TAG = "PullRecommendsAPI";
    }
}
