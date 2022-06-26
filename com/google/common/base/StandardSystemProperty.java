/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import javax.annotation.Nullable;
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
/*     */ @Beta
/*     */ @GwtIncompatible("java.lang.System#getProperty")
/*     */ public enum StandardSystemProperty
/*     */ {
/*  35 */   JAVA_VERSION("java.version"),
/*     */ 
/*     */   
/*  38 */   JAVA_VENDOR("java.vendor"),
/*     */ 
/*     */   
/*  41 */   JAVA_VENDOR_URL("java.vendor.url"),
/*     */ 
/*     */   
/*  44 */   JAVA_HOME("java.home"),
/*     */ 
/*     */   
/*  47 */   JAVA_VM_SPECIFICATION_VERSION("java.vm.specification.version"),
/*     */ 
/*     */   
/*  50 */   JAVA_VM_SPECIFICATION_VENDOR("java.vm.specification.vendor"),
/*     */ 
/*     */   
/*  53 */   JAVA_VM_SPECIFICATION_NAME("java.vm.specification.name"),
/*     */ 
/*     */   
/*  56 */   JAVA_VM_VERSION("java.vm.version"),
/*     */ 
/*     */   
/*  59 */   JAVA_VM_VENDOR("java.vm.vendor"),
/*     */ 
/*     */   
/*  62 */   JAVA_VM_NAME("java.vm.name"),
/*     */ 
/*     */   
/*  65 */   JAVA_SPECIFICATION_VERSION("java.specification.version"),
/*     */ 
/*     */   
/*  68 */   JAVA_SPECIFICATION_VENDOR("java.specification.vendor"),
/*     */ 
/*     */   
/*  71 */   JAVA_SPECIFICATION_NAME("java.specification.name"),
/*     */ 
/*     */   
/*  74 */   JAVA_CLASS_VERSION("java.class.version"),
/*     */ 
/*     */   
/*  77 */   JAVA_CLASS_PATH("java.class.path"),
/*     */ 
/*     */   
/*  80 */   JAVA_LIBRARY_PATH("java.library.path"),
/*     */ 
/*     */   
/*  83 */   JAVA_IO_TMPDIR("java.io.tmpdir"),
/*     */ 
/*     */   
/*  86 */   JAVA_COMPILER("java.compiler"),
/*     */ 
/*     */   
/*  89 */   JAVA_EXT_DIRS("java.ext.dirs"),
/*     */ 
/*     */   
/*  92 */   OS_NAME("os.name"),
/*     */ 
/*     */   
/*  95 */   OS_ARCH("os.arch"),
/*     */ 
/*     */   
/*  98 */   OS_VERSION("os.version"),
/*     */ 
/*     */   
/* 101 */   FILE_SEPARATOR("file.separator"),
/*     */ 
/*     */   
/* 104 */   PATH_SEPARATOR("path.separator"),
/*     */ 
/*     */   
/* 107 */   LINE_SEPARATOR("line.separator"),
/*     */ 
/*     */   
/* 110 */   USER_NAME("user.name"),
/*     */ 
/*     */   
/* 113 */   USER_HOME("user.home"),
/*     */ 
/*     */   
/* 116 */   USER_DIR("user.dir");
/*     */   
/*     */   private final String key;
/*     */   
/*     */   StandardSystemProperty(String key) {
/* 121 */     this.key = key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String key() {
/* 128 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String value() {
/* 137 */     return System.getProperty(this.key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 144 */     String str1 = String.valueOf(String.valueOf(key())), str2 = String.valueOf(String.valueOf(value())); return (new StringBuilder(1 + str1.length() + str2.length())).append(str1).append("=").append(str2).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\StandardSystemProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */