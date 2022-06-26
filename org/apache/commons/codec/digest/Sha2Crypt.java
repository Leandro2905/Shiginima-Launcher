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
/*     */ public class Sha2Crypt
/*     */ {
/*     */   private static final int ROUNDS_DEFAULT = 5000;
/*     */   private static final int ROUNDS_MAX = 999999999;
/*     */   private static final int ROUNDS_MIN = 1000;
/*     */   private static final String ROUNDS_PREFIX = "rounds=";
/*     */   private static final int SHA256_BLOCKSIZE = 32;
/*     */   static final String SHA256_PREFIX = "$5$";
/*     */   private static final int SHA512_BLOCKSIZE = 64;
/*     */   static final String SHA512_PREFIX = "$6$";
/*  68 */   private static final Pattern SALT_PATTERN = Pattern.compile("^\\$([56])\\$(rounds=(\\d+)\\$)?([\\.\\/a-zA-Z0-9]{1,16}).*");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String sha256Crypt(byte[] keyBytes) {
/*  83 */     return sha256Crypt(keyBytes, null);
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
/*     */   public static String sha256Crypt(byte[] keyBytes, String salt) {
/* 102 */     if (salt == null) {
/* 103 */       salt = "$5$" + B64.getRandomSalt(8);
/*     */     }
/* 105 */     return sha2Crypt(keyBytes, salt, "$5$", 32, "SHA-256");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String sha2Crypt(byte[] keyBytes, String salt, String saltPrefix, int blocksize, String algorithm) {
/* 136 */     int keyLen = keyBytes.length;
/*     */ 
/*     */     
/* 139 */     int rounds = 5000;
/* 140 */     boolean roundsCustom = false;
/* 141 */     if (salt == null) {
/* 142 */       throw new IllegalArgumentException("Salt must not be null");
/*     */     }
/*     */     
/* 145 */     Matcher m = SALT_PATTERN.matcher(salt);
/* 146 */     if (m == null || !m.find()) {
/* 147 */       throw new IllegalArgumentException("Invalid salt value: " + salt);
/*     */     }
/* 149 */     if (m.group(3) != null) {
/* 150 */       rounds = Integer.parseInt(m.group(3));
/* 151 */       rounds = Math.max(1000, Math.min(999999999, rounds));
/* 152 */       roundsCustom = true;
/*     */     } 
/* 154 */     String saltString = m.group(4);
/* 155 */     byte[] saltBytes = saltString.getBytes(Charsets.UTF_8);
/* 156 */     int saltLen = saltBytes.length;
/*     */ 
/*     */ 
/*     */     
/* 160 */     MessageDigest ctx = DigestUtils.getDigest(algorithm);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     ctx.update(keyBytes);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     ctx.update(saltBytes);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     MessageDigest altCtx = DigestUtils.getDigest(algorithm);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     altCtx.update(keyBytes);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     altCtx.update(saltBytes);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 206 */     altCtx.update(keyBytes);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     byte[] altResult = altCtx.digest();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     int cnt = keyBytes.length;
/* 223 */     while (cnt > blocksize) {
/* 224 */       ctx.update(altResult, 0, blocksize);
/* 225 */       cnt -= blocksize;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 230 */     ctx.update(altResult, 0, cnt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 246 */     cnt = keyBytes.length;
/* 247 */     while (cnt > 0) {
/* 248 */       if ((cnt & 0x1) != 0) {
/* 249 */         ctx.update(altResult, 0, blocksize);
/*     */       } else {
/* 251 */         ctx.update(keyBytes);
/*     */       } 
/* 253 */       cnt >>= 1;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 260 */     altResult = ctx.digest();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 266 */     altCtx = DigestUtils.getDigest(algorithm);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 275 */     for (int i = 1; i <= keyLen; i++) {
/* 276 */       altCtx.update(keyBytes);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 283 */     byte[] tempResult = altCtx.digest();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 295 */     byte[] pBytes = new byte[keyLen];
/* 296 */     int cp = 0;
/* 297 */     while (cp < keyLen - blocksize) {
/* 298 */       System.arraycopy(tempResult, 0, pBytes, cp, blocksize);
/* 299 */       cp += blocksize;
/*     */     } 
/* 301 */     System.arraycopy(tempResult, 0, pBytes, cp, keyLen - cp);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 307 */     altCtx = DigestUtils.getDigest(algorithm);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 316 */     for (int j = 1; j <= 16 + (altResult[0] & 0xFF); j++) {
/* 317 */       altCtx.update(saltBytes);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 324 */     tempResult = altCtx.digest();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 337 */     byte[] sBytes = new byte[saltLen];
/* 338 */     cp = 0;
/* 339 */     while (cp < saltLen - blocksize) {
/* 340 */       System.arraycopy(tempResult, 0, sBytes, cp, blocksize);
/* 341 */       cp += blocksize;
/*     */     } 
/* 343 */     System.arraycopy(tempResult, 0, sBytes, cp, saltLen - cp);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 356 */     for (int k = 0; k <= rounds - 1; k++) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 361 */       ctx = DigestUtils.getDigest(algorithm);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 368 */       if ((k & 0x1) != 0) {
/* 369 */         ctx.update(pBytes, 0, keyLen);
/*     */       } else {
/* 371 */         ctx.update(altResult, 0, blocksize);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 378 */       if (k % 3 != 0) {
/* 379 */         ctx.update(sBytes, 0, saltLen);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 386 */       if (k % 7 != 0) {
/* 387 */         ctx.update(pBytes, 0, keyLen);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 395 */       if ((k & 0x1) != 0) {
/* 396 */         ctx.update(altResult, 0, blocksize);
/*     */       } else {
/* 398 */         ctx.update(pBytes, 0, keyLen);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 405 */       altResult = ctx.digest();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 423 */     StringBuilder buffer = new StringBuilder(saltPrefix);
/* 424 */     if (roundsCustom) {
/* 425 */       buffer.append("rounds=");
/* 426 */       buffer.append(rounds);
/* 427 */       buffer.append("$");
/*     */     } 
/* 429 */     buffer.append(saltString);
/* 430 */     buffer.append("$");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 456 */     if (blocksize == 32) {
/* 457 */       B64.b64from24bit(altResult[0], altResult[10], altResult[20], 4, buffer);
/* 458 */       B64.b64from24bit(altResult[21], altResult[1], altResult[11], 4, buffer);
/* 459 */       B64.b64from24bit(altResult[12], altResult[22], altResult[2], 4, buffer);
/* 460 */       B64.b64from24bit(altResult[3], altResult[13], altResult[23], 4, buffer);
/* 461 */       B64.b64from24bit(altResult[24], altResult[4], altResult[14], 4, buffer);
/* 462 */       B64.b64from24bit(altResult[15], altResult[25], altResult[5], 4, buffer);
/* 463 */       B64.b64from24bit(altResult[6], altResult[16], altResult[26], 4, buffer);
/* 464 */       B64.b64from24bit(altResult[27], altResult[7], altResult[17], 4, buffer);
/* 465 */       B64.b64from24bit(altResult[18], altResult[28], altResult[8], 4, buffer);
/* 466 */       B64.b64from24bit(altResult[9], altResult[19], altResult[29], 4, buffer);
/* 467 */       B64.b64from24bit((byte)0, altResult[31], altResult[30], 3, buffer);
/*     */     } else {
/* 469 */       B64.b64from24bit(altResult[0], altResult[21], altResult[42], 4, buffer);
/* 470 */       B64.b64from24bit(altResult[22], altResult[43], altResult[1], 4, buffer);
/* 471 */       B64.b64from24bit(altResult[44], altResult[2], altResult[23], 4, buffer);
/* 472 */       B64.b64from24bit(altResult[3], altResult[24], altResult[45], 4, buffer);
/* 473 */       B64.b64from24bit(altResult[25], altResult[46], altResult[4], 4, buffer);
/* 474 */       B64.b64from24bit(altResult[47], altResult[5], altResult[26], 4, buffer);
/* 475 */       B64.b64from24bit(altResult[6], altResult[27], altResult[48], 4, buffer);
/* 476 */       B64.b64from24bit(altResult[28], altResult[49], altResult[7], 4, buffer);
/* 477 */       B64.b64from24bit(altResult[50], altResult[8], altResult[29], 4, buffer);
/* 478 */       B64.b64from24bit(altResult[9], altResult[30], altResult[51], 4, buffer);
/* 479 */       B64.b64from24bit(altResult[31], altResult[52], altResult[10], 4, buffer);
/* 480 */       B64.b64from24bit(altResult[53], altResult[11], altResult[32], 4, buffer);
/* 481 */       B64.b64from24bit(altResult[12], altResult[33], altResult[54], 4, buffer);
/* 482 */       B64.b64from24bit(altResult[34], altResult[55], altResult[13], 4, buffer);
/* 483 */       B64.b64from24bit(altResult[56], altResult[14], altResult[35], 4, buffer);
/* 484 */       B64.b64from24bit(altResult[15], altResult[36], altResult[57], 4, buffer);
/* 485 */       B64.b64from24bit(altResult[37], altResult[58], altResult[16], 4, buffer);
/* 486 */       B64.b64from24bit(altResult[59], altResult[17], altResult[38], 4, buffer);
/* 487 */       B64.b64from24bit(altResult[18], altResult[39], altResult[60], 4, buffer);
/* 488 */       B64.b64from24bit(altResult[40], altResult[61], altResult[19], 4, buffer);
/* 489 */       B64.b64from24bit(altResult[62], altResult[20], altResult[41], 4, buffer);
/* 490 */       B64.b64from24bit((byte)0, (byte)0, altResult[63], 2, buffer);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 498 */     Arrays.fill(tempResult, (byte)0);
/* 499 */     Arrays.fill(pBytes, (byte)0);
/* 500 */     Arrays.fill(sBytes, (byte)0);
/* 501 */     ctx.reset();
/* 502 */     altCtx.reset();
/* 503 */     Arrays.fill(keyBytes, (byte)0);
/* 504 */     Arrays.fill(saltBytes, (byte)0);
/*     */     
/* 506 */     return buffer.toString();
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
/*     */   public static String sha512Crypt(byte[] keyBytes) {
/* 521 */     return sha512Crypt(keyBytes, null);
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
/*     */   public static String sha512Crypt(byte[] keyBytes, String salt) {
/* 540 */     if (salt == null) {
/* 541 */       salt = "$6$" + B64.getRandomSalt(8);
/*     */     }
/* 543 */     return sha2Crypt(keyBytes, salt, "$6$", 64, "SHA-512");
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\digest\Sha2Crypt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */