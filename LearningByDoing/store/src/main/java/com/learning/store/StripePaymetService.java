package com.learning.store;

public class StripePaymetService {
  public void processPayment(double amount) {
    System.out.println("StripePaymentService created");
    System.out.println("Processing payment of $" + amount + " through Stripe.");
  }
}
