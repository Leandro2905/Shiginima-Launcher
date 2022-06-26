/*     */ package net.minecraft.launcher;
/*     */ 
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompatibilityRule
/*     */ {
/*     */   public enum Action
/*     */   {
/*  15 */     ALLOW, DISALLOW;
/*     */   }
/*     */ 
/*     */   
/*     */   public static interface FeatureMatcher
/*     */   {
/*     */     boolean hasFeature(String param1String, Object param1Object);
/*     */   }
/*     */ 
/*     */   
/*     */   public class OSRestriction
/*     */   {
/*     */     private OperatingSystem name;
/*     */     
/*     */     private String version;
/*     */     private String arch;
/*     */     
/*     */     public OSRestriction() {}
/*     */     
/*     */     public OperatingSystem getName() {
/*  35 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getVersion() {
/*  40 */       return this.version;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getArch() {
/*  45 */       return this.arch;
/*     */     }
/*     */ 
/*     */     
/*     */     public OSRestriction(OSRestriction osRestriction) {
/*  50 */       this.name = osRestriction.name;
/*  51 */       this.version = osRestriction.version;
/*  52 */       this.arch = osRestriction.arch;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isCurrentOperatingSystem() {
/*  57 */       if (this.name != null && this.name != OperatingSystem.getCurrentPlatform()) {
/*  58 */         return false;
/*     */       }
/*  60 */       if (this.version != null) {
/*     */         
/*     */         try {
/*  63 */           Pattern pattern = Pattern.compile(this.version);
/*  64 */           Matcher matcher = pattern.matcher(System.getProperty("os.version"));
/*  65 */           if (!matcher.matches()) {
/*  66 */             return false;
/*     */           }
/*     */         }
/*  69 */         catch (Throwable throwable) {}
/*     */       }
/*  71 */       if (this.arch != null) {
/*     */         
/*     */         try {
/*  74 */           Pattern pattern = Pattern.compile(this.arch);
/*  75 */           Matcher matcher = pattern.matcher(System.getProperty("os.arch"));
/*  76 */           if (!matcher.matches()) {
/*  77 */             return false;
/*     */           }
/*     */         }
/*  80 */         catch (Throwable throwable) {}
/*     */       }
/*  82 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  87 */       return "OSRestriction{name=" + this.name + ", version='" + this.version + '\'' + ", arch='" + this.arch + '\'' + '}';
/*     */     }
/*     */   }
/*     */   
/*  91 */   private Action action = Action.ALLOW;
/*     */   
/*     */   private OSRestriction os;
/*     */ 
/*     */   
/*     */   public CompatibilityRule(CompatibilityRule compatibilityRule) {
/*  97 */     this.action = compatibilityRule.action;
/*  98 */     if (compatibilityRule.os != null) {
/*  99 */       this.os = new OSRestriction(compatibilityRule.os);
/*     */     }
/* 101 */     if (compatibilityRule.features != null)
/* 102 */       this.features = compatibilityRule.features; 
/*     */   }
/*     */   private Map<String, Object> features;
/*     */   public CompatibilityRule() {}
/*     */   
/*     */   public Action getAppliedAction(FeatureMatcher featureMatcher) {
/* 108 */     if (this.os != null && !this.os.isCurrentOperatingSystem()) {
/* 109 */       return null;
/*     */     }
/* 111 */     if (this.features != null) {
/*     */       
/* 113 */       if (featureMatcher == null) {
/* 114 */         return null;
/*     */       }
/* 116 */       for (Map.Entry<String, Object> feature : this.features.entrySet()) {
/* 117 */         if (!featureMatcher.hasFeature(feature.getKey(), feature.getValue())) {
/* 118 */           return null;
/*     */         }
/*     */       } 
/*     */     } 
/* 122 */     return this.action;
/*     */   }
/*     */ 
/*     */   
/*     */   public Action getAction() {
/* 127 */     return this.action;
/*     */   }
/*     */ 
/*     */   
/*     */   public OSRestriction getOs() {
/* 132 */     return this.os;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> getFeatures() {
/* 137 */     return this.features;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 142 */     return "Rule{action=" + this.action + ", os=" + this.os + ", features=" + this.features + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\CompatibilityRule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */