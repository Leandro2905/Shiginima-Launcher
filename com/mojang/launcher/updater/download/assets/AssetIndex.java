/*     */ package com.mojang.launcher.updater.download.assets;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AssetIndex
/*     */ {
/*     */   public static final String DEFAULT_ASSET_NAME = "legacy";
/*  16 */   private Map<String, AssetObject> objects = new LinkedHashMap<>();
/*     */   
/*     */   private boolean virtual;
/*     */   
/*     */   public Map<String, AssetObject> getFileMap() {
/*  21 */     return this.objects;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<AssetObject, String> getUniqueObjects() {
/*  26 */     Map<AssetObject, String> result = Maps.newHashMap();
/*  27 */     for (Map.Entry<String, AssetObject> objectEntry : this.objects.entrySet()) {
/*  28 */       result.put(objectEntry.getValue(), objectEntry.getKey());
/*     */     }
/*  30 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isVirtual() {
/*  35 */     return this.virtual;
/*     */   }
/*     */ 
/*     */   
/*     */   public class AssetObject
/*     */   {
/*     */     private String hash;
/*     */     
/*     */     private long size;
/*     */     
/*     */     private boolean reconstruct;
/*     */     private String compressedHash;
/*     */     private long compressedSize;
/*     */     
/*     */     public String getHash() {
/*  50 */       return this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getSize() {
/*  55 */       return this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldReconstruct() {
/*  60 */       return this.reconstruct;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasCompressedAlternative() {
/*  65 */       return (this.compressedHash != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getCompressedHash() {
/*  70 */       return this.compressedHash;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getCompressedSize() {
/*  75 */       return this.compressedSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/*  80 */       if (this == o) {
/*  81 */         return true;
/*     */       }
/*  83 */       if (o == null || getClass() != o.getClass()) {
/*  84 */         return false;
/*     */       }
/*  86 */       AssetObject that = (AssetObject)o;
/*  87 */       if (this.compressedSize != that.compressedSize) {
/*  88 */         return false;
/*     */       }
/*  90 */       if (this.reconstruct != that.reconstruct) {
/*  91 */         return false;
/*     */       }
/*  93 */       if (this.size != that.size) {
/*  94 */         return false;
/*     */       }
/*  96 */       if ((this.compressedHash != null) ? !this.compressedHash.equals(that.compressedHash) : (that.compressedHash != null)) {
/*  97 */         return false;
/*     */       }
/*  99 */       if ((this.hash != null) ? !this.hash.equals(that.hash) : (that.hash != null)) {
/* 100 */         return false;
/*     */       }
/* 102 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 107 */       int result = (this.hash != null) ? this.hash.hashCode() : 0;
/* 108 */       result = 31 * result + (int)(this.size ^ this.size >>> 32L);
/* 109 */       result = 31 * result + (this.reconstruct ? 1 : 0);
/* 110 */       result = 31 * result + ((this.compressedHash != null) ? this.compressedHash.hashCode() : 0);
/* 111 */       result = 31 * result + (int)(this.compressedSize ^ this.compressedSize >>> 32L);
/* 112 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launche\\updater\download\assets\AssetIndex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */