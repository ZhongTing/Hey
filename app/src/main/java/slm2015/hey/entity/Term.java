package slm2015.hey.entity;

public class Term {
    private String term;
    private String showText;
    private boolean isNotInRecommendList;

    public Term(String term) {
        this(term, false);
    }

    public Term(String term, boolean isNotInRecommendList) {
        this.term = term;
        this.isNotInRecommendList = isNotInRecommendList;
        this.showText = isNotInRecommendList ? "找不到\"" + term + "\"嗎？按一下新增" : term;
    }

    public String getShowText() {
        return showText;
    }

    public String getText() {
        return this.term;
    }

    public boolean isNotInRecommendList() {
        return this.isNotInRecommendList;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass().equals(getClass())) {
            Term target = (Term) o;
            return target.showText.equals(this.showText) && target.term.equals(this.term);
        } else {
            return false;
        }
    }

    public void normalize() {
        this.showText = this.term;
        this.isNotInRecommendList = false;
    }
}
