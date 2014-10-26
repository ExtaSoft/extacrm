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

    public static final long OLD_ID_LENGHT = 44;

    public static String toUrl(final String uuidStr) {

        // Обрабатываем "старые" гугловые айдишники
        if(uuidStr.length() >= OLD_ID_LENGHT) {
            return uuidStr;
        }

        final UUID uuid = UUID.fromString(uuidStr);

        return Base64.getUrlEncoder().encodeToString(toByteArray(uuid)).replace("=", "");
    }

    public static String fromUrl(final String base64idStr) {
        // Обрабатываем "старые" гугловые айдишники
        if (base64idStr.length() >= OLD_ID_LENGHT) {
            return base64idStr;
        }
        final byte[] uuidBin = Base64.getUrlDecoder().decode(base64idStr);
        return fromByteArray(uuidBin).toString();
    }

    private static byte[] toByteArray(final UUID uuid) {
        final byte[] byteArray = new byte[(Long.SIZE / Byte.SIZE) * 2];
        final ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        final LongBuffer longBuffer = buffer.asLongBuffer();
        longBuffer.put(new long[] { uuid.getMostSignificantBits(), uuid.getLeastSignificantBits() });
        return byteArray;
    }
    private static UUID fromByteArray(final byte[] bytes) {
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);
        final LongBuffer longBuffer = buffer.asLongBuffer();
        return new UUID(longBuffer.get(0), longBuffer.get(1));
    }}
