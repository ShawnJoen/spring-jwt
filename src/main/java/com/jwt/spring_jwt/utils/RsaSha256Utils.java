package com.jwt.spring_jwt.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import sun.misc.BASE64Encoder;

public class RsaSha256Utils {

	/** 
	 * 1.生成 keystore文件 和 cer文件
	 *   cd C:\Program Files\Java\jdk1.8.0_121\bin;
	 *   #2048位RSA密钥
	 *   ./keytool -genkey -v -alias shawn-key -dname "CN=shawn-key,OU=HE,O=CUI,L=SHENGZHEN,ST=GUANGDONG,C=CN" -keyalg RSA -keysize 2048 -keypass 123456 -keystore d:shawn-key.keystore -storepass 123456 -validity 36500 -storetype JCEKS
	 *   #导出公钥文件
	 *   ./keytool -exportcert -alias shawn-key -file d:/shawn-key.cer -keystore d:/shawn-key.keystore -storepass 123456 -rfc -storetype JCEKS
	 */
	
	/** 
	 * 2.keystore导出私钥 和 cer导出公钥
	 */
	public static void export() throws Exception {
        
        String privatePath = "D:/private.key";	//指定导出私钥的文件  
        String publicPath = "D:/public.key"; 	//指定导出公钥 的文件  
        PrivateKey privateKey = getPrivateKeyFromStore();
        createKeyFile(privateKey, privatePath);
        PublicKey publicKey = getPublicKeyFromCrt();
        createKeyFile(publicKey, publicPath);
        byte[] publicKeyBytes = publicKey.getEncoded();
        byte[] privateKeyBytes = privateKey.getEncoded();
        String publicKeyBase64 = new BASE64Encoder().encode(publicKeyBytes);
        String privateKeyBase64 = new BASE64Encoder().encode(privateKeyBytes);
        System.out.println("publicKeyBase64.length():" + publicKeyBase64.length());
        System.out.println("publicKeyBase64:" + publicKeyBase64);
        System.out.println("privateKeyBase64.length():" + privateKeyBase64.length());
        System.out.println("privateKeyBase64:" + privateKeyBase64);
    }
	
    private static PrivateKey getPrivateKeyFromStore() throws Exception {
        String alias = "shawn-key"; 				//Keytool中生成KeyStore时设置的alias
        String storeType = "JCEKS"; 				//Keytool中生成KeyStore时设置的storetype
        char[] pw = "123456".toCharArray(); 		//Keytool中生成KeyStore时设置的storepass
        String storePath = "D:/shawn-key.keystore"; //Keytool中已生成的KeyStore文件 
        storeType = null == storeType ? KeyStore.getDefaultType() : storeType;
        KeyStore keyStore = KeyStore.getInstance(storeType);
        InputStream is = new FileInputStream(storePath);
        keyStore.load(is, pw);
        //由密钥库获取密钥的两种方式  
        //KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, new KeyStore.PasswordProtection(pw));  
        //return pkEntry.getPrivateKey();
        return (PrivateKey) keyStore.getKey(alias, pw);
    }
  
    private static PublicKey getPublicKeyFromCrt() throws CertificateException, FileNotFoundException {
        String crtPath = "D:/shawn-key.cer"; //Keytool中已生成的证书文件  
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream in = new FileInputStream(crtPath);
        Certificate crt = cf.generateCertificate(in);
        PublicKey publicKey = crt.getPublicKey();
        return publicKey;
    }
  
    private static void createKeyFile(Object key, String filePath) throws Exception {
        FileOutputStream fos = new FileOutputStream(filePath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(key);
        oos.flush();
        oos.close();
    }
}