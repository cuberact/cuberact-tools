package org.cuberact.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class ShortUUIDTest {

    @Test
    public void randomUUID() {
        Set<String> generated = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            String shortUUID = ShortUUID.randomUUID();
            Assertions.assertFalse(generated.contains(shortUUID));
            generated.add(shortUUID);
        }
    }

    @Test
    public void encodeDecode() {
        for (int i = 0; i < 1000; i++) {
            UUID uuidOriginal = UUID.randomUUID();
            String shortUUID = ShortUUID.encode(uuidOriginal);
            UUID uuidDecoded = ShortUUID.decode(shortUUID);
            Assertions.assertEquals(uuidOriginal, uuidDecoded);
        }
    }

    @Test
    public void allowedChars() {
        Pattern allowedCharsPattern = Pattern.compile("^[a-z0-5]+$");
        for (int i = 0; i < 1000; i++) {
            String shortUUID = ShortUUID.randomUUID();
            Assertions.assertTrue(allowedCharsPattern.matcher(shortUUID).matches());
        }
    }

    @Test
    public void allowedLegth() {
        for (int i = 0; i < 1000; i++) {
            String shortUUID = ShortUUID.randomUUID();
            Assertions.assertEquals(26, shortUUID.length());
        }
    }

}
