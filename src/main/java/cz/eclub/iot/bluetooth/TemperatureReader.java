package cz.eclub.iot.bluetooth;

import cz.eclub.iot.model.classes.MessageEntity;
import cz.eclub.iot.model.classes.SensorEntity;
import cz.eclub.iot.services.HubService;
import cz.eclub.iot.services.MessageService;
import cz.eclub.iot.services.SensorService;
import tinyb.BluetoothDevice;
import tinyb.BluetoothGattCharacteristic;
import tinyb.BluetoothGattService;

import javax.smartcardio.TerminalFactory;
import java.util.List;

/**
 * Created by root on 16.8.16.
 */
public class TemperatureReader implements Runnable {

    private BluetoothDevice bluetoothDevice;
    private BluetoothGattService tempService = null;
    private SensorService sensorService;

    private BluetoothGattCharacteristic tempValue;
    private BluetoothGattCharacteristic tempConfig;
    private BluetoothGattCharacteristic tempPeriod;

    public TemperatureReader(BluetoothDevice bluetoothDevice, SensorService sensorService){
        this.bluetoothDevice=bluetoothDevice;
        this.sensorService=sensorService;

        try {
            tempService = getService(bluetoothDevice, "f000aa00-0451-4000-b000-000000000000");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (tempService == null) {
            System.err.println("This device does not have the temperature service we are looking for.");
            bluetoothDevice.disconnect();
            System.exit(-1);
        }
        System.out.println("Found service " + tempService.getUUID());

        tempValue = getCharacteristic(tempService, "f000aa01-0451-4000-b000-000000000000");
        tempConfig = getCharacteristic(tempService, "f000aa02-0451-4000-b000-000000000000");
        tempPeriod = getCharacteristic(tempService, "f000aa03-0451-4000-b000-000000000000");

        if (tempValue == null || tempConfig == null || tempPeriod == null) {
            System.err.println("Could not find the correct characteristics.");
            bluetoothDevice.disconnect();
            System.exit(-1);
        }

        System.out.println("Found the temperature characteristics");
    }

    @Override
    public void run() {
        byte[] config = { 0x01 };
        tempConfig.writeValue(config);

        while (true) {
            byte[] tempRaw = tempValue.readValue();
            int ambientTempRaw = (tempRaw[2] & 0xff) | (tempRaw[3] << 8);
            float ambientTempCelsius = convertCelsius(ambientTempRaw);
            System.out.println(bluetoothDevice.getAddress()+" Temp = "+ambientTempCelsius);

            SensorEntity sensorEntity = new SensorEntity(bluetoothDevice.getAddress(),String.valueOf(ambientTempCelsius),"Temperature","°C",System.currentTimeMillis());


            sensorService.postMessage(sensorEntity);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /*
 * Our device should expose a temperature service, which has a UUID we can find out from the data sheet. The service
 * description of the SensorTag can be found here:
 * http://processors.wiki.ti.com/images/a/a8/BLE_SensorTag_GATT_Server.pdf. The service we are looking for has the
 * short UUID AA00 which we insert into the TI Base UUID: f000XXXX-0451-4000-b000-000000000000
 */
    static BluetoothGattService getService(BluetoothDevice device, String UUID) throws InterruptedException {
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

    static BluetoothGattCharacteristic getCharacteristic(BluetoothGattService service, String UUID) {
        List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
        if (characteristics == null)
            return null;

        for (BluetoothGattCharacteristic characteristic : characteristics) {
            if (characteristic.getUUID().equals(UUID))
                return characteristic;
        }
        return null;
    }

    static float convertCelsius(int raw) {
        return raw / 128f;
    }
}
