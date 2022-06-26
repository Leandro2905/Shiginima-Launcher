/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.DefaultConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternFormatter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternParser;
/*     */ import org.apache.logging.log4j.core.pattern.RegexReplacement;
/*     */ import org.apache.logging.log4j.core.util.Charsets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "PatternLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class PatternLayout
/*     */   extends AbstractStringLayout
/*     */ {
/*     */   public static final String DEFAULT_CONVERSION_PATTERN = "%m%n";
/*     */   public static final String TTCC_CONVERSION_PATTERN = "%r [%t] %p %c %x - %m%n";
/*     */   public static final String SIMPLE_CONVERSION_PATTERN = "%d [%t] %p %c - %m%n";
/*     */   public static final String KEY = "Converter";
/*     */   private final List<PatternFormatter> formatters;
/*     */   private final String conversionPattern;
/*     */   private final Configuration config;
/*     */   private final RegexReplacement replace;
/*     */   private final boolean alwaysWriteExceptions;
/*     */   private final boolean noConsoleNoAnsi;
/*     */   
/*     */   private PatternLayout(Configuration config, RegexReplacement replace, String pattern, Charset charset, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi, String header, String footer) {
/* 117 */     super(charset, toBytes(header, charset), toBytes(footer, charset));
/* 118 */     this.replace = replace;
/* 119 */     this.conversionPattern = pattern;
/* 120 */     this.config = config;
/* 121 */     this.alwaysWriteExceptions = alwaysWriteExceptions;
/* 122 */     this.noConsoleNoAnsi = noConsoleNoAnsi;
/* 123 */     PatternParser parser = createPatternParser(config);
/* 124 */     this.formatters = parser.parse((pattern == null) ? "%m%n" : pattern, this.alwaysWriteExceptions, this.noConsoleNoAnsi);
/*     */   }
/*     */   
/*     */   private static byte[] toBytes(String str, Charset charset) {
/* 128 */     if (str != null) {
/* 129 */       return str.getBytes((charset != null) ? charset : Charset.defaultCharset());
/*     */     }
/* 131 */     return null;
/*     */   }
/*     */   
/*     */   private byte[] strSubstitutorReplace(byte... b) {
/* 135 */     if (b != null && this.config != null) {
/* 136 */       Charset cs = getCharset();
/* 137 */       return this.config.getStrSubstitutor().replace(new String(b, cs)).getBytes(cs);
/*     */     } 
/* 139 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getHeader() {
/* 144 */     return strSubstitutorReplace(super.getHeader());
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getFooter() {
/* 149 */     return strSubstitutorReplace(super.getFooter());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getConversionPattern() {
/* 158 */     return this.conversionPattern;
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
/*     */   public Map<String, String> getContentFormat() {
/* 171 */     Map<String, String> result = new HashMap<String, String>();
/* 172 */     result.put("structured", "false");
/* 173 */     result.put("formatType", "conversion");
/* 174 */     result.put("format", this.conversionPattern);
/* 175 */     return result;
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
/*     */   public String toSerializable(LogEvent event) {
/* 187 */     StringBuilder buf = new StringBuilder();
/* 188 */     for (PatternFormatter formatter : this.formatters) {
/* 189 */       formatter.format(event, buf);
/*     */     }
/* 191 */     String str = buf.toString();
/* 192 */     if (this.replace != null) {
/* 193 */       str = this.replace.format(str);
/*     */     }
/* 195 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PatternParser createPatternParser(Configuration config) {
/* 204 */     if (config == null) {
/* 205 */       return new PatternParser(config, "Converter", LogEventPatternConverter.class);
/*     */     }
/* 207 */     PatternParser parser = (PatternParser)config.getComponent("Converter");
/* 208 */     if (parser == null) {
/* 209 */       parser = new PatternParser(config, "Converter", LogEventPatternConverter.class);
/* 210 */       config.addComponent("Converter", parser);
/* 211 */       parser = (PatternParser)config.getComponent("Converter");
/*     */     } 
/* 213 */     return parser;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 218 */     return this.conversionPattern;
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
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static PatternLayout createLayout(@PluginAttribute(value = "pattern", defaultString = "%m%n") String pattern, @PluginConfiguration Configuration config, @PluginElement("Replace") RegexReplacement replace, @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset, @PluginAttribute(value = "alwaysWriteExceptions", defaultBoolean = true) boolean alwaysWriteExceptions, @PluginAttribute(value = "noConsoleNoAnsi", defaultBoolean = false) boolean noConsoleNoAnsi, @PluginAttribute("header") String header, @PluginAttribute("footer") String footer) {
/* 252 */     return newBuilder().withPattern(pattern).withConfiguration(config).withRegexReplacement(replace).withCharset(charset).withAlwaysWriteExceptions(alwaysWriteExceptions).withNoConsoleNoAnsi(noConsoleNoAnsi).withHeader(header).withFooter(footer).build();
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
/*     */   public static PatternLayout createDefaultLayout() {
/* 272 */     return newBuilder().build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 281 */     return new Builder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<PatternLayout>
/*     */   {
/*     */     @PluginBuilderAttribute
/* 292 */     private String pattern = "%m%n";
/*     */     
/*     */     @PluginConfiguration
/* 295 */     private Configuration configuration = null;
/*     */     
/*     */     @PluginElement("Replace")
/* 298 */     private RegexReplacement regexReplacement = null;
/*     */     
/*     */     @PluginBuilderAttribute
/* 301 */     private Charset charset = Charsets.UTF_8;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean alwaysWriteExceptions = true;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean noConsoleNoAnsi = false;
/*     */     
/*     */     @PluginBuilderAttribute
/* 310 */     private String header = null;
/*     */     
/*     */     @PluginBuilderAttribute
/* 313 */     private String footer = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withPattern(String pattern) {
/* 322 */       this.pattern = pattern;
/* 323 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder withConfiguration(Configuration configuration) {
/* 328 */       this.configuration = configuration;
/* 329 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withRegexReplacement(RegexReplacement regexReplacement) {
/* 333 */       this.regexReplacement = regexReplacement;
/* 334 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withCharset(Charset charset) {
/* 338 */       this.charset = charset;
/* 339 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withAlwaysWriteExceptions(boolean alwaysWriteExceptions) {
/* 343 */       this.alwaysWriteExceptions = alwaysWriteExceptions;
/* 344 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withNoConsoleNoAnsi(boolean noConsoleNoAnsi) {
/* 348 */       this.noConsoleNoAnsi = noConsoleNoAnsi;
/* 349 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withHeader(String header) {
/* 353 */       this.header = header;
/* 354 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withFooter(String footer) {
/* 358 */       this.footer = footer;
/* 359 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public PatternLayout build() {
/* 365 */       if (this.configuration == null) {
/* 366 */         this.configuration = (Configuration)new DefaultConfiguration();
/*     */       }
/* 368 */       return new PatternLayout(this.configuration, this.regexReplacement, this.pattern, this.charset, this.alwaysWriteExceptions, this.noConsoleNoAnsi, this.header, this.footer);
/*     */     }
/*     */     
/*     */     private Builder() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\layout\PatternLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */