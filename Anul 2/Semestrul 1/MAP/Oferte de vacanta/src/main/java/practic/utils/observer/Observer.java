package practic.utils.observer;

import practic.utils.event.Event;

public interface Observer<E extends Event> {
    void update(E e);
}