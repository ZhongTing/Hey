package slm2015.hey.api.issue;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.MultipartPost;
import slm2015.hey.entity.Issue;

public class RaiseIssueAPI extends MultipartPost {
    public RaiseIssueAPI(Issue issue) {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/core/raise");

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.setParam("actor", issue.getSubject());
        this.setParam("event", issue.getDescription());
        if (!issue.getPlace().isEmpty())
            this.setParam("place", issue.getPlace());

        this.TAG = "RaiseIssueAPI";
    }
}
