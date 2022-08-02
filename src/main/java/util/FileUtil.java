package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class FileUtil {
    public static void writeToCSV(Set<String> urls){
        StringBuilder csv = new StringBuilder();
        csv.append("link;\n");
        urls.forEach(url -> csv.append(url).append(";\n"));
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("links.csv"))) {
            bufferedWriter.write(csv.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
