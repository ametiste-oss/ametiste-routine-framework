package org.ametiste.lang;

/**
 *
 * @since
 */
public final class StringUtils {

    private StringUtils() { }

    public static String splitCamelCase(String s) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1-$2";
        return s.replaceAll(regex, replacement)
                .toLowerCase();
    }

}
