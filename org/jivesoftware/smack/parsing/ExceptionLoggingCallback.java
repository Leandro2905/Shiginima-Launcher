/*    */ package org.jivesoftware.smack.parsing;
/*    */ 
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
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
/*    */ public class ExceptionLoggingCallback
/*    */   extends ParsingExceptionCallback
/*    */ {
/* 30 */   private static final Logger LOGGER = Logger.getLogger(ExceptionLoggingCallback.class.getName());
/*    */ 
/*    */   
/*    */   public void handleUnparsablePacket(UnparsablePacket unparsed) throws Exception {
/* 34 */     LOGGER.log(Level.SEVERE, "Smack message parsing exception: ", unparsed.getParsingException());
/* 35 */     LOGGER.severe("Unparsed content: " + unparsed.getContent());
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\parsing\ExceptionLoggingCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */