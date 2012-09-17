package com.epam.cdp.events.handler;


import com.epam.cdp.events.annotation.EventListener;
import com.epam.cdp.events.event.AircraftEvent;
import com.epam.cdp.events.event.RocketEvent;
import com.epam.cdp.events.event.ShipEvent;
import com.epam.cdp.events.event.TankEvent;
import com.epam.cdp.events.model.Event;
import com.epam.cdp.events.model.EventHandler;


public class RadarEventHandler extends EventHandler {

    @Override
    public void handle(Event event) {
        // TODO Auto-generated method stub

    }
    
    @EventListener
    public void destroyAircraft(AircraftEvent event) {
        System.out.println("Aircraft destroyed:" + event.getClass().toString());
    }
    
    @EventListener
    public void destroyTank(TankEvent event) {
        System.out.println("Tank destroyed:" + event.getClass().toString());
    }
    
    @EventListener
    private void destroyRocket(RocketEvent event) {
        System.out.println("Rocket destroyed:" + event.getClass().toString());
    }

    @EventListener
    public static void destroyShip(ShipEvent event) {
        System.out.println("Ship destroyed:" + event.getClass().toString());
    }

    @EventListener
    public void ignore(Event event) {
        System.out.println("Event ignored:" + event.getClass().toString());
    }


}
