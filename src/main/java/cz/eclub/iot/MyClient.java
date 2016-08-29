package cz.eclub.iot;


import cz.eclub.iot.bluetooth.BluetoothTinyb;
import cz.eclub.iot.services.HubService;
import cz.eclub.iot.services.SensorService;
import cz.eclub.iot.utils.DummyScanner;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.net.ssl.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.ext.ExceptionMapper;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;


public class MyClient {
    ClientConfig config;
    Client client;
    WebTarget webTarget;
    HubService hubService;
    SensorService sensorService;


    public MyClient() {
        config = new ClientConfig();
        config = new ClientConfig().register(JacksonFeature.class);

        try {
            SSLContext context;
            KeyManagerFactory kmf;
            KeyStore ks;
            char[] storepass = "changeit".toCharArray();
            char[] keypass = "changeit".toCharArray();
            //String storename = "/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/cacerts";
            String storename = "C:\\Program Files\\Java\\jdk1.8.0_45\\jre\\lib\\security\\cacerts";

            InputStream kis = new FileInputStream(storename);
            KeyStore trustStore = KeyStore.getInstance("jks");
            trustStore.load(kis, keypass);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            context = SSLContext.getInstance("TLS");
            context.init(null, trustManagerFactory.getTrustManagers(), null);


            client = ClientBuilder.newBuilder().sslContext(context).withConfig(config).build();
        } catch (Exception e) {
            e.printStackTrace();
        }


        webTarget = client.target("https://iot.eclubprague.com/iot/");
        //webTarget = client.target("http://192.168.1.117:8080/webapi/");

        sensorService = new SensorService(webTarget);
    }


    public void run() throws InterruptedException {
        //new BluetoothTinyb().scan(sensorService);

        new DummyScanner().scan(sensorService);


        //RTSPStream rtspStream = new RTSPStream();
        //new Thread(rtspStream).start();


    }

    public void runSip() throws InterruptedException {


    }

    public static void main(String[] args) throws InterruptedException {


        System.out.println(System.getProperty("java.library.path"));
        MyClient myClient = new MyClient();
        myClient.run();


    }

}