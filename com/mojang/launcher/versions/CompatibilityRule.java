/*     */ package com.mojang.launcher.versions;
/*     */ 
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompatibilityRule
/*     */ {
/*     */   public enum Action
/*     */   {
/*  13 */     ALLOW, DISALLOW;
/*     */   }
/*     */ 
/*     */   
/*     */   public class OSRestriction
/*     */   {
/*     */     private OperatingSystem name;
/*     */     
/*     */     private String version;
/*     */     
/*     */     private String arch;
/*     */     
/*     */     public OSRestriction() {}
/*     */     
/*     */     public OperatingSystem getName() {
/*  28 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getVersion() {
/*  33 */       return this.version;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getArch() {
/*  38 */       return this.arch;
/*     */     }
/*     */ 
/*     */     
/*     */     public OSRestriction(OSRestriction osRestriction) {
/*  43 */       this.name = osRestriction.name;
/*  44 */       this.version = osRestriction.version;
/*  45 */       this.arch = osRestriction.arch;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isCurrentOperatingSystem() {
/*  50 */       if (this.name != null && this.name != OperatingSystem.getCurrentPlatform()) {
/*  51 */         return false;
/*     */       }
/*  53 */       if (this.version != null) {
/*     */         
/*     */         try {
/*  56 */           Pattern pattern = Pattern.compile(this.version);
/*  57 */           Matcher matcher = pattern.matcher(System.getProperty("os.version"));
/*  58 */           if (!matcher.matches()) {
/*  59 */             return false;
/*     */           }
/*     */         }
/*  62 */         catch (Throwable throwable) {}
/*     */       }
/*  64 */       if (this.arch != null) {
/*     */         
/*     */         try {
/*  67 */           Pattern pattern = Pattern.compile(this.arch);
/*  68 */           Matcher matcher = pattern.matcher(System.getProperty("os.arch"));
/*  69 */           if (!matcher.matches()) {
/*  70 */             return false;
/*     */           }
/*     */         }
/*  73 */         catch (Throwable throwable) {}
/*     */       }
/*  75 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  80 */       return "OSRestriction{name=" + this.name + ", version='" + this.version + '\'' + ", arch='" + this.arch + '\'' + '}';
/*     */     }
/*     */   }
/*     */   
/*  84 */   private Action action = Action.ALLOW;
/*     */   
/*     */   private OSRestriction os;
/*     */   
/*     */   public CompatibilityRule(CompatibilityRule compatibilityRule) {
/*  89 */     this.action = compatibilityRule.action;
/*  90 */     if (compatibilityRule.os != null)
/*  91 */       this.os = new OSRestriction(compatibilityRule.os); 
/*     */   }
/*     */   
/*     */   public CompatibilityRule() {}
/*     */   
/*     */   public Action getAppliedAction() {
/*  97 */     if (this.os != null && !this.os.isCurrentOperatingSystem()) {
/*  98 */       return null;
/*     */     }
/* 100 */     return this.action;
/*     */   }
/*     */ 
/*     */   
/*     */   public Action getAction() {
/* 105 */     return this.action;
/*     */   }
/*     */ 
/*     */   
/*     */   public OSRestriction getOs() {
/* 110 */     return this.os;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 115 */     return "Rule{action=" + this.action + ", os=" + this.os + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\versions\CompatibilityRule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */