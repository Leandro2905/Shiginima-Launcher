/*     */ package joptsimple;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import joptsimple.internal.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OptionSet
/*     */ {
/*     */   private final List<OptionSpec<?>> detectedSpecs;
/*     */   private final Map<String, AbstractOptionSpec<?>> detectedOptions;
/*     */   private final Map<AbstractOptionSpec<?>, List<String>> optionsToArguments;
/*     */   private final Map<String, List<?>> defaultValues;
/*     */   
/*     */   OptionSet(Map<String, List<?>> defaults) {
/*  53 */     this.detectedSpecs = new ArrayList<OptionSpec<?>>();
/*  54 */     this.detectedOptions = new HashMap<String, AbstractOptionSpec<?>>();
/*  55 */     this.optionsToArguments = new IdentityHashMap<AbstractOptionSpec<?>, List<String>>();
/*  56 */     this.defaultValues = new HashMap<String, List<?>>(defaults);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasOptions() {
/*  65 */     return !this.detectedOptions.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean has(String option) {
/*  76 */     return this.detectedOptions.containsKey(option);
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
/*     */   public boolean has(OptionSpec<?> option) {
/*  93 */     return this.optionsToArguments.containsKey(option);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasArgument(String option) {
/* 104 */     AbstractOptionSpec<?> spec = this.detectedOptions.get(option);
/* 105 */     return (spec != null && hasArgument(spec));
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
/*     */   public boolean hasArgument(OptionSpec<?> option) {
/* 123 */     Objects.ensureNotNull(option);
/*     */     
/* 125 */     List<String> values = this.optionsToArguments.get(option);
/* 126 */     return (values != null && !values.isEmpty());
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
/*     */   public Object valueOf(String option) {
/* 144 */     Objects.ensureNotNull(option);
/*     */     
/* 146 */     AbstractOptionSpec<?> spec = this.detectedOptions.get(option);
/* 147 */     if (spec == null) {
/* 148 */       List<?> defaults = defaultValuesFor(option);
/* 149 */       return defaults.isEmpty() ? null : defaults.get(0);
/*     */     } 
/*     */     
/* 152 */     return valueOf(spec);
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
/*     */   public <V> V valueOf(OptionSpec<V> option) {
/* 169 */     Objects.ensureNotNull(option);
/*     */     
/* 171 */     List<V> values = valuesOf(option);
/* 172 */     switch (values.size()) {
/*     */       case 0:
/* 174 */         return null;
/*     */       case 1:
/* 176 */         return values.get(0);
/*     */     } 
/* 178 */     throw new MultipleArgumentsForOptionException(option.options());
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
/*     */   public List<?> valuesOf(String option) {
/* 192 */     Objects.ensureNotNull(option);
/*     */     
/* 194 */     AbstractOptionSpec<?> spec = this.detectedOptions.get(option);
/* 195 */     return (spec == null) ? defaultValuesFor(option) : valuesOf(spec);
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
/*     */   public <V> List<V> valuesOf(OptionSpec<V> option) {
/* 213 */     Objects.ensureNotNull(option);
/*     */     
/* 215 */     List<String> values = this.optionsToArguments.get(option);
/* 216 */     if (values == null || values.isEmpty()) {
/* 217 */       return defaultValueFor(option);
/*     */     }
/* 219 */     AbstractOptionSpec<V> spec = (AbstractOptionSpec<V>)option;
/* 220 */     List<V> convertedValues = new ArrayList<V>();
/* 221 */     for (String each : values) {
/* 222 */       convertedValues.add(spec.convert(each));
/*     */     }
/* 224 */     return Collections.unmodifiableList(convertedValues);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<OptionSpec<?>> specs() {
/* 234 */     List<OptionSpec<?>> specs = this.detectedSpecs;
/* 235 */     specs.remove(this.detectedOptions.get("[arguments]"));
/*     */     
/* 237 */     return Collections.unmodifiableList(specs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<?> nonOptionArguments() {
/* 244 */     return Collections.unmodifiableList(valuesOf(this.detectedOptions.get("[arguments]")));
/*     */   }
/*     */   
/*     */   void add(AbstractOptionSpec<?> spec) {
/* 248 */     addWithArgument(spec, null);
/*     */   }
/*     */   
/*     */   void addWithArgument(AbstractOptionSpec<?> spec, String argument) {
/* 252 */     this.detectedSpecs.add(spec);
/*     */     
/* 254 */     for (String each : spec.options()) {
/* 255 */       this.detectedOptions.put(each, spec);
/*     */     }
/* 257 */     List<String> optionArguments = this.optionsToArguments.get(spec);
/*     */     
/* 259 */     if (optionArguments == null) {
/* 260 */       optionArguments = new ArrayList<String>();
/* 261 */       this.optionsToArguments.put(spec, optionArguments);
/*     */     } 
/*     */     
/* 264 */     if (argument != null) {
/* 265 */       optionArguments.add(argument);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean equals(Object that) {
/* 270 */     if (this == that) {
/* 271 */       return true;
/*     */     }
/* 273 */     if (that == null || !getClass().equals(that.getClass())) {
/* 274 */       return false;
/*     */     }
/* 276 */     OptionSet other = (OptionSet)that;
/* 277 */     Map<AbstractOptionSpec<?>, List<String>> thisOptionsToArguments = new HashMap<AbstractOptionSpec<?>, List<String>>(this.optionsToArguments);
/*     */     
/* 279 */     Map<AbstractOptionSpec<?>, List<String>> otherOptionsToArguments = new HashMap<AbstractOptionSpec<?>, List<String>>(other.optionsToArguments);
/*     */     
/* 281 */     return (this.detectedOptions.equals(other.detectedOptions) && thisOptionsToArguments.equals(otherOptionsToArguments));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 287 */     Map<AbstractOptionSpec<?>, List<String>> thisOptionsToArguments = new HashMap<AbstractOptionSpec<?>, List<String>>(this.optionsToArguments);
/*     */     
/* 289 */     return this.detectedOptions.hashCode() ^ thisOptionsToArguments.hashCode();
/*     */   }
/*     */   
/*     */   private <V> List<V> defaultValuesFor(String option) {
/* 293 */     if (this.defaultValues.containsKey(option)) {
/* 294 */       return (List<V>)this.defaultValues.get(option);
/*     */     }
/* 296 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   private <V> List<V> defaultValueFor(OptionSpec<V> option) {
/* 300 */     return defaultValuesFor(option.options().iterator().next());
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\OptionSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */