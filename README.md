# SpringMVC Json Web token.
使用了 
<dependency>
  <groupId>org.bitbucket.b_c</groupId>
  <artifactId>jose4j</artifactId>
  <version>0.6.3</version>
</dependency>包 实现了Jwt.<br>
# 1. 生成 keystore文件 和 cer文件
  生成工具: C:\Program Files\Java\jdk1.8.0_121\bin\keytool;<br><br>
 · 2048位RSA2密钥<br>
&nbsp;&nbsp;keytool -genkey -v -alias shawn-key -dname "CN=shawn-key,OU=HE,O=CUI,L=SHENGZHEN,ST=GUANGDONG,C=CN" -keyalg RSA -keysize 2048 -keypass 123456 -keystore d:shawn-key.keystore -storepass 123456 -validity 36500 -storetype JCEKS<br><br>
 · 导出公钥文件<br>
&nbsp;&nbsp;keytool -exportcert -alias shawn-key -file d:/shawn-key.cer -keystore d:/shawn-key.keystore -storepass 123456 -rfc -storetype JCEKS<br>
# 2. keystore导出私钥 和 cer导出公钥
&nbsp;&nbsp;http://localhost:8080/rsaShaExport

