/*    */ package com.mojang.launcher.versions;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ExtractRules
/*    */ {
/*  9 */   private List<String> exclude = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public ExtractRules() {}
/*    */   
/*    */   public ExtractRules(String... exclude) {
/* 15 */     if (exclude != null) {
/* 16 */       Collections.addAll(this.exclude, exclude);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public ExtractRules(ExtractRules rules) {
/* 22 */     for (String exclude : rules.exclude) {
/* 23 */       this.exclude.add(exclude);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> getExcludes() {
/* 29 */     return this.exclude;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExtract(String path) {
/* 34 */     if (this.exclude != null) {
/* 35 */       for (String rule : this.exclude) {
/* 36 */         if (path.startsWith(rule)) {
/* 37 */           return false;
/*    */         }
/*    */       } 
/*    */     }
/* 41 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\versions\ExtractRules.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */