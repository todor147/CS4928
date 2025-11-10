package com.cafepos.command;

public final class ApplyDiscountCommand implements Command {
    private final OrderService service;
    private final String discountCode;
    private String previousDiscountCode;

    public ApplyDiscountCommand(OrderService service, String discountCode) {
        this.service = service;
        this.discountCode = discountCode;
    }

    @Override
    public void execute() {
        previousDiscountCode = service.getDiscountCode();
        service.setDiscountCode(discountCode);
    }

    @Override
    public void undo() {
        service.setDiscountCode(previousDiscountCode);
    }
}


