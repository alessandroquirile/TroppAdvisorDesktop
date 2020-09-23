package utils;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class BasicCryptographer {
    public static byte[] encrypt(byte[] data) {
        byte[] encryptedBytes = new byte[data.length];
        for (int i = 0; i < data.length; i++)
            encryptedBytes[i] = (byte) ((i % 2 == 0) ? data[i] + 1 : data[i] - 1);
        return encryptedBytes;
    }

    public static byte[] decrypt(byte[] data) {
        byte[] decryptedData = new byte[data.length];
        for (int i = 0; i < data.length; i++)
            decryptedData[i] = (byte) ((i % 2 == 0) ? data[i] - 1 : data[i] + 1);
        return decryptedData;
    }
}