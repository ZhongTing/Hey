package slm2015.hey.core;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseLoader {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    public void notifyLoaderChanged() {
        for (Observer observer : observers) {
            observer.onLoaderChanged();
        }
    }
}
