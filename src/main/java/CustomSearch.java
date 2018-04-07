import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


public class CustomSearch {
    private final char SEPARATOR = '&';

    private String[] urls;
    private int numOfURLs;
    private String question;

    public CustomSearch(String q, int num) {
        numOfURLs = num;
        question = q;
        urls = new String[numOfURLs];
    }

    public void search() {
        String query = "https://www.googleapis.com/customsearch/v1?" + SEPARATOR
                + "key=AIzaSyBWihBpptfM2IrpjpTSlY9MEN4a3bGU87Y" + SEPARATOR
                + "cx=002837402374397714469:uezd4oewcsy" + SEPARATOR +
                "q=" + question.replaceAll(" ", "%20");

        try {
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            Scanner scanner = new Scanner(conn.getInputStream()).useDelimiter("\\A");

            String next = scanner.next();
            JSONObject jsonObject = new JSONObject(next);
            ArrayList items = (ArrayList) jsonObject.toMap().get("items");
            for (int i = 0; i < numOfURLs; i++) {
                urls[i] = ((HashMap) items.get(i)).get("link").toString();
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getURLs() {
        return urls;
    }
}
