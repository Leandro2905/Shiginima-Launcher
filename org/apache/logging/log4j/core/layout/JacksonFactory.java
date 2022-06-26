/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import com.fasterxml.jackson.core.PrettyPrinter;
/*     */ import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
/*     */ import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectWriter;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
/*     */ import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;
/*     */ import org.apache.logging.log4j.core.jackson.Log4jXmlObjectMapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class JacksonFactory
/*     */ {
/*     */   protected abstract String getPropertNameForContextMap();
/*     */   
/*     */   protected abstract String getPropertNameForSource();
/*     */   
/*     */   protected abstract PrettyPrinter newCompactPrinter();
/*     */   
/*     */   protected abstract ObjectMapper newObjectMapper();
/*     */   
/*     */   protected abstract PrettyPrinter newPrettyPrinter();
/*     */   
/*     */   static class JSON
/*     */     extends JacksonFactory
/*     */   {
/*     */     protected String getPropertNameForContextMap() {
/*  43 */       return "contextMap";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForSource() {
/*  48 */       return "source";
/*     */     }
/*     */ 
/*     */     
/*     */     protected PrettyPrinter newCompactPrinter() {
/*  53 */       return (PrettyPrinter)new MinimalPrettyPrinter();
/*     */     }
/*     */ 
/*     */     
/*     */     protected ObjectMapper newObjectMapper() {
/*  58 */       return (ObjectMapper)new Log4jJsonObjectMapper();
/*     */     }
/*     */ 
/*     */     
/*     */     protected PrettyPrinter newPrettyPrinter() {
/*  63 */       return (PrettyPrinter)new DefaultPrettyPrinter();
/*     */     }
/*     */   }
/*     */   
/*     */   static class XML
/*     */     extends JacksonFactory
/*     */   {
/*     */     protected String getPropertNameForContextMap() {
/*  71 */       return "ContextMap";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForSource() {
/*  76 */       return "Source";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected PrettyPrinter newCompactPrinter() {
/*  82 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     protected ObjectMapper newObjectMapper() {
/*  87 */       return (ObjectMapper)new Log4jXmlObjectMapper();
/*     */     }
/*     */ 
/*     */     
/*     */     protected PrettyPrinter newPrettyPrinter() {
/*  92 */       return (PrettyPrinter)new DefaultXmlPrettyPrinter();
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
/*     */   ObjectWriter newWriter(boolean locationInfo, boolean properties, boolean compact) {
/* 107 */     SimpleFilterProvider filters = new SimpleFilterProvider();
/* 108 */     Set<String> except = new HashSet<String>(2);
/* 109 */     if (!locationInfo) {
/* 110 */       except.add(getPropertNameForSource());
/*     */     }
/* 112 */     if (!properties) {
/* 113 */       except.add(getPropertNameForContextMap());
/*     */     }
/* 115 */     filters.addFilter(Log4jLogEvent.class.getName(), SimpleBeanPropertyFilter.serializeAllExcept(except));
/* 116 */     ObjectWriter writer = newObjectMapper().writer(compact ? newCompactPrinter() : newPrettyPrinter());
/* 117 */     return writer.with((FilterProvider)filters);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\layout\JacksonFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */