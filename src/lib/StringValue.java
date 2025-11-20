package lib;

public class StringValue implements Value {

    private String value;


    public StringValue(String value) {
        this.value = value;
    }


    @Override
    public double asNumber() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Value get(int index) {
        if (index < 0 || index >= value.length()) {
            throw new RuntimeException("String index out of bounds: " + index);
        }
        return new StringValue(String.valueOf(value.charAt(index)));
    }

    @Override
    public String asString() {
        return value;
    }


    @Override
    public String toString() {
        return asString();
    }
}
