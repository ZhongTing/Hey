package slm2015.hey.entity;

public class Selector {
    private boolean notify = false;
    private boolean filter = false;
    private String content = "";

    public Selector(String content) {
        setContent(content);
    }

    public String getContent() {
        return content;
    }

    public boolean isNotify() {
        return notify;
    }

    public boolean isFilter() {
        return filter;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}
