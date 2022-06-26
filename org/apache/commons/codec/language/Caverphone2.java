/*     */ package org.apache.commons.codec.language;
/*     */ 
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Caverphone2
/*     */   extends AbstractCaverphone
/*     */ {
/*     */   private static final String TEN_1 = "1111111111";
/*     */   
/*     */   public String encode(String source) {
/*  46 */     String txt = source;
/*  47 */     if (txt == null || txt.length() == 0) {
/*  48 */       return "1111111111";
/*     */     }
/*     */ 
/*     */     
/*  52 */     txt = txt.toLowerCase(Locale.ENGLISH);
/*     */ 
/*     */     
/*  55 */     txt = txt.replaceAll("[^a-z]", "");
/*     */ 
/*     */     
/*  58 */     txt = txt.replaceAll("e$", "");
/*     */ 
/*     */     
/*  61 */     txt = txt.replaceAll("^cough", "cou2f");
/*  62 */     txt = txt.replaceAll("^rough", "rou2f");
/*  63 */     txt = txt.replaceAll("^tough", "tou2f");
/*  64 */     txt = txt.replaceAll("^enough", "enou2f");
/*  65 */     txt = txt.replaceAll("^trough", "trou2f");
/*     */     
/*  67 */     txt = txt.replaceAll("^gn", "2n");
/*     */ 
/*     */     
/*  70 */     txt = txt.replaceAll("mb$", "m2");
/*     */ 
/*     */     
/*  73 */     txt = txt.replaceAll("cq", "2q");
/*  74 */     txt = txt.replaceAll("ci", "si");
/*  75 */     txt = txt.replaceAll("ce", "se");
/*  76 */     txt = txt.replaceAll("cy", "sy");
/*  77 */     txt = txt.replaceAll("tch", "2ch");
/*  78 */     txt = txt.replaceAll("c", "k");
/*  79 */     txt = txt.replaceAll("q", "k");
/*  80 */     txt = txt.replaceAll("x", "k");
/*  81 */     txt = txt.replaceAll("v", "f");
/*  82 */     txt = txt.replaceAll("dg", "2g");
/*  83 */     txt = txt.replaceAll("tio", "sio");
/*  84 */     txt = txt.replaceAll("tia", "sia");
/*  85 */     txt = txt.replaceAll("d", "t");
/*  86 */     txt = txt.replaceAll("ph", "fh");
/*  87 */     txt = txt.replaceAll("b", "p");
/*  88 */     txt = txt.replaceAll("sh", "s2");
/*  89 */     txt = txt.replaceAll("z", "s");
/*  90 */     txt = txt.replaceAll("^[aeiou]", "A");
/*  91 */     txt = txt.replaceAll("[aeiou]", "3");
/*  92 */     txt = txt.replaceAll("j", "y");
/*  93 */     txt = txt.replaceAll("^y3", "Y3");
/*  94 */     txt = txt.replaceAll("^y", "A");
/*  95 */     txt = txt.replaceAll("y", "3");
/*  96 */     txt = txt.replaceAll("3gh3", "3kh3");
/*  97 */     txt = txt.replaceAll("gh", "22");
/*  98 */     txt = txt.replaceAll("g", "k");
/*  99 */     txt = txt.replaceAll("s+", "S");
/* 100 */     txt = txt.replaceAll("t+", "T");
/* 101 */     txt = txt.replaceAll("p+", "P");
/* 102 */     txt = txt.replaceAll("k+", "K");
/* 103 */     txt = txt.replaceAll("f+", "F");
/* 104 */     txt = txt.replaceAll("m+", "M");
/* 105 */     txt = txt.replaceAll("n+", "N");
/* 106 */     txt = txt.replaceAll("w3", "W3");
/* 107 */     txt = txt.replaceAll("wh3", "Wh3");
/* 108 */     txt = txt.replaceAll("w$", "3");
/* 109 */     txt = txt.replaceAll("w", "2");
/* 110 */     txt = txt.replaceAll("^h", "A");
/* 111 */     txt = txt.replaceAll("h", "2");
/* 112 */     txt = txt.replaceAll("r3", "R3");
/* 113 */     txt = txt.replaceAll("r$", "3");
/* 114 */     txt = txt.replaceAll("r", "2");
/* 115 */     txt = txt.replaceAll("l3", "L3");
/* 116 */     txt = txt.replaceAll("l$", "3");
/* 117 */     txt = txt.replaceAll("l", "2");
/*     */ 
/*     */     
/* 120 */     txt = txt.replaceAll("2", "");
/* 121 */     txt = txt.replaceAll("3$", "A");
/* 122 */     txt = txt.replaceAll("3", "");
/*     */ 
/*     */     
/* 125 */     txt = txt + "1111111111";
/*     */ 
/*     */     
/* 128 */     return txt.substring(0, "1111111111".length());
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\language\Caverphone2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */