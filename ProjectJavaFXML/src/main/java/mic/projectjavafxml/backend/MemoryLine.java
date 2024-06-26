package mic.projectjavafxml.backend;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MemoryLine {
    private short value;
    private final SimpleIntegerProperty address;
    private final SimpleStringProperty stringValue;

    public MemoryLine(short address, short value) {
        this.address = new SimpleIntegerProperty(address);
        this.value = value;
        stringValue = new SimpleStringProperty(NumericFactory.getStringValue16(value));
    }

    public int getAddress() {
        return address.get();
    }

    public SimpleIntegerProperty addressProperty() {
        return address;
    }

    public void setAddress(int address) {
        this.address.set(address);
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
        stringValue.setValue(NumericFactory.getStringValue16(value));
    }

    public String getStringValue() {
        return stringValue.get();
    }

    public SimpleStringProperty stringValueProperty() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue.set(stringValue);
    }
}
