/*     */ package net.minecraft.launcher.updater;
/*     */ 
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import com.mojang.launcher.updater.download.ChecksummedDownloadable;
/*     */ import com.mojang.launcher.updater.download.Downloadable;
/*     */ import com.mojang.launcher.versions.ExtractRules;
/*     */ import java.io.File;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.launcher.CompatibilityRule;
/*     */ import org.apache.commons.lang3.text.StrSubstitutor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Library
/*     */ {
/*  25 */   private static Map valuesMap = new HashMap<>();
/*     */   private static StrSubstitutor SUBSTITUTOR;
/*     */   private String name;
/*     */   private List<CompatibilityRule> rules;
/*     */   private Map<OperatingSystem, String> natives;
/*     */   private ExtractRules extract;
/*     */   private String url;
/*     */   private LibraryDownloadInfo downloads;
/*     */   
/*     */   private void initSubstitutor() {
/*  35 */     valuesMap.put("arch", System.getProperty("sun.arch.data.model"));
/*  36 */     SUBSTITUTOR = new StrSubstitutor(valuesMap);
/*     */   }
/*     */   
/*     */   public Library() {
/*  40 */     initSubstitutor();
/*     */   }
/*     */   
/*     */   public Library(String name) {
/*  44 */     initSubstitutor();
/*  45 */     if (name == null || name.length() == 0) {
/*  46 */       throw new IllegalArgumentException("Library name cannot be null or empty");
/*     */     }
/*  48 */     this.name = name;
/*     */   }
/*     */   
/*     */   public Library(Library library) {
/*  52 */     initSubstitutor();
/*  53 */     this.name = library.name;
/*  54 */     this.url = library.url;
/*  55 */     if (library.extract != null) {
/*  56 */       this.extract = new ExtractRules(library.extract);
/*     */     }
/*  58 */     if (library.rules != null) {
/*  59 */       this.rules = new ArrayList<>();
/*  60 */       for (CompatibilityRule compatibilityRule : library.rules) {
/*  61 */         this.rules.add(new CompatibilityRule(compatibilityRule));
/*     */       }
/*     */     } 
/*  64 */     if (library.natives != null) {
/*  65 */       this.natives = new LinkedHashMap<>();
/*  66 */       for (Map.Entry<OperatingSystem, String> entry : library.getNatives().entrySet()) {
/*  67 */         this.natives.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     } 
/*  70 */     if (library.downloads != null) {
/*  71 */       this.downloads = new LibraryDownloadInfo(library.downloads);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getName() {
/*  76 */     return this.name;
/*     */   }
/*     */   
/*     */   public Library addNative(OperatingSystem operatingSystem, String name) {
/*  80 */     if (operatingSystem == null || !operatingSystem.isSupported()) {
/*  81 */       throw new IllegalArgumentException("Cannot add native for unsupported OS");
/*     */     }
/*  83 */     if (name == null || name.length() == 0) {
/*  84 */       throw new IllegalArgumentException("Cannot add native for null or empty name");
/*     */     }
/*  86 */     if (this.natives == null) {
/*  87 */       this.natives = new EnumMap<>(OperatingSystem.class);
/*     */     }
/*  89 */     this.natives.put(operatingSystem, name);
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public List<CompatibilityRule> getCompatibilityRules() {
/*  94 */     return this.rules;
/*     */   }
/*     */   
/*     */   public boolean appliesToCurrentEnvironment(CompatibilityRule.FeatureMatcher featureMatcher) {
/*  98 */     if (this.rules == null) {
/*  99 */       return true;
/*     */     }
/* 101 */     CompatibilityRule.Action lastAction = CompatibilityRule.Action.DISALLOW;
/* 102 */     for (CompatibilityRule compatibilityRule : this.rules) {
/* 103 */       CompatibilityRule.Action action = compatibilityRule.getAppliedAction(featureMatcher);
/* 104 */       if (action != null) {
/* 105 */         lastAction = action;
/*     */       }
/*     */     } 
/* 108 */     return (lastAction == CompatibilityRule.Action.ALLOW);
/*     */   }
/*     */   
/*     */   public Map<OperatingSystem, String> getNatives() {
/* 112 */     return this.natives;
/*     */   }
/*     */   
/*     */   public ExtractRules getExtractRules() {
/* 116 */     return this.extract;
/*     */   }
/*     */   
/*     */   public Library setExtractRules(ExtractRules rules) {
/* 120 */     this.extract = rules;
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public String getArtifactBaseDir() {
/* 125 */     if (this.name == null) {
/* 126 */       throw new IllegalStateException("Cannot get artifact dir of empty/blank artifact");
/*     */     }
/* 128 */     String[] parts = this.name.split(":", 3);
/* 129 */     return String.format("%s/%s/%s", new Object[] { parts[0].replaceAll("\\.", "/"), parts[1], parts[2] });
/*     */   }
/*     */   
/*     */   public String getArtifactPath() {
/* 133 */     if (this.name == null) {
/* 134 */       throw new IllegalStateException("Cannot get artifact path of empty/blank artifact");
/*     */     }
/* 136 */     return String.format("%s/%s", new Object[] { getArtifactBaseDir(), getArtifactFilename() });
/*     */   }
/*     */   
/*     */   public String getArtifactPath(String classifier) {
/* 140 */     if (this.name == null) {
/* 141 */       throw new IllegalStateException("Cannot get artifact path of empty/blank artifact");
/*     */     }
/* 143 */     return String.format("%s/%s", new Object[] { getArtifactBaseDir(), getArtifactFilename(classifier) });
/*     */   }
/*     */   
/*     */   public String getArtifactFilename() {
/* 147 */     if (this.name == null) {
/* 148 */       throw new IllegalStateException("Cannot get artifact filename of empty/blank artifact");
/*     */     }
/* 150 */     String[] parts = this.name.split(":", 3);
/* 151 */     return String.format("%s-%s.jar", new Object[] { parts[1], parts[2] });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getArtifactFilename(String classifier) {
/* 160 */     if (this.name == null) {
/* 161 */       throw new IllegalStateException("Cannot get artifact filename of empty/blank artifact");
/*     */     }
/* 163 */     String[] parts = this.name.split(":", 3);
/* 164 */     String result = String.format("%s-%s%s.jar", new Object[] { parts[1], parts[2], "-" + classifier });
/*     */     
/* 166 */     return SUBSTITUTOR.replace(result);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 198 */     return "Library{name='" + this.name + '\'' + ", rules=" + this.rules + ", natives=" + this.natives + ", extract=" + this.extract + '}';
/*     */   }
/*     */   
/*     */   public Downloadable createDownload(Proxy proxy, String path, File local, boolean ignoreLocalFiles, String classifier) throws MalformedURLException {
/* 202 */     if (this.url != null) {
/* 203 */       URL url = new URL(this.url + path);
/* 204 */       return (Downloadable)new ChecksummedDownloadable(proxy, url, local, ignoreLocalFiles);
/*     */     } 
/* 206 */     if (this.downloads == null) {
/* 207 */       URL url = new URL("https://libraries.minecraft.net/" + path);
/* 208 */       return (Downloadable)new ChecksummedDownloadable(proxy, url, local, ignoreLocalFiles);
/*     */     } 
/* 210 */     AbstractDownloadInfo info = this.downloads.getDownloadInfo(SUBSTITUTOR.replace(classifier));
/* 211 */     if (info != null) {
/* 212 */       URL url = info.getUrl();
/* 213 */       if (url != null) {
/* 214 */         return new PreHashedDownloadable(proxy, url, local, ignoreLocalFiles, info.getSha1());
/*     */       }
/*     */     } 
/* 217 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\updater\Library.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */