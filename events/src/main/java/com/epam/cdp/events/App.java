package com.epam.cdp.events;


import com.epam.cdp.events.engine.EventDispatcher;
import com.epam.cdp.events.engine.RadarEventDispatcher;
import com.epam.cdp.events.event.AircraftEvent;
import com.epam.cdp.events.event.RocketEvent;
import com.epam.cdp.events.event.ShipEvent;
import com.epam.cdp.events.event.TankEvent;
import com.epam.cdp.events.handler.RadarEventHandler;
import com.epam.cdp.events.model.EventHandler;

/**
 * Hello world!
 * 
 */
public class App {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        EventDispatcher dispatcher = new RadarEventDispatcher();

        EventHandler handler = new RadarEventHandler();

        dispatcher.registerEventHandler(handler);
        dispatcher.dispatchEvent(new RocketEvent());
        dispatcher.dispatchEvent(new ShipEvent());
        dispatcher.dispatchEvent(new AircraftEvent());
        dispatcher.dispatchEvent(new TankEvent());

    }
}
