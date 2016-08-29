package cz.eclub.iot.model.classes;

import javax.persistence.Entity;

@Entity
public class SensorEntity extends AbstractEntity {
    private String UUID,value,unitName,unitSign;
    private long timestamp;

    public SensorEntity(){};

    public SensorEntity(String UUID, String value, String unitName, String unitSign, long timestamp) {
        this.UUID=UUID;
        this.value=value;
        this.unitName=unitName;
        this.unitSign=unitSign;
        this.timestamp=timestamp;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitSign() {
        return unitSign;
    }

    public void setUnitSign(String unitSign) {
        this.unitSign = unitSign;
    }

    @Override
    public String toString() {
        return "SensorEntity{" +
                "UUID='" + UUID + '\'' +
                ", value='" + value + '\'' +
                ", unitName='" + unitName + '\'' +
                ", unitSign='" + unitSign + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
