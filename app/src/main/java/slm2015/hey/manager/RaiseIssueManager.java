package slm2015.hey.manager;

import java.util.ArrayList;

import slm2015.hey.entity.Issue;
import slm2015.hey.entity.Term;

public class RaiseIssueManager {
    private ArrayList<Term> nounList = new ArrayList<Term>();
    private ArrayList<Term> adjList = new ArrayList<Term>();
    private ArrayList<Term> locationList = new ArrayList<Term>();
    private ArrayList<ArrayList<Term>> listMap = new ArrayList<ArrayList<Term>>();
    private int issuePosNum = 0;
    private boolean isPreview = false;
    private Issue issue = new Issue();

    public RaiseIssueManager() {
        String[] nounArray = {"Police", "Sausage", "北科", "Mr.Brown", "Seven-Eleven", "Family-Mart", "Cat", "MRT", "Garbage noodle", "滷肉飯"};
        String[] adjArray = {"is comming", "is dangerous", "opening", "on sale", "sold out", "開單"};
        String[] locationArray = {"忠孝新生", "科研", "綜科", "光華商場", "六教"};
        for (String s : nounArray)
            this.nounList.add(new Term(s));
        for (String s : adjArray)
            this.adjList.add(new Term(s));
        for (String s : locationArray)
            this.locationList.add(new Term(s));
        this.listMap.add(this.nounList);
        this.listMap.add(this.adjList);
        this.listMap.add(this.locationList);
    }

    public ArrayList<Term> getList(int listNum) {
        return this.listMap.get(listNum);
    }

    public void setIssue(String content) {
        final int SUBJECT = 0, DESCRIPTION = 1, LOCATION = 2;
        switch (this.issuePosNum) {
            case SUBJECT:
                this.issue.setSubject(content);
                break;
            case DESCRIPTION:
                this.issue.setDescription(content);
                break;
            case LOCATION:
                this.issue.setLocation(content);
                break;
        }
    }

    public void addTerm(String content) {
        int posNum = this.issuePosNum;
        if (posNum > 0) {
            Term t = new Term(content);
            t.setIsSelected(true);
            if(posNum<2)
                posNum -=1;
            this.getList(posNum).add(t);
        }
    }

    public int getIssuePosNum() {
        return this.issuePosNum;
    }

    public void setIssuePosNum(int issuePosNum) {
        final int ISSUE_DATA = 3;
        if (issuePosNum < ISSUE_DATA)
            this.issuePosNum = issuePosNum;
    }

    public String getIssueInString() {
        return this.issue.getIssue();
    }

    public Issue getIssue() {
        return this.issue;
    }

    public boolean isPreview() {
        String subject = this.issue.getSubject();
        String description = this.issue.getDescription();
        String location = this.issue.getLocation();
        return !(subject.isEmpty() || description.isEmpty() || location.isEmpty()) || this.isPreview;
    }

    public void setIsPreview(boolean isPreview) {
        this.isPreview = isPreview;
    }

    public boolean adjButtonEnable() {
        return !this.issue.getSubject().isEmpty();
    }

    public boolean locationButtonEnable() {
        return !this.issue.getDescription().isEmpty();
    }

    public boolean hetButtonEnable() {
        String subject = this.issue.getSubject();
        String description = this.issue.getDescription();
        return !(subject.isEmpty() || description.isEmpty());
    }
}
