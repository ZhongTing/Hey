package slm2015.hey.api.issue;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.PostBase;
import slm2015.hey.entity.Issue;
import slm2015.hey.util.Helper;

public class RaiseIssueAPI extends PostBase {
    public RaiseIssueAPI(Issue issue, Boolean privacy) {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/issue/raise");

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.setParam("privacy", privacy.toString());
        this.setParam("subject", issue.getSubject());
        this.setParam("description", issue.getDescription());
        if (issue.getImage() != null) {
            this.setFileParam("photo", Helper.limitBitmapSize(issue.getImage()));
        }
        if (!issue.getPlace().isEmpty())
            this.setParam("place", issue.getPlace());

        this.TAG = "RaiseIssueAPI";
    }
}
