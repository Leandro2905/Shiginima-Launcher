/*    */ package com.mojang.authlib;
/*    */ 
/*    */ 
/*    */ public abstract class HttpUserAuthentication
/*    */   extends BaseUserAuthentication
/*    */ {
/*    */   protected HttpUserAuthentication(HttpAuthenticationService authenticationService) {
/*  8 */     super(authenticationService);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpAuthenticationService getAuthenticationService() {
/* 13 */     return (HttpAuthenticationService)super.getAuthenticationService();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\HttpUserAuthentication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */