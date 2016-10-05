package com.mheev.helpthemshop.util;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

/**
 * Created by mheev on 10/4/2016.
 */

public class RxBus<T> {
    private final Subject<T, T> subject;

    public RxBus() {
        this(PublishSubject.<T>create());
    }

    public RxBus(Subject<T, T> subject) {
        this.subject = subject;
    }

    public <E extends T> void post(E event) {
        subject.onNext(event);
    }

    public Observable<T> observe() {
        return subject;
    }

    public <E extends T> Observable<E> observeEvents(Class<E> eventClass) {
        return subject.ofType(eventClass);//pass only events of specified type, filter all other
    }


    /**********Replaying events***********/
    public static <T> RxBus<T> createSimple() {
        return new RxBus<>();//PublishSubject is created in constructor
    }

    public static <T> RxBus<T> createRepeating(int numberOfEventsToRepeat) {
        return new RxBus<>(ReplaySubject.<T>createWithSize(numberOfEventsToRepeat));
    }

    public static <T> RxBus<T> createWithLatest() {
        return new RxBus<>(BehaviorSubject.<T>create());
    }

    //Observing multiple events at once
    public static <E1, E2> Observable<Object> observeEvents(RxBus<Object> bus, Class<E1> class1, Class<E2> class2) {
        return Observable.merge(bus.observeEvents(class1), bus.observeEvents(class2));
    }
}
