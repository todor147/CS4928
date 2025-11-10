package com.cafepos.state;

public final class ReadyState implements State {
    @Override
    public void pay(OrderFSM context) {
        System.out.println("[READY] Payment already received.");
    }

    @Override
    public void prepare(OrderFSM context) {
        System.out.println("[READY] Already prepared.");
    }

    @Override
    public void markReady(OrderFSM context) {
        System.out.println("[READY] Already ready.");
    }

    @Override
    public void deliver(OrderFSM context) {
        System.out.println("[READY] Order delivered. Moving to DELIVERED.");
        context.setState(new DeliveredState());
    }

    @Override
    public void cancel(OrderFSM context) {
        System.out.println("[READY] Order cancelled.");
        context.setState(new CancelledState());
    }

    @Override
    public String name() {
        return "READY";
    }
}


