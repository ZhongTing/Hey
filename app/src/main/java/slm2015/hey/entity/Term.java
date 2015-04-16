package slm2015.hey.entity;

public class Term {
    private String term;
    private boolean isSelected;

    public Term(String term){
        this.term = term;
        this.isSelected = false;
    }

    public String getTerm(){
        return this.term;
    }

    public boolean isSelected(){
        return this.isSelected;
    }

    public void setIsSelected(boolean isSelected){
        this.isSelected = isSelected;
    }
}
