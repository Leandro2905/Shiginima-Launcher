/*    */ package org.jivesoftware.smack.compression;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
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
/*    */ public abstract class XMPPInputOutputStream
/*    */ {
/*    */   protected static FlushMethod flushMethod;
/*    */   protected final String compressionMethod;
/*    */   
/*    */   public static void setFlushMethod(FlushMethod flushMethod) {
/* 36 */     XMPPInputOutputStream.flushMethod = flushMethod;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected XMPPInputOutputStream(String compressionMethod) {
/* 42 */     this.compressionMethod = compressionMethod;
/*    */   }
/*    */   
/*    */   public String getCompressionMethod() {
/* 46 */     return this.compressionMethod;
/*    */   }
/*    */   
/*    */   public abstract boolean isSupported();
/*    */   
/*    */   public abstract InputStream getInputStream(InputStream paramInputStream) throws IOException;
/*    */   
/*    */   public abstract OutputStream getOutputStream(OutputStream paramOutputStream) throws IOException;
/*    */   
/*    */   public enum FlushMethod {
/* 56 */     FULL_FLUSH,
/* 57 */     SYNC_FLUSH;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\compression\XMPPInputOutputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */