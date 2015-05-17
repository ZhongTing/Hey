package slm2015.hey.entity;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import slm2015.hey.core.issue.IssueLoader;
import slm2015.hey.view.component.Card;

public class CardDeck {
    private final int CARD_MAX_AMOUNT = 5;
    private final int REFRESH_ANIMATION_DURATION = 200;
    private final boolean INITIAL = true;

    private IssueLoader issueLoader;
    private Activity activity;
    private ArrayList<Card> cardQueue = new ArrayList<>();
    private float initCardX, initCardY;
    private OnDataSetChanged onDataSetChanged;

    public CardDeck(IssueLoader issueLoader, Activity activity) {
        this.issueLoader = issueLoader;
        this.activity = activity;
        iniCardQueue(INITIAL);
    }

    private void iniCardQueue(boolean initial) {
        for (int count = 0; count < this.CARD_MAX_AMOUNT; count++) {
            Card card = new Card(this.activity);
            Issue temp = new Issue();
            card.assignIssue(temp);
            card.initDefaultMargin();
            card.initialImageLike();
            card.initialImagePass();
            this.cardQueue.add(card);
        }
    }


    private Card poll() {
        Card card = this.cardQueue.get(0);
        card.setOnTouchListener(null);
        this.cardQueue.remove(0);
        return card;
    }

    private Card pop() {
        Card card = this.cardQueue.get(this.cardQueue.size() - 1);
        ((ViewGroup) card.getParent()).removeView(card);
        card.setOnTouchListener(null);
        this.cardQueue.remove(this.cardQueue.size() - 1);
        return card;
    }

    public void reloadDeck() {
        ArrayList<Issue> all = new ArrayList<>();
        all.addAll(this.issueLoader.getIssues());
        all.addAll(this.issueLoader.getNewIssues());
        for (int count = this.CARD_MAX_AMOUNT - 1; count >= 0; count--) {
            int addToIndex = this.cardQueue.size() - 1;
            Card card = poll();
            int index = all.size() - 1 - count;
            Issue issue = all.get(index);
            reassignIssue(card, issue, addToIndex);
        }
    }

    public void operation() {
        int index = this.issueLoader.getIssues().size() - this.cardQueue.size() - 1;
        this.issueLoader.getIssues().remove(this.issueLoader.getIssues().size() - 1);
        Card card = pop();
        if (index > 0) {
            Issue issue = this.issueLoader.getIssues().get(index);
            iniCard(card);
            reassignIssue(card, issue, 0);
            if (onDataSetChanged != null) {
                onDataSetChanged.notifyTest();
            }
        }
    }

    private void iniCard(Card addCard) {
        addCard.setX(this.initCardX);
        addCard.setY(this.initCardY);
        addCard.initialImageLike();
        addCard.initialImagePass();
        if (addCard.getVisibility() != View.VISIBLE)
            addCard.setVisibility(View.VISIBLE);
    }

    private void reassignIssue(Card card, Issue issue, int addTo) {
        card.assignIssue(issue);
        card.initDefaultMargin();
        card.initialImageLike();
        card.initialImagePass();
        this.cardQueue.add(addTo, card);
    }

    public ArrayList<Card> getCardQueue() {
        return cardQueue;
    }

    public void setOnDataSetChanged(OnDataSetChanged listener) {
        this.onDataSetChanged = listener;
    }

    public interface OnDataSetChanged {
        void notifyTest();
    }

    public void setIniCardXY(float initCardX, float initCardY) {
        this.initCardX = initCardX;
        this.initCardY = initCardY;
    }
}
