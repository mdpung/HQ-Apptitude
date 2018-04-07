import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class Answerer {
    private static final String GOOGLE = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
    private static final String CHARSET = "UTF-8";
    private static final int ALPHA = 31;

    private Option option1, option2, option3;

    public Answerer(String o1, String o2, String o3) {
        this.option1 = new Option(o1);
        this.option2 = new Option(o2);
        this.option3 = new Option(o3);
    }

    public void scrapWebPage(String website) throws IOException {
        URL url = new URL(website);
        System.out.println(url);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        PrintWriter pw = new PrintWriter(new File("html.txt"));

        String html = "";
        String line;
        while ((line = br.readLine()) != null) {
            line = line.replaceAll("[^A-Za-z]+", "").toLowerCase();
            html += line;
            pw.append(line);
        }
        searchLine(html);
    }

    public void searchLine(String line) {
        if (line == null)
            return;

        String combinedString = line;

        if (combinedString.length() > 0) {
            getNumInstances(combinedString, option1);
            getNumInstances(combinedString, option2);
            getNumInstances(combinedString, option3);
        }

    }

    public void getNumInstances(String stringLine, Option comp) {
        ArrayList<Character> hashString = new ArrayList<>();
        ArrayList<Integer> hashValues = new ArrayList<>();
        int totalHashValue = 0;

        int length;
        if (comp.getStringLength() < stringLine.length())
            length = comp.getStringLength();
        else
            length = stringLine.length();

        char character;
        int value;

        for (int i = 0; i < length; i++) {
            character = stringLine.charAt(i);
            value = ((int) character) * ((int) Math.pow(ALPHA, length - i - 1));
            totalHashValue += value;
            hashString.add(character);
            hashValues.add(value);
        }

        checkSimilarity(totalHashValue, comp, hashString);

        for (int i = length; i < (stringLine.length()); i++) {
            totalHashValue -= hashValues.remove(0);
            hashString.remove(0);

            totalHashValue *= ALPHA;
            for (int j = 0; j < length - 1; j++) {
                hashValues.set(j, hashValues.get(j) * ALPHA);
            }

            character = stringLine.charAt(i);
            value = ((int) character);
            totalHashValue += value;
            hashValues.add(value);
            hashString.add(character);

            checkSimilarity(totalHashValue, comp, hashString);
        }
    }

    private void checkSimilarity(int totalHashValue, Option comp, ArrayList<Character> hashString) {
        String hs = toString(hashString);
        String cs = comp.getStringCompressed();

        if (totalHashValue == comp.getHashValue()) {
//            System.out.println("identical hash values");
            if (hs.equals(cs)) {
                comp.incrementNumInstances();
            }
        }
    }

    private String toString(ArrayList<Character> arrayList) {
        return arrayList.toString().replaceAll(", |\\[|\\]", "");
    }

    public String getResults() {
        String result = option1.getString() + ": " + option1.getNumInstances() + "\n"
                + option2.getString() + ": " + option2.getNumInstances() + "\n"
                + option3.getString() + ": " + option3.getNumInstances();
        return result;
    }
}
