package utils;

import java.util.Arrays;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class Credentials {
    private final String email;
    private final byte[] password;

    public Credentials(String email, byte[] password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "email='" + email + '\'' +
                ", password=" + Arrays.toString(password) +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public byte[] getPassword() {
        return password;
    }

    /*public static String generateSaltedHash(String data, String algorithm, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.reset();
        messageDigest.update(salt);
        byte[] hash = messageDigest.digest(data.getBytes());
        return bytesToStringHex(hash);
    }

    private static String bytesToStringHex(byte[] hash) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[hash.length * 2];
        for (int i = 0; i < hash.length; i++) {
            int v = hash[i] & 0xFF;
            hexChars[i*2] = hexArray[v >> 4];
            hexChars[i*2+1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] createSalt() {
        byte[] bytes = new byte[20];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(bytes);
        return bytes;
    }*/
}
