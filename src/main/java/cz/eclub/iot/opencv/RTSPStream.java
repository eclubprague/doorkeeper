package cz.eclub.iot.opencv;

import cz.eclub.iot.utils.Constants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

import java.util.concurrent.atomic.AtomicBoolean;

public class RTSPStream implements Runnable{

    private VideoCapture capture;
    private String rtspAddress;
    private AtomicBoolean isRunning = new AtomicBoolean();

    public RTSPStream() {
        this(Constants.RTSP_STREAM_ADDRESS);
    }

    public RTSPStream(String address) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        this.capture = new VideoCapture();
        this.rtspAddress = address;
        this.isRunning.set(false);
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

        }
        System.out.println("Stopping stream!");
        capture.release();
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
