/*     */ package org.apache.logging.log4j.core.appender.db.jpa.converter;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import javax.persistence.AttributeConverter;
/*     */ import javax.persistence.Converter;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Converter(autoApply = false)
/*     */ public class ThrowableAttributeConverter
/*     */   implements AttributeConverter<Throwable, String>
/*     */ {
/*     */   private static final int CAUSED_BY_STRING_LENGTH = 10;
/*     */   private static final Field THROWABLE_CAUSE;
/*     */   private static final Field THROWABLE_MESSAGE;
/*     */   
/*     */   static {
/*     */     try {
/*  46 */       THROWABLE_CAUSE = Throwable.class.getDeclaredField("cause");
/*  47 */       THROWABLE_CAUSE.setAccessible(true);
/*  48 */       THROWABLE_MESSAGE = Throwable.class.getDeclaredField("detailMessage");
/*  49 */       THROWABLE_MESSAGE.setAccessible(true);
/*  50 */     } catch (NoSuchFieldException e) {
/*  51 */       throw new IllegalStateException("Something is wrong with java.lang.Throwable.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String convertToDatabaseColumn(Throwable throwable) {
/*  57 */     if (throwable == null) {
/*  58 */       return null;
/*     */     }
/*     */     
/*  61 */     StringBuilder builder = new StringBuilder();
/*  62 */     convertThrowable(builder, throwable);
/*  63 */     return builder.toString();
/*     */   }
/*     */   
/*     */   private void convertThrowable(StringBuilder builder, Throwable throwable) {
/*  67 */     builder.append(throwable.toString()).append('\n');
/*  68 */     for (StackTraceElement element : throwable.getStackTrace()) {
/*  69 */       builder.append("\tat ").append(element).append('\n');
/*     */     }
/*  71 */     if (throwable.getCause() != null) {
/*  72 */       builder.append("Caused by ");
/*  73 */       convertThrowable(builder, throwable.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable convertToEntityAttribute(String s) {
/*  79 */     if (Strings.isEmpty(s)) {
/*  80 */       return null;
/*     */     }
/*     */     
/*  83 */     List<String> lines = Arrays.asList(s.split("(\n|\r\n)"));
/*  84 */     return convertString(lines.listIterator(), false);
/*     */   }
/*     */   
/*     */   private Throwable convertString(ListIterator<String> lines, boolean removeCausedBy) {
/*  88 */     String throwableClassName, firstLine = lines.next();
/*  89 */     if (removeCausedBy) {
/*  90 */       firstLine = firstLine.substring(10);
/*     */     }
/*  92 */     int colon = firstLine.indexOf(":");
/*     */     
/*  94 */     String message = null;
/*  95 */     if (colon > 1) {
/*  96 */       throwableClassName = firstLine.substring(0, colon);
/*  97 */       if (firstLine.length() > colon + 1) {
/*  98 */         message = firstLine.substring(colon + 1).trim();
/*     */       }
/*     */     } else {
/* 101 */       throwableClassName = firstLine;
/*     */     } 
/*     */     
/* 104 */     List<StackTraceElement> stackTrace = new ArrayList<StackTraceElement>();
/* 105 */     Throwable cause = null;
/* 106 */     while (lines.hasNext()) {
/* 107 */       String line = lines.next();
/*     */       
/* 109 */       if (line.startsWith("Caused by ")) {
/* 110 */         lines.previous();
/* 111 */         cause = convertString(lines, true);
/*     */         
/*     */         break;
/*     */       } 
/* 115 */       stackTrace.add(StackTraceElementAttributeConverter.convertString(line.trim().substring(3).trim()));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 120 */     return getThrowable(throwableClassName, message, cause, stackTrace.<StackTraceElement>toArray(new StackTraceElement[stackTrace.size()]));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Throwable getThrowable(String throwableClassName, String message, Throwable cause, StackTraceElement[] stackTrace) {
/*     */     try {
/*     */       Throwable throwable;
/* 128 */       Class<Throwable> throwableClass = Loader.loadClass(throwableClassName);
/*     */       
/* 130 */       if (!Throwable.class.isAssignableFrom(throwableClass)) {
/* 131 */         return null;
/*     */       }
/*     */ 
/*     */       
/* 135 */       if (message != null && cause != null) {
/* 136 */         throwable = getThrowable(throwableClass, message, cause);
/* 137 */         if (throwable == null) {
/* 138 */           throwable = getThrowable(throwableClass, cause);
/* 139 */           if (throwable == null) {
/* 140 */             throwable = getThrowable(throwableClass, message);
/* 141 */             if (throwable == null) {
/* 142 */               throwable = getThrowable(throwableClass);
/* 143 */               if (throwable != null) {
/* 144 */                 THROWABLE_MESSAGE.set(throwable, message);
/* 145 */                 THROWABLE_CAUSE.set(throwable, cause);
/*     */               } 
/*     */             } else {
/* 148 */               THROWABLE_CAUSE.set(throwable, cause);
/*     */             } 
/*     */           } else {
/* 151 */             THROWABLE_MESSAGE.set(throwable, message);
/*     */           } 
/*     */         } 
/* 154 */       } else if (cause != null) {
/* 155 */         throwable = getThrowable(throwableClass, cause);
/* 156 */         if (throwable == null) {
/* 157 */           throwable = getThrowable(throwableClass);
/* 158 */           if (throwable != null) {
/* 159 */             THROWABLE_CAUSE.set(throwable, cause);
/*     */           }
/*     */         } 
/* 162 */       } else if (message != null) {
/* 163 */         throwable = getThrowable(throwableClass, message);
/* 164 */         if (throwable == null) {
/* 165 */           throwable = getThrowable(throwableClass);
/* 166 */           if (throwable != null) {
/* 167 */             THROWABLE_MESSAGE.set(throwable, cause);
/*     */           }
/*     */         } 
/*     */       } else {
/* 171 */         throwable = getThrowable(throwableClass);
/*     */       } 
/*     */       
/* 174 */       if (throwable == null) {
/* 175 */         return null;
/*     */       }
/* 177 */       throwable.setStackTrace(stackTrace);
/* 178 */       return throwable;
/* 179 */     } catch (Exception e) {
/* 180 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Throwable getThrowable(Class<Throwable> throwableClass, String message, Throwable cause) {
/*     */     try {
/* 188 */       Constructor[] arrayOfConstructor = (Constructor[])throwableClass.getConstructors();
/* 189 */       for (Constructor<Throwable> constructor : arrayOfConstructor) {
/* 190 */         Class<?>[] parameterTypes = constructor.getParameterTypes();
/* 191 */         if (parameterTypes.length == 2) {
/* 192 */           if (String.class == parameterTypes[0] && Throwable.class.isAssignableFrom(parameterTypes[1]))
/* 193 */             return constructor.newInstance(new Object[] { message, cause }); 
/* 194 */           if (String.class == parameterTypes[1] && Throwable.class.isAssignableFrom(parameterTypes[0]))
/*     */           {
/* 196 */             return constructor.newInstance(new Object[] { cause, message });
/*     */           }
/*     */         } 
/*     */       } 
/* 200 */       return null;
/* 201 */     } catch (Exception e) {
/* 202 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Throwable getThrowable(Class<Throwable> throwableClass, Throwable cause) {
/*     */     try {
/* 210 */       Constructor[] arrayOfConstructor = (Constructor[])throwableClass.getConstructors();
/* 211 */       for (Constructor<Throwable> constructor : arrayOfConstructor) {
/* 212 */         Class<?>[] parameterTypes = constructor.getParameterTypes();
/* 213 */         if (parameterTypes.length == 1 && Throwable.class.isAssignableFrom(parameterTypes[0])) {
/* 214 */           return constructor.newInstance(new Object[] { cause });
/*     */         }
/*     */       } 
/* 217 */       return null;
/* 218 */     } catch (Exception e) {
/* 219 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Throwable getThrowable(Class<Throwable> throwableClass, String message) {
/*     */     try {
/* 225 */       return throwableClass.getConstructor(new Class[] { String.class }).newInstance(new Object[] { message });
/* 226 */     } catch (Exception e) {
/* 227 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Throwable getThrowable(Class<Throwable> throwableClass) {
/*     */     try {
/* 233 */       return throwableClass.newInstance();
/* 234 */     } catch (Exception e) {
/* 235 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\db\jpa\converter\ThrowableAttributeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */