package com.tylersuehr.bubbles;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Copyright 2017 Tyler Suehr
 * Created by tyler on 6/9/2017.
 */
@Deprecated
abstract class ObjectPool<T> {
    private long expirationTime;
    private Hashtable<T, Long> locked;
    private Hashtable<T, Long> unlocked;


    ObjectPool() {
        this.expirationTime = 30 * 1000; // 30 seconds
        this.locked = new Hashtable<>();
        this.unlocked = new Hashtable<>();
    }

    protected abstract T onCreate();

    abstract boolean onValidate(T o);

    abstract void onExpired(T o);

    synchronized T checkOut() {
        long now = System.currentTimeMillis();
        T t;

        if (unlocked.size() > 0) {
            Enumeration<T> e = unlocked.keys();
            while (e.hasMoreElements()) {
                t = e.nextElement();
                if ((now - unlocked.get(t)) > expirationTime) {
                    // Object has expired
                    this.unlocked.remove(t);
                    onExpired(t);
                    t = null;
                } else {
                    if (onValidate(t)) {
                        this.unlocked.remove(t);
                        this.locked.put(t, now);
                        return t;
                    } else {
                        // Object failed validation
                        this.unlocked.remove(t);
                        onExpired(t);
                        t = null;
                    }
                }
            }
        }

        // No objects available, onCreate a new one
        t = onCreate();
        this.locked.put(t, now);
        return t;
    }

    synchronized void checkIn(T t) {
        this.locked.remove(t);
        this.unlocked.put(t, System.currentTimeMillis());
    }
}