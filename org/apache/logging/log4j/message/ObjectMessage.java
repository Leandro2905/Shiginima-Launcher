/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjectMessage
/*     */   implements Message
/*     */ {
/*     */   private static final long serialVersionUID = -5903272448334166185L;
/*     */   private transient Object obj;
/*     */   
/*     */   public ObjectMessage(Object obj) {
/*  38 */     if (obj == null) {
/*  39 */       obj = "null";
/*     */     }
/*  41 */     this.obj = obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  50 */     return this.obj.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  59 */     return this.obj.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/*  68 */     return new Object[] { this.obj };
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  73 */     if (this == o) {
/*  74 */       return true;
/*     */     }
/*  76 */     if (o == null || getClass() != o.getClass()) {
/*  77 */       return false;
/*     */     }
/*     */     
/*  80 */     ObjectMessage that = (ObjectMessage)o;
/*     */     
/*  82 */     if ((this.obj != null) ? !this.obj.equals(that.obj) : (that.obj != null)) return false;
/*     */   
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  87 */     return (this.obj != null) ? this.obj.hashCode() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  92 */     return "ObjectMessage[obj=" + this.obj.toString() + ']';
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/*  96 */     out.defaultWriteObject();
/*  97 */     if (this.obj instanceof java.io.Serializable) {
/*  98 */       out.writeObject(this.obj);
/*     */     } else {
/* 100 */       out.writeObject(this.obj.toString());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 105 */     in.defaultReadObject();
/* 106 */     this.obj = in.readObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 116 */     return (this.obj instanceof Throwable) ? (Throwable)this.obj : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\message\ObjectMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */