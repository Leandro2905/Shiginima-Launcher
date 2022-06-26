/*     */ package com.google.common.xml;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.escape.Escaper;
/*     */ import com.google.common.escape.Escapers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public class XmlEscapers
/*     */ {
/*     */   private static final char MIN_ASCII_CONTROL_CHAR = '\000';
/*     */   private static final char MAX_ASCII_CONTROL_CHAR = '\037';
/*     */   private static final Escaper XML_ESCAPER;
/*     */   private static final Escaper XML_CONTENT_ESCAPER;
/*     */   private static final Escaper XML_ATTRIBUTE_ESCAPER;
/*     */   
/*     */   public static Escaper xmlContentEscaper() {
/*  86 */     return XML_CONTENT_ESCAPER;
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
/*     */   public static Escaper xmlAttributeEscaper() {
/* 113 */     return XML_ATTRIBUTE_ESCAPER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 120 */     Escapers.Builder builder = Escapers.builder();
/*     */ 
/*     */ 
/*     */     
/* 124 */     builder.setSafeRange(false, '�');
/*     */     
/* 126 */     builder.setUnsafeReplacement("�");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     for (char c = Character.MIN_VALUE; c <= '\037'; c = (char)(c + 1)) {
/* 139 */       if (c != '\t' && c != '\n' && c != '\r') {
/* 140 */         builder.addEscape(c, "�");
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 146 */     builder.addEscape('&', "&amp;");
/* 147 */     builder.addEscape('<', "&lt;");
/* 148 */     builder.addEscape('>', "&gt;");
/* 149 */     XML_CONTENT_ESCAPER = builder.build();
/* 150 */     builder.addEscape('\'', "&apos;");
/* 151 */     builder.addEscape('"', "&quot;");
/* 152 */     XML_ESCAPER = builder.build();
/* 153 */     builder.addEscape('\t', "&#x9;");
/* 154 */     builder.addEscape('\n', "&#xA;");
/* 155 */     builder.addEscape('\r', "&#xD;");
/* 156 */     XML_ATTRIBUTE_ESCAPER = builder.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\xml\XmlEscapers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */