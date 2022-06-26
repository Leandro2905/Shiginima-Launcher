/*    */ package com.mojang.authlib;
/*    */ 
/*    */ import com.mojang.authlib.properties.PropertyMap;
/*    */ import java.util.UUID;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.commons.lang3.builder.ToStringBuilder;
/*    */ 
/*    */ public class GameProfile
/*    */ {
/*    */   private final UUID id;
/*    */   private final String name;
/* 12 */   private final PropertyMap properties = new PropertyMap();
/*    */   
/*    */   private boolean legacy;
/*    */   
/*    */   public GameProfile(UUID id, String name) {
/* 17 */     if (id == null && StringUtils.isBlank(name)) {
/* 18 */       throw new IllegalArgumentException("Name and ID cannot both be blank");
/*    */     }
/* 20 */     this.id = id;
/* 21 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID getId() {
/* 26 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 31 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public PropertyMap getProperties() {
/* 36 */     return this.properties;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isComplete() {
/* 41 */     return (this.id != null && StringUtils.isNotBlank(getName()));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 46 */     if (this == o) {
/* 47 */       return true;
/*    */     }
/* 49 */     if (o == null || getClass() != o.getClass()) {
/* 50 */       return false;
/*    */     }
/* 52 */     GameProfile that = (GameProfile)o;
/* 53 */     if ((this.id != null) ? !this.id.equals(that.id) : (that.id != null)) {
/* 54 */       return false;
/*    */     }
/* 56 */     if ((this.name != null) ? !this.name.equals(that.name) : (that.name != null)) {
/* 57 */       return false;
/*    */     }
/* 59 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 64 */     int result = (this.id != null) ? this.id.hashCode() : 0;
/* 65 */     result = 31 * result + ((this.name != null) ? this.name.hashCode() : 0);
/* 66 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return (new ToStringBuilder(this)).append("id", this.id).append("name", this.name).append("properties", this.properties).append("legacy", this.legacy).toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isLegacy() {
/* 76 */     return this.legacy;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\GameProfile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */