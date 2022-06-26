/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "SerializedLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class SerializedLayout
/*     */   extends AbstractLayout<LogEvent>
/*     */ {
/*     */   private static byte[] serializedHeader;
/*     */   
/*     */   static {
/*  39 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*     */     try {
/*  41 */       ObjectOutputStream oos = new ObjectOutputStream(baos);
/*  42 */       oos.close();
/*  43 */       serializedHeader = baos.toByteArray();
/*  44 */     } catch (Exception ex) {
/*  45 */       LOGGER.error("Unable to generate Object stream header", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private SerializedLayout() {
/*  50 */     super(null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toByteArray(LogEvent event) {
/*  61 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*     */     try {
/*  63 */       ObjectOutputStream oos = new PrivateObjectOutputStream(baos);
/*     */       try {
/*  65 */         oos.writeObject(event);
/*  66 */         oos.reset();
/*     */       } finally {
/*  68 */         oos.close();
/*     */       } 
/*  70 */     } catch (IOException ioe) {
/*  71 */       LOGGER.error("Serialization of LogEvent failed.", ioe);
/*     */     } 
/*  73 */     return baos.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogEvent toSerializable(LogEvent event) {
/*  84 */     return event;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static SerializedLayout createLayout() {
/*  93 */     return new SerializedLayout();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getHeader() {
/*  98 */     return serializedHeader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getContentFormat() {
/* 107 */     return new HashMap<String, String>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 116 */     return "application/octet-stream";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class PrivateObjectOutputStream
/*     */     extends ObjectOutputStream
/*     */   {
/*     */     public PrivateObjectOutputStream(OutputStream os) throws IOException {
/* 125 */       super(os);
/*     */     }
/*     */     
/*     */     protected void writeStreamHeader() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\layout\SerializedLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */