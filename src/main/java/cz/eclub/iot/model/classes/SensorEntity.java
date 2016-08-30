package cz.eclub.iot.model.classes;

import javax.persistence.Entity;

@Entity
public class SensorEntity extends AbstractEntity {
    private String _UUID, value, unitName, unitSign;
    private long timestamp;

    public SensorEntity() {};

    public SensorEntity(String _UUID, String value, String unitName, String unitSign, long timestamp) {
        this._UUID = _UUID;
        this.value = value;
        this.unitName = unitName;
        this.unitSign = unitSign;
        this.timestamp = timestamp;
    }

    public String get_UUID() {
        return _UUID;
    }

    public void set_UUID(String _UUID) {
        this._UUID = _UUID;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "SensorEntity{" +
                "_UUID='" + _UUID + '\'' +
                ", value='" + value + '\'' +
                ", unitName='" + unitName + '\'' +
                ", unitSign='" + unitSign + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
