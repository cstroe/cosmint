package com.github.cstroe.spendhawk.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class TestUtil {

    public static Boolean saveFileOnError = true;

    /**
     * Find a link to the given path.
     * @return true if the path was found as one of the links' href, false if none was found
     */
    public static boolean hasLink(Document doc, String path, Object... arguments) {
        return getLink(doc, path, arguments).isPresent();
    }


    public static Optional<String> getLink(@Nonnull Document doc, @Nonnull String path, Object... arguments) {
        Elements links = doc.getElementsByTag("a");
        if(links == null) {
            // no links found
            return Optional.empty();
        }

        for(Element link : links) {
            if(matches(link.attr("href"), path, arguments)) {
                return Optional.of(link.attr("href"));
            }
        }

        // link not found, log the html output
        if(saveFileOnError) {
            saveFile("response page", doc.toString());
        }
        return Optional.empty();
    }

    private static boolean matches(String linkHref, String path, Object[] checkArguments) {
        String linkPath = getPath(linkHref);

        if(!linkPath.equals(path)) {
            return false;
        }

        Optional<String> linkArguments = getArguments(linkHref);

        if(!linkArguments.isPresent()) {
            return checkArguments.length == 0;
        } else if(checkArguments.length == 0) {
            return false;
        }

        return matchArguments(createArgumentMap(linkArguments.get()), createArgumentMap(checkArguments));
    }

    public static String getPath(String linkHref) {
        final int questionMarkIndex = linkHref.indexOf("?");
        if(questionMarkIndex == -1) {
            return linkHref;
        } else {
            return linkHref.substring(0, questionMarkIndex);
        }
    }

    public static Optional<String> getArguments(String linkHref) {
        final int questionMarkIndex = linkHref.indexOf("?");
        if(questionMarkIndex == -1 || questionMarkIndex == linkHref.length() - 1) {
            return Optional.empty();
        }

        return Optional.of(linkHref.substring(questionMarkIndex + 1));
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

    public static Map<String, String> createArgumentMap(String linkArguments) {
        Map<String, String> argumentMap = new HashMap<>();

        if(StringUtils.isBlank(linkArguments)) {
            return argumentMap;
        }

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

    private static Map<String, String> createArgumentMap(Object[] arguments) {
        Map<String, String> argumentMap = new HashMap<>();
        for(int i = 0; i < arguments.length; i += 2) {
            argumentMap.put(arguments[i].toString(), arguments[i+1].toString());
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
