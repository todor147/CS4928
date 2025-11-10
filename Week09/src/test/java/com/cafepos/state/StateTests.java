package com.cafepos.state;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class StateTests {

    @Test
    public void orderFSM_startsInNewState() {
        OrderFSM order = new OrderFSM();
        Assertions.assertEquals("NEW", order.status());
    }

    @Test
    public void pay_transitionsFromNewToPreparing() {
        OrderFSM order = new OrderFSM();
        order.pay();
        Assertions.assertEquals("PREPARING", order.status());
    }

    @Test
    public void markReady_transitionsFromPreparingToReady() {
        OrderFSM order = new OrderFSM();
        order.pay();
        order.markReady();
        Assertions.assertEquals("READY", order.status());
    }

    @Test
    public void deliver_transitionsFromReadyToDelivered() {
        OrderFSM order = new OrderFSM();
        order.pay();
        order.markReady();
        order.deliver();
        Assertions.assertEquals("DELIVERED", order.status());
    }

    @Test
    public void cancel_transitionsFromNewToCancelled() {
        OrderFSM order = new OrderFSM();
        order.cancel();
        Assertions.assertEquals("CANCELLED", order.status());
    }

    @Test
    public void cancel_transitionsFromPreparingToCancelled() {
        OrderFSM order = new OrderFSM();
        order.pay();
        order.cancel();
        Assertions.assertEquals("CANCELLED", order.status());
    }

    @Test
    public void cancel_transitionsFromReadyToCancelled() {
        OrderFSM order = new OrderFSM();
        order.pay();
        order.markReady();
        order.cancel();
        Assertions.assertEquals("CANCELLED", order.status());
    }

    @Test
    public void fullHappyPath_transitionsCorrectly() {
        OrderFSM order = new OrderFSM();
        Assertions.assertEquals("NEW", order.status());
        order.pay();
        Assertions.assertEquals("PREPARING", order.status());
        order.markReady();
        Assertions.assertEquals("READY", order.status());
        order.deliver();
        Assertions.assertEquals("DELIVERED", order.status());
    }

    @Test
    public void prepare_rejectedFromNewState() {
        OrderFSM order = new OrderFSM();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        order.prepare();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("Cannot prepare"));
        Assertions.assertTrue(output.contains("payment required"));
        Assertions.assertEquals("NEW", order.status());
    }

    @Test
    public void markReady_rejectedFromNewState() {
        OrderFSM order = new OrderFSM();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        order.markReady();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("Cannot mark ready"));
        Assertions.assertEquals("NEW", order.status());
    }

    @Test
    public void deliver_rejectedFromNewState() {
        OrderFSM order = new OrderFSM();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        order.deliver();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("Cannot deliver"));
        Assertions.assertEquals("NEW", order.status());
    }

    @Test
    public void pay_rejectedFromPreparingState() {
        OrderFSM order = new OrderFSM();
        order.pay();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        order.pay();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("already received"));
        Assertions.assertEquals("PREPARING", order.status());
    }

    @Test
    public void prepare_rejectedFromPreparingState() {
        OrderFSM order = new OrderFSM();
        order.pay();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        order.prepare();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("Already preparing"));
        Assertions.assertEquals("PREPARING", order.status());
    }

    @Test
    public void deliver_rejectedFromPreparingState() {
        OrderFSM order = new OrderFSM();
        order.pay();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        order.deliver();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("Cannot deliver"));
        Assertions.assertTrue(output.contains("not ready"));
        Assertions.assertEquals("PREPARING", order.status());
    }

    @Test
    public void pay_rejectedFromReadyState() {
        OrderFSM order = new OrderFSM();
        order.pay();
        order.markReady();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        order.pay();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("already received"));
        Assertions.assertEquals("READY", order.status());
    }

    @Test
    public void prepare_rejectedFromReadyState() {
        OrderFSM order = new OrderFSM();
        order.pay();
        order.markReady();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        order.prepare();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("Already prepared"));
        Assertions.assertEquals("READY", order.status());
    }

    @Test
    public void markReady_rejectedFromReadyState() {
        OrderFSM order = new OrderFSM();
        order.pay();
        order.markReady();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        order.markReady();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("Already ready"));
        Assertions.assertEquals("READY", order.status());
    }

    @Test
    public void deliveredState_rejectsAllActions() {
        OrderFSM order = new OrderFSM();
        order.pay();
        order.markReady();
        order.deliver();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        order.pay();
        order.prepare();
        order.markReady();
        order.deliver();
        order.cancel();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("already delivered"));
        Assertions.assertEquals("DELIVERED", order.status());
    }

    @Test
    public void cancelledState_rejectsAllActions() {
        OrderFSM order = new OrderFSM();
        order.cancel();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        order.pay();
        order.prepare();
        order.markReady();
        order.deliver();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("cancelled"));
        Assertions.assertEquals("CANCELLED", order.status());
    }

    @Test
    public void cancelledState_cancelAlreadyCancelled() {
        OrderFSM order = new OrderFSM();
        order.cancel();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        order.cancel();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("already cancelled"));
        Assertions.assertEquals("CANCELLED", order.status());
    }

    @Test
    public void newState_pay_outputsCorrectMessage() {
        OrderFSM order = new OrderFSM();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        order.pay();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("[NEW]"));
        Assertions.assertTrue(output.contains("Payment received"));
        Assertions.assertTrue(output.contains("Moving to PREPARING"));
    }

    @Test
    public void preparingState_markReady_outputsCorrectMessage() {
        OrderFSM order = new OrderFSM();
        order.pay();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        order.markReady();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("[PREPARING]"));
        Assertions.assertTrue(output.contains("Order ready"));
        Assertions.assertTrue(output.contains("Moving to READY"));
    }

    @Test
    public void readyState_deliver_outputsCorrectMessage() {
        OrderFSM order = new OrderFSM();
        order.pay();
        order.markReady();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        order.deliver();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("[READY]"));
        Assertions.assertTrue(output.contains("Order delivered"));
        Assertions.assertTrue(output.contains("Moving to DELIVERED"));
    }

    @Test
    public void newState_cancel_outputsCorrectMessage() {
        OrderFSM order = new OrderFSM();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        order.cancel();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("[NEW]"));
        Assertions.assertTrue(output.contains("Order cancelled"));
    }

    @Test
    public void stateNames_areCorrect() {
        OrderFSM order = new OrderFSM();
        Assertions.assertEquals("NEW", order.status());

        order.pay();
        Assertions.assertEquals("PREPARING", order.status());

        order.markReady();
        Assertions.assertEquals("READY", order.status());

        order.deliver();
        Assertions.assertEquals("DELIVERED", order.status());
    }

    @Test
    public void multipleStateInstances_areIndependent() {
        OrderFSM order1 = new OrderFSM();
        OrderFSM order2 = new OrderFSM();

        order1.pay();
        Assertions.assertEquals("PREPARING", order1.status());
        Assertions.assertEquals("NEW", order2.status());

        order2.pay();
        order2.markReady();
        Assertions.assertEquals("PREPARING", order1.status());
        Assertions.assertEquals("READY", order2.status());
    }
}
