/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package processor;

import model.PaymentModel;

/**
 *
 * @author ADMIN
 */
public class CardPaymentProcessor implements PaymentProcessor {
    
    @Override
    public boolean process(PaymentModel payment) {
        try {
            // Card payment processing logic
            System.out.println("Card payment processed for: " + payment.getProductId());
            System.out.println("Amount: " + payment.getTotal());
            
            // Add card-specific logic here
            // - Validate card details
            // - Process card transaction
            // - Generate transaction reference
            
            return true; // Payment successful
            
        } catch (Exception e) {
            System.err.println("Card payment failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
