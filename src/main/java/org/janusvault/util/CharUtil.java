package org.janusvault.util;

public class CharUtil {

    public static boolean containsIgnoreCase(char[] source, char[] target) {
        if (source == null || target == null || target.length > source.length)
            return false;

        for (int i = 0; i <= source.length - target.length; i++) {
            boolean found = true;

            for (int j = 0; j < target.length; j++) {
                char chSource = Character.toLowerCase(source[i + j]);
                char chTarget = Character.toLowerCase(target[j]);

                if (chSource != chTarget) {
                    found = false;
                    break;
                }
            }
            if (found)
                return true;
        }
        return false;
    }

    public static int compareToIgnoreCase(char[] str1, char[] str2) {
        if (str1 == null)
            return (str2 == null) ? 0 : -1;
        if (str2 == null) return 1;

        int minLength = Math.min(str1.length, str2.length);
        for (int i = 0; i < minLength; i++) {
            char a = Character.toLowerCase(str1[i]);
            char b = Character.toLowerCase(str2[i]);
            if (a != b)
                return a - b;
        }
        return str1.length - str2.length;
    }

    public static boolean equalsIgnoreCase(char[] str1, char[] str2) {
        if (str1 == str2)
            return true;
        if (str1 == null || str2 == null || str1.length != str2.length)
            return false;

        for (int i = 0; i < str1.length; i++)
            if (Character.toLowerCase(str1[i]) != Character.toLowerCase(str2[i]))
                return false;
        return true;
    }
}
