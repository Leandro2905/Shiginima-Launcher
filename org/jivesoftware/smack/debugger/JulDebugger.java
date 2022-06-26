/*    */ package org.jivesoftware.smack.debugger;
/*    */ 
/*    */ import java.io.Reader;
/*    */ import java.io.Writer;
/*    */ import java.util.logging.Logger;
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
/*    */ 
/*    */ public class JulDebugger
/*    */   extends AbstractDebugger
/*    */ {
/* 38 */   private static final Logger LOGGER = Logger.getLogger(JulDebugger.class.getName());
/*    */   
/*    */   public JulDebugger(XMPPConnection connection, Writer writer, Reader reader) {
/* 41 */     super(connection, writer, reader);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void log(String logMessage) {
/* 46 */     LOGGER.fine(logMessage);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\debugger\JulDebugger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */