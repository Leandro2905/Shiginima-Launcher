/*    */ package net.minecraft.launcher.updater;
/*    */ 
/*    */ import com.mojang.launcher.versions.ReleaseType;
/*    */ import com.mojang.launcher.versions.Version;
/*    */ import java.net.URL;
/*    */ import java.util.Date;
/*    */ import net.minecraft.launcher.game.MinecraftReleaseType;
/*    */ 
/*    */ public class PartialVersion
/*    */   implements Version
/*    */ {
/*    */   private String id;
/*    */   private Date time;
/*    */   private Date releaseTime;
/*    */   private MinecraftReleaseType type;
/*    */   private URL url;
/*    */   
/*    */   public PartialVersion() {}
/*    */   
/*    */   public PartialVersion(String id, Date releaseTime, Date updateTime, MinecraftReleaseType type, URL url) {
/* 21 */     if (id == null || id.length() == 0) {
/* 22 */       throw new IllegalArgumentException("ID cannot be null or empty");
/*    */     }
/* 24 */     if (releaseTime == null) {
/* 25 */       throw new IllegalArgumentException("Release time cannot be null");
/*    */     }
/* 27 */     if (updateTime == null) {
/* 28 */       throw new IllegalArgumentException("Update time cannot be null");
/*    */     }
/* 30 */     if (type == null) {
/* 31 */       throw new IllegalArgumentException("Release type cannot be null");
/*    */     }
/* 33 */     this.id = id;
/* 34 */     this.releaseTime = releaseTime;
/* 35 */     this.time = updateTime;
/* 36 */     this.type = type;
/* 37 */     this.url = url;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getId() {
/* 42 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public MinecraftReleaseType getType() {
/* 47 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public Date getUpdatedTime() {
/* 52 */     return this.time;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setUpdatedTime(Date time) {
/* 57 */     if (time == null) {
/* 58 */       throw new IllegalArgumentException("Time cannot be null");
/*    */     }
/* 60 */     this.time = time;
/*    */   }
/*    */ 
/*    */   
/*    */   public Date getReleaseTime() {
/* 65 */     return this.releaseTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setReleaseTime(Date time) {
/* 70 */     if (time == null) {
/* 71 */       throw new IllegalArgumentException("Time cannot be null");
/*    */     }
/* 73 */     this.releaseTime = time;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setType(MinecraftReleaseType type) {
/* 78 */     if (type == null) {
/* 79 */       throw new IllegalArgumentException("Release type cannot be null");
/*    */     }
/* 81 */     this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public URL getUrl() {
/* 86 */     return this.url;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setUrl(URL url) {
/* 91 */     this.url = url;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 96 */     return "PartialVersion{id='" + this.id + '\'' + ", updateTime=" + this.time + ", releaseTime=" + this.releaseTime + ", type=" + this.type + ", url=" + this.url + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\updater\PartialVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */