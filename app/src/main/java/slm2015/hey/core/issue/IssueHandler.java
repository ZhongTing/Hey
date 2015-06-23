package slm2015.hey.core.issue;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.api.issue.AddSelectorAPI;
import slm2015.hey.api.issue.FetchIssueAPI;
import slm2015.hey.api.issue.FetchLikeIssueAPI;
import slm2015.hey.api.issue.LikeAPI;
import slm2015.hey.api.issue.RaiseIssueAPI;
import slm2015.hey.api.issue.RegretLikeAPI;
import slm2015.hey.core.BaseAPIHandler;
import slm2015.hey.entity.Issue;
import slm2015.hey.util.Converter;

public class IssueHandler extends BaseAPIHandler {
    public IssueHandler(Context context) {
        super(context);
    }

    public void raise(Issue issue, boolean privacy, final RaiseIssueHandlerCallback callback) {
        this.runAPI(new RaiseIssueAPI(issue, privacy), new Callback() {
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
                    if (jsonObject.has("like"))
                        issue.setLikeCount(jsonObject.getInt("like"));
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

    public void fetchLike(final FetchIssueLikeHandlerCallback callback) {
        this.runAPI(new FetchLikeIssueAPI(), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                JSONArray issueJSONArray = jsonObject.getJSONArray("issues");
                callback.onReceiveLikeIssues(getLikeIssueId(issueJSONArray));
            }

            private List<Integer> getLikeIssueId(JSONArray array) throws JSONException {
                List<Integer> list = new ArrayList<Integer>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    list.add(jsonObject.getInt("id"));
                }
                return list;
            }
        });
    }

    public void like(int issueId) {
        this.runAPI(new LikeAPI(issueId), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                JSONArray issueJSONArray = jsonObject.getJSONArray("like_issue");
            }
        });
    }

    public void regretLike(int issueId) {
        this.runAPI(new RegretLikeAPI(issueId));
    }

    public void addSelector(final String selector, final AddSelectorCallBack callBack) {
        this.runAPI(new AddSelectorAPI(selector), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                callBack.onReceiveSelectorId(jsonObject.getInt("id"));
            }
        });
    }

    public interface FetchIssueHandlerCallback {
        void onReceiveIssues(List<Issue> issues);
    }

    public interface FetchIssueLikeHandlerCallback {
        void onReceiveLikeIssues(List<Integer> likeIssuesId);
    }

    public interface RaiseIssueHandlerCallback {
        void onRaisedIssue();
    }

    public interface AddSelectorCallBack {
        void onReceiveSelectorId(int id);
    }
}
