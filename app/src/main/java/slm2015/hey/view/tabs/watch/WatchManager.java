package slm2015.hey.view.tabs.watch;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import slm2015.hey.core.Observer;
import slm2015.hey.core.issue.IssueLoader;
import slm2015.hey.entity.Issue;

public class WatchManager implements Observer {
    private IssueLoader issueLoader;
    private Queue<Issue> newIssues;
    private ArrayList<Issue> oldIssues;
    private OnReloaded onReloaded;

    public WatchManager(Context context) {
        this.issueLoader = new IssueLoader(context);
        this.issueLoader.addObserver(this);
        this.newIssues = new LinkedList<>();
        this.oldIssues = new ArrayList<>();
    }

    public void reload() {
        this.issueLoader.loadNewIssues();
    }

    public ArrayList<Issue> getIssues() {
        ArrayList<Issue> issues = new ArrayList<>();
        issues.addAll(this.oldIssues);
        issues.addAll(this.newIssues);
        return issues;
    }

    @Override
    public void onSubjectChanged() {
        this.newIssues = this.issueLoader.getNewIssues();
        this.oldIssues = this.issueLoader.getIssues();
        if(this.onReloaded!=null)
            this.onReloaded.notifyReloaded();
    }

    public void setOnReloaded(OnReloaded onReloaded) {
        this.onReloaded = onReloaded;
    }

    public IssueLoader getIssueLoader() {
        return issueLoader;
    }

    public interface OnReloaded {
        void notifyReloaded();
    }
}
