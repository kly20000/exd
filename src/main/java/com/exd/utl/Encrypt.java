package com.exd.utl;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;


public class Encrypt {
	/**
	 * MD5加密
	 * 
	 * @param encryptStr
	 * @return
	 */
	private static byte[] md5Binary(String encryptStr) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(encryptStr.getBytes("utf8"));
			return md5.digest();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String md5(Object encryptStr, Object key) {
		byte[] md5 = md5Binary(encryptStr.toString());
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5.length; i++) {
			int val = ((int) md5[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	/**
	 * base64转码
	 * 
	 * @param b
	 * @return
	 */
	private static String base64(byte[] b) {
		Base64 base64Encode = new Base64();
		return new String(base64Encode.encode(b));
	}
	
	/**
	 * MD5加密再做base64加密
	 * @param str
	 * @return
	 */
	public static String md5ToBase64(Object str, Object key) {
		return base64(md5Binary(str.toString()));
	}
	
	/**
	 * Des加密
	 * */
	public static String desEncrypt(Object data, Object key){
        SecureRandom sr = new SecureRandom();
        try{
        	DESKeySpec dks = new DESKeySpec(key.toString().getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
            SecretKey securekey = keyFactory.generateSecret(dks);  
            Cipher cipher = Cipher.getInstance("DES");  
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr); 
            byte[] b = cipher.doFinal(data.toString().getBytes());
            String hs = "";  
            String stmp = "";  
            for (int n = 0; n < b.length; n++) {  
                stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));  
                if (stmp.length() == 1)  
                    hs = hs + "0" + stmp;  
                else  
                    hs = hs + stmp;  
            }  
            return hs.toUpperCase(); 
        }catch(Exception e){
        	e.printStackTrace();
        }
        return null;
	}
	
	/**
	 * Des解密
	 * */
	public static String desDecrypt(Object data, Object key){
		byte[] b = data.toString().getBytes();
		if ((b.length % 2) != 0)  
            throw new IllegalArgumentException("长度不是偶数");  
        byte[] b2 = new byte[b.length / 2];  
        for (int n = 0; n < b.length; n += 2) {  
            String item = new String(b, n, 2);  
            b2[n / 2] = (byte) Integer.parseInt(item, 16);  
        }  
        SecureRandom sr = new SecureRandom();
        try{
        	DESKeySpec dks = new DESKeySpec(key.toString().getBytes());  
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
            SecretKey securekey = keyFactory.generateSecret(dks);  
            Cipher cipher = Cipher.getInstance("DES");  
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);  
            return new String(cipher.doFinal(b2));  
        }catch(Exception e){
        	e.printStackTrace();
        }
        return null;
	}
}
