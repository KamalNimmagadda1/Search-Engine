import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class ExtractingLinks {
    public static void main(String[] args) throws Exception {
        File crawledFolder = new File("/Users/kamalnimmagadda/Desktop/Information Retrieval/HW4/LATIMES/latimes");
        File urlMap = new File("/Users/kamalnimmagadda/Desktop/Information Retrieval/HW4/LATIMES/URLtoHTML_latimes_news.csv");

        HashMap<String, String> filenameToUrl = new HashMap<>();
        HashMap<String, String> urlToFilename = new HashMap<>();
        Scanner scanner = new Scanner(urlMap);
        while (scanner.hasNext()) {
            String[] tokens = scanner.next().split(",");
            filenameToUrl.put(tokens[0], tokens[1]);
            urlToFilename.put(tokens[1], tokens[0]);
        }
        scanner.close();

        Set<String> edges = new HashSet<>();
        for (File file: Objects.requireNonNull(crawledFolder.listFiles())) {
            Document document = Jsoup.parse(file, "UTF-8", filenameToUrl.get(file.getName()));
            for (Element link: document.select("a[href]")) {
                String url = link.attr("abs:href").trim();
                if (urlToFilename.containsKey(url))
                    edges.add(file.getName() + " " + urlToFilename.get(url));
            }
        }

        FileWriter fileWriter = new FileWriter("/Users/kamalnimmagadda/Desktop/Information Retrieval/HW4/LATIMES/edges.txt");
        for (String e: edges)
            fileWriter.write(e + "\n");
        fileWriter.close();
    }
}
