package com.cafepos.state;

public final class PreparingState implements State {
    @Override
    public void pay(OrderFSM context) {
        System.out.println("[PREPARING] Payment already received.");
    }

    @Override
    public void prepare(OrderFSM context) {
        System.out.println("[PREPARING] Already preparing.");
    }

    @Override
    public void markReady(OrderFSM context) {
        System.out.println("[PREPARING] Order ready. Moving to READY.");
        context.setState(new ReadyState());
    }

    @Override
    public void deliver(OrderFSM context) {
        System.out.println("[PREPARING] Cannot deliver - order not ready yet.");
    }

    @Override
    public void cancel(OrderFSM context) {
        System.out.println("[PREPARING] Order cancelled.");
        context.setState(new CancelledState());
    }

    @Override
    public String name() {
        return "PREPARING";
    }
}


