package slm2015.hey.manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import slm2015.hey.api.APIManager;
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
        try {
            JSONObject temp = APIManager.getInstance().getTemp();
            JSONArray actorArray = temp.getJSONArray("actor");
            JSONArray eventArray = temp.getJSONArray("event");
            JSONArray placeArray = temp.getJSONArray("place");
            for (int i = 0; i < actorArray.length(); i++)
                this.nounList.add(new Term(actorArray.getString(i)));
            for (int i = 0; i < eventArray.length(); i++)
                this.adjList.add(new Term(eventArray.getString(i)));

            this.locationList.add(new Term("(無)"));
            for (int i = 0; i < placeArray.length(); i++)
                this.locationList.add(new Term(placeArray.getString(i)));

            this.listMap.add(this.nounList);
            this.listMap.add(this.adjList);
            this.listMap.add(this.locationList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                this.issue.setPlace(content);
                break;
        }
    }

    public void addTerm(String content) {
        int posNum = this.issuePosNum;
        Term t = new Term(content);
        t.setIsSelected(true);
        this.getList(posNum).add(t);
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
        String location = this.issue.getPlace();
        return !(subject.isEmpty() || description.isEmpty()) && this.isPreview;
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
