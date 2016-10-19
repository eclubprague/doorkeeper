package cz.eclub.iot.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QRCodeReader {

    public static String readCode(BufferedImage image) {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        image)));
        Result qrCodeResult = null;
        try {
            Map<DecodeHintType, Object> map = new HashMap();
            map.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            List<BarcodeFormat> formats = new ArrayList<>();
            formats.add(BarcodeFormat.QR_CODE);
            map.put(DecodeHintType.POSSIBLE_FORMATS, formats);
            map.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            qrCodeResult = new MultiFormatReader().decode(binaryBitmap, map);
            return qrCodeResult.getText();
        } catch (NotFoundException e) {
            return null;
        }

    }
}
