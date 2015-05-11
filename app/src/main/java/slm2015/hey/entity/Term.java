package slm2015.hey.entity;

public class Term {
    private String term;
    private String showText;
    private boolean isSelected;

    public Term(String term) {
        this.term = term;
        this.showText = term;
        this.isSelected = false;
    }

    public Term(String term, boolean add) {
        this.term = term;
        this.showText = add ? "找不到\"" + term + "\"嗎？按一下新增" : term;
        this.isSelected = false;
    }

    public String getShowText() {
        return showText;
    }

    public String getText() {
        return this.term;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean needToAdd() {
        boolean needToAdd = !this.term.equals((String) this.showText);
        if(needToAdd)
            this.showText = this.term;
        return needToAdd;
    }
}
