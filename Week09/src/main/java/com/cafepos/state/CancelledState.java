package com.cafepos.state;

public final class CancelledState implements State {
    @Override
    public void pay(OrderFSM context) {
        System.out.println("[CANCELLED] Cannot pay - order is cancelled.");
    }

    @Override
    public void prepare(OrderFSM context) {
        System.out.println("[CANCELLED] Cannot prepare - order is cancelled.");
    }

    @Override
    public void markReady(OrderFSM context) {
        System.out.println("[CANCELLED] Cannot mark ready - order is cancelled.");
    }

    @Override
    public void deliver(OrderFSM context) {
        System.out.println("[CANCELLED] Cannot deliver - order is cancelled.");
    }

    @Override
    public void cancel(OrderFSM context) {
        System.out.println("[CANCELLED] Order already cancelled.");
    }

    @Override
    public String name() {
        return "CANCELLED";
    }
}


