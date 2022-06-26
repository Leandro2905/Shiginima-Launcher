/*     */ package joptsimple;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class OptionSpecTokenizer
/*     */ {
/*     */   private static final char POSIXLY_CORRECT_MARKER = '+';
/*     */   private static final char HELP_MARKER = '*';
/*     */   private String specification;
/*     */   private int index;
/*     */   
/*     */   OptionSpecTokenizer(String specification) {
/*  45 */     if (specification == null) {
/*  46 */       throw new NullPointerException("null option specification");
/*     */     }
/*  48 */     this.specification = specification;
/*     */   }
/*     */   
/*     */   boolean hasMore() {
/*  52 */     return (this.index < this.specification.length());
/*     */   }
/*     */   AbstractOptionSpec<?> next() {
/*     */     AbstractOptionSpec<?> spec;
/*  56 */     if (!hasMore()) {
/*  57 */       throw new NoSuchElementException();
/*     */     }
/*     */     
/*  60 */     String optionCandidate = String.valueOf(this.specification.charAt(this.index));
/*  61 */     this.index++;
/*     */ 
/*     */     
/*  64 */     if ("W".equals(optionCandidate)) {
/*  65 */       spec = handleReservedForExtensionsToken();
/*     */       
/*  67 */       if (spec != null) {
/*  68 */         return spec;
/*     */       }
/*     */     } 
/*  71 */     ParserRules.ensureLegalOption(optionCandidate);
/*     */     
/*  73 */     if (hasMore()) {
/*  74 */       boolean forHelp = false;
/*  75 */       if (this.specification.charAt(this.index) == '*') {
/*  76 */         forHelp = true;
/*  77 */         this.index++;
/*     */       } 
/*  79 */       spec = (hasMore() && this.specification.charAt(this.index) == ':') ? handleArgumentAcceptingOption(optionCandidate) : new NoArgumentOptionSpec(optionCandidate);
/*     */ 
/*     */       
/*  82 */       if (forHelp)
/*  83 */         spec.forHelp(); 
/*     */     } else {
/*  85 */       spec = new NoArgumentOptionSpec(optionCandidate);
/*     */     } 
/*  87 */     return spec;
/*     */   }
/*     */   
/*     */   void configure(OptionParser parser) {
/*  91 */     adjustForPosixlyCorrect(parser);
/*     */     
/*  93 */     while (hasMore())
/*  94 */       parser.recognize(next()); 
/*     */   }
/*     */   
/*     */   private void adjustForPosixlyCorrect(OptionParser parser) {
/*  98 */     if ('+' == this.specification.charAt(0)) {
/*  99 */       parser.posixlyCorrect(true);
/* 100 */       this.specification = this.specification.substring(1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private AbstractOptionSpec<?> handleReservedForExtensionsToken() {
/* 105 */     if (!hasMore()) {
/* 106 */       return new NoArgumentOptionSpec("W");
/*     */     }
/* 108 */     if (this.specification.charAt(this.index) == ';') {
/* 109 */       this.index++;
/* 110 */       return new AlternativeLongOptionSpec();
/*     */     } 
/*     */     
/* 113 */     return null;
/*     */   }
/*     */   
/*     */   private AbstractOptionSpec<?> handleArgumentAcceptingOption(String candidate) {
/* 117 */     this.index++;
/*     */     
/* 119 */     if (hasMore() && this.specification.charAt(this.index) == ':') {
/* 120 */       this.index++;
/* 121 */       return new OptionalArgumentOptionSpec(candidate);
/*     */     } 
/*     */     
/* 124 */     return new RequiredArgumentOptionSpec(candidate);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\OptionSpecTokenizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */