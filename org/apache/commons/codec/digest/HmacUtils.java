/*     */ package org.apache.commons.codec.digest;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import javax.crypto.Mac;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import org.apache.commons.codec.binary.Hex;
/*     */ import org.apache.commons.codec.binary.StringUtils;
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
/*     */ public final class HmacUtils
/*     */ {
/*     */   private static final int STREAM_BUFFER_LENGTH = 1024;
/*     */   
/*     */   public static Mac getHmacMd5(byte[] key) {
/*  63 */     return getInitializedMac(HmacAlgorithms.HMAC_MD5, key);
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
/*     */   public static Mac getHmacSha1(byte[] key) {
/*  81 */     return getInitializedMac(HmacAlgorithms.HMAC_SHA_1, key);
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
/*     */   public static Mac getHmacSha256(byte[] key) {
/*  99 */     return getInitializedMac(HmacAlgorithms.HMAC_SHA_256, key);
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
/*     */   public static Mac getHmacSha384(byte[] key) {
/* 117 */     return getInitializedMac(HmacAlgorithms.HMAC_SHA_384, key);
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
/*     */   public static Mac getHmacSha512(byte[] key) {
/* 135 */     return getInitializedMac(HmacAlgorithms.HMAC_SHA_512, key);
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
/*     */   public static Mac getInitializedMac(HmacAlgorithms algorithm, byte[] key) {
/* 155 */     return getInitializedMac(algorithm.toString(), key);
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
/*     */   public static Mac getInitializedMac(String algorithm, byte[] key) {
/* 176 */     if (key == null) {
/* 177 */       throw new IllegalArgumentException("Null key");
/*     */     }
/*     */     
/*     */     try {
/* 181 */       SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
/* 182 */       Mac mac = Mac.getInstance(algorithm);
/* 183 */       mac.init(keySpec);
/* 184 */       return mac;
/* 185 */     } catch (NoSuchAlgorithmException e) {
/* 186 */       throw new IllegalArgumentException(e);
/* 187 */     } catch (InvalidKeyException e) {
/* 188 */       throw new IllegalArgumentException(e);
/*     */     } 
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
/*     */   public static byte[] hmacMd5(byte[] key, byte[] valueToDigest) {
/*     */     try {
/* 207 */       return getHmacMd5(key).doFinal(valueToDigest);
/* 208 */     } catch (IllegalStateException e) {
/*     */       
/* 210 */       throw new IllegalArgumentException(e);
/*     */     } 
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
/*     */   public static byte[] hmacMd5(byte[] key, InputStream valueToDigest) throws IOException {
/* 231 */     return updateHmac(getHmacMd5(key), valueToDigest).doFinal();
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
/*     */   public static byte[] hmacMd5(String key, String valueToDigest) {
/* 246 */     return hmacMd5(StringUtils.getBytesUtf8(key), StringUtils.getBytesUtf8(valueToDigest));
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
/*     */   public static String hmacMd5Hex(byte[] key, byte[] valueToDigest) {
/* 261 */     return Hex.encodeHexString(hmacMd5(key, valueToDigest));
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
/*     */   public static String hmacMd5Hex(byte[] key, InputStream valueToDigest) throws IOException {
/* 281 */     return Hex.encodeHexString(hmacMd5(key, valueToDigest));
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
/*     */   public static String hmacMd5Hex(String key, String valueToDigest) {
/* 296 */     return Hex.encodeHexString(hmacMd5(key, valueToDigest));
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
/*     */   public static byte[] hmacSha1(byte[] key, byte[] valueToDigest) {
/*     */     try {
/* 314 */       return getHmacSha1(key).doFinal(valueToDigest);
/* 315 */     } catch (IllegalStateException e) {
/*     */       
/* 317 */       throw new IllegalArgumentException(e);
/*     */     } 
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
/*     */   public static byte[] hmacSha1(byte[] key, InputStream valueToDigest) throws IOException {
/* 338 */     return updateHmac(getHmacSha1(key), valueToDigest).doFinal();
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
/*     */   public static byte[] hmacSha1(String key, String valueToDigest) {
/* 353 */     return hmacSha1(StringUtils.getBytesUtf8(key), StringUtils.getBytesUtf8(valueToDigest));
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
/*     */   public static String hmacSha1Hex(byte[] key, byte[] valueToDigest) {
/* 368 */     return Hex.encodeHexString(hmacSha1(key, valueToDigest));
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
/*     */   public static String hmacSha1Hex(byte[] key, InputStream valueToDigest) throws IOException {
/* 388 */     return Hex.encodeHexString(hmacSha1(key, valueToDigest));
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
/*     */   public static String hmacSha1Hex(String key, String valueToDigest) {
/* 403 */     return Hex.encodeHexString(hmacSha1(key, valueToDigest));
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
/*     */   public static byte[] hmacSha256(byte[] key, byte[] valueToDigest) {
/*     */     try {
/* 421 */       return getHmacSha256(key).doFinal(valueToDigest);
/* 422 */     } catch (IllegalStateException e) {
/*     */       
/* 424 */       throw new IllegalArgumentException(e);
/*     */     } 
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
/*     */   public static byte[] hmacSha256(byte[] key, InputStream valueToDigest) throws IOException {
/* 445 */     return updateHmac(getHmacSha256(key), valueToDigest).doFinal();
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
/*     */   public static byte[] hmacSha256(String key, String valueToDigest) {
/* 460 */     return hmacSha256(StringUtils.getBytesUtf8(key), StringUtils.getBytesUtf8(valueToDigest));
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
/*     */   public static String hmacSha256Hex(byte[] key, byte[] valueToDigest) {
/* 475 */     return Hex.encodeHexString(hmacSha256(key, valueToDigest));
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
/*     */   public static String hmacSha256Hex(byte[] key, InputStream valueToDigest) throws IOException {
/* 495 */     return Hex.encodeHexString(hmacSha256(key, valueToDigest));
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
/*     */   public static String hmacSha256Hex(String key, String valueToDigest) {
/* 510 */     return Hex.encodeHexString(hmacSha256(key, valueToDigest));
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
/*     */   public static byte[] hmacSha384(byte[] key, byte[] valueToDigest) {
/*     */     try {
/* 528 */       return getHmacSha384(key).doFinal(valueToDigest);
/* 529 */     } catch (IllegalStateException e) {
/*     */       
/* 531 */       throw new IllegalArgumentException(e);
/*     */     } 
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
/*     */   public static byte[] hmacSha384(byte[] key, InputStream valueToDigest) throws IOException {
/* 552 */     return updateHmac(getHmacSha384(key), valueToDigest).doFinal();
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
/*     */   public static byte[] hmacSha384(String key, String valueToDigest) {
/* 567 */     return hmacSha384(StringUtils.getBytesUtf8(key), StringUtils.getBytesUtf8(valueToDigest));
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
/*     */   public static String hmacSha384Hex(byte[] key, byte[] valueToDigest) {
/* 582 */     return Hex.encodeHexString(hmacSha384(key, valueToDigest));
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
/*     */   public static String hmacSha384Hex(byte[] key, InputStream valueToDigest) throws IOException {
/* 602 */     return Hex.encodeHexString(hmacSha384(key, valueToDigest));
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
/*     */   public static String hmacSha384Hex(String key, String valueToDigest) {
/* 617 */     return Hex.encodeHexString(hmacSha384(key, valueToDigest));
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
/*     */   public static byte[] hmacSha512(byte[] key, byte[] valueToDigest) {
/*     */     try {
/* 635 */       return getHmacSha512(key).doFinal(valueToDigest);
/* 636 */     } catch (IllegalStateException e) {
/*     */       
/* 638 */       throw new IllegalArgumentException(e);
/*     */     } 
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
/*     */   public static byte[] hmacSha512(byte[] key, InputStream valueToDigest) throws IOException {
/* 659 */     return updateHmac(getHmacSha512(key), valueToDigest).doFinal();
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
/*     */   public static byte[] hmacSha512(String key, String valueToDigest) {
/* 674 */     return hmacSha512(StringUtils.getBytesUtf8(key), StringUtils.getBytesUtf8(valueToDigest));
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
/*     */   public static String hmacSha512Hex(byte[] key, byte[] valueToDigest) {
/* 689 */     return Hex.encodeHexString(hmacSha512(key, valueToDigest));
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
/*     */   public static String hmacSha512Hex(byte[] key, InputStream valueToDigest) throws IOException {
/* 709 */     return Hex.encodeHexString(hmacSha512(key, valueToDigest));
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
/*     */   public static String hmacSha512Hex(String key, String valueToDigest) {
/* 724 */     return Hex.encodeHexString(hmacSha512(key, valueToDigest));
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
/*     */   public static Mac updateHmac(Mac mac, byte[] valueToDigest) {
/* 742 */     mac.reset();
/* 743 */     mac.update(valueToDigest);
/* 744 */     return mac;
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
/*     */   public static Mac updateHmac(Mac mac, InputStream valueToDigest) throws IOException {
/* 765 */     mac.reset();
/* 766 */     byte[] buffer = new byte[1024];
/* 767 */     int read = valueToDigest.read(buffer, 0, 1024);
/*     */     
/* 769 */     while (read > -1) {
/* 770 */       mac.update(buffer, 0, read);
/* 771 */       read = valueToDigest.read(buffer, 0, 1024);
/*     */     } 
/*     */     
/* 774 */     return mac;
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
/*     */   public static Mac updateHmac(Mac mac, String valueToDigest) {
/* 790 */     mac.reset();
/* 791 */     mac.update(StringUtils.getBytesUtf8(valueToDigest));
/* 792 */     return mac;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\digest\HmacUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */