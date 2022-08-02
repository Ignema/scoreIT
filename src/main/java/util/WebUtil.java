package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebUtil {
    private static final Pattern urlPattern = Pattern.compile("href=\"(?!#|java)(.+?)(\\?|\"|#)");
    private static final Pattern validUrlPattern = Pattern.compile("[^(/|?|\"|tel)].+");

    public static void processURL(String url, Set<String> collectedUrls, int depth) {
        //System.out.printf("Processing URL: %s%n", url);

        if (collectedUrls.contains(url)) {
            return;
        }

        collectedUrls.add(url);

        if (depth == 0) {
            return;
        }

        extractURLs(url)
                .parallelStream()
                .filter(WebUtil::isValidUrl)
                .forEach(nextUrl -> processURL(nextUrl, collectedUrls, depth - 1));
    }

    private static boolean isValidUrl(String url) {
        return validUrlPattern.matcher(url).matches();
    }

    private static Set<String> extractURLs(List<String> domains) {
        Set<String> urls = new HashSet<>();

        domains.stream()
                .map(WebUtil::getHTMLPage)
                .forEach(page -> {
                    Matcher urlMatcher = urlPattern.matcher(page);
                    while (urlMatcher.find()) {
                        urls.add(urlMatcher.group(1));
                    }
                });

        return urls;
    }

    private static Set<String> extractURLs(String domain) {
        return extractURLs(List.of(domain));
    }

    private static String getHTMLPage(String domain) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(domain);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            reader.lines().forEach(result::append);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
