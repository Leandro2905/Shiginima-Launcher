/*    */ package org.xmlpull.v1.builder;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XmlBuilderException
/*    */   extends RuntimeException
/*    */ {
/*    */   protected Throwable detail;
/*    */   
/*    */   public XmlBuilderException(String s) {
/* 18 */     super(s);
/*    */   }
/*    */ 
/*    */   
/*    */   public XmlBuilderException(String s, Throwable thrwble) {
/* 23 */     super(s);
/* 24 */     this.detail = thrwble;
/*    */   }
/*    */   
/*    */   public Throwable getDetail() {
/* 28 */     return this.detail;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 32 */     if (this.detail == null) {
/* 33 */       return super.getMessage();
/*    */     }
/* 35 */     return super.getMessage() + "; nested exception is: \n\t" + this.detail.getMessage();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void printStackTrace(PrintStream ps) {
/* 41 */     if (this.detail == null) {
/* 42 */       super.printStackTrace(ps);
/*    */     } else {
/* 44 */       synchronized (ps) {
/*    */         
/* 46 */         ps.println(super.getMessage() + "; nested exception is:");
/* 47 */         this.detail.printStackTrace(ps);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void printStackTrace() {
/* 53 */     printStackTrace(System.err);
/*    */   }
/*    */   
/*    */   public void printStackTrace(PrintWriter pw) {
/* 57 */     if (this.detail == null) {
/* 58 */       super.printStackTrace(pw);
/*    */     } else {
/* 60 */       synchronized (pw) {
/*    */         
/* 62 */         pw.println(super.getMessage() + "; nested exception is:");
/* 63 */         this.detail.printStackTrace(pw);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\XmlBuilderException.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */