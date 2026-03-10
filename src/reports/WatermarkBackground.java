/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reports;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.*;

/**
 *
 * @author ADMIN
 */


public class WatermarkBackground extends PdfPageEventHelper {

    private com.itextpdf.text.Image watermarkImage;

    public WatermarkBackground(com.itextpdf.text.Image watermarkImage) {
        this.watermarkImage = watermarkImage;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {

        try {
            PdfContentByte canvas = writer.getDirectContentUnder(); // IMPORTANT

            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.30f);   // Light transparency   //Stronger watermark 0.50f  //1.0 = fully opaque

            canvas.saveState();
            canvas.setGState(gs);

            float x = (document.getPageSize().getWidth() - watermarkImage.getScaledWidth()) / 2;
            float y = (document.getPageSize().getHeight() - watermarkImage.getScaledHeight()) / 2;

            canvas.addImage(watermarkImage, 
                watermarkImage.getScaledWidth(), 0,
                0, watermarkImage.getScaledHeight(),
                x, y);

            canvas.restoreState();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
