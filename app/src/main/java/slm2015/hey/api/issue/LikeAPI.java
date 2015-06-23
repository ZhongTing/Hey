package slm2015.hey.api.issue;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.PostBase;

public class LikeAPI extends PostBase {
    public LikeAPI(int issueId) {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/issue/like");

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.setParam("issueId", String.valueOf(issueId));

        this.TAG = "LikeAPI";
    }
}
