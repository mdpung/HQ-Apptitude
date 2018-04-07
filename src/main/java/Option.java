public class Option {
    private String string;
    private String stringCompressed;
    private int hashValue;
    private int numInstances;

    public Option(String str) {
        string = str;
        stringCompressed = compress(string);
        hashValue = stringCompressed.hashCode();
        numInstances = 0;

    }

    private String compress(String str) {
        return str.replaceAll("[^A-Za-z]+", "").toLowerCase();
    }

    public int getHashValue() {
        return hashValue;
    }

    public String getString() {
        return string;
    }

    public String getStringCompressed() {
        return stringCompressed;
    }

    public int getStringLength() {
        return string.length();
    }

    public int getNumInstances() {
        return numInstances;
    }

    public void incrementNumInstances() {
        numInstances++;
    }
}
