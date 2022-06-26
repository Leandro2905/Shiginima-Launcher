/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractStyleNameConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*     */   private final List<PatternFormatter> formatters;
/*     */   private final String style;
/*     */   
/*     */   protected AbstractStyleNameConverter(String name, List<PatternFormatter> formatters, String styling) {
/*  46 */     super(name, "style");
/*  47 */     this.formatters = formatters;
/*  48 */     this.style = styling;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "black", category = "Converter")
/*     */   @ConverterKeys({"black"})
/*     */   public static final class Black
/*     */     extends AbstractStyleNameConverter
/*     */   {
/*     */     protected static final String NAME = "black";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Black(List<PatternFormatter> formatters, String styling) {
/*  68 */       super("black", formatters, styling);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Black newInstance(Configuration config, String[] options) {
/*  80 */       return newInstance(Black.class, "black", config, options);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "blue", category = "Converter")
/*     */   @ConverterKeys({"blue"})
/*     */   public static final class Blue
/*     */     extends AbstractStyleNameConverter
/*     */   {
/*     */     protected static final String NAME = "blue";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Blue(List<PatternFormatter> formatters, String styling) {
/* 101 */       super("blue", formatters, styling);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Blue newInstance(Configuration config, String[] options) {
/* 113 */       return newInstance(Blue.class, "blue", config, options);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "cyan", category = "Converter")
/*     */   @ConverterKeys({"cyan"})
/*     */   public static final class Cyan
/*     */     extends AbstractStyleNameConverter
/*     */   {
/*     */     protected static final String NAME = "cyan";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Cyan(List<PatternFormatter> formatters, String styling) {
/* 134 */       super("cyan", formatters, styling);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Cyan newInstance(Configuration config, String[] options) {
/* 146 */       return newInstance(Cyan.class, "cyan", config, options);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "green", category = "Converter")
/*     */   @ConverterKeys({"green"})
/*     */   public static final class Green
/*     */     extends AbstractStyleNameConverter
/*     */   {
/*     */     protected static final String NAME = "green";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Green(List<PatternFormatter> formatters, String styling) {
/* 167 */       super("green", formatters, styling);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Green newInstance(Configuration config, String[] options) {
/* 179 */       return newInstance(Green.class, "green", config, options);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "magenta", category = "Converter")
/*     */   @ConverterKeys({"magenta"})
/*     */   public static final class Magenta
/*     */     extends AbstractStyleNameConverter
/*     */   {
/*     */     protected static final String NAME = "magenta";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Magenta(List<PatternFormatter> formatters, String styling) {
/* 200 */       super("magenta", formatters, styling);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Magenta newInstance(Configuration config, String[] options) {
/* 212 */       return newInstance(Magenta.class, "magenta", config, options);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "red", category = "Converter")
/*     */   @ConverterKeys({"red"})
/*     */   public static final class Red
/*     */     extends AbstractStyleNameConverter
/*     */   {
/*     */     protected static final String NAME = "red";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Red(List<PatternFormatter> formatters, String styling) {
/* 233 */       super("red", formatters, styling);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Red newInstance(Configuration config, String[] options) {
/* 245 */       return newInstance(Red.class, "red", config, options);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "white", category = "Converter")
/*     */   @ConverterKeys({"white"})
/*     */   public static final class White
/*     */     extends AbstractStyleNameConverter
/*     */   {
/*     */     protected static final String NAME = "white";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public White(List<PatternFormatter> formatters, String styling) {
/* 266 */       super("white", formatters, styling);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static White newInstance(Configuration config, String[] options) {
/* 278 */       return newInstance(White.class, "white", config, options);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "yellow", category = "Converter")
/*     */   @ConverterKeys({"yellow"})
/*     */   public static final class Yellow
/*     */     extends AbstractStyleNameConverter
/*     */   {
/*     */     protected static final String NAME = "yellow";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Yellow(List<PatternFormatter> formatters, String styling) {
/* 299 */       super("yellow", formatters, styling);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Yellow newInstance(Configuration config, String[] options) {
/* 311 */       return newInstance(Yellow.class, "yellow", config, options);
/*     */     }
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
/*     */   protected static <T extends AbstractStyleNameConverter> T newInstance(Class<T> asnConverterClass, String name, Configuration config, String[] options) {
/* 326 */     List<PatternFormatter> formatters = toPatternFormatterList(config, options);
/* 327 */     if (formatters == null) {
/* 328 */       return null;
/*     */     }
/*     */     try {
/* 331 */       Constructor<T> constructor = asnConverterClass.getConstructor(new Class[] { List.class, String.class });
/* 332 */       return constructor.newInstance(new Object[] { formatters, AnsiEscape.createSequence(new String[] { name }) });
/* 333 */     } catch (SecurityException e) {
/* 334 */       LOGGER.error(e.toString(), e);
/* 335 */     } catch (NoSuchMethodException e) {
/* 336 */       LOGGER.error(e.toString(), e);
/* 337 */     } catch (IllegalArgumentException e) {
/* 338 */       LOGGER.error(e.toString(), e);
/* 339 */     } catch (InstantiationException e) {
/* 340 */       LOGGER.error(e.toString(), e);
/* 341 */     } catch (IllegalAccessException e) {
/* 342 */       LOGGER.error(e.toString(), e);
/* 343 */     } catch (InvocationTargetException e) {
/* 344 */       LOGGER.error(e.toString(), e);
/*     */     } 
/* 346 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<PatternFormatter> toPatternFormatterList(Configuration config, String[] options) {
/* 357 */     if (options.length == 0 || options[0] == null) {
/* 358 */       LOGGER.error("No pattern supplied on style for config=" + config);
/* 359 */       return null;
/*     */     } 
/* 361 */     PatternParser parser = PatternLayout.createPatternParser(config);
/* 362 */     if (parser == null) {
/* 363 */       LOGGER.error("No PatternParser created for config=" + config + ", options=" + Arrays.toString((Object[])options));
/* 364 */       return null;
/*     */     } 
/* 366 */     return parser.parse(options[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 374 */     StringBuilder buf = new StringBuilder();
/* 375 */     for (PatternFormatter formatter : this.formatters) {
/* 376 */       formatter.format(event, buf);
/*     */     }
/* 378 */     if (buf.length() > 0)
/* 379 */       toAppendTo.append(this.style).append(buf.toString()).append(AnsiEscape.getDefaultStyle()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\AbstractStyleNameConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */