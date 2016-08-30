package cz.eclub.iot.scanner;

import cz.eclub.iot.model.classes.SensorEntity;
import cz.eclub.iot.services.SensorService;

public class DummyScanner implements Runnable {

    private SensorService sensorService;

    public DummyScanner(SensorService sensorService) {
        this.sensorService=sensorService;
    }


    @Override
    public void run() {
        while (true) {
            SensorEntity sensorEntity = new SensorEntity("DUMMY", String.valueOf(Math.random()), "Random", "number", System.currentTimeMillis());
            System.out.println(sensorEntity);
            sensorService.postMessage(sensorEntity);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
