/*    */ package com.mojang.authlib.minecraft;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.Calendar;
/*    */ import java.util.Date;
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ public class InsecureTextureException
/*    */   extends RuntimeException
/*    */ {
/*    */   public InsecureTextureException(String message) {
/* 13 */     super(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public static class OutdatedTextureException
/*    */     extends InsecureTextureException
/*    */   {
/*    */     private final Date validFrom;
/*    */     private final Calendar limit;
/*    */     
/*    */     public OutdatedTextureException(Date validFrom, Calendar limit) {
/* 24 */       super("No texture found");
/* 25 */       this.validFrom = validFrom;
/* 26 */       this.limit = limit;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static class WrongTextureOwnerException
/*    */     extends InsecureTextureException
/*    */   {
/*    */     private final GameProfile expected;
/*    */     private final UUID resultId;
/*    */     private final String resultName;
/*    */     
/*    */     public WrongTextureOwnerException(GameProfile expected, UUID resultId, String resultName) {
/* 39 */       super("Wrong texture owner");
/* 40 */       this.expected = expected;
/* 41 */       this.resultId = resultId;
/* 42 */       this.resultName = resultName;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static class MissingTextureException
/*    */     extends InsecureTextureException
/*    */   {
/*    */     public MissingTextureException() {
/* 51 */       super("Missing Texture");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\minecraft\InsecureTextureException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */