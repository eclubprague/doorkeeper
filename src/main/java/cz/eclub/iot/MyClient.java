package cz.eclub.iot;


import cz.eclub.iot.opencv.RTSPStream;
import cz.eclub.iot.utils.Constants;

import java.util.Scanner;


public class MyClient {

    public MyClient() {
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("-----------------------------------------------");
        System.out.println("Doorkeeper v0.0.1");
        System.out.println("Copyright (c) 2016 eClub | www.eclubprague.com");
        System.out.println("-----------------------------------------------");
        Scanner scan = new Scanner(System.in);
        System.out.println("Please insert IP Address:");
        String ip = scan.nextLine();
        while(ip.equals("")) {
            System.out.println("IP address was not specified! Please specify!");
            ip = scan.nextLine();
        }

        Constants.RTSP_STREAM_ADDRESS = "rtsp://"+ip+"/";
        Constants.DOORBELL_IP = ip;
        MyClient myClient = new MyClient();
        myClient.run();

    }

    public void run() throws InterruptedException {

        RTSPStream rtspStream = new RTSPStream();
        rtspStream.run();

    }


}