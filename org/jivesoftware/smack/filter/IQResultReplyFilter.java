/*    */ package org.jivesoftware.smack.filter;
/*    */ 
/*    */ import org.jivesoftware.smack.XMPPConnection;
/*    */ import org.jivesoftware.smack.packet.IQ;
/*    */ import org.jivesoftware.smack.packet.Stanza;
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
/*    */ public class IQResultReplyFilter
/*    */   extends IQReplyFilter
/*    */ {
/*    */   public IQResultReplyFilter(IQ iqPacket, XMPPConnection conn) {
/* 30 */     super(iqPacket, conn);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accept(Stanza packet) {
/* 35 */     if (!super.accept(packet)) {
/* 36 */       return false;
/*    */     }
/* 38 */     return IQTypeFilter.RESULT.accept(packet);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 43 */     StringBuilder sb = new StringBuilder();
/* 44 */     sb.append(getClass().getSimpleName());
/* 45 */     sb.append(" (" + super.toString() + ')');
/* 46 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\IQResultReplyFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */