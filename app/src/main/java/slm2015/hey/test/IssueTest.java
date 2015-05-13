//package slm2015.hey.test;
//
//import android.graphics.Bitmap;
//
//import junit.framework.Assert;
//import junit.framework.TestCase;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//
//import slm2015.hey.entity.Issue;
//
//public class IssueTest extends TestCase {
//    private Issue issue;
//    private final String subject = "testSubject";
//    private final String description = "testDescription";
//    private final String position = "testPosition";
//    private Field s, d, p, i;
//
//    protected void setUp() throws Exception {
//        super.setUp();
//        this.issue = new Issue();
//        s = Issue.class.getDeclaredField("subject");
//        s.setAccessible(true);
//        s.set(this.issue, this.subject);
//        d = Issue.class.getDeclaredField("description");
//        d.setAccessible(true);
//        d.set(this.issue, this.description);
//        p = Issue.class.getDeclaredField("position");
//        p.setAccessible(true);
//        p.set(this.issue, this.position);
//        i = Issue.class.getDeclaredField("image");
//        i.setAccessible(true);
//    }
//
//    public void testupdateIssue() throws Exception {
//        final String expect_s = "updateSubject";
//        final String expect_d = "updateDescription";
//        final String expect_p = "updatePosition";
//        s.set(this.issue, expect_s);
//        d.set(this.issue, expect_d);
//        p.set(this.issue, expect_p);
//        this.issue.updateIssue();
//        Assert.assertEquals(expect_s, s.get(this.issue));
//        Assert.assertEquals(expect_d, d.get(this.issue));
//        Assert.assertEquals(expect_p, p.get(this.issue));
//    }
//
//    public void testgetIssueInArray() throws Exception {
//        String[] expect = {this.subject, this.description, this.position};
//        Method privateMethod = Issue.class.getDeclaredMethod("getIssueInArray", null);
//        privateMethod.setAccessible(true);
//        String[] actual = (String[]) privateMethod.invoke(this.issue);
//        Assert.assertEquals(expect.length, actual.length);
//        Assert.assertEquals(expect[0], actual[0]);
//        Assert.assertEquals(expect[1], actual[1]);
//        Assert.assertEquals(expect[2], actual[2]);
//    }
//
//    public void testgetIssue() throws Exception {
//        String expect = this.subject + " " + this.description + " " + this.position;
//        Assert.assertEquals(expect, this.issue.getIssue());
//    }
//
//    public void testgetIssue2() throws Exception {
//        p.set(this.issue, "");
//        String expect = this.subject + " " + this.description;
//        Assert.assertEquals(expect, this.issue.getIssue());
//    }
//
//    public void testgetIssue3() throws Exception {
//        d.set(this.issue, "");
//        String expect = this.subject + " " + this.position;
//        Assert.assertEquals(expect, this.issue.getIssue());
//    }
//
//    public void testgetIssue4() throws Exception {
//        s.set(this.issue, "");
//        String expect = this.description + " " + this.position;
//        Assert.assertEquals(expect, this.issue.getIssue());
//    }
//
//    public void testgetIssue5() throws Exception {
//        s.set(this.issue, "");
//        d.set(this.issue, "");
//        String expect = this.position;
//        Assert.assertEquals(expect, this.issue.getIssue());
//    }
//
//    public void testgetIssue6() throws Exception {
//        d.set(this.issue, "");
//        p.set(this.issue, "");
//        String expect = this.subject;
//        Assert.assertEquals(expect, this.issue.getIssue());
//    }
//
//    public void testgetIssue7() throws Exception {
//        s.set(this.issue, "");
//        p.set(this.issue, "");
//        String expect = this.description;
//        Assert.assertEquals(expect, this.issue.getIssue());
//    }
//
//    public void testgetIssue8() throws Exception {
//        s.set(this.issue, "");
//        d.set(this.issue, "");
//        p.set(this.issue, "");
//        String expect = "";
//        Assert.assertEquals(expect, this.issue.getIssue());
//    }
//
//    public void testsetImage() throws Exception {
//        Bitmap expect = Bitmap.createBitmap(100, 100, Bitmap.Config.ALPHA_8);
//        this.issue.setImage(expect);
//        Assert.assertEquals(expect, i.get(this.issue));
//    }
//
//    public void testsetSubject() throws Exception {
//        String expect = "testSubject";
//        this.issue.setSubject(expect);
//        Assert.assertEquals(expect, s.get(this.issue));
//    }
//
//    public void testsetDescription() throws Exception {
//        String expect = "testDescription";
//        this.issue.setDescription(expect);
//        Assert.assertEquals(expect, d.get(this.issue));
//    }
//
//    public void testsetPosition() throws Exception {
//        String expect = "testPosition";
//        this.issue.setPlace(expect);
//        Assert.assertEquals(expect, p.get(this.issue));
//    }
//
//    public void testgetSubject() throws Exception {
//        Assert.assertEquals(this.subject, this.issue.getSubject());
//    }
//
//    public void testgetDescription() throws Exception {
//        Assert.assertEquals(this.description, this.issue.getDescription());
//    }
//
//    public void testgetPosition() throws Exception {
//        Assert.assertEquals(this.position, this.issue.getPlace());
//    }
//}
