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
/*     */ public class Caverphone1
/*     */   extends AbstractCaverphone
/*     */ {
/*     */   private static final String SIX_1 = "111111";
/*     */   
/*     */   public String encode(String source) {
/*  46 */     String txt = source;
/*  47 */     if (txt == null || txt.length() == 0) {
/*  48 */       return "111111";
/*     */     }
/*     */ 
/*     */     
/*  52 */     txt = txt.toLowerCase(Locale.ENGLISH);
/*     */ 
/*     */     
/*  55 */     txt = txt.replaceAll("[^a-z]", "");
/*     */ 
/*     */ 
/*     */     
/*  59 */     txt = txt.replaceAll("^cough", "cou2f");
/*  60 */     txt = txt.replaceAll("^rough", "rou2f");
/*  61 */     txt = txt.replaceAll("^tough", "tou2f");
/*  62 */     txt = txt.replaceAll("^enough", "enou2f");
/*  63 */     txt = txt.replaceAll("^gn", "2n");
/*     */ 
/*     */     
/*  66 */     txt = txt.replaceAll("mb$", "m2");
/*     */ 
/*     */     
/*  69 */     txt = txt.replaceAll("cq", "2q");
/*  70 */     txt = txt.replaceAll("ci", "si");
/*  71 */     txt = txt.replaceAll("ce", "se");
/*  72 */     txt = txt.replaceAll("cy", "sy");
/*  73 */     txt = txt.replaceAll("tch", "2ch");
/*  74 */     txt = txt.replaceAll("c", "k");
/*  75 */     txt = txt.replaceAll("q", "k");
/*  76 */     txt = txt.replaceAll("x", "k");
/*  77 */     txt = txt.replaceAll("v", "f");
/*  78 */     txt = txt.replaceAll("dg", "2g");
/*  79 */     txt = txt.replaceAll("tio", "sio");
/*  80 */     txt = txt.replaceAll("tia", "sia");
/*  81 */     txt = txt.replaceAll("d", "t");
/*  82 */     txt = txt.replaceAll("ph", "fh");
/*  83 */     txt = txt.replaceAll("b", "p");
/*  84 */     txt = txt.replaceAll("sh", "s2");
/*  85 */     txt = txt.replaceAll("z", "s");
/*  86 */     txt = txt.replaceAll("^[aeiou]", "A");
/*     */     
/*  88 */     txt = txt.replaceAll("[aeiou]", "3");
/*  89 */     txt = txt.replaceAll("3gh3", "3kh3");
/*  90 */     txt = txt.replaceAll("gh", "22");
/*  91 */     txt = txt.replaceAll("g", "k");
/*  92 */     txt = txt.replaceAll("s+", "S");
/*  93 */     txt = txt.replaceAll("t+", "T");
/*  94 */     txt = txt.replaceAll("p+", "P");
/*  95 */     txt = txt.replaceAll("k+", "K");
/*  96 */     txt = txt.replaceAll("f+", "F");
/*  97 */     txt = txt.replaceAll("m+", "M");
/*  98 */     txt = txt.replaceAll("n+", "N");
/*  99 */     txt = txt.replaceAll("w3", "W3");
/* 100 */     txt = txt.replaceAll("wy", "Wy");
/* 101 */     txt = txt.replaceAll("wh3", "Wh3");
/* 102 */     txt = txt.replaceAll("why", "Why");
/* 103 */     txt = txt.replaceAll("w", "2");
/* 104 */     txt = txt.replaceAll("^h", "A");
/* 105 */     txt = txt.replaceAll("h", "2");
/* 106 */     txt = txt.replaceAll("r3", "R3");
/* 107 */     txt = txt.replaceAll("ry", "Ry");
/* 108 */     txt = txt.replaceAll("r", "2");
/* 109 */     txt = txt.replaceAll("l3", "L3");
/* 110 */     txt = txt.replaceAll("ly", "Ly");
/* 111 */     txt = txt.replaceAll("l", "2");
/* 112 */     txt = txt.replaceAll("j", "y");
/* 113 */     txt = txt.replaceAll("y3", "Y3");
/* 114 */     txt = txt.replaceAll("y", "2");
/*     */ 
/*     */     
/* 117 */     txt = txt.replaceAll("2", "");
/* 118 */     txt = txt.replaceAll("3", "");
/*     */ 
/*     */     
/* 121 */     txt = txt + "111111";
/*     */ 
/*     */     
/* 124 */     return txt.substring(0, "111111".length());
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\language\Caverphone1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */