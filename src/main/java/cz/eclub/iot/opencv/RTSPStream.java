package cz.eclub.iot.opencv;

import cz.eclub.iot.http.HTTPClient;
import cz.eclub.iot.qrcode.QRCodeReader;
import cz.eclub.iot.utils.Constants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.concurrent.atomic.AtomicBoolean;

public class RTSPStream implements Runnable{
    private int id = 0;

    private VideoCapture capture;
    private String rtspAddress;
    private AtomicBoolean isRunning = new AtomicBoolean();
    private HTTPClient httpClient;

    public RTSPStream() {
        this(Constants.RTSP_STREAM_ADDRESS);
    }

    public RTSPStream(String address) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        this.capture = new VideoCapture();
        this.rtspAddress = address;
        this.isRunning.set(false);

        httpClient = new HTTPClient();
    }

    @Override
    public void run() {
        System.out.println("Starting stream!");
        //Open the stream
        capture.open(rtspAddress);


        if(capture.isOpened()) {
            System.out.println("Stream is running!");
            isRunning.set(true);
        }

        while (isRunning.get()) {
            process();
        }
        System.out.println("Stopping stream!");
        capture.release();
    }

    private void process() {
        Mat frame = readFrame();
        Highgui.imwrite("camera.jpg", frame);
        BufferedImage im = ImageConverter.bufferedImageFromMat(frame);
        File outputfile = new File("saved"+id+".jpg");

        String res = QRCodeReader.readCode(im);
        if(res != null) {
            id++;
            /*try {
                ImageIO.write(im, "jpg", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            //Try sending requests:
            if(res.equals("RELAY1")) {
                httpClient.get("http://192.168.1.250/relay_control?1=on");
            }else if(res.equals("RELAY2")) {
                httpClient.get("http://192.168.1.250/relay_control?2=on");
            }
        }
        System.out.println("READ this: "+res);
        //kill();
    }

    public void kill() {
        this.isRunning.set(false);
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    public synchronized Mat readFrame() {
        if(isRunning()) {
            Mat frame = new Mat();
            if(capture.isOpened()) {
                capture.read(frame);
                System.out.println("Frame read: W: "+frame.width()+" H: "+frame.height());
                return frame;
            }else {
                System.err.println("ERROR: No stream opened!");
                return null;
            }

        }else {
            System.err.println("ERROR: Stream service is not running!");
            return null;
        }
    }
}
