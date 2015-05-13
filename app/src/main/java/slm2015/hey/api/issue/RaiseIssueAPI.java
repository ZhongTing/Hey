package slm2015.hey.api.issue;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.PostBase;
import slm2015.hey.entity.Issue;

public class RaiseIssueAPI extends PostBase {
    public RaiseIssueAPI(Issue issue) {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/issue/raise");

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.setParam("subject", issue.getSubject());
        this.setParam("description", issue.getDescription());
        if (!issue.getPlace().isEmpty())
            this.setParam("place", issue.getPlace());

        this.TAG = "RaiseIssueAPI";
    }
}
