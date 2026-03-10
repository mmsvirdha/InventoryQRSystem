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
public class CashPaymentProcessor implements PaymentProcessor {
    
    @Override
    public boolean process(PaymentModel payment) {
        System.out.println("Cash payment processed for: " + payment.getProductId());
        return true;
    }
}
