/*    */ package org.jivesoftware.smack.debugger;
/*    */ 
/*    */ import java.io.Reader;
/*    */ import java.io.Writer;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import org.jivesoftware.smack.XMPPConnection;
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
/*    */ 
/*    */ 
/*    */ public class ConsoleDebugger
/*    */   extends AbstractDebugger
/*    */ {
/* 38 */   private final SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm:ss aaa");
/*    */   
/*    */   public ConsoleDebugger(XMPPConnection connection, Writer writer, Reader reader) {
/* 41 */     super(connection, writer, reader);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void log(String logMessage) {
/*    */     String formatedDate;
/* 47 */     synchronized (this.dateFormatter) {
/* 48 */       formatedDate = this.dateFormatter.format(new Date());
/*    */     } 
/* 50 */     System.out.println(formatedDate + ' ' + logMessage);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\debugger\ConsoleDebugger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */