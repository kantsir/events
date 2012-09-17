package com.epam.cdp.events.engine;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import com.epam.cdp.events.annotation.EventListener;
import com.epam.cdp.events.model.Event;
import com.epam.cdp.events.model.EventHandler;

public class RadarEventDispatcher implements EventDispatcher {

    private List<EventHandler> handlers;
    private Map<Class<?>, List<Method>> handlersMappedMethods;

    public RadarEventDispatcher() {
        handlers = new LinkedList<EventHandler>();
        handlersMappedMethods = new HashMap<Class<?>, List<Method>>();
    }

    public void registerEventHandler(EventHandler eventHandler) {
        handlers.add(eventHandler);
        parseHandlerMethods(eventHandler);

    }

    private void parseHandlerMethods(EventHandler eventHandler) {
        Method[] methods = eventHandler.getDeclaredMethods();
        for(Method method : methods) {
            int methodModifier = method.getModifiers();
            boolean isPublic = Modifier.isPublic(methodModifier);

            if(isPublic) {
                registerMethod(method);
            }
        }
    }

    private void registerMethod(Method method) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        if(annotations != null) {
            for(Annotation annotation : annotations) {
                if(annotation.annotationType().equals(EventListener.class)) {
                    addToRegisteredMethods(method);
                }
            }
        }
    }

    private void addToRegisteredMethods(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        List<Method> handlersMethods = null;
        for(Class<?> type : parameterTypes) {
            handlersMethods = handlersMappedMethods.get(type);
            if(handlersMethods == null) {
                handlersMethods = new LinkedList<Method>();
            }
            handlersMethods.add(method);
            handlersMappedMethods.put(type, handlersMethods);
        }
    }

    public void dispatchEvent(Event event) {
        List<Method> methods = handlersMappedMethods.get(event.getClass());
        if(methods != null) {
            for(Method methodToHandle : methods) {
                try {
                    methodToHandle.invoke(methodToHandle.getDeclaringClass().newInstance(), event);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            return;
        }

    }

}
