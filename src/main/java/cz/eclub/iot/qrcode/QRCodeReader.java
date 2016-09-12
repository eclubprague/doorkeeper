package cz.eclub.iot.qrcode;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;

public class QRCodeReader {

    public static String readCode(BufferedImage image) {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        image)));
        Result qrCodeResult = null;
        try {
            qrCodeResult = new MultiFormatReader().decode(binaryBitmap);
            return qrCodeResult.getText();
        } catch (NotFoundException e) {

            return null;
        }

    }
}
