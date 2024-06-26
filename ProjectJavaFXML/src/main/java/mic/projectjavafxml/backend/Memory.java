package mic.projectjavafxml.backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Memory {
    private final ObservableList<MemoryLine> memory;
    private short address;
    private byte readCounter, writeCounter;

    public Memory() {
        memory = FXCollections.observableArrayList();
        for (short i = 0; i < 2048; i++)
            memory.add(new MemoryLine(i,(short) 0x7000));
        for (short i = 2048; i < 4096; i++)
            memory.add(new MemoryLine(i, (short) 0));

        address = 0;
        readCounter = 0;
        writeCounter = 0;
    }

    public void setMemoryInitial() {
        for (short i = 0; i < 2048; i++)
            memory.get(i).setValue((short) 0x7000);
        for (short i = 2048; i < 4096; i++)
            memory.get(i).setValue((short) 0);

        address = 0;
        readCounter = 0;
        writeCounter = 0;
    }

    public boolean isReadReady() {
        return readCounter == 2;
    }

    public void incrementReadCounter() {
        readCounter++;
    }

    public short read() {
        readCounter = 0;
        return memory.get(address).getValue();
    }

    public boolean isWriteReady() {
        return writeCounter == 2;
    }

    public void incrementWriteCounter() {
        writeCounter++;
    }

    public void write(short value) {
        writeCounter = 0;
        memory.get(address).setValue(value);
    }

    private boolean isValidAddress(short address) {
        return address >= 0 && address <= 4095;
    }

    public void setAddress(short address) {
        if (!isValidAddress(address))
            return;
        this.address = address;
    }

    public void write(short address, short value) {
        if (!isValidAddress(address))
            return;
        memory.get(address).setValue(value);
    }

    public void write(short[] codeData) {
        int length = codeData.length;
        if (length > 4096) {
            length = 4096;
        }
        for (short i = 0; i < length; i++) {
            memory.get(i).setValue(codeData[i]);
        }
    }

    public short read(short address) {
        if (!isValidAddress(address))
            return 0;
        return memory.get(address).getValue();
    }

    public ObservableList<MemoryLine> getMemory() {
        return memory;
    }
}
