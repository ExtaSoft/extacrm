package ru.extas.web.commons;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.Base64;
import java.util.UUID;

/**
 * @author Valery Orlov
 *         Date: 28.08.2014
 *         Time: 12:27
 */
public class UUIDUtils {

    public static String toUrl(String uuidStr) {

        UUID uuid = UUID.fromString(uuidStr);

        return Base64.getUrlEncoder().encodeToString(toByteArray(uuid)).replace("=", "");
    }

    public static String fromUrl(String base64idStr) {
        byte[] uuidBin = Base64.getUrlDecoder().decode(base64idStr);
        return fromByteArray(uuidBin).toString();
    }

    private static byte[] toByteArray(UUID uuid) {
        byte[] byteArray = new byte[(Long.SIZE / Byte.SIZE) * 2];
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        LongBuffer longBuffer = buffer.asLongBuffer();
        longBuffer.put(new long[] { uuid.getMostSignificantBits(), uuid.getLeastSignificantBits() });
        return byteArray;
    }
    private static UUID fromByteArray(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        LongBuffer longBuffer = buffer.asLongBuffer();
        return new UUID(longBuffer.get(0), longBuffer.get(1));
    }}
