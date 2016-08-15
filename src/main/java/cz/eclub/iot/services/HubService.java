package cz.eclub.iot.services;

import cz.eclub.iot.model.classes.MessageEntity;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Tom on 11.08.2016.
 */
public class HubService {
    WebTarget webTarget;

    public HubService(WebTarget webTarget) {
        this.webTarget=webTarget.path("hub");


    }

    public MessageEntity getHubById(Integer id){
        WebTarget helloworldWebTarget = webTarget.path(id.toString());
        Invocation.Builder invocationBuilder = helloworldWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        //System.out.println(response.getStatus());
        return response.readEntity(MessageEntity.class);
    }
}
