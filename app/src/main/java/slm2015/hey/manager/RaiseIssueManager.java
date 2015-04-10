package slm2015.hey.manager;

import java.util.ArrayList;

import slm2015.hey.entity.Term;

public class RaiseIssueManager {
    private ArrayList<Term> _nounList = new ArrayList<Term>();
    private ArrayList<Term> _adjList = new ArrayList<Term>();
    private ArrayList<Term> _locationList = new ArrayList<Term>();
    private ArrayList<ArrayList<Term>> _listMap = new ArrayList<ArrayList<Term>>();
    private int _issuePosNum = 0;

    private String[] _issue = {"", "", ""};

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
        _issue[_issuePosNum] = content;
    }

    public int getIssuePosNum() {
        return _issuePosNum;
    }

    public void setIssuePosNum(int issuePosNum) {
        if (issuePosNum < _issue.length)
            _issuePosNum = issuePosNum;
    }

    public String getIssueInString() {
        String issue = "";
        for (String s : _issue) {
            if (issue.isEmpty())
                issue = s;
            else if (!s.isEmpty())
                issue += " " + s;
        }
        return issue;
    }

    public boolean adjButtonEnable() {
        return !_issue[0].isEmpty();
    }

    public boolean locationButtonEnable() {
        return !_issue[1].isEmpty();
    }
}
