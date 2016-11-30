package cz.eclub.iot;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import cz.eclub.iot.qrcode.QRCodeReaderMine;
import cz.eclub.iot.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.*;


public class MyClient implements Runnable{

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> scheduleHandler;
    private OkHttpClient client = new OkHttpClient();
    private int id = 0;

    public MyClient() {}

    public static void main(String[] args) throws InterruptedException, UnknownHostException {
        System.out.println("-----------------------------------------------");
        System.out.println("Doorkeeper v0.0.1");
        System.out.println("Copyright (c) 2016 eClub | www.eclubprague.com");
        System.out.println("-----------------------------------------------");

        Scanner scan = new Scanner(System.in);
        InetAddress localhost = InetAddress.getLocalHost();
        System.out.println("Your local IP address is: " + localhost.getHostAddress());
        System.out.println("Please insert IP Address:");
        String ip = scan.nextLine();
        while(ip.equals("")) {
            System.out.println("IP address was not specified! Please specify!");
            ip = scan.nextLine();
        }
        System.out.println("Connecting to: "+ip+" ...");
        Constants.RTSP_STREAM_ADDRESS = "rtsp://"+ip+"/";
        Constants.DOORBELL_IP = ip;
        MyClient myClient = new MyClient();

        /*File mustDoFile = new File("mustdo.jpg");
        try {
            BufferedImage imBuff = ImageIO.read(mustDoFile);
            System.out.println(QRCodeReaderMine.readCode(imBuff));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        scheduleHandler = scheduler.scheduleAtFixedRate(myClient, 0, 250, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        //RTSPStream rtspStream = new RTSPStream();
        //rtspStream.run();
        Request request = new Request.Builder().url("http://"+Constants.DOORBELL_IP+"/video.jpg").build();
        try {
            Response response = client.newCall(request).execute();
            InputStream inputStream = response.body().byteStream();
            BufferedImage imBuff = ImageIO.read(inputStream);

            String data = QRCodeReaderMine.readCode(imBuff);
            System.out.println(data);
            String add = "";
            File outputfile;
            if(data !=null) {
                outputfile = new File(add+"saved_ROZP" + (id++) + ".jpg");
            }else {
                outputfile = new File(add+"saved" + (id++) + ".jpg");
            }


            try {
                ImageIO.write(imBuff, "jpg", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}