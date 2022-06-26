/*     */ package joptsimple;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import joptsimple.internal.Reflection;
/*     */ import joptsimple.internal.ReflectionException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractOptionSpec<V>
/*     */   implements OptionSpec<V>, OptionDescriptor
/*     */ {
/*  44 */   private final List<String> options = new ArrayList<String>();
/*     */   private final String description;
/*     */   private boolean forHelp;
/*     */   
/*     */   protected AbstractOptionSpec(String option) {
/*  49 */     this(Collections.singletonList(option), "");
/*     */   }
/*     */   
/*     */   protected AbstractOptionSpec(Collection<String> options, String description) {
/*  53 */     arrangeOptions(options);
/*     */     
/*  55 */     this.description = description;
/*     */   }
/*     */   
/*     */   public final Collection<String> options() {
/*  59 */     return Collections.unmodifiableList(this.options);
/*     */   }
/*     */   
/*     */   public final List<V> values(OptionSet detectedOptions) {
/*  63 */     return detectedOptions.valuesOf(this);
/*     */   }
/*     */   
/*     */   public final V value(OptionSet detectedOptions) {
/*  67 */     return detectedOptions.valueOf(this);
/*     */   }
/*     */   
/*     */   public String description() {
/*  71 */     return this.description;
/*     */   }
/*     */   
/*     */   public final AbstractOptionSpec<V> forHelp() {
/*  75 */     this.forHelp = true;
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public final boolean isForHelp() {
/*  80 */     return this.forHelp;
/*     */   }
/*     */   
/*     */   public boolean representsNonOptions() {
/*  84 */     return false;
/*     */   }
/*     */   
/*     */   protected abstract V convert(String paramString);
/*     */   
/*     */   protected V convertWith(ValueConverter<V> converter, String argument) {
/*     */     try {
/*  91 */       return (V)Reflection.convertWith(converter, argument);
/*     */     }
/*  93 */     catch (ReflectionException ex) {
/*  94 */       throw new OptionArgumentConversionException(options(), argument, converter.valueType(), ex);
/*     */     }
/*  96 */     catch (ValueConversionException ex) {
/*  97 */       throw new OptionArgumentConversionException(options(), argument, converter.valueType(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String argumentTypeIndicatorFrom(ValueConverter<V> converter) {
/* 102 */     if (converter == null) {
/* 103 */       return null;
/*     */     }
/* 105 */     String pattern = converter.valuePattern();
/* 106 */     return (pattern == null) ? converter.valueType().getName() : pattern;
/*     */   }
/*     */ 
/*     */   
/*     */   abstract void handleOption(OptionParser paramOptionParser, ArgumentList paramArgumentList, OptionSet paramOptionSet, String paramString);
/*     */   
/*     */   private void arrangeOptions(Collection<String> unarranged) {
/* 113 */     if (unarranged.size() == 1) {
/* 114 */       this.options.addAll(unarranged);
/*     */       
/*     */       return;
/*     */     } 
/* 118 */     List<String> shortOptions = new ArrayList<String>();
/* 119 */     List<String> longOptions = new ArrayList<String>();
/*     */     
/* 121 */     for (String each : unarranged) {
/* 122 */       if (each.length() == 1) {
/* 123 */         shortOptions.add(each); continue;
/*     */       } 
/* 125 */       longOptions.add(each);
/*     */     } 
/*     */     
/* 128 */     Collections.sort(shortOptions);
/* 129 */     Collections.sort(longOptions);
/*     */     
/* 131 */     this.options.addAll(shortOptions);
/* 132 */     this.options.addAll(longOptions);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object that) {
/* 137 */     if (!(that instanceof AbstractOptionSpec)) {
/* 138 */       return false;
/*     */     }
/* 140 */     AbstractOptionSpec<?> other = (AbstractOptionSpec)that;
/* 141 */     return this.options.equals(other.options);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 146 */     return this.options.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 151 */     return this.options.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\AbstractOptionSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */