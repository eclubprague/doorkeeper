package cz.eclub.iot.bluetooth;

import cz.eclub.iot.services.HubService;
import cz.eclub.iot.services.MessageService;
import cz.eclub.iot.services.SensorService;
import tinyb.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by root on 16.8.16.
 */
public class BluetoothTinyb {
    private static final float SCALE_LSB = 0.03125f;
    static boolean running = true;

    HashMap<String,Boolean> allowedMACs;


    public BluetoothTinyb(){
        allowedMACs = new HashMap<String, Boolean>();
        allowedMACs.put("B0:B4:48:BF:C6:84",false);
        allowedMACs.put("B0:B4:48:BD:D3:05",false);


    }


    static void printDevice(BluetoothDevice device) {
        System.out.print("Address = " + device.getAddress());
        System.out.print(" Name = " + device.getName());
        System.out.print(" Connected = " + device.getConnected());
        System.out.print(" RSSI="+device.getRSSI());
        System.out.print(" "+ Arrays.toString(device.getUUIDs()));
        System.out.println();
    }

    public void scan(SensorService sensorService) throws InterruptedException {

        BluetoothManager manager = BluetoothManager.getBluetoothManager();
        boolean discoveryStarted = manager.startDiscovery();
        System.out.println("The discovery started: " + (discoveryStarted ? "true" : "false"));

        BluetoothDevice sensor = null;
        while(true){
            List<BluetoothDevice> list = manager.getDevices();
            for (BluetoothDevice device : list) {
                //if(allowedMACs.containsKey(device.getAddress())){
                //    allowedMACs.put(device.getAddress(),device.getConnected());
                //}

                if(!allowedMACs.getOrDefault(device.getAddress(),true)){
                    System.out.println("needs to connect to the "+device.getAddress());
                    if(device.connect()){
                        System.out.println("successfully connected");
                        allowedMACs.put(device.getAddress(),true);
                        new Thread(new TemperatureReader(device,sensorService)).start();


                    }
                }
            }

        }
    }




    /*
     * After discovery is started, new devices will be detected. We can get a list of all devices through the manager's
     * getDevices method. We can the look through the list of devices to find the device with the MAC which we provided
     * as a parameter. We continue looking until we find it, or we try 15 times (1 minutes).
     */
    static BluetoothDevice getDevice(String address) throws InterruptedException {
        BluetoothManager manager = BluetoothManager.getBluetoothManager();
        BluetoothDevice sensor = null;
        for (int i = 0; (i < 15) && running; ++i) {
            List<BluetoothDevice> list = manager.getDevices();
            if (list == null)
                return null;

            System.out.println("");
            for (BluetoothDevice device : list) {
                printDevice(device);
                /*
                 * Here we check if the address matches.
                 */
                if (device.getAddress().equals(address))
                    sensor = device;
            }

            if (sensor != null) {
                return sensor;
            }
            Thread.sleep(4000);
        }
        return null;
    }



}
