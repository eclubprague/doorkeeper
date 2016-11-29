package cz.eclub.iot.opencv;

import cz.eclub.iot.http.HTTPClient;
import cz.eclub.iot.qrcode.QRCodeReaderMine;
import cz.eclub.iot.utils.Constants;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class RTSPStream {
    private int id = 0;

    private VideoCapture videoCapture;
    private String rtspAddress;
    private AtomicBoolean isRunning = new AtomicBoolean();
    private HTTPClient httpClient;
    private String lastMessage = "";
    public RTSPStream() {
        this(Constants.RTSP_STREAM_ADDRESS);
    }

    public RTSPStream(String address) {

        if(System.getProperty("sun.arch.data.model").equals("64")) {
            System.loadLibrary("opencv_java249_64");
            System.loadLibrary("opencv_ffmpeg249_64");
        }else {
            System.loadLibrary("opencv_java249");
            System.loadLibrary("opencv_ffmpeg249");
        }

        this.videoCapture = new VideoCapture();
        this.rtspAddress = address;
        this.isRunning.set(false);

        httpClient = new HTTPClient();
    }


    public void run() {
        System.out.println("Starting stream ...");
        //Open the stream
        videoCapture.open(rtspAddress);

        if (videoCapture.isOpened()) {
            System.out.println("Stream is running!");
            isRunning.set(true);
        }

        while (isRunning.get()) {
            process();
        }
        System.out.println("Stopping stream!");
        videoCapture.release();
    }

    private void process() {
        Mat frame = readFrame();
        //Highgui.imwrite("camera.jpg", frame);
        BufferedImage im = ImageConverter.bufferedImageFromMat(frame);
        File outputfile = new File("saved" + id + ".jpg");

        String res = QRCodeReaderMine.readCode(im);
        id++;
        if (res != null) {
            System.out.println("FOUND WITH ID: "+id);


            //Try sending requests:
            if (res.equals("RELAY1")) {
                httpClient.get("http://"+Constants.DOORBELL_IP+"/relay_control?1=on");
            } else if (res.equals("RELAY2")) {
                httpClient.get("http://"+Constants.DOORBELL_IP+"/relay_control?2=on");
            }
        }

        try {
                ImageIO.write(im, "jpg", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
        }
        //kill();
    }

    public void kill() {
        this.isRunning.set(false);
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    public synchronized Mat readFrame() {
        if (isRunning()) {
                Mat frame = new Mat();
                if (videoCapture.isOpened()) {
                    videoCapture.read(frame);
                    //System.out.println("Frame read: W: " + frame.width() + " H: " + frame.height());
                    return frame;
                } else {
                    System.err.println("ERROR: No stream opened!");
                    return null;
                }
        } else {
            System.err.println("ERROR: Stream service is not running!");
            return null;
        }
    }
}
