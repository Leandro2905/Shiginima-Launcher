/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
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
/*     */ public final class ExtendedStackTraceElement
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2171069569241280505L;
/*     */   private final ExtendedClassInfo extraClassInfo;
/*     */   private final StackTraceElement stackTraceElement;
/*     */   
/*     */   public ExtendedStackTraceElement(StackTraceElement stackTraceElement, ExtendedClassInfo extraClassInfo) {
/*  43 */     this.stackTraceElement = stackTraceElement;
/*  44 */     this.extraClassInfo = extraClassInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedStackTraceElement(String declaringClass, String methodName, String fileName, int lineNumber, boolean exact, String location, String version) {
/*  52 */     this(new StackTraceElement(declaringClass, methodName, fileName, lineNumber), new ExtendedClassInfo(exact, location, version));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  58 */     if (this == obj) {
/*  59 */       return true;
/*     */     }
/*  61 */     if (obj == null) {
/*  62 */       return false;
/*     */     }
/*  64 */     if (!(obj instanceof ExtendedStackTraceElement)) {
/*  65 */       return false;
/*     */     }
/*  67 */     ExtendedStackTraceElement other = (ExtendedStackTraceElement)obj;
/*  68 */     if (this.extraClassInfo == null) {
/*  69 */       if (other.extraClassInfo != null) {
/*  70 */         return false;
/*     */       }
/*  72 */     } else if (!this.extraClassInfo.equals(other.extraClassInfo)) {
/*  73 */       return false;
/*     */     } 
/*  75 */     if (this.stackTraceElement == null) {
/*  76 */       if (other.stackTraceElement != null) {
/*  77 */         return false;
/*     */       }
/*  79 */     } else if (!this.stackTraceElement.equals(other.stackTraceElement)) {
/*  80 */       return false;
/*     */     } 
/*  82 */     return true;
/*     */   }
/*     */   
/*     */   public String getClassName() {
/*  86 */     return this.stackTraceElement.getClassName();
/*     */   }
/*     */   
/*     */   public boolean getExact() {
/*  90 */     return this.extraClassInfo.getExact();
/*     */   }
/*     */   
/*     */   public ExtendedClassInfo getExtraClassInfo() {
/*  94 */     return this.extraClassInfo;
/*     */   }
/*     */   
/*     */   public String getFileName() {
/*  98 */     return this.stackTraceElement.getFileName();
/*     */   }
/*     */   
/*     */   public int getLineNumber() {
/* 102 */     return this.stackTraceElement.getLineNumber();
/*     */   }
/*     */   
/*     */   public String getLocation() {
/* 106 */     return this.extraClassInfo.getLocation();
/*     */   }
/*     */   
/*     */   public String getMethodName() {
/* 110 */     return this.stackTraceElement.getMethodName();
/*     */   }
/*     */   
/*     */   public StackTraceElement getStackTraceElement() {
/* 114 */     return this.stackTraceElement;
/*     */   }
/*     */   
/*     */   public String getVersion() {
/* 118 */     return this.extraClassInfo.getVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 123 */     int prime = 31;
/* 124 */     int result = 1;
/* 125 */     result = 31 * result + ((this.extraClassInfo == null) ? 0 : this.extraClassInfo.hashCode());
/* 126 */     result = 31 * result + ((this.stackTraceElement == null) ? 0 : this.stackTraceElement.hashCode());
/* 127 */     return result;
/*     */   }
/*     */   
/*     */   public boolean isNativeMethod() {
/* 131 */     return this.stackTraceElement.isNativeMethod();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 136 */     StringBuilder sb = new StringBuilder();
/* 137 */     sb.append(this.stackTraceElement);
/* 138 */     sb.append(" ");
/* 139 */     sb.append(this.extraClassInfo);
/* 140 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\impl\ExtendedStackTraceElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */