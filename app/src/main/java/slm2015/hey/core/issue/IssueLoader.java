package slm2015.hey.core.issue;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import slm2015.hey.core.Subject;
import slm2015.hey.entity.Issue;

public class IssueLoader extends Subject {
    private IssueHandler issueHandler;
    private Integer lastIssueId = null;
    private Queue<Issue> issueQueue = new LinkedList<>();

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
                    IssueLoader.this.issueQueue.addAll(issues);
                }

                notifySubjectChanged();
            }
        });
    }

    public Queue<Issue> getIssueQueue() {
        return issueQueue;
    }
}
