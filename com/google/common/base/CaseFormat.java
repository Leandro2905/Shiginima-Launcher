/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public enum CaseFormat
/*     */ {
/*  40 */   LOWER_HYPHEN(CharMatcher.is('-'), "-") {
/*     */     String normalizeWord(String word) {
/*  42 */       return Ascii.toLowerCase(word);
/*     */     }
/*     */     String convert(CaseFormat format, String s) {
/*  45 */       if (format == LOWER_UNDERSCORE) {
/*  46 */         return s.replace('-', '_');
/*     */       }
/*  48 */       if (format == UPPER_UNDERSCORE) {
/*  49 */         return Ascii.toUpperCase(s.replace('-', '_'));
/*     */       }
/*  51 */       return super.convert(format, s);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   LOWER_UNDERSCORE(CharMatcher.is('_'), "_") {
/*     */     String normalizeWord(String word) {
/*  60 */       return Ascii.toLowerCase(word);
/*     */     }
/*     */     String convert(CaseFormat format, String s) {
/*  63 */       if (format == LOWER_HYPHEN) {
/*  64 */         return s.replace('_', '-');
/*     */       }
/*  66 */       if (format == UPPER_UNDERSCORE) {
/*  67 */         return Ascii.toUpperCase(s);
/*     */       }
/*  69 */       return super.convert(format, s);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
/*     */     String normalizeWord(String word) {
/*  78 */       return firstCharOnlyToUpper(word);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
/*     */     String normalizeWord(String word) {
/*  87 */       return firstCharOnlyToUpper(word);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   UPPER_UNDERSCORE(CharMatcher.is('_'), "_") {
/*     */     String normalizeWord(String word) {
/*  96 */       return Ascii.toUpperCase(word);
/*     */     }
/*     */     String convert(CaseFormat format, String s) {
/*  99 */       if (format == LOWER_HYPHEN) {
/* 100 */         return Ascii.toLowerCase(s.replace('_', '-'));
/*     */       }
/* 102 */       if (format == LOWER_UNDERSCORE) {
/* 103 */         return Ascii.toLowerCase(s);
/*     */       }
/* 105 */       return super.convert(format, s);
/*     */     }
/*     */   };
/*     */   
/*     */   private final CharMatcher wordBoundary;
/*     */   private final String wordSeparator;
/*     */   
/*     */   CaseFormat(CharMatcher wordBoundary, String wordSeparator) {
/* 113 */     this.wordBoundary = wordBoundary;
/* 114 */     this.wordSeparator = wordSeparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String to(CaseFormat format, String str) {
/* 123 */     Preconditions.checkNotNull(format);
/* 124 */     Preconditions.checkNotNull(str);
/* 125 */     return (format == this) ? str : convert(format, str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String convert(CaseFormat format, String s) {
/* 133 */     StringBuilder out = null;
/* 134 */     int i = 0;
/* 135 */     int j = -1;
/* 136 */     while ((j = this.wordBoundary.indexIn(s, ++j)) != -1) {
/* 137 */       if (i == 0) {
/*     */         
/* 139 */         out = new StringBuilder(s.length() + 4 * this.wordSeparator.length());
/* 140 */         out.append(format.normalizeFirstWord(s.substring(i, j)));
/*     */       } else {
/* 142 */         out.append(format.normalizeWord(s.substring(i, j)));
/*     */       } 
/* 144 */       out.append(format.wordSeparator);
/* 145 */       i = j + this.wordSeparator.length();
/*     */     } 
/* 147 */     return (i == 0) ? format.normalizeFirstWord(s) : out.append(format.normalizeWord(s.substring(i))).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public Converter<String, String> converterTo(CaseFormat targetFormat) {
/* 159 */     return new StringConverter(this, targetFormat);
/*     */   }
/*     */   
/*     */   private static final class StringConverter
/*     */     extends Converter<String, String> implements Serializable {
/*     */     private final CaseFormat sourceFormat;
/*     */     private final CaseFormat targetFormat;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     StringConverter(CaseFormat sourceFormat, CaseFormat targetFormat) {
/* 169 */       this.sourceFormat = Preconditions.<CaseFormat>checkNotNull(sourceFormat);
/* 170 */       this.targetFormat = Preconditions.<CaseFormat>checkNotNull(targetFormat);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doForward(String s) {
/* 175 */       return (s == null) ? null : this.sourceFormat.to(this.targetFormat, s);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(String s) {
/* 180 */       return (s == null) ? null : this.targetFormat.to(this.sourceFormat, s);
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 184 */       if (object instanceof StringConverter) {
/* 185 */         StringConverter that = (StringConverter)object;
/* 186 */         return (this.sourceFormat.equals(that.sourceFormat) && this.targetFormat.equals(that.targetFormat));
/*     */       } 
/*     */       
/* 189 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 193 */       return this.sourceFormat.hashCode() ^ this.targetFormat.hashCode();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 197 */       String str1 = String.valueOf(String.valueOf(this.sourceFormat)), str2 = String.valueOf(String.valueOf(this.targetFormat)); return (new StringBuilder(14 + str1.length() + str2.length())).append(str1).append(".converterTo(").append(str2).append(")").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String normalizeFirstWord(String word) {
/* 206 */     return (this == LOWER_CAMEL) ? Ascii.toLowerCase(word) : normalizeWord(word);
/*     */   }
/*     */   
/*     */   private static String firstCharOnlyToUpper(String word) {
/* 210 */     return word.isEmpty() ? word : (new StringBuilder(word.length())).append(Ascii.toUpperCase(word.charAt(0))).append(Ascii.toLowerCase(word.substring(1))).toString();
/*     */   }
/*     */   
/*     */   abstract String normalizeWord(String paramString);
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\CaseFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */