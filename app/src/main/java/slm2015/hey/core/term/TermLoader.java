package slm2015.hey.core.term;

import android.content.Context;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.core.BaseLoader;
import slm2015.hey.entity.Subject;
import slm2015.hey.entity.Term;

public class TermLoader extends BaseLoader implements TermHandler.TermHandlerCallback {
    private TermHandler termHandler;
    private List<Subject> subjectRecommends;
    private List<Term> descriptionRecommends;
    private List<Term> placeRecommends;

    private List<Term> subjects;
    private List<Term> descriptions;
    private List<Term> places;

    public TermLoader(Context context) {
        this.termHandler = new TermHandler(context, this);
    }

    public void loadRecommends() {
        this.termHandler.loadRecommends();
    }

    public List<Term> getTerms(TermType type) {
        switch (type) {
            case SUBJECT:
                return this.subjects;
            case DESCRIPTION:
                return this.descriptions;
            case PLACE:
                return this.places;
        }
        Assert.assertFalse(true);
        return null;
    }

    public void setCurrentSubject(String content) {
        boolean isFind = false;
        for (Subject subject : subjectRecommends) {
            if (subject.is(content)) {
                this.descriptions = subject.getDescriptionList();
                this.places = subject.getPlaceList();
                isFind = true;
                break;
            }
        }
        if (!isFind) {
            this.descriptions = descriptionRecommends;
            this.places = placeRecommends;
        }
        this.notifyLoaderChanged();
    }

    @Override
    public void onReceiveRecommends(List<Subject> recommends, List<Term> descriptions, List<Term> places) {
        this.subjectRecommends = recommends;

        this.subjects = new ArrayList<>();
        for (Subject subject : recommends) {
            this.subjects.add(subject.getTerm());
        }

        this.descriptions = descriptions;
        this.places = places;

        this.descriptionRecommends = descriptions;
        this.placeRecommends = places;

        notifyLoaderChanged();
    }
}
