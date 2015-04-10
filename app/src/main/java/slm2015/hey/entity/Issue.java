package slm2015.hey.entity;

import org.json.JSONObject;

public class Issue {
    //todo implement issue data structure here
    private String subject;
    private String description;
    private String location;

    public Issue(JSONObject object) {

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
