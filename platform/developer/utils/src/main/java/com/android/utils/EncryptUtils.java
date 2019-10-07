package com.android.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * created by jiangshide on 2019-08-16.
 * email:18311271399@163.com
 */
public class EncryptUtils {
    /**
     * MD5加密：生成16位密文
     *
     * @param originString 加密字符串
     * @param isUpperCase  是否生成大写密文
     */
    public static String MD5StringTo16Bit(String originString, boolean isUpperCase) throws Exception {
        String result = MD5StringTo32Bit(originString, isUpperCase);
        if (result.length() == 32) {
            return result.substring(8, 24);
        }
        return "";
    }

    /**
     * MD5加密：生成32位密文
     *
     * @param originString 加密字符串
     * @param isUpperCase  是否生成大写密文
     */
    public static String MD5StringTo32Bit(String originString, boolean isUpperCase) throws Exception {
        StringBuilder result = new StringBuilder("");
        if (originString != null) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte bytes[] = md.digest(originString.getBytes());
            for (byte b : bytes) {
                String str = Integer.toHexString(b & 0xFF);
                if (str.length() == 1) {
                    str += "F";
                }
                result.append(str);
            }
        }
        if (isUpperCase) {
            return result.toString().toUpperCase();
        } else {
            return result.toString().toLowerCase();
        }
    }

    /**
     * 获取文件的MD5
     *
     * @param filePath 文件路径
     */
    public static String getFileMd5(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        return getFileMd5(new File(filePath));
    }

    /**
     * 获取文件的MD5
     *
     * @param file 文件
     */
    public static String getFileMd5(File file) {
        MessageDigest messageDigest;
        RandomAccessFile randomAccessFile = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            if (file == null) {
                return "";
            }
            if (!file.exists()) {
                return "";
            }
            randomAccessFile = new RandomAccessFile(file, "r");
            byte[] bytes = new byte[1024 * 1024 * 10];
            int len = 0;
            while ((len = randomAccessFile.read(bytes)) != -1) {
                messageDigest.update(bytes, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, messageDigest.digest());
            String md5 = bigInt.toString(16);
            while (md5.length() < 32) {
                md5 = "0" + md5;
            }
            return md5;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    //md5
    public static String encryptMd5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    public static String convertMD5(String source) {
        char[] a = source.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;
    }

    public static String decryptMD5(String source) {
        return convertMD5(convertMD5(source));
    }


    //DES
    private final static String DES = "DES";

    private static byte[] encryptDES(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }


    private static byte[] decryptDES(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }


    //AES
    private static byte[] iv = {'c', 'i', 'd', 't', 'e', 'c', 'h', '1'};

    public static String encryptDES(String encryptString, String encryptKey) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
        return Base64.encode(encryptedData);
    }


    //Base64
    public static String encryptBase64(String data, String key) throws Exception {
        byte[] bt = encryptDES(data.getBytes(), key.getBytes());
        String strs = Base64.encode(bt);
        // String strs = Base64.encodeToString(bt, Base64.DEFAULT);
        return strs;
    }


    public static String decryptBase64(String data, String key) throws IOException,
            Exception {
        if (data == null)
            return null;
        byte[] buf = Base64.decode(data);
        byte[] bt = decryptDES(buf, key.getBytes());
        return new String(bt);
    }


    public static class Base64 {
        /**
         * Base64编码表。
         */
        private static final char[] BASE64CODE =
                {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                        'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
                        'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/',};

        /**
         * Base64解码表。
         */
        private static final byte[] BASE64DECODE =
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                        -1, -1, -1,
                        -1,
                        -1, // 注意两个63，为兼容SMP，
                        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 63,
                        -1,
                        63, // “/”和“-”都翻译成63。
                        52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, 0, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
                        12, 13,
                        14, // 注意两个0：
                        15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1,
                        -1, // “A”和“=”都翻译成0。
                        -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51,
                        -1, -1, -1, -1, -1,};

        private static final int HEX_255 = 0x0000ff;
        private static final int HEX_16515072 = 0xfc0000;
        private static final int HEX_258048 = 0x3f000;
        private static final int HEX_4032 = 0xfc0;
        private static final int HEX_63 = 0x3f;
        private static final int HEX_16711680 = 0xff0000;
        private static final int HEX_65280 = 0x00ff00;
        private static final int NUMBER_TWO = 2;
        private static final int NUMBER_THREE = 3;
        private static final int NUMBER_FOUR = 4;
        private static final int NUMBER_SIX = 6;
        private static final int NUMBER_EIGHT = 8;
        private static final int NUMBER_TWELVE = 12;
        private static final int NUMBER_SIXTEEN = 16;
        private static final int NUMBER_EIGHTEEN = 18;

        /**
         * 构造方法私有化，防止实例化。
         */
        private Base64() {
        }

        /**
         * Base64编码。将字节数组中字节3个一组编码成4个可见字符。
         *
         * @param b 需要被编码的字节数据。
         * @return 编码后的Base64字符串。
         */
        public static String encode(byte[] b) {
            int code = 0;

            // 按实际编码后长度开辟内存，加快速度
            StringBuffer sb = new StringBuffer(((b.length - 1) / NUMBER_THREE) << NUMBER_TWO + NUMBER_FOUR);
            // 进行编码
            for (int i = 0; i < b.length; i++) {
                code |=
                        (b[i] << (NUMBER_SIXTEEN - i % NUMBER_THREE * NUMBER_EIGHT))
                                & (HEX_255 << (NUMBER_SIXTEEN - i % NUMBER_THREE * NUMBER_EIGHT));
                if (i % NUMBER_THREE == NUMBER_TWO || i == b.length - 1) {
                    sb.append(BASE64CODE[(code & HEX_16515072) >>> NUMBER_EIGHTEEN]);
                    sb.append(BASE64CODE[(code & HEX_258048) >>> NUMBER_TWELVE]);
                    sb.append(BASE64CODE[(code & HEX_4032) >>> NUMBER_SIX]);
                    sb.append(BASE64CODE[code & HEX_63]);
                    code = 0;
                }
            }

            // 对于长度非3的整数倍的字节数组，编码前先补0，编码后结尾处编码用=代替，
            // =的个数和短缺的长度一致，以此来标识出数据实际长度
            if (b.length % NUMBER_THREE > 0) {
                sb.setCharAt(sb.length() - 1, '=');
            }
            if (b.length % NUMBER_THREE == 1) {
                sb.setCharAt(sb.length() - NUMBER_TWO, '=');
            }
            return sb.toString();
        }

        /**
         * Base64解码。
         *
         * @param code 用Base64编码的ASCII字符串
         * @return 解码后的字节数据
         */
        public static byte[] decode(String code) {
            // 检查参数合法性
            if (code == null) {
                return null;
            }
            int len = code.length();
            if (len % NUMBER_FOUR != 0) {
                throw new IllegalArgumentException("Base64 string length must be 4*n");
            }
            if (code.length() == 0) {
                return new byte[0];
            }

            // 统计填充的等号个数
            int pad = 0;
            if (code.charAt(len - 1) == '=') {
                pad++;
            }
            if (code.charAt(len - NUMBER_TWO) == '=') {
                pad++;
            }

            // 根据填充等号的个数来计算实际数据长度
            int retLen = len / NUMBER_FOUR * NUMBER_THREE - pad;

            // 分配字节数组空间
            byte[] ret = new byte[retLen];

            // 查表解码
            char ch1, ch2, ch3, ch4;
            int i;
            for (i = 0; i < len; i += NUMBER_FOUR) {
                int j = i / NUMBER_FOUR * NUMBER_THREE;
                ch1 = code.charAt(i);
                ch2 = code.charAt(i + 1);
                ch3 = code.charAt(i + NUMBER_TWO);
                ch4 = code.charAt(i + NUMBER_THREE);
                int tmp =
                        (BASE64DECODE[ch1] << NUMBER_EIGHTEEN) | (BASE64DECODE[ch2] << NUMBER_TWELVE)
                                | (BASE64DECODE[ch3] << NUMBER_SIX) | (BASE64DECODE[ch4]);
                ret[j] = (byte) ((tmp & HEX_16711680) >> NUMBER_SIXTEEN);
                if (i < len - NUMBER_FOUR) {
                    ret[j + 1] = (byte) ((tmp & HEX_65280) >> NUMBER_EIGHT);
                    ret[j + NUMBER_TWO] = (byte) ((tmp & HEX_255));

                } else {
                    if (j + 1 < retLen) {
                        ret[j + 1] = (byte) ((tmp & HEX_65280) >> NUMBER_EIGHT);
                    }
                    if (j + NUMBER_TWO < retLen) {
                        ret[j + NUMBER_TWO] = (byte) ((tmp & HEX_255));
                    }
                }
            }
            return ret;
        }
    }

    /**
     * 将签名字符串转换成需要的32位签名
     *
     * @param paramArrayOfByte
     * @return
     */
    private static String hexdigest(byte[] paramArrayOfByte) {
        final char[] hexDigits =
                {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            for (int i = 0, j = 0; ; i++, j++) {
                if (i >= 16) {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取应用程序的签名
     *
     * @param context
     * @param pkgName
     */
    public static String getSign(Context context, String pkgName) {
        try {
            PackageInfo pis = context.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
            return hexdigest(pis.signatures[0].toByteArray());
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(SystemUtil.class.getName() + "the "
                    + pkgName + "'s application not found");
        }
    }
}
