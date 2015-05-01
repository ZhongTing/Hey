package slm2015.hey.entity;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

public class Issue {
    //todo implement issue data structure here
    private String subject = "";
    private String description = "";
    private String place = "";
    private JSONObject issueJSON;
    private Bitmap image;

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

    public Issue(String s, String d, String p) {
        this.issueJSON = new JSONObject();
        setSubject(s);
        setDescription(d);
        setPlace(p);
    }

    public void updateIssue(){
        try {
            this.issueJSON.put("subject", this.subject);
            this.issueJSON.put("description", this.description);
            this.issueJSON.put("location", this.place);
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
        String[] issueInArray = {this.subject, this.description, this.place};
        return issueInArray;
    }

    public void setSubject(String subject) {
        this.subject = subject;
        updateIssue();
    }

    public void setPlace(String location) {
        this.place = location;
        updateIssue();
    }

    public void setDescription(String description) {
        this.description = description;
        updateIssue();
    }

    public void setImage(Bitmap image){
        this.image = image;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPlace() {
        return this.place;
    }

    public Bitmap getImage(){
        return this.image;
    }
}
