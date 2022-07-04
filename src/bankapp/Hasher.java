package bankapp;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;


public class Hasher {

    /**
     * Function provides hashed value of the argument.
     * @param originalString value to hash.
     * @throws NoSuchAlgorithmException
     */
    public static String hash (String originalString) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance(originalString);
        final byte[] hashbytes = digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8)
        );
        return bytesToHex(hashbytes);
    }

    public static String sha256(String tekst) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] input_msg = tekst.getBytes();
        byte[] output_msg = sha256.digest(input_msg);
        BigInteger bigInt = new BigInteger(1, output_msg);
        String hashtext = bigInt.toString(16);
        return hashtext;
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}