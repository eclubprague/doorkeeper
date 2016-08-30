package cz.eclub.iot.scanner.bluetooth;

import cz.eclub.iot.model.classes.SensorEntity;
import cz.eclub.iot.services.SensorService;
import tinyb.BluetoothDevice;
import tinyb.BluetoothGattCharacteristic;
import tinyb.BluetoothGattService;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TemperatureReader implements Runnable {

    private BluetoothDevice bluetoothDevice;
    private BluetoothGattService tempService = null;
    private SensorService sensorService;

    private BluetoothGattCharacteristic tempValue;
    private BluetoothGattCharacteristic tempConfig;
    private BluetoothGattCharacteristic tempPeriod;

    private ConcurrentHashMap<String, Boolean> allowedMACs;


    public TemperatureReader(BluetoothDevice device, SensorService sensorService, ConcurrentHashMap<String, Boolean> allowedMACs) {
        this.bluetoothDevice = device;
        this.sensorService = sensorService;
        this.allowedMACs = allowedMACs;

    }


    @Override
    public void run() {
        try {
            System.out.println(allowedMACs);
            System.out.println("trying to connect");
            if (bluetoothDevice.connect()) {
                System.out.println("successfully connected");
                allowedMACs.put(bluetoothDevice.getAddress(), true);
            }


            tempService = getService(bluetoothDevice, "f000aa00-0451-4000-b000-000000000000");

            if (tempService == null) {
                System.err.println("This device does not have the temperature service we are looking for.");
                bluetoothDevice.disconnect();

            }

            tempValue = getCharacteristic(tempService, "f000aa01-0451-4000-b000-000000000000");
            tempConfig = getCharacteristic(tempService, "f000aa02-0451-4000-b000-000000000000");
            tempPeriod = getCharacteristic(tempService, "f000aa03-0451-4000-b000-000000000000");

            if (tempValue == null || tempConfig == null || tempPeriod == null) {
            //if (tempValue == null ) {
                System.err.println("Could not find the correct characteristics.");
                bluetoothDevice.disconnect();
                System.exit(-1);
            }

            byte[] config = {0x01};
            tempConfig.writeValue(config);

            while (true) {
                byte[] tempRaw = tempValue.readValue();
                int ambientTempRaw = (tempRaw[2] & 0xff) | (tempRaw[3] << 8);
                double ambientTempCelsius = convertCelsius(ambientTempRaw);


                SensorEntity sensorEntity = new SensorEntity(bluetoothDevice.getAddress().replaceAll(":", ""), "" + ambientTempCelsius, "Temperature", "degree celsius", System.currentTimeMillis());
                System.out.println(sensorEntity);
                sensorService.postMessage(sensorEntity);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.err.println("device " + bluetoothDevice.getAddress() + " disconnected");
            System.err.println(bluetoothDevice.getAddress());
            System.err.println(e.getMessage());
            bluetoothDevice.disconnect();
            allowedMACs.put(bluetoothDevice.getAddress(), false);
        }
    }


    private BluetoothGattService getService(BluetoothDevice device, String UUID) throws InterruptedException {
        System.out.println("Services exposed by device:");
        BluetoothGattService tempService = null;
        List<BluetoothGattService> bluetoothServices = null;
        do {
            bluetoothServices = device.getServices();
            if (bluetoothServices == null)
                return null;

            for (BluetoothGattService service : bluetoothServices) {
                System.out.println("UUID: " + service.getUUID());
                if (service.getUUID().equals(UUID))
                    tempService = service;
            }
            Thread.sleep(4000);
        } while (bluetoothServices.isEmpty());
        return tempService;
    }

    private BluetoothGattCharacteristic getCharacteristic(BluetoothGattService service, String UUID) {
        List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
        if (characteristics == null)
            return null;

        for (BluetoothGattCharacteristic characteristic : characteristics) {
            if (characteristic.getUUID().equals(UUID))
                return characteristic;
        }
        return null;
    }

    private double convertCelsius(int raw) {
        return raw / 128.0;
    }
}
