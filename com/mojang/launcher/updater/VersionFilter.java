/*    */ package com.mojang.launcher.updater;
/*    */ 
/*    */ import com.google.common.collect.Iterables;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.mojang.launcher.versions.ReleaseType;
/*    */ import com.mojang.launcher.versions.ReleaseTypeFactory;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class VersionFilter<T extends ReleaseType>
/*    */ {
/* 12 */   private final Set<T> types = Sets.newHashSet();
/* 13 */   private int maxCount = 5;
/*    */ 
/*    */   
/*    */   public VersionFilter(ReleaseTypeFactory<T> factory) {
/* 17 */     Iterables.addAll(this.types, (Iterable)factory);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<T> getTypes() {
/* 22 */     return this.types;
/*    */   }
/*    */ 
/*    */   
/*    */   public VersionFilter<T> onlyForTypes(T... types) {
/* 27 */     this.types.clear();
/* 28 */     includeTypes(types);
/* 29 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public VersionFilter<T> includeTypes(T... types) {
/* 34 */     if (types != null) {
/* 35 */       Collections.addAll(this.types, types);
/*    */     }
/* 37 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public VersionFilter<T> excludeTypes(T... types) {
/* 42 */     if (types != null) {
/* 43 */       for (T type : types) {
/* 44 */         this.types.remove(type);
/*    */       }
/*    */     }
/* 47 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxCount() {
/* 52 */     return this.maxCount;
/*    */   }
/*    */ 
/*    */   
/*    */   public VersionFilter<T> setMaxCount(int maxCount) {
/* 57 */     this.maxCount = maxCount;
/* 58 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launche\\updater\VersionFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */