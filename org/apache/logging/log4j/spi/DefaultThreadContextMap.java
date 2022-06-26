/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*     */ public class DefaultThreadContextMap
/*     */   implements ThreadContextMap
/*     */ {
/*     */   public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";
/*     */   private final boolean useMap;
/*     */   private final ThreadLocal<Map<String, String>> localMap;
/*     */   
/*     */   public DefaultThreadContextMap(boolean useMap) {
/*  42 */     this.useMap = useMap;
/*  43 */     this.localMap = createThreadLocalMap(useMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ThreadLocal<Map<String, String>> createThreadLocalMap(final boolean isMapEnabled) {
/*  49 */     PropertiesUtil managerProps = PropertiesUtil.getProperties();
/*  50 */     boolean inheritable = managerProps.getBooleanProperty("isThreadContextMapInheritable");
/*  51 */     if (inheritable) {
/*  52 */       return new InheritableThreadLocal<Map<String, String>>()
/*     */         {
/*     */           protected Map<String, String> childValue(Map<String, String> parentValue) {
/*  55 */             return (parentValue != null && isMapEnabled) ? Collections.<String, String>unmodifiableMap(new HashMap<String, String>(parentValue)) : null;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  62 */     return new ThreadLocal<Map<String, String>>();
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
/*     */   public void put(String key, String value) {
/*  77 */     if (!this.useMap) {
/*     */       return;
/*     */     }
/*  80 */     Map<String, String> map = this.localMap.get();
/*  81 */     map = (map == null) ? new HashMap<String, String>() : new HashMap<String, String>(map);
/*  82 */     map.put(key, value);
/*  83 */     this.localMap.set(Collections.unmodifiableMap(map));
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
/*     */   public String get(String key) {
/*  95 */     Map<String, String> map = this.localMap.get();
/*  96 */     return (map == null) ? null : map.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/* 106 */     Map<String, String> map = this.localMap.get();
/* 107 */     if (map != null) {
/* 108 */       Map<String, String> copy = new HashMap<String, String>(map);
/* 109 */       copy.remove(key);
/* 110 */       this.localMap.set(Collections.unmodifiableMap(copy));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 119 */     this.localMap.remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(String key) {
/* 129 */     Map<String, String> map = this.localMap.get();
/* 130 */     return (map != null && map.containsKey(key));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getCopy() {
/* 139 */     Map<String, String> map = this.localMap.get();
/* 140 */     return (map == null) ? new HashMap<String, String>() : new HashMap<String, String>(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getImmutableMapOrNull() {
/* 149 */     return this.localMap.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 158 */     Map<String, String> map = this.localMap.get();
/* 159 */     return (map == null || map.size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 164 */     Map<String, String> map = this.localMap.get();
/* 165 */     return (map == null) ? "{}" : map.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 170 */     int prime = 31;
/* 171 */     int result = 1;
/* 172 */     Map<String, String> map = this.localMap.get();
/* 173 */     result = 31 * result + ((map == null) ? 0 : map.hashCode());
/* 174 */     result = 31 * result + (this.useMap ? 1231 : 1237);
/* 175 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 180 */     if (this == obj) {
/* 181 */       return true;
/*     */     }
/* 183 */     if (obj == null) {
/* 184 */       return false;
/*     */     }
/* 186 */     if (obj instanceof DefaultThreadContextMap) {
/* 187 */       DefaultThreadContextMap defaultThreadContextMap = (DefaultThreadContextMap)obj;
/* 188 */       if (this.useMap != defaultThreadContextMap.useMap) {
/* 189 */         return false;
/*     */       }
/*     */     } 
/* 192 */     if (!(obj instanceof ThreadContextMap)) {
/* 193 */       return false;
/*     */     }
/* 195 */     ThreadContextMap other = (ThreadContextMap)obj;
/* 196 */     Map<String, String> map = this.localMap.get();
/* 197 */     Map<String, String> otherMap = other.getImmutableMapOrNull();
/* 198 */     if (map == null) {
/* 199 */       if (otherMap != null) {
/* 200 */         return false;
/*     */       }
/* 202 */     } else if (!map.equals(otherMap)) {
/* 203 */       return false;
/*     */     } 
/* 205 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\spi\DefaultThreadContextMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */