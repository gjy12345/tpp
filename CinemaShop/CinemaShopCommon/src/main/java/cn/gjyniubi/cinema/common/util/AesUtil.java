package cn.gjyniubi.cinema.common.util;

import cn.gjyniubi.cinema.common.exception.AesException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Author gujianyang
 * @Date 2021/2/20
 * @Class AesUtil
 */
public class AesUtil {
    private static final String key = "OK7x87G$yuBI0456";
    private static final String iv = "NIfb&98GUYYCGf66";

    /**
     * @param data 明文
     * @return 密文
     * @Description AES算法加密明文
     */
    public static String encryptAES(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return encode(encrypted).trim(); // BASE64做转码。
        } catch (Exception e) {
            throw new AesException(e);
        }
    }

    /**
     * @param data 密文
     * @return 明文
     * @Description AES算法解密密文
     */
    public static String decryptAES(String data){
        try {
            byte[] encrypted1 = decode(data);//先用base64解密
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString.trim();
        } catch (Exception e) {
            throw new AesException(e);
        }
    }

    /**
     * 编码
     *
     * @param byteArray
     * @return
     */
    private static String encode(byte[] byteArray) {
        return new String(new Base64().encode(byteArray));
    }

    /**
     * 解码
     *
     * @param base64EncodedString
     * @return
     */
    private static byte[] decode(String base64EncodedString) {
        return new Base64().decode(base64EncodedString);
    }

    public static void main(String[] args) {
        System.out.println(AesUtil.encryptAES("956115488"));

    }
}
