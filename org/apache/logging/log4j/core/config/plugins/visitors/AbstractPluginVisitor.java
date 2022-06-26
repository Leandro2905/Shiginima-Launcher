/*     */ package org.apache.logging.log4j.core.config.plugins.visitors;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Member;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.TypeConverters;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.core.util.Assert;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ 
/*     */ public abstract class AbstractPluginVisitor<A extends Annotation>
/*     */   implements PluginVisitor<A>
/*     */ {
/*  39 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   protected final Class<A> clazz;
/*     */   
/*     */   protected A annotation;
/*     */   
/*     */   protected String[] aliases;
/*     */   
/*     */   protected Class<?> conversionType;
/*     */   
/*     */   protected StrSubstitutor substitutor;
/*     */   
/*     */   protected Member member;
/*     */   
/*     */   protected AbstractPluginVisitor(Class<A> clazz) {
/*  54 */     this.clazz = clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PluginVisitor<A> setAnnotation(Annotation annotation) {
/*  60 */     Annotation a = (Annotation)Assert.requireNonNull(annotation, "No annotation was provided");
/*  61 */     if (this.clazz.isInstance(annotation)) {
/*  62 */       this.annotation = (A)annotation;
/*     */     }
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public PluginVisitor<A> setAliases(String... aliases) {
/*  69 */     this.aliases = aliases;
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public PluginVisitor<A> setConversionType(Class<?> conversionType) {
/*  75 */     this.conversionType = (Class)Assert.requireNonNull(conversionType, "No conversion type class was provided");
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public PluginVisitor<A> setStrSubstitutor(StrSubstitutor substitutor) {
/*  81 */     this.substitutor = (StrSubstitutor)Assert.requireNonNull(substitutor, "No StrSubstitutor was provided");
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public PluginVisitor<A> setMember(Member member) {
/*  87 */     this.member = member;
/*  88 */     return this;
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
/*     */   protected static String removeAttributeValue(Map<String, String> attributes, String name, String... aliases) {
/* 102 */     for (Map.Entry<String, String> entry : attributes.entrySet()) {
/* 103 */       String key = entry.getKey();
/* 104 */       String value = entry.getValue();
/* 105 */       if (key.equalsIgnoreCase(name)) {
/* 106 */         attributes.remove(key);
/* 107 */         return value;
/*     */       } 
/* 109 */       if (aliases != null) {
/* 110 */         for (String alias : aliases) {
/* 111 */           if (key.equalsIgnoreCase(alias)) {
/* 112 */             attributes.remove(key);
/* 113 */             return value;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object convert(String value, Object defaultValue) {
/* 129 */     if (defaultValue instanceof String) {
/* 130 */       return TypeConverters.convert(value, this.conversionType, Strings.trimToNull((String)defaultValue));
/*     */     }
/* 132 */     return TypeConverters.convert(value, this.conversionType, defaultValue);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugins\visitors\AbstractPluginVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */