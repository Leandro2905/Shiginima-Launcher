/*     */ package org.apache.commons.codec.digest;
/*     */ 
/*     */ import java.security.MessageDigest;
/*     */ import java.util.Arrays;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.codec.Charsets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Md5Crypt
/*     */ {
/*     */   static final String APR1_PREFIX = "$apr1$";
/*     */   private static final int BLOCKSIZE = 16;
/*     */   static final String MD5_PREFIX = "$1$";
/*     */   private static final int ROUNDS = 1000;
/*     */   
/*     */   public static String apr1Crypt(byte[] keyBytes) {
/*  72 */     return apr1Crypt(keyBytes, "$apr1$" + B64.getRandomSalt(8));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String apr1Crypt(byte[] keyBytes, String salt) {
/*  89 */     if (salt != null && !salt.startsWith("$apr1$")) {
/*  90 */       salt = "$apr1$" + salt;
/*     */     }
/*  92 */     return md5Crypt(keyBytes, salt, "$apr1$");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String apr1Crypt(String keyBytes) {
/* 105 */     return apr1Crypt(keyBytes.getBytes(Charsets.UTF_8));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String apr1Crypt(String keyBytes, String salt) {
/* 126 */     return apr1Crypt(keyBytes.getBytes(Charsets.UTF_8), salt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String md5Crypt(byte[] keyBytes) {
/* 141 */     return md5Crypt(keyBytes, "$1$" + B64.getRandomSalt(8));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String md5Crypt(byte[] keyBytes, String salt) {
/* 161 */     return md5Crypt(keyBytes, salt, "$1$");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String md5Crypt(byte[] keyBytes, String salt, String prefix) {
/*     */     String saltString;
/* 180 */     int keyLen = keyBytes.length;
/*     */ 
/*     */ 
/*     */     
/* 184 */     if (salt == null) {
/* 185 */       saltString = B64.getRandomSalt(8);
/*     */     } else {
/* 187 */       Pattern p = Pattern.compile("^" + prefix.replace("$", "\\$") + "([\\.\\/a-zA-Z0-9]{1,8}).*");
/* 188 */       Matcher m = p.matcher(salt);
/* 189 */       if (m == null || !m.find()) {
/* 190 */         throw new IllegalArgumentException("Invalid salt value: " + salt);
/*     */       }
/* 192 */       saltString = m.group(1);
/*     */     } 
/* 194 */     byte[] saltBytes = saltString.getBytes(Charsets.UTF_8);
/*     */     
/* 196 */     MessageDigest ctx = DigestUtils.getMd5Digest();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     ctx.update(keyBytes);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 206 */     ctx.update(prefix.getBytes(Charsets.UTF_8));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     ctx.update(saltBytes);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     MessageDigest ctx1 = DigestUtils.getMd5Digest();
/* 217 */     ctx1.update(keyBytes);
/* 218 */     ctx1.update(saltBytes);
/* 219 */     ctx1.update(keyBytes);
/* 220 */     byte[] finalb = ctx1.digest();
/* 221 */     int ii = keyLen;
/* 222 */     while (ii > 0) {
/* 223 */       ctx.update(finalb, 0, (ii > 16) ? 16 : ii);
/* 224 */       ii -= 16;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 230 */     Arrays.fill(finalb, (byte)0);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 235 */     ii = keyLen;
/* 236 */     int j = 0;
/* 237 */     while (ii > 0) {
/* 238 */       if ((ii & 0x1) == 1) {
/* 239 */         ctx.update(finalb[0]);
/*     */       } else {
/* 241 */         ctx.update(keyBytes[0]);
/*     */       } 
/* 243 */       ii >>= 1;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 249 */     StringBuilder passwd = new StringBuilder(prefix + saltString + "$");
/* 250 */     finalb = ctx.digest();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 256 */     for (int i = 0; i < 1000; i++) {
/* 257 */       ctx1 = DigestUtils.getMd5Digest();
/* 258 */       if ((i & 0x1) != 0) {
/* 259 */         ctx1.update(keyBytes);
/*     */       } else {
/* 261 */         ctx1.update(finalb, 0, 16);
/*     */       } 
/*     */       
/* 264 */       if (i % 3 != 0) {
/* 265 */         ctx1.update(saltBytes);
/*     */       }
/*     */       
/* 268 */       if (i % 7 != 0) {
/* 269 */         ctx1.update(keyBytes);
/*     */       }
/*     */       
/* 272 */       if ((i & 0x1) != 0) {
/* 273 */         ctx1.update(finalb, 0, 16);
/*     */       } else {
/* 275 */         ctx1.update(keyBytes);
/*     */       } 
/* 277 */       finalb = ctx1.digest();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 283 */     B64.b64from24bit(finalb[0], finalb[6], finalb[12], 4, passwd);
/* 284 */     B64.b64from24bit(finalb[1], finalb[7], finalb[13], 4, passwd);
/* 285 */     B64.b64from24bit(finalb[2], finalb[8], finalb[14], 4, passwd);
/* 286 */     B64.b64from24bit(finalb[3], finalb[9], finalb[15], 4, passwd);
/* 287 */     B64.b64from24bit(finalb[4], finalb[10], finalb[5], 4, passwd);
/* 288 */     B64.b64from24bit((byte)0, (byte)0, finalb[11], 2, passwd);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 294 */     ctx.reset();
/* 295 */     ctx1.reset();
/* 296 */     Arrays.fill(keyBytes, (byte)0);
/* 297 */     Arrays.fill(saltBytes, (byte)0);
/* 298 */     Arrays.fill(finalb, (byte)0);
/*     */     
/* 300 */     return passwd.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\digest\Md5Crypt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */