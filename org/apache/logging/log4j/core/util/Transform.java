/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Transform
/*     */ {
/*     */   private static final String CDATA_START = "<![CDATA[";
/*     */   private static final String CDATA_END = "]]>";
/*     */   private static final String CDATA_PSEUDO_END = "]]&gt;";
/*     */   private static final String CDATA_EMBEDED_END = "]]>]]&gt;<![CDATA[";
/*  31 */   private static final int CDATA_END_LEN = "]]>".length();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escapeHtmlTags(String input) {
/*  49 */     if (Strings.isEmpty(input) || (input.indexOf('"') == -1 && input.indexOf('&') == -1 && input.indexOf('<') == -1 && input.indexOf('>') == -1))
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*  54 */       return input;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     StringBuilder buf = new StringBuilder(input.length() + 6);
/*  61 */     char ch = ' ';
/*     */     
/*  63 */     int len = input.length();
/*  64 */     for (int i = 0; i < len; i++) {
/*  65 */       ch = input.charAt(i);
/*  66 */       if (ch > '>') {
/*  67 */         buf.append(ch);
/*  68 */       } else if (ch == '<') {
/*  69 */         buf.append("&lt;");
/*  70 */       } else if (ch == '>') {
/*  71 */         buf.append("&gt;");
/*  72 */       } else if (ch == '&') {
/*  73 */         buf.append("&amp;");
/*  74 */       } else if (ch == '"') {
/*  75 */         buf.append("&quot;");
/*     */       } else {
/*  77 */         buf.append(ch);
/*     */       } 
/*     */     } 
/*  80 */     return buf.toString();
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
/*     */   public static void appendEscapingCData(StringBuilder buf, String str) {
/*  93 */     if (str != null) {
/*  94 */       int end = str.indexOf("]]>");
/*  95 */       if (end < 0) {
/*  96 */         buf.append(str);
/*     */       } else {
/*  98 */         int start = 0;
/*  99 */         while (end > -1) {
/* 100 */           buf.append(str.substring(start, end));
/* 101 */           buf.append("]]>]]&gt;<![CDATA[");
/* 102 */           start = end + CDATA_END_LEN;
/* 103 */           if (start < str.length()) {
/* 104 */             end = str.indexOf("]]>", start);
/*     */             continue;
/*     */           } 
/*     */           return;
/*     */         } 
/* 109 */         buf.append(str.substring(start));
/*     */       } 
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
/*     */   public static String escapeJsonControlCharacters(String input) {
/* 127 */     if (Strings.isEmpty(input) || (input.indexOf('"') == -1 && input.indexOf('\\') == -1 && input.indexOf('/') == -1 && input.indexOf('\b') == -1 && input.indexOf('\f') == -1 && input.indexOf('\n') == -1 && input.indexOf('\r') == -1 && input.indexOf('\t') == -1))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 136 */       return input;
/*     */     }
/*     */     
/* 139 */     StringBuilder buf = new StringBuilder(input.length() + 6);
/*     */     
/* 141 */     int len = input.length();
/* 142 */     for (int i = 0; i < len; i++) {
/* 143 */       char ch = input.charAt(i);
/* 144 */       String escBs = "\\";
/* 145 */       switch (ch) {
/*     */         case '"':
/* 147 */           buf.append("\\");
/* 148 */           buf.append(ch);
/*     */           break;
/*     */         case '\\':
/* 151 */           buf.append("\\");
/* 152 */           buf.append(ch);
/*     */           break;
/*     */         case '/':
/* 155 */           buf.append("\\");
/* 156 */           buf.append(ch);
/*     */           break;
/*     */         case '\b':
/* 159 */           buf.append("\\");
/* 160 */           buf.append('b');
/*     */           break;
/*     */         case '\f':
/* 163 */           buf.append("\\");
/* 164 */           buf.append('f');
/*     */           break;
/*     */         case '\n':
/* 167 */           buf.append("\\");
/* 168 */           buf.append('n');
/*     */           break;
/*     */         case '\r':
/* 171 */           buf.append("\\");
/* 172 */           buf.append('r');
/*     */           break;
/*     */         case '\t':
/* 175 */           buf.append("\\");
/* 176 */           buf.append('t');
/*     */           break;
/*     */         default:
/* 179 */           buf.append(ch); break;
/*     */       } 
/*     */     } 
/* 182 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\Transform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */