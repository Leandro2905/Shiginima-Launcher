/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UuidUtil
/*     */ {
/*     */   public static final String UUID_SEQUENCE = "org.apache.logging.log4j.uuidSequence";
/*     */   private static final String ASSIGNED_SEQUENCES = "org.apache.logging.log4j.assignedSequences";
/*  44 */   private static final AtomicInteger count = new AtomicInteger(0);
/*     */   
/*     */   private static final long TYPE1 = 4096L;
/*     */   
/*     */   private static final byte VARIANT = -128;
/*     */   
/*     */   private static final int SEQUENCE_MASK = 16383;
/*     */   
/*     */   private static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 122192928000000000L;
/*     */   
/*  54 */   private static final long uuidSequence = PropertiesUtil.getProperties().getLongProperty("org.apache.logging.log4j.uuidSequence", 0L);
/*     */   
/*     */   private static final long least;
/*     */   
/*     */   private static final long LOW_MASK = 4294967295L;
/*     */   
/*     */   private static final long MID_MASK = 281470681743360L;
/*     */   
/*     */   private static final long HIGH_MASK = 1152640029630136320L;
/*     */   
/*     */   private static final int NODE_SIZE = 8;
/*     */ 
/*     */   
/*     */   static {
/*  68 */     byte[] mac = null;
/*     */     try {
/*  70 */       InetAddress address = InetAddress.getLocalHost();
/*     */       try {
/*  72 */         NetworkInterface ni = NetworkInterface.getByInetAddress(address);
/*  73 */         if (ni != null && !ni.isLoopback() && ni.isUp()) {
/*  74 */           Method method = ni.getClass().getMethod("getHardwareAddress", new Class[0]);
/*  75 */           if (method != null) {
/*  76 */             mac = (byte[])method.invoke(ni, new Object[0]);
/*     */           }
/*     */         } 
/*     */         
/*  80 */         if (mac == null) {
/*  81 */           Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
/*  82 */           while (enumeration.hasMoreElements() && mac == null) {
/*  83 */             ni = enumeration.nextElement();
/*  84 */             if (ni != null && ni.isUp() && !ni.isLoopback()) {
/*  85 */               Method method = ni.getClass().getMethod("getHardwareAddress", new Class[0]);
/*  86 */               if (method != null) {
/*  87 */                 mac = (byte[])method.invoke(ni, new Object[0]);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*  92 */       } catch (Exception ex) {
/*  93 */         ex.printStackTrace();
/*     */       } 
/*     */       
/*  96 */       if (mac == null || mac.length == 0) {
/*  97 */         mac = address.getAddress();
/*     */       }
/*  99 */     } catch (UnknownHostException e) {}
/*     */ 
/*     */     
/* 102 */     Random randomGenerator = new SecureRandom();
/* 103 */     if (mac == null || mac.length == 0) {
/* 104 */       mac = new byte[6];
/* 105 */       randomGenerator.nextBytes(mac);
/*     */     } 
/* 107 */     int length = (mac.length >= 6) ? 6 : mac.length;
/* 108 */     int index = (mac.length >= 6) ? (mac.length - 6) : 0;
/* 109 */     byte[] node = new byte[8];
/* 110 */     node[0] = Byte.MIN_VALUE;
/* 111 */     node[1] = 0;
/* 112 */     for (int i = 2; i < 8; i++) {
/* 113 */       node[i] = 0;
/*     */     }
/* 115 */     System.arraycopy(mac, index, node, index + 2, length);
/* 116 */     ByteBuffer buf = ByteBuffer.wrap(node);
/* 117 */     long rand = uuidSequence;
/* 118 */     String assigned = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.assignedSequences");
/*     */     
/* 120 */     if (assigned == null) {
/* 121 */       sequences = new long[0];
/*     */     } else {
/* 123 */       String[] array = assigned.split(Patterns.COMMA_SEPARATOR);
/* 124 */       sequences = new long[array.length];
/* 125 */       int j = 0;
/* 126 */       for (String value : array) {
/* 127 */         sequences[j] = Long.parseLong(value);
/* 128 */         j++;
/*     */       } 
/*     */     } 
/* 131 */     if (rand == 0L) {
/* 132 */       rand = randomGenerator.nextLong();
/*     */     }
/* 134 */     rand &= 0x3FFFL;
/*     */     
/*     */     while (true) {
/* 137 */       boolean duplicate = false;
/* 138 */       for (long sequence : sequences) {
/* 139 */         if (sequence == rand) {
/* 140 */           duplicate = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 144 */       if (duplicate) {
/* 145 */         rand = rand + 1L & 0x3FFFL;
/*     */       }
/* 147 */       if (!duplicate) {
/* 148 */         assigned = (assigned == null) ? Long.toString(rand) : (assigned + ',' + Long.toString(rand));
/* 149 */         System.setProperty("org.apache.logging.log4j.assignedSequences", assigned);
/*     */         
/* 151 */         least = buf.getLong() | rand << 48L;
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int SHIFT_2 = 16;
/*     */ 
/*     */   
/*     */   private static final int SHIFT_4 = 32;
/*     */ 
/*     */   
/*     */   private static final int SHIFT_6 = 48;
/*     */ 
/*     */   
/*     */   private static final int HUNDRED_NANOS_PER_MILLI = 10000;
/*     */ 
/*     */   
/*     */   static {
/*     */     long[] sequences;
/*     */   }
/*     */ 
/*     */   
/*     */   public static UUID getTimeBasedUuid() {
/* 177 */     long time = System.currentTimeMillis() * 10000L + 122192928000000000L + (count.incrementAndGet() % 10000);
/*     */     
/* 179 */     long timeLow = (time & 0xFFFFFFFFL) << 32L;
/* 180 */     long timeMid = (time & 0xFFFF00000000L) >> 16L;
/* 181 */     long timeHi = (time & 0xFFF000000000000L) >> 48L;
/* 182 */     long most = timeLow | timeMid | 0x1000L | timeHi;
/* 183 */     return new UUID(most, least);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\UuidUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */