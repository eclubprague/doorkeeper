package cz.eclub.iot.opencv;

import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageConverter {

    public static BufferedImage bufferedImageFromMat(Mat mat) {
        if ( mat != null ) {
            int cols = mat.cols();
            int rows = mat.rows();
            int elemSize = (int) mat.elemSize();
            byte[] data = new byte[cols * rows * elemSize];
            int type;
            mat.get(0, 0, data);
            switch (mat.channels()) {
                case 1:
                    type = BufferedImage.TYPE_BYTE_GRAY;
                    break;
                case 3:
                    type = BufferedImage.TYPE_3BYTE_BGR;
                    // bgr to rgb
                    byte b;
                    for (int i = 0; i < data.length; i = i + 3) {
                        b = data[i];
                        data[i] = data[i + 2];
                        data[i + 2] = b;
                    }
                    break;
                default:
                    return null;
            }
            BufferedImage bimg = new BufferedImage(cols, rows, type);
            bimg.getRaster().setDataElements(0, 0, cols, rows, data);
            return bimg;
        }else {
            return null;
        }
    }
}
