package com.cafepos.state;

public final class NewState implements State {
    @Override
    public void pay(OrderFSM context) {
        System.out.println("[NEW] Payment received. Moving to PREPARING.");
        context.setState(new PreparingState());
    }

    @Override
    public void prepare(OrderFSM context) {
        System.out.println("[NEW] Cannot prepare - payment required first.");
    }

    @Override
    public void markReady(OrderFSM context) {
        System.out.println("[NEW] Cannot mark ready - order not prepared yet.");
    }

    @Override
    public void deliver(OrderFSM context) {
        System.out.println("[NEW] Cannot deliver - order not ready yet.");
    }

    @Override
    public void cancel(OrderFSM context) {
        System.out.println("[NEW] Order cancelled.");
        context.setState(new CancelledState());
    }

    @Override
    public String name() {
        return "NEW";
    }
}


