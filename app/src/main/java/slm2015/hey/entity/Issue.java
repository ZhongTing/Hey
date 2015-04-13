package slm2015.hey.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Issue {
    //todo implement issue data structure here
    private String subject = "";
    private String description = "";
    private String location = "";
    private JSONObject issueJSON;

    public Issue() {
        this.issueJSON = new JSONObject();
        try {
            this.issueJSON.put("subject", "");
            this.issueJSON.put("description", "");
            this.issueJSON.put("location", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void updateIssue(){
        try {
            this.issueJSON.put("subject", this.subject);
            this.issueJSON.put("description", this.description);
            this.issueJSON.put("location", this.location);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getIssue(){
        String issue = "";
        for (String s : getIssueInArray()) {
            if (issue.isEmpty())
                issue = s;
            else if (!s.isEmpty())
                issue += " " + s;
        }
        return issue;
    }

    private String[] getIssueInArray(){
        String[] issueInArray = {this.subject, this.description, this.location};
        return issueInArray;
    }

    public void setSubject(String subject) {
        this.subject = subject;
        updateIssue();
    }

    public void setLocation(String location) {
        this.location = location;
        updateIssue();
    }

    public void setDescription(String description) {
        this.description = description;
        updateIssue();
    }

    public String getSubject() {
        return this.subject;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLocation() {
        return this.location;
    }
}
