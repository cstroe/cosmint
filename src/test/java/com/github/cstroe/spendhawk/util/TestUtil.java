package com.github.cstroe.spendhawk.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class TestUtil {

    public static Boolean saveFileOnError = true;

    public static String getLink(@Nonnull Document doc, @Nonnull String path, String... arguments) {
        Elements links = doc.getElementsByTag("a");
        if(links == null) {
            // no links found
            return null;
        }

        for(Element link : links) {
            if(matches(link.attr("href"), path, arguments)) {
                return link.attr("href");
            }
        }

        // link not found, log the html output
        if(saveFileOnError) {
            saveFile("response page", doc.toString());
        }
        return null;
    }

    private static boolean matches(String linkHref, String path, String[] checkArguments) {
        final int questionMarkIndex = linkHref.indexOf("?");

        if(questionMarkIndex == -1) {
            return checkArguments.length == 0 && linkHref.equals(path);
        }

        String linkPath = linkHref.substring(0, questionMarkIndex);
        if(!linkPath.equals(path)) {
            return false;
        }

        String linkArguments = linkHref.substring(questionMarkIndex + 1);

        return matchArguments(createArgumentMap(linkArguments), createArgumentMap(checkArguments));
    }

    private static boolean matchArguments(
            Map<String, String> linkArguments, Map<String, String> checkArguments
    ) {
        if( linkArguments.size() != checkArguments.size() ) {
            return false;
        }

        for(Map.Entry<String, String> entry : linkArguments.entrySet()) {
            if(!checkArguments.containsKey(entry.getKey())) {
                return false;
            }

            if(!checkArguments.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }

        return true;
    }

    private static Map<String, String> createArgumentMap(String linkArguments) {
        Map<String, String> argumentMap = new HashMap<>();
        String[] keyValuePairs = linkArguments.split("&");

        Arrays.asList(keyValuePairs).stream().forEach(kvp -> {
            String[] kvpArray = kvp.split("=");
            String key = kvpArray[0];
            String value = "";
            if(kvpArray.length > 1) {
                value = Arrays.asList(Arrays.copyOfRange(kvpArray, 1, kvpArray.length))
                        .stream().collect(Collectors.joining());
            }
            argumentMap.put(key, value);
        });

        return argumentMap;
    }

    private static Map<String, String> createArgumentMap(String[] arguments) {
        Map<String, String> argumentMap = new HashMap<>();
        for(int i = 0; i < arguments.length; i += 2) {
            argumentMap.put(arguments[i], arguments[i+1]);
        }
        return argumentMap;
    }

    /**
     * An integration test utility for saving a temp file.
     * @param fileDescription Used in message to output to console.
     * @param contents The contents of the file.
     */
    public static void saveFile(String fileDescription, String contents) {
        try {
            File tempFile = File.createTempFile("integrationTest", ".html");
            FileWriter writer = new FileWriter(tempFile);
            writer.write(contents);
            writer.close();
            System.out.println("Saved " + fileDescription + " to: file://" + tempFile.getAbsolutePath());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
