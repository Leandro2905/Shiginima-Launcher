/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Log4jStackTraceElementDeserializer
/*    */   extends StdScalarDeserializer<StackTraceElement>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public Log4jStackTraceElementDeserializer() {
/* 41 */     super(StackTraceElement.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public StackTraceElement deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
/* 47 */     JsonToken t = jp.getCurrentToken();
/*    */     
/* 49 */     if (t == JsonToken.START_OBJECT) {
/* 50 */       String className = null, methodName = null, fileName = null;
/* 51 */       int lineNumber = -1;
/*    */       
/* 53 */       while ((t = jp.nextValue()) != JsonToken.END_OBJECT) {
/* 54 */         String propName = jp.getCurrentName();
/* 55 */         if ("class".equals(propName)) {
/* 56 */           className = jp.getText(); continue;
/* 57 */         }  if ("file".equals(propName)) {
/* 58 */           fileName = jp.getText(); continue;
/* 59 */         }  if ("line".equals(propName)) {
/* 60 */           if (t.isNumeric()) {
/* 61 */             lineNumber = jp.getIntValue();
/*    */             continue;
/*    */           } 
/*    */           try {
/* 65 */             lineNumber = Integer.valueOf(jp.getText().trim()).intValue();
/* 66 */           } catch (NumberFormatException e) {
/* 67 */             throw JsonMappingException.from(jp, "Non-numeric token (" + t + ") for property 'line'", e);
/*    */           }  continue;
/*    */         } 
/* 70 */         if ("method".equals(propName)) {
/* 71 */           methodName = jp.getText(); continue;
/* 72 */         }  if ("nativeMethod".equals(propName)) {
/*    */           continue;
/*    */         }
/* 75 */         handleUnknownProperty(jp, ctxt, this._valueClass, propName);
/*    */       } 
/*    */       
/* 78 */       return new StackTraceElement(className, methodName, fileName, lineNumber);
/*    */     } 
/* 80 */     throw ctxt.mappingException(this._valueClass, t);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jackson\Log4jStackTraceElementDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */