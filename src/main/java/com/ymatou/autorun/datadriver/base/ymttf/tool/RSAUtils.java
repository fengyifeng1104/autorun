package com.ymatou.autorun.datadriver.base.ymttf.tool;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSAUtils {

	/**
	 * 生成公钥和私钥
	 * @throws NoSuchAlgorithmException 
	 *
	 */
	public static HashMap<String, Object> getKeys() throws NoSuchAlgorithmException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        map.put("public", publicKey);
        map.put("private", privateKey);
        return map;
	}
	/**
	 * 使用模和指数生成RSA公钥
	 * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如<a href="http://lib.csdn.net/base/15" class="replace_word" title="undefined" target="_blank" style="color:#df3434; font-weight:bold;">Android</a>默认是RSA
	 * /None/NoPadding】
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @return
	 */
	public static RSAPublicKey getPublicKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 使用模和指数生成RSA私钥
	 * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
	 * /None/NoPadding】
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @return
	 */
	public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 公钥加密
	 * 
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		// 模长
		int key_len = publicKey.getModulus().bitLength() / 8;
		// 加密数据长度 <= 模长-11
		String[] datas = splitString(data, key_len - 11);
		String mi = "";
		//如果明文长度大于模长-11则要分组加密
		for (String s : datas) {
			mi += bcd2Str(cipher.doFinal(s.getBytes()));
		}
		return mi;
	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		//模长
		int key_len = privateKey.getModulus().bitLength() / 8;
		byte[] bytes = data.getBytes();
		byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
		Logger.debug(bcd.length);
		//如果密文长度大于模长则要分组解密
		String ming = "";
		byte[][] arrays = splitArray(bcd, key_len);
		for(byte[] arr : arrays){
			ming += new String(cipher.doFinal(arr));
		}
		return ming;
	}
	/**
	 * ASCII码转BCD码
	 * 
	 */
	public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc_to_bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}
	public static byte asc_to_bcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
		return bcd;
	}
	/**
	 * BCD转字符串
	 */
	public static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;

		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}
	/**
	 * 拆分字符串
	 */
	public static String[] splitString(String string, int len) {
		int x = string.length() / len;
		int y = string.length() % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		String[] strings = new String[x + z];
		String str = "";
		for (int i=0; i<x+z; i++) {
			if (i==x+z-1 && y!=0) {
				str = string.substring(i*len, i*len+y);
			}else{
				str = string.substring(i*len, i*len+len);
			}
			strings[i] = str;
		}
		return strings;
	}
	/**
	 *拆分数组 
	 */
	public static byte[][] splitArray(byte[] data,int len){
		int x = data.length / len;
		int y = data.length % len;
		int z = 0;
		if(y!=0){
			z = 1;
		}
		byte[][] arrays = new byte[x+z][];
		byte[] arr;
		for(int i=0; i<x+z; i++){
			arr = new byte[len];
			if(i==x+z-1 && y!=0){
				System.arraycopy(data, i*len, arr, 0, y);
			}else{
				System.arraycopy(data, i*len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}
	/**
	 * SHA1WithRSA签名
	 * @param context 原始数据
	 * @param privateKey 私钥
	 * @return base64加过的sign签名
	 * @throws Exception
	 */
	public static String SignSHA1WithRSA(String src,PrivateKey privateKey) throws Exception{
		Signature sign = Signature.getInstance("SHA1WithRSA");
		sign.initSign(privateKey);
		sign.update(src.getBytes());
		byte[] data = sign.sign();
		return Base64.getEncoder().encodeToString(data);
	}
	/**SHA-256签名 cmb签名用
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static String SignSHA256(String src) throws Exception{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(src.getBytes());
		byte[] data = md.digest();
		String datas=Hex.encodeHexStr(data, false);
		return datas;
	}
	/**
	 * 验证
	 * @param src 原始信息
	 * @param publicKey 公钥
	 * @param b64signature base64加过的签
	 * @return 是否验签成功
	 * @throws Exception
	 */
	public static boolean verifySign(String src,PublicKey publicKey,String b64signature) throws Exception{
		BASE64Decoder base64Decoder= new BASE64Decoder();
		byte[] signaturebuff= base64Decoder.decodeBuffer(b64signature);
		Signature sign = Signature.getInstance("SHA1WithRSA");
		sign.initVerify(publicKey);
		sign.update(src.getBytes());
		boolean isVerify = sign.verify(signaturebuff);
		BASE64Encoder base64Encoder= new BASE64Encoder();
		return isVerify;
	}
	/**
	 * 从字符串中加载公钥
	 * @param publicKeyStr 公钥数据字符串
	 * @throws Exception 加载公钥时产生的异常
	 */
	public static PublicKey loadPublicKey(String publicKeyStr) throws Exception{
		PublicKey publicKey=null;
		try {
			BASE64Decoder base64Decoder= new BASE64Decoder();
			byte[] buffer= base64Decoder.decodeBuffer(publicKeyStr);
			KeyFactory keyFactory= KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
			publicKey= (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (IOException e) {
			throw new Exception("公钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
		return publicKey;
	}
	/**
	 * 把base64串转换成PrivateKey
	 * @param privateKeyStr PrivateKey对应的base64string
	 * @return PrivateKey
	 * @throws Exception
	 */
	public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception{
		PrivateKey privateKey =null;
		try {
			BASE64Decoder base64Decoder= new BASE64Decoder();
			byte[] buffer= base64Decoder.decodeBuffer(privateKeyStr);
			PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory= KeyFactory.getInstance("RSA");
			privateKey= (PrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (IOException e) {
			throw new Exception("私钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
		return privateKey;
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = RSAUtils.getKeys();
		//生成公钥和私钥
		RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
		RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
		
		//模
		String modulus = publicKey.getModulus().toString();
		//公钥指数
		String public_exponent = publicKey.getPublicExponent().toString();
		//私钥指数
		String private_exponent = privateKey.getPrivateExponent().toString();
		//明文
		String ming = "123456789";
		//使用模和指数生成公钥和私钥
		RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);
		RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);
		//加密后的密文
		String mi = RSAUtils.encryptByPublicKey(ming, pubKey);
		System.out.println(mi);
		//解密后的明文
		ming = RSAUtils.decryptByPrivateKey(mi, priKey);
		System.out.println(ming);
		PrivateKey privateKey2=RSAUtils.loadPrivateKey("MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOY/57sgqIjHEZjNhmXlLapuYJuSACN4zzWaDCnujAEd2dxulxp57ajrnNgUjRJcm7O7mtkF7cE8u5y5l6De3sWM1/YLuPyB8Nhy+IxqY7AjtW5Zn5kS0IdIeQ6FSXBy6XnEsxeXac93VjMvrMwJ7ZUArvlwegEqd34OYRVI5CKlAgMBAAECgYBApPCKuUCYJkvqesmhEhcgIp09EGC5lNGYWwfPPgpQxfDE0sfZxyHSq1P91sdEwHt2mtV+2QtHlaWW+wR3RhuFEuGM1z8fsvongAk9bNDPvaPz07HF1YwXuviakDYk1bWwqCS+9VFJ82fGae4+ftUQOmJYSH+LV89RRqWdCP5GgQJBAP/nwVbgw/bBR04UDfUK2Bdr+Op+6WFdFoyzK7Kvr5sjO0T5ewswHJ34+B26X50kGqkIU2h2AXh8/AX1ZJUB5vsCQQDmVbglUXIrjLG5zraxstNlDnJvDL3WmYtZJxbKWq9QgSWYzf4iCaAVqsjZHfAHAV2iMGf+x55QGuHk7hZ0SGrfAkEAlVRE0xCX6c8BcANt3Zc1X/2GpDfosgMjHHmVP1Eb1RirBmXasj2iBWD6UEaocsdVs1uDaIqr8wZj/ooi5nzUrwJBAM5R2jETU4FO9aPKVju2Q0UyO67dau7fesLREMkRkhg6lsLZQdqbZJoD8QUKnAaqYoT1dzHw/Q4kBlRaMCLY+2ECQQDVAbyCAAte4LH9EndxkisOZXKMLbjhlmORpyKBUwSwW6Hk4If4hlKTIOUCuwXJzb08BK40AGD+pw6P35e+B3Dh");
		PublicKey publicKey2=RSAUtils.loadPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmP+e7IKiIxxGYzYZl5S2qbmCbkgAjeM81mgwp7owBHdncbpcaee2o65zYFI0SXJuzu5rZBe3BPLucuZeg3t7FjNf2C7j8gfDYcviMamOwI7VuWZ+ZEtCHSHkOhUlwcul5xLMXl2nPd1YzL6zMCe2VAK75cHoBKnd+DmEVSOQipQIDAQAB");
		String signature=RSAUtils.SignSHA1WithRSA("123456", privateKey2);
		System.out.println("sign:"+signature);
		System.out.println("verifySign:"+RSAUtils.verifySign("123456", publicKey2,signature));
		
		signature=RSAUtils.SignSHA256("123456");
		System.out.println("SignSHA256:"+signature);
		
	}
}
