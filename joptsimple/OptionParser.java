/*     */ package joptsimple;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import joptsimple.internal.AbbreviationMap;
/*     */ import joptsimple.util.KeyValuePair;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OptionParser
/*     */ {
/*     */   private final AbbreviationMap<AbstractOptionSpec<?>> recognizedOptions;
/*     */   private final Map<Collection<String>, Set<OptionSpec<?>>> requiredIf;
/*     */   private OptionParserState state;
/*     */   private boolean posixlyCorrect;
/*     */   private boolean allowsUnrecognizedOptions;
/* 200 */   private HelpFormatter helpFormatter = new BuiltinHelpFormatter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OptionParser() {
/* 207 */     this.recognizedOptions = new AbbreviationMap();
/* 208 */     this.requiredIf = new HashMap<Collection<String>, Set<OptionSpec<?>>>();
/* 209 */     this.state = OptionParserState.moreOptions(false);
/*     */     
/* 211 */     recognize(new NonOptionArgumentSpec());
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
/*     */   public OptionParser(String optionSpecification) {
/* 225 */     this();
/*     */     
/* 227 */     (new OptionSpecTokenizer(optionSpecification)).configure(this);
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
/*     */   public OptionSpecBuilder accepts(String option) {
/* 250 */     return acceptsAll(Collections.singletonList(option));
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
/*     */   public OptionSpecBuilder accepts(String option, String description) {
/* 265 */     return acceptsAll(Collections.singletonList(option), description);
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
/*     */   public OptionSpecBuilder acceptsAll(Collection<String> options) {
/* 278 */     return acceptsAll(options, "");
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
/*     */   public OptionSpecBuilder acceptsAll(Collection<String> options, String description) {
/* 294 */     if (options.isEmpty()) {
/* 295 */       throw new IllegalArgumentException("need at least one option");
/*     */     }
/* 297 */     ParserRules.ensureLegalOptions(options);
/*     */     
/* 299 */     return new OptionSpecBuilder(this, options, description);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NonOptionArgumentSpec<String> nonOptions() {
/* 310 */     NonOptionArgumentSpec<String> spec = new NonOptionArgumentSpec<String>();
/*     */     
/* 312 */     recognize(spec);
/*     */     
/* 314 */     return spec;
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
/*     */   public NonOptionArgumentSpec<String> nonOptions(String description) {
/* 326 */     NonOptionArgumentSpec<String> spec = new NonOptionArgumentSpec<String>(description);
/*     */     
/* 328 */     recognize(spec);
/*     */     
/* 330 */     return spec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void posixlyCorrect(boolean setting) {
/* 339 */     this.posixlyCorrect = setting;
/* 340 */     this.state = OptionParserState.moreOptions(setting);
/*     */   }
/*     */   
/*     */   boolean posixlyCorrect() {
/* 344 */     return this.posixlyCorrect;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void allowsUnrecognizedOptions() {
/* 354 */     this.allowsUnrecognizedOptions = true;
/*     */   }
/*     */   
/*     */   boolean doesAllowsUnrecognizedOptions() {
/* 358 */     return this.allowsUnrecognizedOptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recognizeAlternativeLongOptions(boolean recognize) {
/* 367 */     if (recognize) {
/* 368 */       recognize(new AlternativeLongOptionSpec());
/*     */     } else {
/* 370 */       this.recognizedOptions.remove(String.valueOf("W"));
/*     */     } 
/*     */   }
/*     */   void recognize(AbstractOptionSpec<?> spec) {
/* 374 */     this.recognizedOptions.putAll(spec.options(), spec);
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
/*     */   public void printHelpOn(OutputStream sink) throws IOException {
/* 388 */     printHelpOn(new OutputStreamWriter(sink));
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
/*     */   public void printHelpOn(Writer sink) throws IOException {
/* 402 */     sink.write(this.helpFormatter.format(this.recognizedOptions.toJavaUtilMap()));
/* 403 */     sink.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void formatHelpWith(HelpFormatter formatter) {
/* 413 */     if (formatter == null) {
/* 414 */       throw new NullPointerException();
/*     */     }
/* 416 */     this.helpFormatter = formatter;
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
/*     */   public OptionSet parse(String... arguments) {
/* 428 */     ArgumentList argumentList = new ArgumentList(arguments);
/* 429 */     OptionSet detected = new OptionSet(defaultValues());
/* 430 */     detected.add((AbstractOptionSpec)this.recognizedOptions.get("[arguments]"));
/*     */     
/* 432 */     while (argumentList.hasMore()) {
/* 433 */       this.state.handleArgument(this, argumentList, detected);
/*     */     }
/* 435 */     reset();
/*     */     
/* 437 */     ensureRequiredOptions(detected);
/*     */     
/* 439 */     return detected;
/*     */   }
/*     */   
/*     */   private void ensureRequiredOptions(OptionSet options) {
/* 443 */     Collection<String> missingRequiredOptions = missingRequiredOptions(options);
/* 444 */     boolean helpOptionPresent = isHelpOptionPresent(options);
/*     */     
/* 446 */     if (!missingRequiredOptions.isEmpty() && !helpOptionPresent)
/* 447 */       throw new MissingRequiredOptionException(missingRequiredOptions); 
/*     */   }
/*     */   
/*     */   private Collection<String> missingRequiredOptions(OptionSet options) {
/* 451 */     Collection<String> missingRequiredOptions = new HashSet<String>();
/*     */     
/* 453 */     for (AbstractOptionSpec<?> each : (Iterable<AbstractOptionSpec<?>>)this.recognizedOptions.toJavaUtilMap().values()) {
/* 454 */       if (each.isRequired() && !options.has(each)) {
/* 455 */         missingRequiredOptions.addAll(each.options());
/*     */       }
/*     */     } 
/* 458 */     for (Map.Entry<Collection<String>, Set<OptionSpec<?>>> eachEntry : this.requiredIf.entrySet()) {
/* 459 */       AbstractOptionSpec<?> required = specFor(((Collection<String>)eachEntry.getKey()).iterator().next());
/*     */       
/* 461 */       if (optionsHasAnyOf(options, eachEntry.getValue()) && !options.has(required)) {
/* 462 */         missingRequiredOptions.addAll(required.options());
/*     */       }
/*     */     } 
/*     */     
/* 466 */     return missingRequiredOptions;
/*     */   }
/*     */   
/*     */   private boolean optionsHasAnyOf(OptionSet options, Collection<OptionSpec<?>> specs) {
/* 470 */     for (OptionSpec<?> each : specs) {
/* 471 */       if (options.has(each)) {
/* 472 */         return true;
/*     */       }
/*     */     } 
/* 475 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isHelpOptionPresent(OptionSet options) {
/* 479 */     boolean helpOptionPresent = false;
/* 480 */     for (AbstractOptionSpec<?> each : (Iterable<AbstractOptionSpec<?>>)this.recognizedOptions.toJavaUtilMap().values()) {
/* 481 */       if (each.isForHelp() && options.has(each)) {
/* 482 */         helpOptionPresent = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 486 */     return helpOptionPresent;
/*     */   }
/*     */   
/*     */   void handleLongOptionToken(String candidate, ArgumentList arguments, OptionSet detected) {
/* 490 */     KeyValuePair optionAndArgument = parseLongOptionWithArgument(candidate);
/*     */     
/* 492 */     if (!isRecognized(optionAndArgument.key)) {
/* 493 */       throw OptionException.unrecognizedOption(optionAndArgument.key);
/*     */     }
/* 495 */     AbstractOptionSpec<?> optionSpec = specFor(optionAndArgument.key);
/* 496 */     optionSpec.handleOption(this, arguments, detected, optionAndArgument.value);
/*     */   }
/*     */   
/*     */   void handleShortOptionToken(String candidate, ArgumentList arguments, OptionSet detected) {
/* 500 */     KeyValuePair optionAndArgument = parseShortOptionWithArgument(candidate);
/*     */     
/* 502 */     if (isRecognized(optionAndArgument.key)) {
/* 503 */       specFor(optionAndArgument.key).handleOption(this, arguments, detected, optionAndArgument.value);
/*     */     } else {
/*     */       
/* 506 */       handleShortOptionCluster(candidate, arguments, detected);
/*     */     } 
/*     */   }
/*     */   private void handleShortOptionCluster(String candidate, ArgumentList arguments, OptionSet detected) {
/* 510 */     char[] options = extractShortOptionsFrom(candidate);
/* 511 */     validateOptionCharacters(options);
/*     */     
/* 513 */     for (int i = 0; i < options.length; i++) {
/* 514 */       AbstractOptionSpec<?> optionSpec = specFor(options[i]);
/*     */       
/* 516 */       if (optionSpec.acceptsArguments() && options.length > i + 1) {
/* 517 */         String detectedArgument = String.valueOf(options, i + 1, options.length - 1 - i);
/* 518 */         optionSpec.handleOption(this, arguments, detected, detectedArgument);
/*     */         
/*     */         break;
/*     */       } 
/* 522 */       optionSpec.handleOption(this, arguments, detected, (String)null);
/*     */     } 
/*     */   }
/*     */   
/*     */   void handleNonOptionArgument(String candidate, ArgumentList arguments, OptionSet detectedOptions) {
/* 527 */     specFor("[arguments]").handleOption(this, arguments, detectedOptions, candidate);
/*     */   }
/*     */   
/*     */   void noMoreOptions() {
/* 531 */     this.state = OptionParserState.noMoreOptions();
/*     */   }
/*     */   
/*     */   boolean looksLikeAnOption(String argument) {
/* 535 */     return (ParserRules.isShortOptionToken(argument) || ParserRules.isLongOptionToken(argument));
/*     */   }
/*     */   
/*     */   boolean isRecognized(String option) {
/* 539 */     return this.recognizedOptions.contains(option);
/*     */   }
/*     */   
/*     */   void requiredIf(Collection<String> precedentSynonyms, String required) {
/* 543 */     requiredIf(precedentSynonyms, specFor(required));
/*     */   }
/*     */   
/*     */   void requiredIf(Collection<String> precedentSynonyms, OptionSpec<?> required) {
/* 547 */     for (String each : precedentSynonyms) {
/* 548 */       AbstractOptionSpec<?> spec = specFor(each);
/* 549 */       if (spec == null) {
/* 550 */         throw new UnconfiguredOptionException(precedentSynonyms);
/*     */       }
/*     */     } 
/* 553 */     Set<OptionSpec<?>> associated = this.requiredIf.get(precedentSynonyms);
/* 554 */     if (associated == null) {
/* 555 */       associated = new HashSet<OptionSpec<?>>();
/* 556 */       this.requiredIf.put(precedentSynonyms, associated);
/*     */     } 
/*     */     
/* 559 */     associated.add(required);
/*     */   }
/*     */   
/*     */   private AbstractOptionSpec<?> specFor(char option) {
/* 563 */     return specFor(String.valueOf(option));
/*     */   }
/*     */   
/*     */   private AbstractOptionSpec<?> specFor(String option) {
/* 567 */     return (AbstractOptionSpec)this.recognizedOptions.get(option);
/*     */   }
/*     */   
/*     */   private void reset() {
/* 571 */     this.state = OptionParserState.moreOptions(this.posixlyCorrect);
/*     */   }
/*     */   
/*     */   private static char[] extractShortOptionsFrom(String argument) {
/* 575 */     char[] options = new char[argument.length() - 1];
/* 576 */     argument.getChars(1, argument.length(), options, 0);
/*     */     
/* 578 */     return options;
/*     */   }
/*     */   
/*     */   private void validateOptionCharacters(char[] options) {
/* 582 */     for (char each : options) {
/* 583 */       String option = String.valueOf(each);
/*     */       
/* 585 */       if (!isRecognized(option)) {
/* 586 */         throw OptionException.unrecognizedOption(option);
/*     */       }
/* 588 */       if (specFor(option).acceptsArguments())
/*     */         return; 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static KeyValuePair parseLongOptionWithArgument(String argument) {
/* 594 */     return KeyValuePair.valueOf(argument.substring(2));
/*     */   }
/*     */   
/*     */   private static KeyValuePair parseShortOptionWithArgument(String argument) {
/* 598 */     return KeyValuePair.valueOf(argument.substring(1));
/*     */   }
/*     */   
/*     */   private Map<String, List<?>> defaultValues() {
/* 602 */     Map<String, List<?>> defaults = new HashMap<String, List<?>>();
/* 603 */     for (Map.Entry<String, AbstractOptionSpec<?>> each : (Iterable<Map.Entry<String, AbstractOptionSpec<?>>>)this.recognizedOptions.toJavaUtilMap().entrySet())
/* 604 */       defaults.put(each.getKey(), ((AbstractOptionSpec)each.getValue()).defaultValues()); 
/* 605 */     return defaults;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\OptionParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */