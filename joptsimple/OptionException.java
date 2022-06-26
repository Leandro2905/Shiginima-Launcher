/*    */ package joptsimple;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class OptionException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -1L;
/* 45 */   private final List<String> options = new ArrayList<String>();
/*    */   
/*    */   protected OptionException(Collection<String> options) {
/* 48 */     this.options.addAll(options);
/*    */   }
/*    */   
/*    */   protected OptionException(Collection<String> options, Throwable cause) {
/* 52 */     super(cause);
/*    */     
/* 54 */     this.options.addAll(options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> options() {
/* 63 */     return Collections.unmodifiableCollection(this.options);
/*    */   }
/*    */   
/*    */   protected final String singleOptionMessage() {
/* 67 */     return singleOptionMessage(this.options.get(0));
/*    */   }
/*    */   
/*    */   protected final String singleOptionMessage(String option) {
/* 71 */     return "'" + option + "'";
/*    */   }
/*    */   
/*    */   protected final String multipleOptionMessage() {
/* 75 */     StringBuilder buffer = new StringBuilder("[");
/*    */     
/* 77 */     for (Iterator<String> iter = this.options.iterator(); iter.hasNext(); ) {
/* 78 */       buffer.append(singleOptionMessage(iter.next()));
/* 79 */       if (iter.hasNext()) {
/* 80 */         buffer.append(", ");
/*    */       }
/*    */     } 
/* 83 */     buffer.append(']');
/*    */     
/* 85 */     return buffer.toString();
/*    */   }
/*    */   
/*    */   static OptionException unrecognizedOption(String option) {
/* 89 */     return new UnrecognizedOptionException(option);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\OptionException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */