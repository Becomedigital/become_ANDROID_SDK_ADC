package com.example.demosdk.utils;


import android.graphics.Bitmap;
import android.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;


import java.io.IOException;


/**
 * Created by g501999 on 18/03/2018.
 */

public class QRCodeGenerator {


    public Bitmap getQRCodeImage(String text, int width, int height, int colorBack) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        byte[] encodedBytes = Base64.encode(text.getBytes(),text.getBytes().length );
//        String dataQR = new String (encodedBytes);
        String dataQR = new String (text);
        System.out.println("encodedBytes " + dataQR);

        BitMatrix bitMatrix = qrCodeWriter.encode(dataQR, BarcodeFormat.QR_CODE, width, height);

        int lwidth = bitMatrix.getWidth();
        int lheight = bitMatrix.getHeight();
        int colorFront = 0xFFFFFFFF;

        int[] pixels = new int[lwidth * lheight];
        for (int y = 0; y < lheight; y++)
        {
            int offset = y * lwidth;
            for (int x = 0; x < lwidth; x++)
            {

                pixels[offset + x] = bitMatrix.get(x, y) ? colorBack : colorFront;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(lwidth, lheight, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, lwidth, 0, 0, lwidth, lheight);
        return bitmap;
    }

    public Bitmap getBarCodeImage(String text, int width, int height, int colorBack) throws WriterException, IOException {
        Code128Writer code128Writer = new Code128Writer();

        BitMatrix bitMatrix = code128Writer.encode(text, BarcodeFormat.CODE_128, width, height);

        int lwidth = bitMatrix.getWidth();
        int lheight = bitMatrix.getHeight();
        int colorFront = 0xFFFFFFFF;

        int[] pixels = new int[lwidth * lheight];
        for (int y = 0; y < lheight; y++)
        {
            int offset = y * lwidth;
            for (int x = 0; x < lwidth; x++)
            {

                pixels[offset + x] = bitMatrix.get(x, y) ? colorBack : colorFront;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(lwidth, lheight, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, lwidth, 0, 0, lwidth, lheight);
        return bitmap;
    }

}
