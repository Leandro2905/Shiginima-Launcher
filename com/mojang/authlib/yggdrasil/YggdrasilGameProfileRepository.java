/*    */ package com.mojang.authlib.yggdrasil;
/*    */ 
/*    */ import com.google.common.base.Strings;
/*    */ import com.google.common.collect.Iterables;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.mojang.authlib.Agent;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.authlib.GameProfileRepository;
/*    */ import com.mojang.authlib.HttpAuthenticationService;
/*    */ import com.mojang.authlib.ProfileLookupCallback;
/*    */ import com.mojang.authlib.exceptions.AuthenticationException;
/*    */ import com.mojang.authlib.yggdrasil.response.ProfileSearchResultsResponse;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class YggdrasilGameProfileRepository
/*    */   implements GameProfileRepository
/*    */ {
/* 21 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private static final String BASE_URL = "https://api.mojang.com/";
/*    */   private static final String SEARCH_PAGE_URL = "https://api.mojang.com/profiles/";
/*    */   private static final int ENTRIES_PER_PAGE = 2;
/*    */   private static final int MAX_FAIL_COUNT = 3;
/*    */   private static final int DELAY_BETWEEN_PAGES = 100;
/*    */   private static final int DELAY_BETWEEN_FAILURES = 750;
/*    */   private final YggdrasilAuthenticationService authenticationService;
/*    */   
/*    */   public YggdrasilGameProfileRepository(YggdrasilAuthenticationService authenticationService) {
/* 32 */     this.authenticationService = authenticationService;
/*    */   }
/*    */ 
/*    */   
/*    */   public void findProfilesByNames(String[] names, Agent agent, ProfileLookupCallback callback) {
/* 37 */     Set<String> criteria = Sets.newHashSet();
/* 38 */     for (String name : names) {
/* 39 */       if (!Strings.isNullOrEmpty(name)) {
/* 40 */         criteria.add(name.toLowerCase());
/*    */       }
/*    */     } 
/* 43 */     int page = 0;
/* 44 */     label48: for (List<String> request : (Iterable<List<String>>)Iterables.partition(criteria, 2)) {
/*    */       
/* 46 */       int failCount = 0;
/*    */ 
/*    */       
/*    */       while (true) {
/* 50 */         boolean failed = false;
/*    */         
/*    */         try {
/* 53 */           ProfileSearchResultsResponse response = this.authenticationService.<ProfileSearchResultsResponse>makeRequest(HttpAuthenticationService.constantURL("https://api.mojang.com/profiles/" + agent.getName().toLowerCase()), request, ProfileSearchResultsResponse.class);
/* 54 */           failCount = 0;
/*    */           
/* 56 */           LOGGER.debug("Page {} returned {} results, parsing", new Object[] { Integer.valueOf(page), Integer.valueOf((response.getProfiles()).length) });
/*    */           
/* 58 */           Set<String> missing = Sets.newHashSet(request);
/* 59 */           for (GameProfile profile : response.getProfiles()) {
/*    */             
/* 61 */             LOGGER.debug("Successfully looked up profile {}", new Object[] { profile });
/* 62 */             missing.remove(profile.getName().toLowerCase());
/* 63 */             callback.onProfileLookupSucceeded(profile);
/*    */           } 
/* 65 */           for (String name : missing) {
/*    */             
/* 67 */             LOGGER.debug("Couldn't find profile {}", new Object[] { name });
/* 68 */             callback.onProfileLookupFailed(new GameProfile(null, name), new ProfileNotFoundException("Server did not find the requested profile"));
/*    */           } 
/*    */           
/*    */           try {
/* 72 */             Thread.sleep(100L);
/*    */           }
/* 74 */           catch (InterruptedException interruptedException) {}
/*    */         }
/* 76 */         catch (AuthenticationException e) {
/*    */           
/* 78 */           failCount++;
/* 79 */           if (failCount == 3) {
/*    */             
/* 81 */             for (String name : request) {
/*    */               
/* 83 */               LOGGER.debug("Couldn't find profile {} because of a server error", new Object[] { name });
/* 84 */               callback.onProfileLookupFailed(new GameProfile(null, name), (Exception)e);
/*    */             } 
/*    */           } else {
/*    */ 
/*    */             
/*    */             try {
/*    */               
/* 91 */               Thread.sleep(750L);
/*    */             }
/* 93 */             catch (InterruptedException interruptedException) {}
/* 94 */             failed = true;
/*    */           } 
/*    */         } 
/* 97 */         if (!failed)
/*    */           continue label48; 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\yggdrasil\YggdrasilGameProfileRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */