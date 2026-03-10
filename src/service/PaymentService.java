/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.PaymentDAO;
import model.PaymentModel;
import processor.PaymentProcessor;

import utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author ADMIN
 */

public class PaymentService {

    private final PaymentDAO paymentDAO = new PaymentDAO();

    public boolean completePayment(PaymentModel payment, PaymentProcessor processor) throws Exception {
        boolean processed = processor.process(payment);
        if (!processed) return false;

        boolean saved = paymentDAO.save(payment);
        if (!saved) return false;

        return true;
    }

    // Add this method to reduce stock
    public boolean reduceStock(String productId, int qty) throws Exception {
        String sql = "UPDATE products SET quantity = quantity - ? WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, qty);
            pst.setString(2, productId);
            return pst.executeUpdate() > 0;
        }
    }
}
