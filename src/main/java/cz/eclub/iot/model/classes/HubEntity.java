package cz.eclub.iot.model.classes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;

/**
 * Created by Tom on 02.08.2016.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubEntity extends AbstractEntity{
    private long uuid;
    private String name;
    private String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
