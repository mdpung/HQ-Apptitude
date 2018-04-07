// Imports the Google Cloud client library

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TextRecognition {
    private String option1 = "";
    private String option2 = "";
    private String option3 = "";
    private String question = "";

    private int numOfLinks;

    private String filePath;

    public TextRecognition(String pathOfFile, int numLinks) throws Exception {
        filePath = pathOfFile;
        numOfLinks = numLinks;
    }

    public void detectText(PrintStream out) throws Exception, IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                EntityAnnotation annotation = res.getTextAnnotationsList().get(0);
                Scanner scanner = new Scanner(annotation.getDescription());

                scanner.nextLine();
                scanner.nextLine();
                while ((question += " " + scanner.nextLine()).charAt(question.length() - 1) != '?');

                System.out.println(question);

                option1 = scanner.nextLine();
                option2 = scanner.nextLine();
                option3 = scanner.nextLine();

                System.out.println(option1);
                System.out.println(option2);
                System.out.println(option3);
            }
        }
    }

    public String getAnswers() throws Exception {
        PrintStream ps = new PrintStream(System.out);
        detectText(ps);
        Answerer answerer = new Answerer(option1, option2, option3);
        CustomSearch cs = new CustomSearch(question, numOfLinks);

        cs.search();
        String[] websites = cs.getURLs();

        for (String url : websites) {
            answerer.scrapWebPage(url);

        }

        String result = ""  + "" + "";

        return answerer.getResults();
    }
}
