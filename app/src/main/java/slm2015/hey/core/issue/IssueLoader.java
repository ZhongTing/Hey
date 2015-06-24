package slm2015.hey.core.issue;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import slm2015.hey.core.BaseLoader;
import slm2015.hey.entity.Issue;
import slm2015.hey.util.LocalPreference;

public class IssueLoader extends BaseLoader {
    private IssueHandler issueHandler;
    private Integer lastIssueId = null;
    private ArrayList<Issue> issues = new ArrayList<>();
    private ArrayList<Issue> historyIssues = new ArrayList<>();
    private Queue<Issue> newIssues = new LinkedList<>();
    private ArrayList<Issue> popularIssues = new ArrayList<>();

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
                String moveToTopIssueId = LocalPreference.instance().getMoveToTopIssueId();
                if (moveToTopIssueId != null) {
                    LocalPreference.instance().setMoveToTopIssueId(null);
                    moveIssueToTop(moveToTopIssueId);
                }

                notifyLoaderChanged();
            }
        }, true);
        loadPopularIssues();
        this.issueHandler.fetchLike(new IssueHandler.FetchIssueLikeHandlerCallback() {
            @Override
            public void onReceiveLikeIssues(List<int[]> likeIssuesId) {
                modifyLikeCount(likeIssuesId);
                notifyLoaderChanged();
            }
        });
    }

    private void moveIssueToTop(String issueId) {
        for (Issue issue : this.issues) {
            if (issue.getId().toString().equals(issueId)) {
                this.issues.remove(issue);
                this.issues.add(issue);
                break;
            }
        }
    }

    public void loadPopularIssues() {
        this.issueHandler.fetchPopular(new IssueHandler.FetchPopularIssueHandlerCallback() {
            @Override
            public void onReceivePopularIssue(List<Issue> issues) {
                if (issues.size() > 0) {
                    IssueLoader.this.popularIssues.addAll(issues);
                }
            }
        });
    }

    public void likeIssue(Issue issue) {
        this.issueHandler.like(issue.getId(), new IssueHandler.LikeIssueHandlerCallback() {
            @Override
            public void onReceiveLikeIssue(List<int[]> likeIssueId) {
                modifyLikeCount(likeIssueId);
                notifyLoaderChanged();
            }
        });
    }

    public void regretLikeIssue(Issue issue) {
        this.issueHandler.regretLike(issue.getId(), new IssueHandler.RegretLikeIssueHandlerCallback() {
            @Override
            public void onReceiveRegretLikeIssue(List<int[]> regretLikeIssueId) {
                modifyLikeCount(regretLikeIssueId);
                notifyLoaderChanged();
            }
        });
    }

    public ArrayList<Issue> getIssues() {
        return this.issues;
    }

    public ArrayList<Issue> getHistoryIssues() {
        return this.historyIssues;
    }

    private void modifyLikeCount(List<int[]> likeIssueIdList) {
        for (Issue issue : this.historyIssues) {
            for (int[] likeIssue : likeIssueIdList) {
                if (issue.getId() == likeIssue[0])
                    issue.setLikeCount(likeIssue[1]);
            }
        }
    }

    public ArrayList<Issue> getPopularIssues() {
        return popularIssues;
    }
}
