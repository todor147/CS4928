package com.cafepos.state;

public final class OrderFSM {
    private State currentState;

    public OrderFSM() {
        this.currentState = new NewState();
    }

    public void pay() {
        currentState.pay(this);
    }

    public void prepare() {
        currentState.prepare(this);
    }

    public void markReady() {
        currentState.markReady(this);
    }

    public void deliver() {
        currentState.deliver(this);
    }

    public void cancel() {
        currentState.cancel(this);
    }

    public String status() {
        return currentState.name();
    }

    void setState(State state) {
        if (state == null)
            throw new IllegalArgumentException("state required");
        this.currentState = state;
    }
}


