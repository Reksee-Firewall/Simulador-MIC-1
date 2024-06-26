package mic.projectjavafxml.backend;

public class Mux {
    private short output = 0;

    public short getOutput() {
        return output;
    }

    public void setOutput(short output) {
        this.output = output;
    }

    public void decideOutput(boolean control, short inputA, short inputB) {
        if (control)
            output = inputB;
        else
            output = inputA;
    }

    @Override
    public String toString() {
        return "Mux{" +
                "output=" + output +
                '}';
    }
}
