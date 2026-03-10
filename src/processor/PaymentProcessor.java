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
public interface PaymentProcessor {
    boolean process(PaymentModel payment);
}
