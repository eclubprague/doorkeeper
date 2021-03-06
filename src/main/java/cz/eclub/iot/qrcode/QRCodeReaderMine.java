package cz.eclub.iot.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QRCodeReaderMine {

    public static String readCode(BufferedImage image) {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        image)));
        Result qrCodeResult = null;
        try {
            List<BarcodeFormat> formats = new ArrayList<>();
            formats.add(BarcodeFormat.QR_CODE);

            Map hintMap = new HashMap();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hintMap.put(DecodeHintType.POSSIBLE_FORMATS, formats);
            hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

            qrCodeResult = new MultiFormatReader().decode(binaryBitmap,hintMap);
            return qrCodeResult.getText();
        } catch (NotFoundException e) {
            return null;
        }

    }
}
