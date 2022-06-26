/*    */ package org.xmlpull.v1;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XmlPullParserException
/*    */   extends Exception
/*    */ {
/*    */   protected Throwable detail;
/* 13 */   protected int row = -1;
/* 14 */   protected int column = -1;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XmlPullParserException(String s) {
/* 20 */     super(s);
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
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XmlPullParserException(String msg, XmlPullParser parser, Throwable chain) {
/* 37 */     super(((msg == null) ? "" : (msg + " ")) + ((parser == null) ? "" : ("(position:" + parser.getPositionDescription() + ") ")) + ((chain == null) ? "" : ("caused by: " + chain)));
/*    */ 
/*    */ 
/*    */     
/* 41 */     if (parser != null) {
/* 42 */       this.row = parser.getLineNumber();
/* 43 */       this.column = parser.getColumnNumber();
/*    */     } 
/* 45 */     this.detail = chain;
/*    */   }
/*    */   public Throwable getDetail() {
/* 48 */     return this.detail;
/*    */   }
/* 50 */   public int getLineNumber() { return this.row; } public int getColumnNumber() {
/* 51 */     return this.column;
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
/*    */   
/*    */   public void printStackTrace() {
/* 65 */     if (this.detail == null) {
/* 66 */       super.printStackTrace();
/*    */     } else {
/* 68 */       synchronized (System.err) {
/* 69 */         System.err.println(getMessage() + "; nested exception is:");
/* 70 */         this.detail.printStackTrace();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\XmlPullParserException.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */