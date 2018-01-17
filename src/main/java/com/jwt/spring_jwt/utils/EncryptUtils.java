package com.jwt.spring_jwt.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EncryptUtils {
	private static final Log LOG = LogFactory.getLog(EncryptUtils.class);
	
	/**
	 * 用MD5算法进行加密
	 * 
	 * @param str要加密的字符串
	 * @return MD5加密后的结果
	 */
	public static String encodeMD5String(String str ,String salt) {
		return encode(str + salt, "MD5");
	}
	
	private static String encode(String str ,String method) {
		
		MessageDigest mdInst = null;
		//把密文转换成十六进制的字符串形式
		StringBuilder dstr = new StringBuilder();
		try {
			//获得MD5摘要算法的 MessageDigest对象
			mdInst = MessageDigest.getInstance(method);
			//使用指定的字节更新摘要
			mdInst.update(str.getBytes());
			//获得密文
			byte[] md = mdInst.digest();
			for (int i = 0; i < md.length; i++) {
				int tmp = md[i];
				if (tmp < 0) {
					tmp += 256;
				}
				if (tmp < 16) {
					dstr.append("0");
				}
				dstr.append(Integer.toHexString(tmp));
			}
		} catch (NoSuchAlgorithmException e) {
			LOG.error(e);
		}
		
		return dstr.toString();
	}
}