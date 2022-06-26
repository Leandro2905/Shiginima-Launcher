/*    */ package org.jivesoftware.smack.packet.id;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import org.jivesoftware.smack.util.StringUtils;
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
/*    */ public class StanzaIdUtil
/*    */ {
/* 28 */   private static final String PREFIX = StringUtils.randomString(5) + "-";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   private static final AtomicLong ID = new AtomicLong();
/*    */   
/*    */   public static String newStanzaId() {
/* 37 */     return PREFIX + Long.toString(ID.incrementAndGet());
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\id\StanzaIdUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */