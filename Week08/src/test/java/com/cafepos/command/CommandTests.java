package com.cafepos.command;

import com.cafepos.order.*;
import com.cafepos.payment.CardPayment;
import com.cafepos.common.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class CommandTests {

    @Test
    public void addItemCommand_executes_successfully() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        
        AddItemCommand cmd = new AddItemCommand(service, "ESP", 1);
        
        Assertions.assertEquals(0, order.getItemCount());
        cmd.execute();
        Assertions.assertEquals(1, order.getItemCount());
    }

    @Test
    public void addItemCommand_undo_removes_last_item() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        
        AddItemCommand cmd = new AddItemCommand(service, "ESP", 1);
        
        cmd.execute();
        Assertions.assertEquals(1, order.getItemCount());
        
        cmd.undo();
        Assertions.assertEquals(0, order.getItemCount());
    }

    @Test
    public void payOrderCommand_executes_successfully() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        
        service.addItem("ESP", 1);
        Money total = order.totalWithTax(10);
        
        PayOrderCommand cmd = new PayOrderCommand(service, new CardPayment("1234567890123456"), 10);
        
        // Should not throw
        cmd.execute();
    }

    @Test
    public void posRemote_press_executes_command() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(1);
        
        AddItemCommand cmd = new AddItemCommand(service, "ESP", 1);
        remote.setSlot(0, cmd);
        
        Assertions.assertEquals(0, order.getItemCount());
        remote.press(0);
        Assertions.assertEquals(1, order.getItemCount());
    }

    @Test
    public void posRemote_undo_reverses_last_command() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(1);
        
        AddItemCommand cmd = new AddItemCommand(service, "ESP", 1);
        remote.setSlot(0, cmd);
        
        remote.press(0);
        Assertions.assertEquals(1, order.getItemCount());
        
        remote.undo();
        Assertions.assertEquals(0, order.getItemCount());
    }

    @Test
    public void macroCommand_executes_all_steps() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        
        AddItemCommand cmd1 = new AddItemCommand(service, "ESP", 1);
        AddItemCommand cmd2 = new AddItemCommand(service, "LAT", 1);
        
        MacroCommand macro = new MacroCommand(cmd1, cmd2);
        
        macro.execute();
        
        Assertions.assertEquals(2, order.getItemCount());
    }

    @Test
    public void macroCommand_undo_reverses_in_reverse_order() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        
        AddItemCommand cmd1 = new AddItemCommand(service, "ESP", 1);
        AddItemCommand cmd2 = new AddItemCommand(service, "LAT", 1);
        
        MacroCommand macro = new MacroCommand(cmd1, cmd2);
        
        macro.execute();
        Assertions.assertEquals(2, order.getItemCount());
        
        macro.undo();
        Assertions.assertEquals(0, order.getItemCount());
    }
}


