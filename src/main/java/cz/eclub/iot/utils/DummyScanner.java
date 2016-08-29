package cz.eclub.iot.utils;

import cz.eclub.iot.model.classes.SensorEntity;
import cz.eclub.iot.services.SensorService;

/**
 * Created by tom on 29.8.16.
 */
public class DummyScanner {
    public void scan(SensorService sensorService) {
        while (true) {


            SensorEntity sensorEntity = new SensorEntity("dummy",String.valueOf(Math.random()),"Random","number",System.currentTimeMillis());

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
