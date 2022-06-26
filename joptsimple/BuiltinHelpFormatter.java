/*     */ package joptsimple;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import joptsimple.internal.Classes;
/*     */ import joptsimple.internal.Rows;
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
/*     */ public class BuiltinHelpFormatter
/*     */   implements HelpFormatter
/*     */ {
/*     */   private final Rows nonOptionRows;
/*     */   private final Rows optionRows;
/*     */   
/*     */   BuiltinHelpFormatter() {
/*  60 */     this(80, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BuiltinHelpFormatter(int desiredOverallWidth, int desiredColumnSeparatorWidth) {
/*  71 */     this.nonOptionRows = new Rows(desiredOverallWidth * 2, 0);
/*  72 */     this.optionRows = new Rows(desiredOverallWidth, desiredColumnSeparatorWidth);
/*     */   }
/*     */   
/*     */   public String format(Map<String, ? extends OptionDescriptor> options) {
/*  76 */     Comparator<OptionDescriptor> comparator = new Comparator<OptionDescriptor>()
/*     */       {
/*     */         public int compare(OptionDescriptor first, OptionDescriptor second) {
/*  79 */           return ((String)first.options().iterator().next()).compareTo(second.options().iterator().next());
/*     */         }
/*     */       };
/*     */     
/*  83 */     Set<OptionDescriptor> sorted = new TreeSet<OptionDescriptor>(comparator);
/*  84 */     sorted.addAll(options.values());
/*     */     
/*  86 */     addRows(sorted);
/*     */     
/*  88 */     return formattedHelpOutput();
/*     */   }
/*     */   
/*     */   private String formattedHelpOutput() {
/*  92 */     StringBuilder formatted = new StringBuilder();
/*  93 */     String nonOptionDisplay = this.nonOptionRows.render();
/*  94 */     if (!Strings.isNullOrEmpty(nonOptionDisplay))
/*  95 */       formatted.append(nonOptionDisplay).append(Strings.LINE_SEPARATOR); 
/*  96 */     formatted.append(this.optionRows.render());
/*     */     
/*  98 */     return formatted.toString();
/*     */   }
/*     */   
/*     */   private void addRows(Collection<? extends OptionDescriptor> options) {
/* 102 */     addNonOptionsDescription(options);
/*     */     
/* 104 */     if (options.isEmpty()) {
/* 105 */       this.optionRows.add("No options specified", "");
/*     */     } else {
/* 107 */       addHeaders(options);
/* 108 */       addOptions(options);
/*     */     } 
/*     */     
/* 111 */     fitRowsToWidth();
/*     */   }
/*     */   
/*     */   private void addNonOptionsDescription(Collection<? extends OptionDescriptor> options) {
/* 115 */     OptionDescriptor nonOptions = findAndRemoveNonOptionsSpec(options);
/* 116 */     if (shouldShowNonOptionArgumentDisplay(nonOptions)) {
/* 117 */       this.nonOptionRows.add("Non-option arguments:", "");
/* 118 */       this.nonOptionRows.add(createNonOptionArgumentsDisplay(nonOptions), "");
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean shouldShowNonOptionArgumentDisplay(OptionDescriptor nonOptions) {
/* 123 */     return (!Strings.isNullOrEmpty(nonOptions.description()) || !Strings.isNullOrEmpty(nonOptions.argumentTypeIndicator()) || !Strings.isNullOrEmpty(nonOptions.argumentDescription()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String createNonOptionArgumentsDisplay(OptionDescriptor nonOptions) {
/* 129 */     StringBuilder buffer = new StringBuilder();
/* 130 */     maybeAppendOptionInfo(buffer, nonOptions);
/* 131 */     maybeAppendNonOptionsDescription(buffer, nonOptions);
/*     */     
/* 133 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   private void maybeAppendNonOptionsDescription(StringBuilder buffer, OptionDescriptor nonOptions) {
/* 137 */     buffer.append((buffer.length() > 0 && !Strings.isNullOrEmpty(nonOptions.description())) ? " -- " : "").append(nonOptions.description());
/*     */   }
/*     */ 
/*     */   
/*     */   private OptionDescriptor findAndRemoveNonOptionsSpec(Collection<? extends OptionDescriptor> options) {
/* 142 */     for (Iterator<? extends OptionDescriptor> it = options.iterator(); it.hasNext(); ) {
/* 143 */       OptionDescriptor next = it.next();
/* 144 */       if (next.representsNonOptions()) {
/* 145 */         it.remove();
/* 146 */         return next;
/*     */       } 
/*     */     } 
/*     */     
/* 150 */     throw new AssertionError("no non-options argument spec");
/*     */   }
/*     */   
/*     */   private void addHeaders(Collection<? extends OptionDescriptor> options) {
/* 154 */     if (hasRequiredOption(options)) {
/* 155 */       this.optionRows.add("Option (* = required)", "Description");
/* 156 */       this.optionRows.add("---------------------", "-----------");
/*     */     } else {
/* 158 */       this.optionRows.add("Option", "Description");
/* 159 */       this.optionRows.add("------", "-----------");
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasRequiredOption(Collection<? extends OptionDescriptor> options) {
/* 164 */     for (OptionDescriptor each : options) {
/* 165 */       if (each.isRequired()) {
/* 166 */         return true;
/*     */       }
/*     */     } 
/* 169 */     return false;
/*     */   }
/*     */   
/*     */   private void addOptions(Collection<? extends OptionDescriptor> options) {
/* 173 */     for (OptionDescriptor each : options) {
/* 174 */       if (!each.representsNonOptions())
/* 175 */         this.optionRows.add(createOptionDisplay(each), createDescriptionDisplay(each)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private String createOptionDisplay(OptionDescriptor descriptor) {
/* 180 */     StringBuilder buffer = new StringBuilder(descriptor.isRequired() ? "* " : "");
/*     */     
/* 182 */     for (Iterator<String> i = descriptor.options().iterator(); i.hasNext(); ) {
/* 183 */       String option = i.next();
/* 184 */       buffer.append((option.length() > 1) ? "--" : ParserRules.HYPHEN);
/* 185 */       buffer.append(option);
/*     */       
/* 187 */       if (i.hasNext()) {
/* 188 */         buffer.append(", ");
/*     */       }
/*     */     } 
/* 191 */     maybeAppendOptionInfo(buffer, descriptor);
/*     */     
/* 193 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   private void maybeAppendOptionInfo(StringBuilder buffer, OptionDescriptor descriptor) {
/* 197 */     String indicator = extractTypeIndicator(descriptor);
/* 198 */     String description = descriptor.argumentDescription();
/* 199 */     if (indicator != null || !Strings.isNullOrEmpty(description))
/* 200 */       appendOptionHelp(buffer, indicator, description, descriptor.requiresArgument()); 
/*     */   }
/*     */   
/*     */   private String extractTypeIndicator(OptionDescriptor descriptor) {
/* 204 */     String indicator = descriptor.argumentTypeIndicator();
/*     */     
/* 206 */     if (!Strings.isNullOrEmpty(indicator) && !String.class.getName().equals(indicator)) {
/* 207 */       return Classes.shortNameOf(indicator);
/*     */     }
/* 209 */     return null;
/*     */   }
/*     */   
/*     */   private void appendOptionHelp(StringBuilder buffer, String typeIndicator, String description, boolean required) {
/* 213 */     if (required) {
/* 214 */       appendTypeIndicator(buffer, typeIndicator, description, '<', '>');
/*     */     } else {
/* 216 */       appendTypeIndicator(buffer, typeIndicator, description, '[', ']');
/*     */     } 
/*     */   }
/*     */   
/*     */   private void appendTypeIndicator(StringBuilder buffer, String typeIndicator, String description, char start, char end) {
/* 221 */     buffer.append(' ').append(start);
/* 222 */     if (typeIndicator != null) {
/* 223 */       buffer.append(typeIndicator);
/*     */     }
/* 225 */     if (!Strings.isNullOrEmpty(description)) {
/* 226 */       if (typeIndicator != null) {
/* 227 */         buffer.append(": ");
/*     */       }
/* 229 */       buffer.append(description);
/*     */     } 
/*     */     
/* 232 */     buffer.append(end);
/*     */   }
/*     */   
/*     */   private String createDescriptionDisplay(OptionDescriptor descriptor) {
/* 236 */     List<?> defaultValues = descriptor.defaultValues();
/* 237 */     if (defaultValues.isEmpty()) {
/* 238 */       return descriptor.description();
/*     */     }
/* 240 */     String defaultValuesDisplay = createDefaultValuesDisplay(defaultValues);
/* 241 */     return (descriptor.description() + ' ' + Strings.surround("default: " + defaultValuesDisplay, '(', ')')).trim();
/*     */   }
/*     */   
/*     */   private String createDefaultValuesDisplay(List<?> defaultValues) {
/* 245 */     return (defaultValues.size() == 1) ? defaultValues.get(0).toString() : defaultValues.toString();
/*     */   }
/*     */   
/*     */   private void fitRowsToWidth() {
/* 249 */     this.nonOptionRows.fitToWidth();
/* 250 */     this.optionRows.fitToWidth();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\BuiltinHelpFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */