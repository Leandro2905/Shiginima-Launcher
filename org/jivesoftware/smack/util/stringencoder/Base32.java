/*     */ package org.jivesoftware.smack.util.stringencoder;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Base32
/*     */ {
/*  35 */   private static final StringEncoder base32Stringencoder = new StringEncoder()
/*     */     {
/*     */       public String encode(String string)
/*     */       {
/*  39 */         return Base32.encode(string);
/*     */       }
/*     */ 
/*     */       
/*     */       public String decode(String string) {
/*  44 */         return Base32.decode(string);
/*     */       }
/*     */     };
/*     */   
/*     */   private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ2345678";
/*     */   
/*     */   public static StringEncoder getStringEncoder() {
/*  51 */     return base32Stringencoder;
/*     */   }
/*     */   
/*     */   public static String decode(String str) {
/*  55 */     ByteArrayOutputStream bs = new ByteArrayOutputStream();
/*  56 */     byte[] raw = str.getBytes();
/*  57 */     for (int i = 0; i < raw.length; i++) {
/*  58 */       char c = (char)raw[i];
/*  59 */       if (!Character.isWhitespace(c)) {
/*  60 */         c = Character.toUpperCase(c);
/*  61 */         bs.write((byte)c);
/*     */       } 
/*     */     } 
/*     */     
/*  65 */     while (bs.size() % 8 != 0) {
/*  66 */       bs.write(56);
/*     */     }
/*  68 */     byte[] in = bs.toByteArray();
/*     */     
/*  70 */     bs.reset();
/*  71 */     DataOutputStream ds = new DataOutputStream(bs);
/*     */     
/*  73 */     for (int j = 0; j < in.length / 8; j++) {
/*  74 */       short[] s = new short[8];
/*  75 */       int[] t = new int[5];
/*     */       
/*  77 */       int padlen = 8;
/*  78 */       for (int k = 0; k < 8; k++) {
/*  79 */         char c = (char)in[j * 8 + k];
/*  80 */         if (c == '8')
/*     */           break; 
/*  82 */         s[k] = (short)"ABCDEFGHIJKLMNOPQRSTUVWXYZ2345678".indexOf(in[j * 8 + k]);
/*  83 */         if (s[k] < 0)
/*  84 */           return null; 
/*  85 */         padlen--;
/*     */       } 
/*  87 */       int blocklen = paddingToLen(padlen);
/*  88 */       if (blocklen < 0) {
/*  89 */         return null;
/*     */       }
/*     */       
/*  92 */       t[0] = s[0] << 3 | s[1] >> 2;
/*     */       
/*  94 */       t[1] = (s[1] & 0x3) << 6 | s[2] << 1 | s[3] >> 4;
/*     */       
/*  96 */       t[2] = (s[3] & 0xF) << 4 | s[4] >> 1 & 0xF;
/*     */       
/*  98 */       t[3] = s[4] << 7 | s[5] << 2 | s[6] >> 3;
/*     */       
/* 100 */       t[4] = (s[6] & 0x7) << 5 | s[7];
/*     */       
/*     */       try {
/* 103 */         for (int m = 0; m < blocklen; m++)
/* 104 */           ds.writeByte((byte)(t[m] & 0xFF)); 
/* 105 */       } catch (IOException e) {}
/*     */     } 
/*     */ 
/*     */     
/* 109 */     return new String(bs.toByteArray());
/*     */   }
/*     */   
/*     */   public static String encode(String str) {
/* 113 */     byte[] b = str.getBytes();
/* 114 */     ByteArrayOutputStream os = new ByteArrayOutputStream();
/*     */     
/* 116 */     for (int i = 0; i < (b.length + 4) / 5; i++) {
/* 117 */       short[] s = new short[5];
/* 118 */       int[] t = new int[8];
/*     */       
/* 120 */       int blocklen = 5;
/* 121 */       for (int j = 0; j < 5; j++) {
/* 122 */         if (i * 5 + j < b.length) {
/* 123 */           s[j] = (short)(b[i * 5 + j] & 0xFF);
/*     */         } else {
/* 125 */           s[j] = 0;
/* 126 */           blocklen--;
/*     */         } 
/*     */       } 
/* 129 */       int padlen = lenToPadding(blocklen);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 134 */       t[0] = (byte)(s[0] >> 3 & 0x1F);
/*     */       
/* 136 */       t[1] = (byte)((s[0] & 0x7) << 2 | s[1] >> 6 & 0x3);
/*     */       
/* 138 */       t[2] = (byte)(s[1] >> 1 & 0x1F);
/*     */       
/* 140 */       t[3] = (byte)((s[1] & 0x1) << 4 | s[2] >> 4 & 0xF);
/*     */       
/* 142 */       t[4] = (byte)((s[2] & 0xF) << 1 | s[3] >> 7 & 0x1);
/*     */       
/* 144 */       t[5] = (byte)(s[3] >> 2 & 0x1F);
/*     */       
/* 146 */       t[6] = (byte)((s[3] & 0x3) << 3 | s[4] >> 5 & 0x7);
/*     */       
/* 148 */       t[7] = (byte)(s[4] & 0x1F);
/*     */ 
/*     */       
/* 151 */       for (int k = 0; k < t.length - padlen; k++) {
/* 152 */         char c = "ABCDEFGHIJKLMNOPQRSTUVWXYZ2345678".charAt(t[k]);
/* 153 */         os.write(c);
/*     */       } 
/*     */     } 
/* 156 */     return new String(os.toByteArray());
/*     */   }
/*     */   
/*     */   private static int lenToPadding(int blocklen) {
/* 160 */     switch (blocklen) {
/*     */       case 1:
/* 162 */         return 6;
/*     */       case 2:
/* 164 */         return 4;
/*     */       case 3:
/* 166 */         return 3;
/*     */       case 4:
/* 168 */         return 1;
/*     */       case 5:
/* 170 */         return 0;
/*     */     } 
/* 172 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int paddingToLen(int padlen) {
/* 177 */     switch (padlen) {
/*     */       case 6:
/* 179 */         return 1;
/*     */       case 4:
/* 181 */         return 2;
/*     */       case 3:
/* 183 */         return 3;
/*     */       case 1:
/* 185 */         return 4;
/*     */       case 0:
/* 187 */         return 5;
/*     */     } 
/* 189 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\stringencoder\Base32.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */