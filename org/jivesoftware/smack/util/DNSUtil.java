/*     */ package org.jivesoftware.smack.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.jivesoftware.smack.util.dns.DNSResolver;
/*     */ import org.jivesoftware.smack.util.dns.HostAddress;
/*     */ import org.jivesoftware.smack.util.dns.SRVRecord;
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
/*     */ public class DNSUtil
/*     */ {
/*  39 */   private static final Logger LOGGER = Logger.getLogger(DNSUtil.class.getName());
/*  40 */   private static DNSResolver dnsResolver = null;
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
/*  52 */   private static StringTransformer idnaTransformer = new StringTransformer()
/*     */     {
/*     */       public String transform(String string) {
/*  55 */         return string;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDNSResolver(DNSResolver resolver) {
/*  65 */     dnsResolver = resolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DNSResolver getDNSResolver() {
/*  74 */     return dnsResolver;
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
/*     */   public static void setIdnaTransformer(StringTransformer idnaTransformer) {
/*  86 */     if (idnaTransformer == null) {
/*  87 */       throw new NullPointerException();
/*     */     }
/*  89 */     DNSUtil.idnaTransformer = idnaTransformer;
/*     */   }
/*     */   
/*     */   private enum DomainType {
/*  93 */     Server,
/*  94 */     Client;
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
/*     */   public static List<HostAddress> resolveXMPPDomain(String domain, List<HostAddress> failedAddresses) {
/* 113 */     domain = idnaTransformer.transform(domain);
/* 114 */     if (dnsResolver == null) {
/* 115 */       LOGGER.warning("No DNS Resolver active in Smack, will be unable to perform DNS SRV lookups");
/* 116 */       List<HostAddress> addresses = new ArrayList<>(1);
/* 117 */       addresses.add(new HostAddress(domain, 5222));
/* 118 */       return addresses;
/*     */     } 
/* 120 */     return resolveDomain(domain, DomainType.Client, failedAddresses);
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
/*     */   public static List<HostAddress> resolveXMPPServerDomain(String domain, List<HostAddress> failedAddresses) {
/* 138 */     domain = idnaTransformer.transform(domain);
/* 139 */     if (dnsResolver == null) {
/* 140 */       LOGGER.warning("No DNS Resolver active in Smack, will be unable to perform DNS SRV lookups");
/* 141 */       List<HostAddress> addresses = new ArrayList<>(1);
/* 142 */       addresses.add(new HostAddress(domain, 5269));
/* 143 */       return addresses;
/*     */     } 
/* 145 */     return resolveDomain(domain, DomainType.Server, failedAddresses);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<HostAddress> resolveDomain(String domain, DomainType domainType, List<HostAddress> failedAddresses) {
/*     */     String srvDomain;
/* 156 */     List<HostAddress> addresses = new ArrayList<>();
/*     */ 
/*     */ 
/*     */     
/* 160 */     switch (domainType) {
/*     */       case Server:
/* 162 */         srvDomain = "_xmpp-server._tcp." + domain;
/*     */         break;
/*     */       case Client:
/* 165 */         srvDomain = "_xmpp-client._tcp." + domain;
/*     */         break;
/*     */       default:
/* 168 */         throw new AssertionError();
/*     */     } 
/*     */     try {
/* 171 */       List<SRVRecord> srvRecords = dnsResolver.lookupSRVRecords(srvDomain);
/* 172 */       if (LOGGER.isLoggable(Level.FINE)) {
/* 173 */         String logMessage = "Resolved SRV RR for " + srvDomain + ":";
/* 174 */         for (SRVRecord r : srvRecords)
/* 175 */           logMessage = logMessage + " " + r; 
/* 176 */         LOGGER.fine(logMessage);
/*     */       } 
/* 178 */       List<HostAddress> sortedRecords = sortSRVRecords(srvRecords);
/* 179 */       addresses.addAll(sortedRecords);
/*     */     }
/* 181 */     catch (Exception e) {
/* 182 */       LOGGER.log(Level.WARNING, "Exception while resovling SRV records for " + domain + ". Consider adding '_xmpp-(server|client)._tcp' DNS SRV Records", e);
/*     */       
/* 184 */       if (failedAddresses != null) {
/* 185 */         HostAddress failedHostAddress = new HostAddress(srvDomain);
/* 186 */         failedHostAddress.setException(e);
/* 187 */         failedAddresses.add(failedHostAddress);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 192 */     addresses.add(new HostAddress(domain));
/*     */     
/* 194 */     return addresses;
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
/*     */   private static List<HostAddress> sortSRVRecords(List<SRVRecord> records) {
/* 208 */     if (records.size() == 1 && ((SRVRecord)records.get(0)).getFQDN().equals(".")) {
/* 209 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 212 */     Collections.sort(records);
/*     */ 
/*     */     
/* 215 */     SortedMap<Integer, List<SRVRecord>> buckets = new TreeMap<>();
/* 216 */     for (SRVRecord r : records) {
/* 217 */       Integer priority = Integer.valueOf(r.getPriority());
/* 218 */       List<SRVRecord> bucket = buckets.get(priority);
/*     */       
/* 220 */       if (bucket == null) {
/* 221 */         bucket = new LinkedList<>();
/* 222 */         buckets.put(priority, bucket);
/*     */       } 
/* 224 */       bucket.add(r);
/*     */     } 
/*     */     
/* 227 */     List<HostAddress> res = new ArrayList<>(records.size());
/*     */     
/* 229 */     for (Integer priority : buckets.keySet()) {
/* 230 */       List<SRVRecord> bucket = buckets.get(priority);
/*     */       int bucketSize;
/* 232 */       while ((bucketSize = bucket.size()) > 0) {
/* 233 */         int selectedPos, totals[] = new int[bucket.size()];
/* 234 */         int running_total = 0;
/* 235 */         int count = 0;
/* 236 */         int zeroWeight = 1;
/*     */         
/* 238 */         for (SRVRecord r : bucket) {
/* 239 */           if (r.getWeight() > 0) {
/* 240 */             zeroWeight = 0;
/*     */           }
/*     */         } 
/* 243 */         for (SRVRecord r : bucket) {
/* 244 */           running_total += r.getWeight() + zeroWeight;
/* 245 */           totals[count] = running_total;
/* 246 */           count++;
/*     */         } 
/*     */         
/* 249 */         if (running_total == 0) {
/*     */ 
/*     */ 
/*     */           
/* 253 */           selectedPos = (int)(Math.random() * bucketSize);
/*     */         } else {
/* 255 */           double rnd = Math.random() * running_total;
/* 256 */           selectedPos = bisect(totals, rnd);
/*     */         } 
/*     */ 
/*     */         
/* 260 */         SRVRecord chosenSRVRecord = bucket.remove(selectedPos);
/* 261 */         res.add(chosenSRVRecord);
/*     */       } 
/*     */     } 
/*     */     
/* 265 */     return res;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int bisect(int[] array, double value) {
/* 270 */     int pos = 0;
/* 271 */     for (int element : array) {
/* 272 */       if (value < element)
/*     */         break; 
/* 274 */       pos++;
/*     */     } 
/* 276 */     return pos;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\DNSUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */