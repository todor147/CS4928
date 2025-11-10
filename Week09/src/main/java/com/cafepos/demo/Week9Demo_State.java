package com.cafepos.demo;

import com.cafepos.state.OrderFSM;

public final class Week9Demo_State {
    public static void main(String[] args) {
        OrderFSM order = new OrderFSM();

        System.out.println("Initial status: " + order.status());
        System.out.println();

        System.out.println("Transition: pay()");
        order.pay();
        System.out.println("Status: " + order.status());
        System.out.println();

        System.out.println("Transition: markReady()");
        order.markReady();
        System.out.println("Status: " + order.status());
        System.out.println();

        System.out.println("Transition: deliver()");
        order.deliver();
        System.out.println("Status: " + order.status());
        System.out.println();

        System.out.println("Attempt invalid transition: prepare()");
        order.prepare();
        System.out.println("Status: " + order.status());
    }
}


