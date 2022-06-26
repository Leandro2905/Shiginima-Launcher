/*     */ package joptsimple;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OptionSpecBuilder
/*     */   extends NoArgumentOptionSpec
/*     */ {
/*     */   private final OptionParser parser;
/*     */   
/*     */   OptionSpecBuilder(OptionParser parser, Collection<String> options, String description) {
/*  64 */     super(options, description);
/*     */     
/*  66 */     this.parser = parser;
/*  67 */     attachToParser();
/*     */   }
/*     */   
/*     */   private void attachToParser() {
/*  71 */     this.parser.recognize(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArgumentAcceptingOptionSpec<String> withRequiredArg() {
/*  80 */     ArgumentAcceptingOptionSpec<String> newSpec = new RequiredArgumentOptionSpec<String>(options(), description());
/*     */     
/*  82 */     this.parser.recognize(newSpec);
/*     */     
/*  84 */     return newSpec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArgumentAcceptingOptionSpec<String> withOptionalArg() {
/*  93 */     ArgumentAcceptingOptionSpec<String> newSpec = new OptionalArgumentOptionSpec<String>(options(), description());
/*     */     
/*  95 */     this.parser.recognize(newSpec);
/*     */     
/*  97 */     return newSpec;
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
/*     */   public OptionSpecBuilder requiredIf(String dependent, String... otherDependents) {
/* 110 */     List<String> dependents = new ArrayList<String>();
/* 111 */     dependents.add(dependent);
/* 112 */     Collections.addAll(dependents, otherDependents);
/*     */     
/* 114 */     for (String each : dependents) {
/* 115 */       if (!this.parser.isRecognized(each)) {
/* 116 */         throw new UnconfiguredOptionException(each);
/*     */       }
/* 118 */       this.parser.requiredIf(options(), dependent);
/*     */     } 
/*     */     
/* 121 */     return this;
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
/*     */   public OptionSpecBuilder requiredIf(OptionSpec<?> dependent, OptionSpec<?>... otherDependents) {
/* 135 */     this.parser.requiredIf(options(), dependent);
/* 136 */     for (OptionSpec<?> each : otherDependents) {
/* 137 */       this.parser.requiredIf(options(), each);
/*     */     }
/* 139 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\OptionSpecBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */