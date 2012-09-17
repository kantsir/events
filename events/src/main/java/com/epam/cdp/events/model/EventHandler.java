package com.epam.cdp.events.model;

import java.lang.reflect.Method;

public abstract class EventHandler {

    public abstract void handle(Event event);
    
    public Method[] getDeclaredMethods(){
        return getClass().getDeclaredMethods();
    }
}
