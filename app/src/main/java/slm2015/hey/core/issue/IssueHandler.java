package slm2015.hey.core.issue;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.api.issue.FetchIssueAPI;
import slm2015.hey.api.issue.RaiseIssueAPI;
import slm2015.hey.core.BaseAPIHandler;
import slm2015.hey.entity.Issue;
import slm2015.hey.util.Converter;

public class IssueHandler extends BaseAPIHandler {
    public IssueHandler(Context context) {
        super(context);
    }

    public void raise(Issue issue, final RaiseIssueHandlerCallback callback) {
        this.runAPI(new RaiseIssueAPI(issue), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                callback.onRaisedIssue();
            }
        });
    }

    public void fetch(Integer lastIssueId, final FetchIssueHandlerCallback callback) {
        this.runAPI(new FetchIssueAPI(lastIssueId), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                JSONArray issueJSONArray = jsonObject.getJSONArray("issues");
                callback.onReceiveIssues(parseIssueArray(issueJSONArray));
            }

            private List<Issue> parseIssueArray(JSONArray array) throws JSONException {
                List<Issue> list = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    Issue issue = new Issue();
                    issue.setId(jsonObject.getInt("id"));
                    issue.setSubject(jsonObject.getString("subject"));
                    issue.setDescription(jsonObject.getString("description"));
                    issue.setTimestamp(Converter.convertToDate(jsonObject.getString("timestamp")));
                    if (jsonObject.has("place"))
                        issue.setPlace(jsonObject.getString("place"));

                    if (jsonObject.has("photoURL"))
                        issue.setPhotoURL(jsonObject.getString("photoURL"));

                    issue.getTimestamp();
                    list.add(issue);
                }
                return list;
            }
        });
    }

    public interface FetchIssueHandlerCallback {
        void onReceiveIssues(List<Issue> issues);
    }

    public interface RaiseIssueHandlerCallback {
        void onRaisedIssue();
    }
}
