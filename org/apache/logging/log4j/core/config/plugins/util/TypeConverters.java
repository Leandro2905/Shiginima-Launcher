/*     */ package org.apache.logging.log4j.core.config.plugins.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.layout.HtmlLayout;
/*     */ import org.apache.logging.log4j.core.net.Facility;
/*     */ import org.apache.logging.log4j.core.net.Protocol;
/*     */ import org.apache.logging.log4j.core.util.Assert;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.EnglishEnums;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TypeConverters
/*     */ {
/*     */   public static class BigDecimalConverter
/*     */     implements TypeConverter<BigDecimal>
/*     */   {
/*     */     public BigDecimal convert(String s) {
/*  61 */       return new BigDecimal(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class BigIntegerConverter
/*     */     implements TypeConverter<BigInteger>
/*     */   {
/*     */     public BigInteger convert(String s) {
/*  71 */       return new BigInteger(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class BooleanConverter
/*     */     implements TypeConverter<Boolean>
/*     */   {
/*     */     public Boolean convert(String s) {
/*  81 */       return Boolean.valueOf(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ByteArrayConverter
/*     */     implements TypeConverter<byte[]>
/*     */   {
/*     */     private static final String PREFIX_0x = "0x";
/*     */ 
/*     */ 
/*     */     
/*     */     private static final String PREFIX_BASE64 = "Base64:";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte[] convert(String value) {
/*     */       byte[] bytes;
/* 103 */       if (value == null || value.isEmpty()) {
/* 104 */         bytes = new byte[0];
/* 105 */       } else if (value.startsWith("Base64:")) {
/* 106 */         String lexicalXSDBase64Binary = value.substring("Base64:".length());
/* 107 */         bytes = DatatypeConverter.parseBase64Binary(lexicalXSDBase64Binary);
/* 108 */       } else if (value.startsWith("0x")) {
/* 109 */         String lexicalXSDHexBinary = value.substring("0x".length());
/* 110 */         bytes = DatatypeConverter.parseHexBinary(lexicalXSDHexBinary);
/*     */       } else {
/* 112 */         bytes = value.getBytes(Charset.defaultCharset());
/*     */       } 
/* 114 */       return bytes;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ByteConverter
/*     */     implements TypeConverter<Byte>
/*     */   {
/*     */     public Byte convert(String s) {
/* 124 */       return Byte.valueOf(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class CharacterConverter
/*     */     implements TypeConverter<Character>
/*     */   {
/*     */     public Character convert(String s) {
/* 134 */       if (s.length() != 1) {
/* 135 */         throw new IllegalArgumentException("Character string must be of length 1: " + s);
/*     */       }
/* 137 */       return Character.valueOf(s.toCharArray()[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class CharArrayConverter
/*     */     implements TypeConverter<char[]>
/*     */   {
/*     */     public char[] convert(String s) {
/* 147 */       return s.toCharArray();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class CharsetConverter
/*     */     implements TypeConverter<Charset>
/*     */   {
/*     */     public Charset convert(String s) {
/* 157 */       return Charset.forName(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ClassConverter
/*     */     implements TypeConverter<Class<?>>
/*     */   {
/*     */     public Class<?> convert(String s) throws ClassNotFoundException {
/* 167 */       return Loader.loadClass(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class DoubleConverter
/*     */     implements TypeConverter<Double>
/*     */   {
/*     */     public Double convert(String s) {
/* 177 */       return Double.valueOf(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class EnumConverter<E extends Enum<E>>
/*     */     implements TypeConverter<E>
/*     */   {
/*     */     private final Class<E> clazz;
/*     */ 
/*     */ 
/*     */     
/*     */     private EnumConverter(Class<E> clazz) {
/* 191 */       this.clazz = clazz;
/*     */     }
/*     */ 
/*     */     
/*     */     public E convert(String s) {
/* 196 */       return (E)EnglishEnums.valueOf(this.clazz, s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FileConverter
/*     */     implements TypeConverter<File>
/*     */   {
/*     */     public File convert(String s) {
/* 206 */       return new File(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FloatConverter
/*     */     implements TypeConverter<Float>
/*     */   {
/*     */     public Float convert(String s) {
/* 216 */       return Float.valueOf(s);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Holder {
/* 221 */     private static final TypeConverters INSTANCE = new TypeConverters();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class IntegerConverter
/*     */     implements TypeConverter<Integer>
/*     */   {
/*     */     public Integer convert(String s) {
/* 230 */       return Integer.valueOf(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class LevelConverter
/*     */     implements TypeConverter<Level>
/*     */   {
/*     */     public Level convert(String s) {
/* 240 */       return Level.valueOf(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class LongConverter
/*     */     implements TypeConverter<Long>
/*     */   {
/*     */     public Long convert(String s) {
/* 250 */       return Long.valueOf(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class PatternConverter
/*     */     implements TypeConverter<Pattern>
/*     */   {
/*     */     public Pattern convert(String s) {
/* 260 */       return Pattern.compile(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SecurityProviderConverter
/*     */     implements TypeConverter<Provider>
/*     */   {
/*     */     public Provider convert(String s) {
/* 270 */       return Security.getProvider(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ShortConverter
/*     */     implements TypeConverter<Short>
/*     */   {
/*     */     public Short convert(String s) {
/* 280 */       return Short.valueOf(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class StringConverter
/*     */     implements TypeConverter<String>
/*     */   {
/*     */     public String convert(String s) {
/* 290 */       return s;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class UriConverter
/*     */     implements TypeConverter<URI>
/*     */   {
/*     */     public URI convert(String s) throws URISyntaxException {
/* 300 */       return new URI(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class UrlConverter
/*     */     implements TypeConverter<URL>
/*     */   {
/*     */     public URL convert(String s) throws MalformedURLException {
/* 310 */       return new URL(s);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object convert(String s, Class<?> clazz, Object defaultValue) {
/* 332 */     TypeConverter<?> converter = findTypeConverter((Class)Assert.requireNonNull(clazz, "No class specified to convert to."));
/*     */     
/* 334 */     if (converter == null) {
/* 335 */       throw new IllegalArgumentException("No type converter found for class: " + clazz.getName());
/*     */     }
/* 337 */     if (s == null)
/*     */     {
/*     */       
/* 340 */       return parseDefaultValue(converter, defaultValue);
/*     */     }
/*     */     try {
/* 343 */       return converter.convert(s);
/* 344 */     } catch (Exception e) {
/* 345 */       LOGGER.warn("Error while converting string [{}] to type [{}]. Using default value [{}].", new Object[] { s, clazz, defaultValue, e });
/*     */       
/* 347 */       return parseDefaultValue(converter, defaultValue);
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
/*     */   
/*     */   public static TypeConverter<?> findTypeConverter(Class<?> clazz) {
/* 363 */     return Holder.INSTANCE.registry.get(clazz);
/*     */   }
/*     */   
/*     */   private static Object parseDefaultValue(TypeConverter<?> converter, Object defaultValue) {
/* 367 */     if (defaultValue == null) {
/* 368 */       return null;
/*     */     }
/* 370 */     if (!(defaultValue instanceof String)) {
/* 371 */       return defaultValue;
/*     */     }
/*     */     try {
/* 374 */       return converter.convert((String)defaultValue);
/* 375 */     } catch (Exception e) {
/* 376 */       LOGGER.debug("Can't parse default value [{}] for type [{}].", new Object[] { defaultValue, converter.getClass(), e });
/* 377 */       return null;
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
/*     */   public static void registerTypeConverter(Class<?> clazz, TypeConverter<?> converter) {
/* 391 */     Holder.INSTANCE.registry.put(clazz, converter);
/*     */   }
/*     */   
/* 394 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/* 396 */   private final Map<Class<?>, TypeConverter<?>> registry = new ConcurrentHashMap<Class<?>, TypeConverter<?>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypeConverters() {
/* 403 */     this.registry.put(Boolean.class, new BooleanConverter());
/* 404 */     this.registry.put(Byte.class, new ByteConverter());
/* 405 */     this.registry.put(Character.class, new CharacterConverter());
/* 406 */     this.registry.put(Double.class, new DoubleConverter());
/* 407 */     this.registry.put(Float.class, new FloatConverter());
/* 408 */     this.registry.put(Integer.class, new IntegerConverter());
/* 409 */     this.registry.put(Long.class, new LongConverter());
/* 410 */     this.registry.put(Short.class, new ShortConverter());
/*     */     
/* 412 */     this.registry.put(boolean.class, this.registry.get(Boolean.class));
/* 413 */     this.registry.put(byte.class, new ByteConverter());
/* 414 */     this.registry.put(char[].class, new CharArrayConverter());
/* 415 */     this.registry.put(double.class, this.registry.get(Double.class));
/* 416 */     this.registry.put(float.class, this.registry.get(Float.class));
/* 417 */     this.registry.put(int.class, this.registry.get(Integer.class));
/* 418 */     this.registry.put(long.class, this.registry.get(Long.class));
/* 419 */     this.registry.put(short.class, this.registry.get(Short.class));
/*     */     
/* 421 */     this.registry.put(byte[].class, new ByteArrayConverter());
/* 422 */     this.registry.put(char.class, new CharacterConverter());
/*     */     
/* 424 */     this.registry.put(BigInteger.class, new BigIntegerConverter());
/* 425 */     this.registry.put(BigDecimal.class, new BigDecimalConverter());
/*     */     
/* 427 */     this.registry.put(String.class, new StringConverter());
/* 428 */     this.registry.put(Charset.class, new CharsetConverter());
/* 429 */     this.registry.put(File.class, new FileConverter());
/* 430 */     this.registry.put(URL.class, new UrlConverter());
/* 431 */     this.registry.put(URI.class, new UriConverter());
/* 432 */     this.registry.put(Class.class, new ClassConverter());
/* 433 */     this.registry.put(Pattern.class, new PatternConverter());
/* 434 */     this.registry.put(Provider.class, new SecurityProviderConverter());
/*     */     
/* 436 */     this.registry.put(Level.class, new LevelConverter());
/* 437 */     this.registry.put(Filter.Result.class, new EnumConverter(Filter.Result.class));
/* 438 */     this.registry.put(Facility.class, new EnumConverter(Facility.class));
/* 439 */     this.registry.put(Protocol.class, new EnumConverter(Protocol.class));
/* 440 */     this.registry.put(HtmlLayout.FontSize.class, new EnumConverter(HtmlLayout.FontSize.class));
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugin\\util\TypeConverters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */