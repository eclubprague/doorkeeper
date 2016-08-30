package cz.eclub.iot.scanner.bluetooth;

import cz.eclub.iot.services.SensorService;
import tinyb.BluetoothDevice;
import tinyb.BluetoothManager;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class BluetoothScanner implements Runnable {

    private ConcurrentHashMap<String, Boolean> allowedMACs;
    private SensorService sensorService;

    public BluetoothScanner(SensorService sensorService) {
        allowedMACs = new ConcurrentHashMap<String, Boolean>();
        allowedMACs.put("B0:B4:48:BF:C6:84", false);
        allowedMACs.put("B0:B4:48:BD:D3:05", false);

        this.sensorService = sensorService;

        unblockBluetooth();

    }

    private void unblockBluetooth() {
        try {
            String cmd = "rfkill unblock bluetooth";

            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(cmd);
            pr.waitFor();
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        BluetoothManager manager = BluetoothManager.getBluetoothManager();
        manager.getAdapters().forEach(bluetoothAdapter -> {
            if (bluetoothAdapter.getAddress().equalsIgnoreCase("00:1A:7D:DA:71:09")) {
                bluetoothAdapter.setPowered(false);
                bluetoothAdapter.setPowered(true);
                manager.setDefaultAdapter(bluetoothAdapter);
                System.out.println("set adapter");
            }
        });

        boolean discoveryStarted = manager.startDiscovery();

        BluetoothDevice sensor = null;
        while (true) {
            List<BluetoothDevice> list = manager.getDevices();
            for (BluetoothDevice device : list) {
                if (!allowedMACs.getOrDefault(device.getAddress(), true)) {
                    System.out.println("needs to connect to the " + device.getAddress());
                    allowedMACs.put(device.getAddress(), true);
                    new Thread(new TemperatureReader(device, sensorService, allowedMACs)).start();


                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
