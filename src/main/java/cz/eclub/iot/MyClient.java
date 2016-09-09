package cz.eclub.iot;


import cz.eclub.iot.opencv.RTSPStream;


public class MyClient {

    public MyClient() {
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println(System.getProperty("java.library.path"));
        MyClient myClient = new MyClient();
        myClient.run();

    }

    public void run() throws InterruptedException {

        RTSPStream rtspStream = new RTSPStream();
        new Thread(rtspStream).start();


    }


}