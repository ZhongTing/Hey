package slm2015.hey.api.issue;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.GetBase;

public class FetchIssueAPI extends GetBase {
    public FetchIssueAPI(Integer lastIssueId) {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/issue/fetch");

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        if (lastIssueId != null)
            this.setParam("lastFetchIssueId", lastIssueId.toString());

        this.TAG = "FetchIssueAPI";
    }
}
