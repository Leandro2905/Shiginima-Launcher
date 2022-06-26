/*    */ package com.mojang.authlib.legacy;
/*    */ 
/*    */ import com.mojang.authlib.Agent;
/*    */ import com.mojang.authlib.GameProfileRepository;
/*    */ import com.mojang.authlib.HttpAuthenticationService;
/*    */ import com.mojang.authlib.UserAuthentication;
/*    */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*    */ import java.net.Proxy;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class LegacyAuthenticationService
/*    */   extends HttpAuthenticationService {
/*    */   protected LegacyAuthenticationService(Proxy proxy) {
/* 14 */     super(proxy);
/*    */   }
/*    */ 
/*    */   
/*    */   public LegacyUserAuthentication createUserAuthentication(Agent agent) {
/* 19 */     Validate.notNull(agent);
/* 20 */     if (agent != Agent.MINECRAFT) {
/* 21 */       throw new IllegalArgumentException("Legacy authentication cannot handle anything but Minecraft");
/*    */     }
/* 23 */     return new LegacyUserAuthentication(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public LegacyMinecraftSessionService createMinecraftSessionService() {
/* 28 */     return new LegacyMinecraftSessionService(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public GameProfileRepository createProfileRepository() {
/* 33 */     throw new UnsupportedOperationException("Legacy authentication service has no profile repository");
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\legacy\LegacyAuthenticationService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */