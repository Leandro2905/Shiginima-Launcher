/*     */ package javax.xml.namespace;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
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
/*     */ public class QName
/*     */   implements Serializable
/*     */ {
/*  77 */   private static final String emptyString = "".intern();
/*     */ 
/*     */ 
/*     */   
/*     */   private String namespaceURI;
/*     */ 
/*     */ 
/*     */   
/*     */   private String localPart;
/*     */ 
/*     */ 
/*     */   
/*     */   private String prefix;
/*     */ 
/*     */ 
/*     */   
/*     */   public QName(String localPart) {
/*  94 */     this(emptyString, localPart, emptyString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QName(String namespaceURI, String localPart) {
/* 104 */     this(namespaceURI, localPart, emptyString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QName(String namespaceURI, String localPart, String prefix) {
/* 115 */     this.namespaceURI = (namespaceURI == null) ? emptyString : namespaceURI.intern();
/*     */ 
/*     */     
/* 118 */     if (localPart == null) {
/* 119 */       throw new IllegalArgumentException("invalid QName local part");
/*     */     }
/* 121 */     this.localPart = localPart.intern();
/*     */ 
/*     */     
/* 124 */     if (prefix == null) {
/* 125 */       throw new IllegalArgumentException("invalid QName prefix");
/*     */     }
/* 127 */     this.prefix = prefix.intern();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNamespaceURI() {
/* 137 */     return this.namespaceURI;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocalPart() {
/* 146 */     return this.localPart;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrefix() {
/* 155 */     return this.prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 165 */     return (this.namespaceURI == emptyString) ? this.localPart : ('{' + this.namespaceURI + '}' + this.localPart);
/*     */   }
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
/*     */   public final boolean equals(Object obj) {
/* 191 */     if (obj == this) {
/* 192 */       return true;
/*     */     }
/*     */     
/* 195 */     if (!(obj instanceof QName)) {
/* 196 */       return false;
/*     */     }
/*     */     
/* 199 */     if (this.namespaceURI == ((QName)obj).namespaceURI && this.localPart == ((QName)obj).localPart)
/*     */     {
/* 201 */       return true;
/*     */     }
/*     */     
/* 204 */     return false;
/*     */   }
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
/*     */   public static QName valueOf(String s) {
/* 225 */     if (s == null || s.equals("")) {
/* 226 */       throw new IllegalArgumentException("invalid QName literal");
/*     */     }
/*     */     
/* 229 */     if (s.charAt(0) == '{') {
/* 230 */       int i = s.indexOf('}');
/*     */       
/* 232 */       if (i == -1) {
/* 233 */         throw new IllegalArgumentException("invalid QName literal");
/*     */       }
/*     */       
/* 236 */       if (i == s.length() - 1) {
/* 237 */         throw new IllegalArgumentException("invalid QName literal");
/*     */       }
/* 239 */       return new QName(s.substring(1, i), s.substring(i + 1));
/*     */     } 
/*     */     
/* 242 */     return new QName(s);
/*     */   }
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
/*     */   public final int hashCode() {
/* 255 */     return this.namespaceURI.hashCode() ^ this.localPart.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 264 */     in.defaultReadObject();
/*     */     
/* 266 */     this.namespaceURI = this.namespaceURI.intern();
/* 267 */     this.localPart = this.localPart.intern();
/* 268 */     this.prefix = this.prefix.intern();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\javax\xml\namespace\QName.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */