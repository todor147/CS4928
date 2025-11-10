package com.cafepos.state;

public final class DeliveredState implements State {
    @Override
    public void pay(OrderFSM context) {
        System.out.println("[DELIVERED] Order already delivered. Payment already received.");
    }

    @Override
    public void prepare(OrderFSM context) {
        System.out.println("[DELIVERED] Order already delivered.");
    }

    @Override
    public void markReady(OrderFSM context) {
        System.out.println("[DELIVERED] Order already delivered.");
    }

    @Override
    public void deliver(OrderFSM context) {
        System.out.println("[DELIVERED] Order already delivered.");
    }

    @Override
    public void cancel(OrderFSM context) {
        System.out.println("[DELIVERED] Cannot cancel - order already delivered.");
    }

    @Override
    public String name() {
        return "DELIVERED";
    }
}


