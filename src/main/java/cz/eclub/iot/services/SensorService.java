package cz.eclub.iot.services;

import cz.eclub.iot.model.classes.SensorEntity;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SensorService {
    WebTarget webTarget;

    public SensorService(WebTarget webTarget) {
        this.webTarget = webTarget.path("sensor");


    }

    public void postMessage(SensorEntity messageEntity) {

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(messageEntity, MediaType.APPLICATION_JSON));
        System.out.println(response.getStatus());
    }
}
