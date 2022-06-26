/*    */ package org.jivesoftware.smack.filter;
/*    */ 
/*    */ import org.jivesoftware.smack.packet.IQ;
/*    */ import org.jivesoftware.smack.packet.Stanza;
/*    */ import org.jivesoftware.smack.util.Objects;
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
/*    */ public class IQTypeFilter
/*    */   extends FlexibleStanzaTypeFilter<IQ>
/*    */ {
/* 32 */   public static final StanzaFilter GET = new IQTypeFilter(IQ.Type.get);
/* 33 */   public static final StanzaFilter SET = new IQTypeFilter(IQ.Type.set);
/* 34 */   public static final StanzaFilter RESULT = new IQTypeFilter(IQ.Type.result);
/* 35 */   public static final StanzaFilter ERROR = new IQTypeFilter(IQ.Type.error);
/* 36 */   public static final StanzaFilter GET_OR_SET = new OrFilter(new StanzaFilter[] { GET, SET });
/*    */   
/*    */   private final IQ.Type type;
/*    */   
/*    */   private IQTypeFilter(IQ.Type type) {
/* 41 */     super(IQ.class);
/* 42 */     this.type = (IQ.Type)Objects.requireNonNull(type, "Type must not be null");
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean acceptSpecific(IQ iq) {
/* 47 */     return (iq.getType() == this.type);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 52 */     return getClass().getSimpleName() + ": type=" + this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\IQTypeFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */