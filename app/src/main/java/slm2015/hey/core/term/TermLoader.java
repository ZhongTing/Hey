package slm2015.hey.core.term;

import android.content.Context;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.core.Subject;
import slm2015.hey.entity.Term;

public class TermLoader extends Subject implements TermHandler.TermHandlerCallback {
    private TermHandler termHandler;
    private List<Term> subjects;
    private List<Term> descriptions;
    private List<Term> places;

    public TermLoader(Context context) {
        this.termHandler = new TermHandler(context, this);
    }

    public void loadRecommends() {
        this.termHandler.loadRecommends();
    }

    public List<Term> getTerms(Type type) {
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

    @Override
    public void onReceiveRecommends(List<Term> subjects, List<Term> descriptions, List<Term> places) {
        this.subjects = subjects;
        this.descriptions = descriptions;
        this.places = places;

        notifySubjectChanged();
    }

    public enum Type {
        SUBJECT,
        DESCRIPTION,
        PLACE
    }
}
