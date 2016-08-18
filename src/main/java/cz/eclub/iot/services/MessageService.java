package cz.eclub.iot.services;

import cz.eclub.iot.model.classes.MessageEntity;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by root on 16.8.16.
 */
public class MessageService {
    WebTarget webTarget;

    public MessageService(WebTarget webTarget) {
        this.webTarget=webTarget.path("message");


    }

    public void postMessage(MessageEntity messageEntity){

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(messageEntity,MediaType.APPLICATION_JSON));
    }
}
