package slm2015.hey.core.issue;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import slm2015.hey.core.BaseLoader;
import slm2015.hey.entity.Issue;

public class IssueLoader extends BaseLoader {
    private IssueHandler issueHandler;
    private Integer lastIssueId = null;
    private ArrayList<Issue> issues = new ArrayList<>();
    private ArrayList<Issue> historyIssues = new ArrayList<>();
    private Queue<Issue> newIssues = new LinkedList<>();

    public IssueLoader(Context context) {
        this.issueHandler = new IssueHandler(context);
    }

    public void loadNewIssues() {
        this.issueHandler.fetch(lastIssueId, new IssueHandler.FetchIssueHandlerCallback() {
            @Override
            public void onReceiveIssues(List<Issue> issues) {
                if (issues.size() > 0) {
                    Issue lastIssue = issues.get(issues.size() - 1);
                    IssueLoader.this.lastIssueId = lastIssue.getId();
                    IssueLoader.this.newIssues.addAll(issues);
                    IssueLoader.this.historyIssues.addAll(issues);
                    IssueLoader.this.issues.addAll(issues);
                }

                notifyLoaderChanged();
            }
        });
//        this.issueHandler.fetchLike(new IssueHandler.FetchIssueLikeHandlerCallback() {
//            @Override
//            public void onReceiveLikeIssues(List<Integer> likeIssuesId) {
//                boolean test = false;
//            }
//        });
    }

    public ArrayList<Issue> getIssues() {
        return this.issues;
    }

    public ArrayList<Issue> getHistoryIssues() {
        return this.historyIssues;
    }

    public Queue<Issue> getNewIssues() {
        return newIssues;
    }

    public void clearIssues() {
        this.newIssues.clear();
        this.issues.clear();
    }
}
