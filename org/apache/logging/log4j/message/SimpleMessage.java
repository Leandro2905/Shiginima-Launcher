/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleMessage
/*     */   implements Message
/*     */ {
/*     */   private static final long serialVersionUID = -8398002534962715992L;
/*     */   private final String message;
/*     */   
/*     */   public SimpleMessage() {
/*  31 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleMessage(String message) {
/*  39 */     this.message = message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  48 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  57 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  71 */     if (this == o) {
/*  72 */       return true;
/*     */     }
/*  74 */     if (o == null || getClass() != o.getClass()) {
/*  75 */       return false;
/*     */     }
/*     */     
/*  78 */     SimpleMessage that = (SimpleMessage)o;
/*     */     
/*  80 */     if ((this.message != null) ? !this.message.equals(that.message) : (that.message != null)) return false;
/*     */   
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  85 */     return (this.message != null) ? this.message.hashCode() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  90 */     return "SimpleMessage[message=" + this.message + ']';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 100 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\message\SimpleMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */