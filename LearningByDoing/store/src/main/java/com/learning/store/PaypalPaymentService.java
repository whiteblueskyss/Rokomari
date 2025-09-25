package com.learning.store;

public class PaypalPaymentService implements PaymentService {
  public void processPayment(double amount) {
    System.out.println("PaypalPaymentService created");
    System.out.println("Processing payment of $" + amount + " through PayPal.");
  }
  
}
