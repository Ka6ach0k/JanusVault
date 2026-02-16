package janus.util;

import org.janusvault.util.CharUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CharUtilTest {

    @Test
    void testContainsIgnoredCase() {
        char[] title = "Google Accounts".toCharArray();

        assertTrue(CharUtil.containsIgnoreCase(title, "gooGLE".toCharArray()));
        assertTrue(CharUtil.containsIgnoreCase(title, "accOUNT".toCharArray()));
        assertTrue(CharUtil.containsIgnoreCase(title, "gOO".toCharArray()));
        assertTrue(CharUtil.containsIgnoreCase(title, "aCC".toCharArray()));
        assertFalse(CharUtil.containsIgnoreCase(title, "github".toCharArray()));

        assertTrue(CharUtil.containsIgnoreCase(title, "".toCharArray()));
        assertFalse(CharUtil.containsIgnoreCase(null, "Google Accounts".toCharArray()));
    }

    @Test
    void testCompareToIgnoreCase() {
        assertEquals(0, CharUtil.compareToIgnoreCase("".toCharArray(), "".toCharArray()));

        assertTrue(CharUtil.compareToIgnoreCase("apple".toCharArray(), "blackberry".toCharArray()) < 0);
        assertTrue(CharUtil.compareToIgnoreCase("blackberry".toCharArray(), "apple".toCharArray()) > 0);

        assertTrue(CharUtil.compareToIgnoreCase("apple".toCharArray(), "appleInc".toCharArray()) < 0);

        assertTrue(CharUtil.compareToIgnoreCase(null, "blackberry".toCharArray()) < 0);
        assertEquals(0, CharUtil.compareToIgnoreCase(null, null));
        assertTrue(CharUtil.compareToIgnoreCase("blackberry".toCharArray(), null) > 0);

        assertTrue(CharUtil.compareToIgnoreCase("123".toCharArray(), "abc".toCharArray()) < 0);
    }

}
