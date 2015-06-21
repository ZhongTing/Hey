package slm2015.hey.api.issue;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.PostBase;

public class RegretLikeAPI extends PostBase {
    public RegretLikeAPI(int issueId) {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/issue/regret");

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.setParam("issueId", issueId + "");

        this.TAG = "RegretLikeAPI";
    }
}
