package slm2015.hey.api.issue;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import slm2015.hey.api.MultipartPost;
import slm2015.hey.entity.Issue;
import slm2015.hey.manager.APIManager;

public class RaiseIssueAPI extends MultipartPost {
    public RaiseIssueAPI(Issue issue, Callback callback) {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/issue/raise", callback);

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.setParam("actor", issue.getSubject());
        this.setParam("event", issue.getDescription());
        if (!issue.getPosition().isEmpty())
            this.setParam("place", issue.getPosition());

        this.TAG = "RaiseIssueAPI";
    }

    @Override
    protected void runSuccess(JSONObject object) throws JSONException {
        Toast.makeText(this.activity, "訊息成功送出", Toast.LENGTH_SHORT).show();
        super.runSuccess(object);
    }
}
