package slm2015.hey.view.tabs.watch;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.core.Observer;
import slm2015.hey.core.issue.IssueLoader;
import slm2015.hey.entity.Issue;
import slm2015.hey.entity.Selector;

public class WatchManager implements Observer {
    private IssueLoader issueLoader;
    private ArrayList<Issue> newIssues;
    private ArrayList<Issue> oldIssues;
    private ArrayList<Issue> allIssues;
    private OnReloaded onReloaded;
    private List<OnReloaded> observers;
    private ArrayList<Issue> issuesForWatch;
    private ArrayList<Issue> modifiedIssues;
    private ArrayList<Selector> selectors;

    public WatchManager(Context context) {
        this.issueLoader = new IssueLoader(context);
        this.issueLoader.addObserver(this);
        this.newIssues = new ArrayList<>();
        this.oldIssues = new ArrayList<>();
        this.issuesForWatch = new ArrayList<>();
        this.modifiedIssues = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.selectors = new ArrayList<>();
        this.allIssues = new ArrayList<>();
    }

    public void reload() {
        this.issueLoader.loadNewIssues();
    }

    // carddeck used
    public ArrayList<Issue> getIssues() {
        return selectorList(this.oldIssues);
    }

    // carddeck used
    public ArrayList<Issue> getNewIssues() {
        return selectorList(this.newIssues);
    }

    // history used
    public ArrayList<Issue> getHistory() {
//        ArrayList<Issue> issues = new ArrayList<>();
//        issues.addAll(getIssues());
//        issues.addAll(this.modifiedIssues);
//        issues.addAll(getNewIssues());
        return selectorList(this.allIssues);
    }

    public ArrayList<Issue> getModifiedIssues() {
        return modifiedIssues;
    }

    @Override
    public void onLoaderChanged() {
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
            this.newIssues.add(this.newIssues.size(), this.issueLoader.getNewIssues().poll());
        ArrayList<Issue> temp = this.issueLoader.getIssues();
        this.oldIssues.addAll((ArrayList) temp.clone());
        //code below are new architecture test by kevinho
        this.allIssues.addAll((ArrayList<Issue>) this.newIssues.clone());
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
        Issue issue = pollNewIssues();
        this.oldIssues.add(issue);
    }

    public void addSelector(Selector selector) {
        this.selectors.add(selector);
    }

    private ArrayList<Issue> selectorList(ArrayList<Issue> issues) {
        ArrayList<Issue> results = new ArrayList<>();
        results.addAll((ArrayList<Issue>) issues.clone());
        for (Issue issue : issues) {
            boolean contains = false;
            for (Selector selector : this.selectors) {
                String content = selector.getContent();
                contains = selector.isFilter() && (issue.getSubject().contains(content) || issue.getDescription().contains(content));
                if (contains)
                    break;
            }
            if (!contains)
                results.remove(issue);
        }
        if (needToSelect())
            return results;
        return issues;
    }

    public boolean needToSelect() {
        for (Selector selector : this.selectors)
            if (selector.isFilter())
                return true;
        return false;
    }

    public Issue pollNewIssues() {
        Issue issue = this.newIssues.get(0);
        this.newIssues.remove(0);
        return issue;
    }

    public void removeIssue(Issue issue) {
        if (this.oldIssues.contains(issue))
            this.oldIssues.remove(issue);

    }
}
