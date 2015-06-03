package slm2015.hey.entity;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private Term term;
    private List<Term> descriptionList = new ArrayList<>();
    private List<Term> placeList = new ArrayList<>();

    public Subject(String content) {
        this.term = new Term(content);
    }

    public Term getTerm() {
        return term;
    }

    public List<Term> getDescriptionList() {
        return descriptionList;
    }

    public List<Term> getPlaceList() {
        return placeList;
    }
}
