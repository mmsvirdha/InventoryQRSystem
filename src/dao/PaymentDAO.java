/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.PaymentModel;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utils.DBConnection;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author ADMIN
 */
public class PaymentDAO {
    

 public boolean save(PaymentModel payment) throws Exception {
        String sql = "INSERT INTO payments "
                   + "(payment_id, product_id, quantity, total_amount, payment_method, payment_date, transaction_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, payment.getPaymentId());
            pst.setString(2, payment.getProductId());
            pst.setInt(3, payment.getQuantity());
            pst.setDouble(4, payment.getTotal()); // total_amount
            pst.setString(5, payment.getMethod());
            pst.setTimestamp(6, new java.sql.Timestamp(payment.getPaymentDate().getTime()));
            pst.setString(7, payment.getTransactionId());

            return pst.executeUpdate() > 0;
        }
    }
    
  
}