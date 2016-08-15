package cz.eclub.iot;


import cz.eclub.iot.services.HubService;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.bluetooth.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.IOException;
import java.util.Arrays;


public class MyClient {
    ClientConfig config;
    Client client;
    WebTarget webTarget;

    public static void main(String[] args) throws BluetoothStateException, InterruptedException {
        MyClient myClient = new MyClient();
        //myClient.run();

        Object lock = new Object();

        DiscoveryListener listener = new DiscoveryListener() {
            @Override
            public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
                try {
                    System.out.println(remoteDevice.getFriendlyName(true));
                    System.out.println(remoteDevice.getBluetoothAddress());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {
                System.out.println(Arrays.toString(serviceRecords));
            }

            @Override
            public void serviceSearchCompleted(int i, int i1) {
                System.out.println("c");
            }

            @Override
            public void inquiryCompleted(int i) {
                System.out.println("d");
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
        };

        synchronized (lock) {
            if (LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener)) {
                lock.wait();
            }
        }

    }

    public MyClient() {
        config = new ClientConfig();
        config = new ClientConfig().register(JacksonFeature.class);
        client = ClientBuilder.newClient(config);
        webTarget = client.target("http://iot.eclubprague.com:8080/iot-server/webapi/");
    }

    public void run(){

        HubService hubService = new HubService(webTarget);
        System.out.println(hubService.getHubById(4));

    }

}