/*     */ package org.apache.logging.log4j.core.net.server;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.LogEventListener;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class InputStreamLogEventBridge
/*     */   extends AbstractLogEventBridge<InputStream>
/*     */ {
/*     */   private final int bufferSize;
/*     */   private final Charset charset;
/*     */   private final String eventEndMarker;
/*     */   private final ObjectReader objectReader;
/*     */   
/*     */   public InputStreamLogEventBridge(ObjectMapper mapper, int bufferSize, Charset charset, String eventEndMarker) {
/*  45 */     this.bufferSize = bufferSize;
/*  46 */     this.charset = charset;
/*  47 */     this.eventEndMarker = eventEndMarker;
/*  48 */     this.objectReader = mapper.reader(Log4jLogEvent.class);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract int[] getEventIndices(String paramString, int paramInt);
/*     */   
/*     */   public void logEvents(InputStream inputStream, LogEventListener logEventListener) throws IOException {
/*  55 */     String workingText = "";
/*     */     
/*     */     try {
/*  58 */       byte[] buffer = new byte[this.bufferSize];
/*  59 */       String textRemains = workingText = "";
/*     */       
/*     */       label25: while (true) {
/*  62 */         int streamReadLength = inputStream.read(buffer);
/*  63 */         if (streamReadLength == -1) {
/*     */           break;
/*     */         }
/*     */         
/*  67 */         String text = workingText = textRemains + new String(buffer, 0, streamReadLength, this.charset);
/*  68 */         int beginIndex = 0;
/*     */         
/*     */         while (true) {
/*  71 */           int[] pair = getEventIndices(text, beginIndex);
/*  72 */           int eventStartMarkerIndex = pair[0];
/*  73 */           if (eventStartMarkerIndex < 0) {
/*     */ 
/*     */             
/*  76 */             textRemains = text.substring(beginIndex);
/*     */             continue label25;
/*     */           } 
/*  79 */           int eventEndMarkerIndex = pair[1];
/*  80 */           if (eventEndMarkerIndex > 0) {
/*  81 */             int eventEndXmlIndex = eventEndMarkerIndex + this.eventEndMarker.length();
/*  82 */             String textEvent = workingText = text.substring(eventStartMarkerIndex, eventEndXmlIndex);
/*  83 */             Log4jLogEvent log4jLogEvent = unmarshal(textEvent);
/*  84 */             logEventListener.log((LogEvent)log4jLogEvent);
/*  85 */             beginIndex = eventEndXmlIndex; continue;
/*     */           } 
/*     */           break;
/*     */         } 
/*  89 */         textRemains = text.substring(beginIndex);
/*     */       
/*     */       }
/*     */     
/*     */     }
/*  94 */     catch (IOException ex) {
/*  95 */       logger.error(workingText, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Log4jLogEvent unmarshal(String jsonEvent) throws IOException {
/* 100 */     return (Log4jLogEvent)this.objectReader.readValue(jsonEvent);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\server\InputStreamLogEventBridge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */