/*    */ package org.apache.commons.io.output;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class DemuxOutputStream
/*    */   extends OutputStream
/*    */ {
/* 31 */   private final InheritableThreadLocal<OutputStream> m_streams = new InheritableThreadLocal<OutputStream>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OutputStream bindStream(OutputStream output) {
/* 41 */     OutputStream stream = this.m_streams.get();
/* 42 */     this.m_streams.set(output);
/* 43 */     return stream;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 55 */     OutputStream output = this.m_streams.get();
/* 56 */     if (null != output)
/*    */     {
/* 58 */       output.close();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void flush() throws IOException {
/* 71 */     OutputStream output = this.m_streams.get();
/* 72 */     if (null != output)
/*    */     {
/* 74 */       output.flush();
/*    */     }
/*    */   }
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
/*    */   public void write(int ch) throws IOException {
/* 88 */     OutputStream output = this.m_streams.get();
/* 89 */     if (null != output)
/*    */     {
/* 91 */       output.write(ch);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\output\DemuxOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */