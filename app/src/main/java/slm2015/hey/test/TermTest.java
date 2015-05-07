package slm2015.hey.test;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.lang.reflect.Field;

import slm2015.hey.entity.Term;

public class TermTest extends TestCase {

    private Term term;
    private final String termString = "test";

    protected void setUp() throws Exception {
        super.setUp();
        this.term = new Term(this.termString);
    }

    public void testgetTerm() {
        Assert.assertEquals(this.termString, this.term.getText());
    }

    public void testisSelectedFalse() {
        Assert.assertEquals(false, this.term.isSelected());
    }

    public void testisSelectedTrue() throws Exception {
        Field isSelected = Term.class.getDeclaredField("isSelected");
        isSelected.setAccessible(true);
        isSelected.set(this.term, true);
        Assert.assertEquals(true, this.term.isSelected());
    }

    public void testsetIsSelected() throws Exception{
        boolean expect = true;
        this.term.setIsSelected(expect);
        Field isSelected = Term.class.getDeclaredField("isSelected");
        isSelected.setAccessible(true);
        Assert.assertEquals(expect, isSelected.get(this.term));
    }
}
