/*     */ package joptsimple;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import joptsimple.internal.Objects;
/*     */ import joptsimple.internal.Reflection;
/*     */ import joptsimple.internal.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ArgumentAcceptingOptionSpec<V>
/*     */   extends AbstractOptionSpec<V>
/*     */ {
/*     */   private static final char NIL_VALUE_SEPARATOR = '\000';
/*     */   private boolean optionRequired;
/*     */   private final boolean argumentRequired;
/*     */   private ValueConverter<V> converter;
/*  64 */   private String argumentDescription = "";
/*  65 */   private String valueSeparator = String.valueOf(false);
/*  66 */   private final List<V> defaultValues = new ArrayList<V>();
/*     */   
/*     */   ArgumentAcceptingOptionSpec(String option, boolean argumentRequired) {
/*  69 */     super(option);
/*     */     
/*  71 */     this.argumentRequired = argumentRequired;
/*     */   }
/*     */   
/*     */   ArgumentAcceptingOptionSpec(Collection<String> options, boolean argumentRequired, String description) {
/*  75 */     super(options, description);
/*     */     
/*  77 */     this.argumentRequired = argumentRequired;
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
/*     */   public final <T> ArgumentAcceptingOptionSpec<T> ofType(Class<T> argumentType) {
/* 106 */     return withValuesConvertedBy(Reflection.findConverter(argumentType));
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
/*     */   public final <T> ArgumentAcceptingOptionSpec<T> withValuesConvertedBy(ValueConverter<T> aConverter) {
/* 123 */     if (aConverter == null) {
/* 124 */       throw new NullPointerException("illegal null converter");
/*     */     }
/* 126 */     this.converter = aConverter;
/* 127 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ArgumentAcceptingOptionSpec<V> describedAs(String description) {
/* 138 */     this.argumentDescription = description;
/* 139 */     return this;
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
/*     */   public final ArgumentAcceptingOptionSpec<V> withValuesSeparatedBy(char separator) {
/* 164 */     if (separator == '\000') {
/* 165 */       throw new IllegalArgumentException("cannot use U+0000 as separator");
/*     */     }
/* 167 */     this.valueSeparator = String.valueOf(separator);
/* 168 */     return this;
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
/*     */   public final ArgumentAcceptingOptionSpec<V> withValuesSeparatedBy(String separator) {
/* 193 */     if (separator.indexOf(false) != -1) {
/* 194 */       throw new IllegalArgumentException("cannot use U+0000 in separator");
/*     */     }
/* 196 */     this.valueSeparator = separator;
/* 197 */     return this;
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
/*     */   public ArgumentAcceptingOptionSpec<V> defaultsTo(V value, V... values) {
/* 210 */     addDefaultValue(value);
/* 211 */     defaultsTo(values);
/*     */     
/* 213 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArgumentAcceptingOptionSpec<V> defaultsTo(V[] values) {
/* 224 */     for (V each : values) {
/* 225 */       addDefaultValue(each);
/*     */     }
/* 227 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArgumentAcceptingOptionSpec<V> required() {
/* 238 */     this.optionRequired = true;
/* 239 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/* 243 */     return this.optionRequired;
/*     */   }
/*     */   
/*     */   private void addDefaultValue(V value) {
/* 247 */     Objects.ensureNotNull(value);
/* 248 */     this.defaultValues.add(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void handleOption(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions, String detectedArgument) {
/* 255 */     if (Strings.isNullOrEmpty(detectedArgument)) {
/* 256 */       detectOptionArgument(parser, arguments, detectedOptions);
/*     */     } else {
/* 258 */       addArguments(detectedOptions, detectedArgument);
/*     */     } 
/*     */   }
/*     */   protected void addArguments(OptionSet detectedOptions, String detectedArgument) {
/* 262 */     StringTokenizer lexer = new StringTokenizer(detectedArgument, this.valueSeparator);
/* 263 */     if (!lexer.hasMoreTokens()) {
/* 264 */       detectedOptions.addWithArgument(this, detectedArgument);
/*     */     } else {
/* 266 */       while (lexer.hasMoreTokens()) {
/* 267 */         detectedOptions.addWithArgument(this, lexer.nextToken());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final V convert(String argument) {
/* 276 */     return convertWith(this.converter, argument);
/*     */   }
/*     */   
/*     */   protected boolean canConvertArgument(String argument) {
/* 280 */     StringTokenizer lexer = new StringTokenizer(argument, this.valueSeparator);
/*     */     
/*     */     try {
/* 283 */       while (lexer.hasMoreTokens())
/* 284 */         convert(lexer.nextToken()); 
/* 285 */       return true;
/*     */     }
/* 287 */     catch (OptionException ignored) {
/* 288 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isArgumentOfNumberType() {
/* 293 */     return (this.converter != null && Number.class.isAssignableFrom(this.converter.valueType()));
/*     */   }
/*     */   
/*     */   public boolean acceptsArguments() {
/* 297 */     return true;
/*     */   }
/*     */   
/*     */   public boolean requiresArgument() {
/* 301 */     return this.argumentRequired;
/*     */   }
/*     */   
/*     */   public String argumentDescription() {
/* 305 */     return this.argumentDescription;
/*     */   }
/*     */   
/*     */   public String argumentTypeIndicator() {
/* 309 */     return argumentTypeIndicatorFrom(this.converter);
/*     */   }
/*     */   
/*     */   public List<V> defaultValues() {
/* 313 */     return Collections.unmodifiableList(this.defaultValues);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object that) {
/* 318 */     if (!super.equals(that)) {
/* 319 */       return false;
/*     */     }
/* 321 */     ArgumentAcceptingOptionSpec<?> other = (ArgumentAcceptingOptionSpec)that;
/* 322 */     return (requiresArgument() == other.requiresArgument());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 327 */     return super.hashCode() ^ (this.argumentRequired ? 0 : 1);
/*     */   }
/*     */   
/*     */   protected abstract void detectOptionArgument(OptionParser paramOptionParser, ArgumentList paramArgumentList, OptionSet paramOptionSet);
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\ArgumentAcceptingOptionSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */