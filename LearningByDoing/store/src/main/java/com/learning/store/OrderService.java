package com.learning.store;

public class OrderService {

  public void placeOrder() {
    var paymentService = new StripePaymentService();
    paymentService.processPayment(100.0);
  }
}
