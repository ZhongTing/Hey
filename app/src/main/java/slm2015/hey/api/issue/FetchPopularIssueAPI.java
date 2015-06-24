package slm2015.hey.api.issue;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.GetBase;

public class FetchPopularIssueAPI extends GetBase{
    public FetchPopularIssueAPI() {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/issue/popular/fetch");

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.TAG = "FetchPopularIssueAPI";
    }
}
