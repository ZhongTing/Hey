package slm2015.hey.view.tabs.watch;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import slm2015.hey.core.Observer;
import slm2015.hey.core.issue.IssueLoader;
import slm2015.hey.entity.Issue;

public class WatchManager implements Observer {
    private IssueLoader issueLoader;
    private Queue<Issue> newIssues;
    private ArrayList<Issue> oldIssues;
    private OnReloaded onReloaded;
    private List<OnReloaded> observers;
    private ArrayList<Issue> issuesForWatch;
    private ArrayList<Issue> modifiedIssues;

    public WatchManager(Context context) {
        this.issueLoader = new IssueLoader(context);
        this.issueLoader.addObserver(this);
        this.newIssues = new LinkedList<>();
        this.oldIssues = new ArrayList<>();
        this.issuesForWatch = new ArrayList<>();
        this.modifiedIssues = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public void reload() {
        this.issueLoader.loadNewIssues();
    }

    public ArrayList<Issue> getIssues() {
//        ArrayList<Issue> issues = new ArrayList<>();
//        issues.addAll(this.oldIssues);
//        issues.addAll(this.newIssues);
        return this.oldIssues;
    }

    public Queue<Issue> getNewIssues() {
        return newIssues;
    }

    public ArrayList<Issue> getHistory() {
        ArrayList<Issue> issues = new ArrayList<>();
        issues.addAll(this.oldIssues);
        issues.addAll(this.modifiedIssues);
        issues.addAll(this.newIssues);
        return issues;
    }

    public ArrayList<Issue> getModifiedIssues() {
        return modifiedIssues;
    }

    @Override
    public void onSubjectChanged() {
        cloneToSelf();
        this.issueLoader.clearIssues();
        this.issuesForWatch = (ArrayList) this.oldIssues.clone();
        for (OnReloaded reloaded : observers) {
            if (reloaded != null)
                reloaded.notifyReloaded();
        }
    }

    private void cloneToSelf() {
        while (this.issueLoader.getNewIssues().size() > 0)
            this.newIssues.offer(this.issueLoader.getNewIssues().poll());
        ArrayList<Issue> temp = this.issueLoader.getIssues();
        this.oldIssues.addAll((ArrayList) temp.clone());
    }

    public void setOnReloaded(OnReloaded onReloaded) {
        this.onReloaded = onReloaded;
    }

    public IssueLoader getIssueLoader() {
        return issueLoader;
    }

    public ArrayList<Issue> getIssuesForWatch() {
        return (ArrayList) this.oldIssues.clone();
    }

    public interface OnReloaded {
        void notifyReloaded();
    }

    public void addObserver(OnReloaded onReloaded) {
        this.observers.add(onReloaded);
    }

    //poll from new Issues to issues
    public void pushToIssues() {
        Issue issue = this.newIssues.poll();
        this.oldIssues.add(issue);
    }
}
