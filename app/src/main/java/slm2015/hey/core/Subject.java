package slm2015.hey.core;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    public void notifySubjectChanged() {
        for (Observer observer : observers) {
            observer.onSubjectChanged();
        }
    }
}
