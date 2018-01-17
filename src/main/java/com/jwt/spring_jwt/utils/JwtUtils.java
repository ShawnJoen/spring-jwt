package com.jwt.spring_jwt.utils;

import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

@Service("jwtUtils")
public class JwtUtils {
	private	static final Logger log = LoggerFactory.getLogger(JwtUtils.class);
	
	/**私钥*/
	private Key privateKey;
	private String privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDRlPBOcFCpDmk5kibfkgB9ZCfQ6zlTrw7lIMvqIrz2dCyRRHGwupQtOzUhaUzN0cVg8usG9MmET7oL4hfktn3f8KNovHPX5a+ZnnjhKTkh+mGuJN3/lDbPSNteW41bjHgUa6T85UpkH4DOcBIBi0obi9IwL28l00oH7MiiT/GW3NEYknchU2o5N3MJY1EW4Am5O8oFAQxed0nDtW+eftYjfo0shWIatxkM5/vgzvxWJXnQ+KylXIsf96pgefneaV1JxOnzwOo6BNSZf5IQaqutvMsEw992ymAb4FAHnMsgskrZE7kwkG1LWDj1g53RKNFJoUkzq2Cxa06SUzrTqa7pAgMBAAECggEAARJA1DXQw7x2Hzw3BKeLZxiu92tsZJgdfGBZin3Y922KmMd/yUZ64WxPDjeqk9bBqogzWVrA+PMrYUorkxi3tgBV5YApG25qIl/kAEqkpx7PhwNbOyY3zYFy1Z2kMFaceoY+VqiZY7+heeyquuVKYK+rSGHFsKzO0RGNyP6dnobKEBZ91AZa7up1D5/TW9dp3EuaE9aeV3/TNuHA/Gs5y6tPVvkMF2mJoawQHdEKaWxewdmuYRqBbUQ0Bg8HZYin8Wnmcn1hZVhk1zW961Vv1hObbbOlvoRJ6hEHLStpiuNpr0nPiyvHplnsm9e/NxXxQhRH4iggULN4ubqNCFhx7QKBgQDxc43HOA14K0c62JsAb8LtOJ4noIY3hPdZYLQhFpi51nkpsNm2GVsVFHpM5eFrioSveE2o5LTUXspVK+AAodFtnWc6sMedhI3OKtTZErDtEkJSN8W4h1+KicNhGBQbg8UUzV/2AcrEnBe8POLcA4DpkgbL1d1v/p6n8/VihDa4JwKBgQDeNcno1IAOHzdOShZGNWzQTpc8a5b36WuGY8smqVxk+mi5nobxMclCeQtEFlF/oQAswcorp4Zy6rm4WT3slG8SAhLmv6qtNHq0NCTkfdXjqOUSKioiOYR8p43Yn5wCrcXM0T74sG9xkD79SAWVm90iit6HuwbkmoIzExMjR3E6bwKBgQCPikPCKKeRRiFNcZIMW2TFxD/5jOvdu7WUs/HCLf3TlRZo25NCN6a3THo8lvlBBpNFAb8hZf2+ty+QHMpJdencQZQqBwhUs4XeCAaLmhoGhjFZUYDA5/G7mZTGdxsVFGwopsOiq4QRCD7wED5Jz+dmdAKIUXcYaoWFkXnS/IjPRwKBgFXDENCJ43qP1bqjoYErcXncO0aYh8XbEyLlNS0QxJY8h1rMsmw4uOKs8QowSvglXXnouWyr1ZpYuaFK4DmE+HFM15qzct4ymQEf9vUxE/Iv52lhWK2o4VdzG631vDLUerkYMWOuRPThpU4AFBev9mM0kbp1Fq34bDPmMsa09iCFAoGAL3wh0TW6nuQD3JMKFpY4Vt4CKKb1U2siA1C2+bWsYGzmwIBZ3mzMMpgxxQbwx2oYoHqXal1Wh1AHOP5Dq4wkp79GQmDgTKSMmljVNpyYzEhfnRlQmiir73l0+aeBGs+xQa5HjMJ5aytLvovvveT33RaLEAv0LxRWHMyRMV6Lt+E=";//SystemConfig.JWT_RSA_PRIVATEKEY;//私钥key
	/**公钥*/
	private Key publicKey;
	private String pubicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0ZTwTnBQqQ5pOZIm35IAfWQn0Os5U68O5SDL6iK89nQskURxsLqULTs1IWlMzdHFYPLrBvTJhE+6C+IX5LZ93/CjaLxz1+WvmZ544Sk5IfphriTd/5Q2z0jbXluNW4x4FGuk/OVKZB+AznASAYtKG4vSMC9vJdNKB+zIok/xltzRGJJ3IVNqOTdzCWNRFuAJuTvKBQEMXndJw7Vvnn7WI36NLIViGrcZDOf74M78ViV50PispVyLH/eqYHn53mldScTp88DqOgTUmX+SEGqrrbzLBMPfdspgG+BQB5zLILJK2RO5MJBtS1g49YOd0SjRSaFJM6tgsWtOklM606mu6QIDAQAB";//SystemConfig.JWT_RSA_PUBLICKEY;//公钥key

	/**
	 * 生成token方法
	 * @param issUser 令牌的创建者 网站或应用clientId
	 * @param audience 令牌使用者 用户ID
	 * @param minutes 有效期时间 
	 * @param payLoadMap 令牌有效负载参数，即需要在token中保存的用户信息
	 * @return
	 * @throws JoseException
	 */
	public String generateToken(String issUser ,String audience ,Float minutes ,Map<String, Object> payLoadMap) throws JoseException {
		if (StringUtils.isBlank(issUser)) {
			log.info("令牌创建者为空");
			return null;
		}
		
		if (StringUtils.isBlank(audience)) {
			log.info("令牌使用者为空");
			return null;
		}
		 
		if (minutes == null) {
			log.info("令牌有效时间为空");
			return null;
		}
		
		if (privateKey == null) {
			if (!getPrivateKey(privateKeyStr)) {
				log.info("读取私钥失败");
				return null;
			}
		}
		 
		JwtClaims claims = new JwtClaims();
		claims.setIssuer(issUser);  							//令牌创建者
		claims.setAudience(issUser); 							//令牌使用者
		claims.setExpirationTimeMinutesInTheFuture(minutes);	//有效时间
		claims.setGeneratedJwtId(); 							//令牌ID，默认16
		claims.setIssuedAtToNow();  							//令牌的发出时间（设置为）
		claims.setNotBeforeMinutesInThePast(1); 				//time before which the token is not yet valid (2 minutes ago)
		claims.setSubject("subject"); 							//the subject/principal is whom the token is about
		for (Map.Entry<String, Object> entry : payLoadMap.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue() == null ? "" : entry.getValue().toString();
			claims.setClaim(key, value);
		}
		claims.setClaim("audience", audience);			//此处将用户编号作为payload传入
		
		JsonWebSignature jws = new JsonWebSignature(); 	//A JWT is a JWS and/or a JWE with JSON claims as the payload.
		jws.setPayload(claims.toJson());				//设置json web token 的 payLoad
		jws.setKey(privateKey);							//设置私钥
		jws.setKeyIdHeaderValue("k1");					//key Id
		jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
		String jwt = jws.getCompactSerialization();
		
		return jwt;
	}
	
	/**
	 * 获取刷新token的方法
	 * @param issUser 令牌的创建者 网站或应用clientId
	 * @param audience 令牌使用者 用户ID
	 * @param payLoadMap 令牌有效负载参数，即需要在token中保存的用户信息
	 * @return
	 * @throws JoseException
	 */
	public String generateRefreshToken(String issUser ,String audience ,Map<String, Object> payLoadMap) throws JoseException {
		if (StringUtils.isBlank(issUser)) {
			log.info("令牌创建者为空");
			return null;
		}
		
		if (StringUtils.isBlank(audience)) {
			log.info("令牌使用者为空");
			return null;
		}
		
		if (privateKey == null) {
			if (!getPrivateKey(privateKeyStr)) {
				log.info("读取私钥失败");
				return null;
			}
		}
		
		JwtClaims claims = new JwtClaims();
		claims.setIssuer(issUser);  				//令牌创建者
		claims.setAudience(issUser); 				//令牌使用者
		//claims.setExpirationTimeMinutesInTheFuture(0);//设置为0 
		claims.setGeneratedJwtId(); 				//令牌ID，默认16
		claims.setIssuedAtToNow();  				//令牌的发出时间（设置为）
		claims.setNotBeforeMinutesInThePast(2); 	//time before which the token is not yet valid (2 minutes ago)
		claims.setSubject("subject"); 				//the subject/principal is whom the token is about
		for (Map.Entry<String, Object> entry : payLoadMap.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			claims.setClaim(key, value);
		}
		claims.setClaim("audience", audience);			//此处将用户编号作为payload传入
		
		JsonWebSignature jws = new JsonWebSignature(); 	//A JWT is a JWS and/or a JWE with JSON claims as the payload.
		jws.setPayload(claims.toJson());				//设置json web token 的 payLoad
		jws.setKey(privateKey);							//设置私钥
		jws.setKeyIdHeaderValue("k1");					//key Id
		jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
		String jwt = jws.getCompactSerialization();
		
		return jwt;
	}
	
	/**
	 * token 校验方方
	 * @param token token串
	 * @param isUser token创建者
	 * @param audience token使用者
	 * @return 令牌有效负载参数
	 * @throws InvalidJwtException 
	 */
	public Map<String, Object> checkToken(String isUser ,String audience, String token) throws InvalidJwtException {
		
		if (publicKey == null) {
			if (!getPublicKey(pubicKeyStr)) {
				log.info("读取公钥失败");
				return null;
			}
		}
		
		JwtConsumer jwtConsumer = new JwtConsumerBuilder()
			.setRequireExpirationTime() 		//the JWT must have an expiration time
			.setAllowedClockSkewInSeconds(30) 	//allow some leeway in validating time based claims to account for clock skew
			.setRequireSubject() 				//the JWT must have a subject claim
			.setExpectedIssuer(isUser) 			//whom the JWT needs to have been issued by
			.setExpectedAudience(audience) 		//to whom the JWT is intended for
			.setVerificationKey(publicKey) 		//verify the signature with the public key
			.build(); 							//create the JwtConsumer instance
		//Validate the JWT and process it to the Claims
		JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
		Map<String , Object> payLoadMap = jwtClaims.getClaimsMap();
		log.debug("token 校验成功" + payLoadMap);
		return payLoadMap;
	}
	
	/**
	 * RefreshToken 刷新token的校验方法
	 * @param refreshToken token串
	 * @param issUer token创建者
	 * @param audience token使用者
	 * @return 令牌有效负载参数
	 */
	public Map<String,Object> checkRefreshToken(String refreshToken ,String issUer ,String audience) throws InvalidJwtException {
		
		if (publicKey == null) {
			if (!getPublicKey(pubicKeyStr)) {
				log.info("读取公钥失败");
				return null;
			}
		}

		JwtConsumer jwtConsumer = new JwtConsumerBuilder()
			.setAllowedClockSkewInSeconds(30) 	//allow some leeway in validating time based claims to account for clock skew
			.setRequireSubject() 				//the JWT must have a subject claim
			.setExpectedIssuer(issUer) 			//whom the JWT needs to have been issued by
			.setExpectedAudience(audience) 		//to whom the JWT is intended for
			.setVerificationKey(publicKey) 		//verify the signature with the public key
			.build(); 							//create the JwtConsumer instance
		//Validate the JWT and process it to the Claims
		JwtClaims jwtClaims = jwtConsumer.processToClaims(refreshToken);
		Map<String , Object> payLoadMap = jwtClaims.getClaimsMap();
		log.debug("token 校验成功" + payLoadMap);
		return payLoadMap;
	}
	
	/**
	 * 获取公钥
	 * @param publicKeyStr
	 * @return
	 */
	@SuppressWarnings("restriction")
	private boolean getPublicKey(String publicKeyStr) {
        try {
        	byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(publicKeyStr);
        	X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        	publicKey = (Key)keyFactory.generatePublic(keySpec);
        	return true;
		} catch (IOException e) {
			log.error("IO异常:", e);
		} catch (NoSuchAlgorithmException e) {
			log.error("NoSuchAlgorithmException:", e);
		} catch (InvalidKeySpecException e) {
			log.error("InvalidKeySpecException:", e);
		}
        
       return false;
	}
	
	/**
	 * 用来获取私钥
	 * @param privateKeyStr
	 * @return
	 */
	@SuppressWarnings("restriction")
	private boolean getPrivateKey(String privateKeyStr) {
		try {
	        byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(privateKeyStr);
	        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        privateKey = (Key)keyFactory.generatePrivate(keySpec);
	        return true;
		} catch (IOException e) {
			log.error("IO异常:", e);
		} catch (NoSuchAlgorithmException e) {
			log.error("NoSuchAlgorithmException:", e);
		} catch (InvalidKeySpecException e) {
			log.error("InvalidKeySpecException:", e);
		}
		
		return false;
	}
}