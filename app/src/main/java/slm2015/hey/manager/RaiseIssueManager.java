package slm2015.hey.manager;

import java.util.ArrayList;

import slm2015.hey.entity.Issue;
import slm2015.hey.entity.Term;

public class RaiseIssueManager {
    private ArrayList<Term> _nounList = new ArrayList<Term>();
    private ArrayList<Term> _adjList = new ArrayList<Term>();
    private ArrayList<Term> _locationList = new ArrayList<Term>();
    private ArrayList<ArrayList<Term>> _listMap = new ArrayList<ArrayList<Term>>();
    private int _issuePosNum = 0;
    private boolean isPreview = false;

        private String[] _issue = {"", "", ""};
    private Issue issue = new Issue();

    public RaiseIssueManager() {
        String[] nounArray = {"Police", "Sausage", "北科", "Mr.Brown", "Seven-Eleven", "Family-Mart", "Cat", "MRT", "Garbage noodle"};
        String[] adjArray = {"is comming", "is dangerous", "opening", "on sale", "sold out"};
        String[] locationArray = {"忠孝新生", "科研", "綜科", "光華商場", "六教"};
        for (String s : nounArray)
            this._nounList.add(new Term(s));
        for (String s : adjArray)
            _adjList.add(new Term(s));
        for (String s : locationArray)
            _locationList.add(new Term(s));
        _listMap.add(_nounList);
        _listMap.add(_adjList);
        _listMap.add(_locationList);
    }

    public ArrayList<Term> getList(int listNum) {
        return _listMap.get(listNum);
    }

    public void setIssue(String content) {
//        _issue[_issuePosNum] = content;
        final int SUBJECT = 0, DESCRIPTION = 1, LOCATION = 2;
        switch (this._issuePosNum) {
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

    public int getIssuePosNum() {
        return _issuePosNum;
    }

    public void setIssuePosNum(int issuePosNum) {
        final int ISSUE_DATA = 3;
        if (issuePosNum < ISSUE_DATA)
            _issuePosNum = issuePosNum;
    }

    public String getIssueInString() {
        return this.issue.getIssue();
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
