/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.nio.file.Paths;





/**
 *
 * @author ADMIN
 */
public class QRCodeGenerator {
    
    
   // Reusable method
    public static void generateQRCode(String data, String path) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 200, 200);
        MatrixToImageWriter.writeToPath(matrix, "PNG", Paths.get(path));
    }

    // Optional: main for testing
    public static void main(String[] args) {
        try {
            generateQRCode("TEST123", "C:\\QRCode\\test.png");
            System.out.println("✅ QR Code generated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
