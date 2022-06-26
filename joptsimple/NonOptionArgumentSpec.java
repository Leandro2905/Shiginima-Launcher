/*     */ package joptsimple;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import joptsimple.internal.Reflection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NonOptionArgumentSpec<V>
/*     */   extends AbstractOptionSpec<V>
/*     */ {
/*     */   static final String NAME = "[arguments]";
/*     */   private ValueConverter<V> converter;
/*  57 */   private String argumentDescription = "";
/*     */   
/*     */   NonOptionArgumentSpec() {
/*  60 */     this("");
/*     */   }
/*     */   
/*     */   NonOptionArgumentSpec(String description) {
/*  64 */     super(Arrays.asList(new String[] { "[arguments]" }, ), description);
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
/*     */   public <T> NonOptionArgumentSpec<T> ofType(Class<T> argumentType) {
/*  94 */     this.converter = Reflection.findConverter(argumentType);
/*  95 */     return this;
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
/*     */   public final <T> NonOptionArgumentSpec<T> withValuesConvertedBy(ValueConverter<T> aConverter) {
/* 112 */     if (aConverter == null) {
/* 113 */       throw new NullPointerException("illegal null converter");
/*     */     }
/* 115 */     this.converter = aConverter;
/* 116 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NonOptionArgumentSpec<V> describedAs(String description) {
/* 127 */     this.argumentDescription = description;
/* 128 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final V convert(String argument) {
/* 133 */     return convertWith(this.converter, argument);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void handleOption(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions, String detectedArgument) {
/* 140 */     detectedOptions.addWithArgument(this, detectedArgument);
/*     */   }
/*     */   
/*     */   public List<?> defaultValues() {
/* 144 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/* 148 */     return false;
/*     */   }
/*     */   
/*     */   public boolean acceptsArguments() {
/* 152 */     return false;
/*     */   }
/*     */   
/*     */   public boolean requiresArgument() {
/* 156 */     return false;
/*     */   }
/*     */   
/*     */   public String argumentDescription() {
/* 160 */     return this.argumentDescription;
/*     */   }
/*     */   
/*     */   public String argumentTypeIndicator() {
/* 164 */     return argumentTypeIndicatorFrom(this.converter);
/*     */   }
/*     */   
/*     */   public boolean representsNonOptions() {
/* 168 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\NonOptionArgumentSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */