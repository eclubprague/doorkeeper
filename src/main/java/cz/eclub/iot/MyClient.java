package cz.eclub.iot;


import cz.eclub.iot.bluetooth.BluetoothTinyb;
import cz.eclub.iot.services.HubService;
import cz.eclub.iot.services.MessageService;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;


public class MyClient {
    ClientConfig config;
    Client client;
    WebTarget webTarget;
    HubService hubService;
    MessageService messageService;


    public MyClient() {
        config = new ClientConfig();
        config = new ClientConfig().register(JacksonFeature.class);
        client = ClientBuilder.newClient(config);
        webTarget = client.target("http://iot.eclubprague.com:8080/iot-server/webapi/");
        messageService = new MessageService(webTarget);
    }


    public void run() throws InterruptedException {
        new BluetoothTinyb().scan(messageService);

    }


    public static void main(String[] args) throws InterruptedException {

        System.out.println(System.getProperty("java.library.path"));
        MyClient myClient = new MyClient();
        myClient.run();

        //runBT();

        //TODO STREAM
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture capture = new VideoCapture();
        capture.open("rtsp://192.168.1.250/");
        while (capture.isOpened()) {

            Mat frame = new Mat();

            //camera.grab();
            //System.out.println("Frame Grabbed");
            //camera.retrieve(frame);
            //System.out.println("Frame Decoded");

            capture.read(frame);
            System.out.println("Frame Obtained");

            System.out.println("Captured Frame Width " + frame.width());
            Highgui.imwrite("camera.jpg", frame);
            System.out.println("OK");

            break;
        }
        capture.release();
    }

}