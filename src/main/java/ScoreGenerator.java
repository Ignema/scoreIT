import util.GraphUtil;
import util.WebUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static util.FileUtil.writeToCSV;

public class ScoreGenerator {
    private static final int MAX_DEPTH = 3;
    private static final List<String> urls = List.of("https://stackoverflow.com");

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Set<String> collectedUrls = Collections.synchronizedSet(new HashSet<>());
        urls.forEach(url -> WebUtil.processURL(url, collectedUrls, MAX_DEPTH));
        writeToCSV(collectedUrls);
        //try (GraphUtil graph = new GraphUtil("bolt://localhost:7687", "neo4j", "scoreIT")) {
        //    graph.addLink("https://stackoverflow.com", "https://google.com");
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        System.out.printf("Elapsed time: %s ms%n", System.currentTimeMillis() - start);
    }
}
