package slm2015.hey.core.issue;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public void raise(Issue issue, boolean privacy, final RaiseIssueHandlerCallback callback, boolean showLoading) {
        this.runAPI(new RaiseIssueAPI(issue, privacy), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                callback.onRaisedIssue();
            }
        }, showLoading);
    }

    public void fetch(Integer lastIssueId, final FetchIssueHandlerCallback callback, boolean showLoading) {
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
        }, showLoading);
    }

    public void fetchLike(final FetchIssueLikeHandlerCallback callback) {
        this.runAPI(new FetchLikeIssueAPI(), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                JSONArray issueJSONArray = jsonObject.getJSONArray("issues");
                callback.onReceiveLikeIssues(getLikeIssueId(issueJSONArray));
            }

            private List<int[]> getLikeIssueId(JSONArray array) throws JSONException {
                List<int[]> list = new ArrayList<int[]>();
                for (int i = 0; i < array.length(); i++) {
                    int[] data = new int[2];
                    JSONObject jsonObject = array.getJSONObject(i);
                    data[0] = jsonObject.getInt("id");
                    data[1] = jsonObject.getInt("like");
                    list.add(data);
                }
                return list;
            }
        });
    }

    public void like(final int issueId, final LikeIssueHandlerCallback callback) {
        this.runAPI(new LikeAPI(issueId), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                List<int[]> likeIssueIdList = new ArrayList<int[]>();
                likeIssueIdList.add(new int[]{issueId, jsonObject.getInt("count")});
                callback.onReceiveLikeIssue(likeIssueIdList);
            }
        });
    }

    public void regretLike(final int issueId, final RegretLikeIssueHandlerCallback callback) {
        this.runAPI(new RegretLikeAPI(issueId), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                List<int[]> regretLikeIssueIdList = new ArrayList<int[]>();
                regretLikeIssueIdList.add(new int[]{issueId, jsonObject.getInt("count")});
                callback.onReceiveRegretLikeIssue(regretLikeIssueIdList);
            }
        });
    }

    public interface LikeIssueHandlerCallback {
        void onReceiveLikeIssue(List<int[]> likeIssueId);
    }

    public interface RegretLikeIssueHandlerCallback {
        void onReceiveRegretLikeIssue(List<int[]> regretLikeIssueId);
    }

    public interface FetchIssueHandlerCallback {
        void onReceiveIssues(List<Issue> issues);
    }

    public interface FetchIssueLikeHandlerCallback {
        void onReceiveLikeIssues(List<int[]> likeIssuesId);
    }

    public interface RaiseIssueHandlerCallback {
        void onRaisedIssue();
    }
}
