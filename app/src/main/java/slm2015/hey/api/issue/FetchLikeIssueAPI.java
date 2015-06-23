package slm2015.hey.api.issue;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.GetBase;

public class FetchLikeIssueAPI extends GetBase{
    public FetchLikeIssueAPI() {
        super(APIManager.HEY_SERVER_BASE_URL, "api/issue/like/fetch");

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.TAG = "FetchLikeIssueAPI";
    }
}
