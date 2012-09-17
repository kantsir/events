package com.epam.cdp.events.engine;

import com.epam.cdp.events.model.Event;
import com.epam.cdp.events.model.EventHandler;

public interface EventDispatcher {

    void registerEventHandler(EventHandler eventHandler);

    void dispatchEvent(Event event);
}
