/*     */ package org.apache.logging.log4j;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ public final class MarkerManager
/*     */ {
/*  30 */   private static final ConcurrentMap<String, Marker> markerMap = new ConcurrentHashMap<String, Marker>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clear() {
/*  40 */     markerMap.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Marker getMarker(String name) {
/*  50 */     markerMap.putIfAbsent(name, new Log4jMarker(name));
/*  51 */     return markerMap.get(name);
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
/*     */   @Deprecated
/*     */   public static Marker getMarker(String name, String parent) {
/*  64 */     Marker parentMarker = markerMap.get(parent);
/*  65 */     if (parentMarker == null) {
/*  66 */       throw new IllegalArgumentException("Parent Marker " + parent + " has not been defined");
/*     */     }
/*     */     
/*  69 */     Marker marker = getMarker(name, parentMarker);
/*  70 */     return marker;
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
/*     */   @Deprecated
/*     */   public static Marker getMarker(String name, Marker parent) {
/*  83 */     markerMap.putIfAbsent(name, new Log4jMarker(name));
/*  84 */     return ((Marker)markerMap.get(name)).addParents(new Marker[] { parent });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Log4jMarker
/*     */     implements Marker
/*     */   {
/*     */     private static final long serialVersionUID = 100L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private volatile Marker[] parents;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Log4jMarker() {
/* 111 */       this.name = null;
/* 112 */       this.parents = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Log4jMarker(String name) {
/* 121 */       if (name == null)
/*     */       {
/*     */         
/* 124 */         throw new IllegalArgumentException("Marker name cannot be null.");
/*     */       }
/* 126 */       this.name = name;
/* 127 */       this.parents = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized Marker addParents(Marker... parents) {
/* 134 */       if (parents == null) {
/* 135 */         throw new IllegalArgumentException("A parent marker must be specified");
/*     */       }
/*     */ 
/*     */       
/* 139 */       Marker[] localParents = this.parents;
/*     */       
/* 141 */       int count = 0;
/* 142 */       int size = parents.length;
/* 143 */       if (localParents != null) {
/* 144 */         for (Marker parent : parents) {
/* 145 */           if (!contains(parent, localParents) && !parent.isInstanceOf(this)) {
/* 146 */             count++;
/*     */           }
/*     */         } 
/* 149 */         if (count == 0) {
/* 150 */           return this;
/*     */         }
/* 152 */         size = localParents.length + count;
/*     */       } 
/* 154 */       Marker[] markers = new Marker[size];
/* 155 */       if (localParents != null)
/*     */       {
/*     */         
/* 158 */         System.arraycopy(localParents, 0, markers, 0, localParents.length);
/*     */       }
/* 160 */       int index = (localParents == null) ? 0 : localParents.length;
/* 161 */       for (Marker parent : parents) {
/* 162 */         if (localParents == null || (!contains(parent, localParents) && !parent.isInstanceOf(this))) {
/* 163 */           markers[index++] = parent;
/*     */         }
/*     */       } 
/* 166 */       this.parents = markers;
/* 167 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized boolean remove(Marker parent) {
/* 172 */       if (parent == null) {
/* 173 */         throw new IllegalArgumentException("A parent marker must be specified");
/*     */       }
/* 175 */       Marker[] localParents = this.parents;
/* 176 */       if (localParents == null) {
/* 177 */         return false;
/*     */       }
/* 179 */       int localParentsLength = localParents.length;
/* 180 */       if (localParentsLength == 1) {
/* 181 */         if (localParents[0].equals(parent)) {
/* 182 */           this.parents = null;
/* 183 */           return true;
/*     */         } 
/* 185 */         return false;
/*     */       } 
/* 187 */       int index = 0;
/* 188 */       Marker[] markers = new Marker[localParentsLength - 1];
/*     */       
/* 190 */       for (int i = 0; i < localParentsLength; i++) {
/* 191 */         Marker marker = localParents[i];
/* 192 */         if (!marker.equals(parent)) {
/* 193 */           if (index == localParentsLength - 1)
/*     */           {
/* 195 */             return false;
/*     */           }
/* 197 */           markers[index++] = marker;
/*     */         } 
/*     */       } 
/* 200 */       this.parents = markers;
/* 201 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Marker setParents(Marker... markers) {
/* 206 */       if (markers == null || markers.length == 0) {
/* 207 */         this.parents = null;
/*     */       } else {
/* 209 */         Marker[] array = new Marker[markers.length];
/* 210 */         System.arraycopy(markers, 0, array, 0, markers.length);
/* 211 */         this.parents = array;
/*     */       } 
/* 213 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 218 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public Marker[] getParents() {
/* 223 */       if (this.parents == null) {
/* 224 */         return null;
/*     */       }
/* 226 */       return Arrays.<Marker>copyOf(this.parents, this.parents.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasParents() {
/* 231 */       return (this.parents == null);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isInstanceOf(Marker marker) {
/* 236 */       if (marker == null) {
/* 237 */         throw new IllegalArgumentException("A marker parameter is required");
/*     */       }
/* 239 */       if (this == marker) {
/* 240 */         return true;
/*     */       }
/* 242 */       Marker[] localParents = this.parents;
/* 243 */       if (localParents != null) {
/*     */         
/* 245 */         int localParentsLength = localParents.length;
/* 246 */         if (localParentsLength == 1) {
/* 247 */           return checkParent(localParents[0], marker);
/*     */         }
/* 249 */         if (localParentsLength == 2) {
/* 250 */           return (checkParent(localParents[0], marker) || checkParent(localParents[1], marker));
/*     */         }
/*     */         
/* 253 */         for (int i = 0; i < localParentsLength; i++) {
/* 254 */           Marker localParent = localParents[i];
/* 255 */           if (checkParent(localParent, marker)) {
/* 256 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 260 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isInstanceOf(String markerName) {
/* 265 */       if (markerName == null) {
/* 266 */         throw new IllegalArgumentException("A marker name is required");
/*     */       }
/* 268 */       if (markerName.equals(getName())) {
/* 269 */         return true;
/*     */       }
/*     */       
/* 272 */       Marker marker = (Marker)MarkerManager.markerMap.get(markerName);
/* 273 */       if (marker == null) {
/* 274 */         return false;
/*     */       }
/* 276 */       Marker[] localParents = this.parents;
/* 277 */       if (localParents != null) {
/* 278 */         int localParentsLength = localParents.length;
/* 279 */         if (localParentsLength == 1) {
/* 280 */           return checkParent(localParents[0], marker);
/*     */         }
/* 282 */         if (localParentsLength == 2) {
/* 283 */           return (checkParent(localParents[0], marker) || checkParent(localParents[1], marker));
/*     */         }
/*     */         
/* 286 */         for (int i = 0; i < localParentsLength; i++) {
/* 287 */           Marker localParent = localParents[i];
/* 288 */           if (checkParent(localParent, marker)) {
/* 289 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 294 */       return false;
/*     */     }
/*     */     
/*     */     private static boolean checkParent(Marker parent, Marker marker) {
/* 298 */       if (parent == marker) {
/* 299 */         return true;
/*     */       }
/* 301 */       Marker[] localParents = (parent instanceof Log4jMarker) ? ((Log4jMarker)parent).parents : parent.getParents();
/* 302 */       if (localParents != null) {
/* 303 */         int localParentsLength = localParents.length;
/* 304 */         if (localParentsLength == 1) {
/* 305 */           return checkParent(localParents[0], marker);
/*     */         }
/* 307 */         if (localParentsLength == 2) {
/* 308 */           return (checkParent(localParents[0], marker) || checkParent(localParents[1], marker));
/*     */         }
/*     */         
/* 311 */         for (int i = 0; i < localParentsLength; i++) {
/* 312 */           Marker localParent = localParents[i];
/* 313 */           if (checkParent(localParent, marker)) {
/* 314 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 318 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static boolean contains(Marker parent, Marker... localParents) {
/* 326 */       for (int i = 0, localParentsLength = localParents.length; i < localParentsLength; i++) {
/* 327 */         Marker marker = localParents[i];
/* 328 */         if (marker == parent) {
/* 329 */           return true;
/*     */         }
/*     */       } 
/* 332 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 337 */       if (this == o) {
/* 338 */         return true;
/*     */       }
/* 340 */       if (o == null || !(o instanceof Marker)) {
/* 341 */         return false;
/*     */       }
/* 343 */       Marker marker = (Marker)o;
/* 344 */       return this.name.equals(marker.getName());
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 349 */       return this.name.hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 355 */       StringBuilder sb = new StringBuilder(this.name);
/* 356 */       Marker[] localParents = this.parents;
/* 357 */       if (localParents != null) {
/* 358 */         addParentInfo(sb, localParents);
/*     */       }
/* 360 */       return sb.toString();
/*     */     }
/*     */     
/*     */     private static void addParentInfo(StringBuilder sb, Marker... parents) {
/* 364 */       sb.append("[ ");
/* 365 */       boolean first = true;
/*     */       
/* 367 */       for (int i = 0, parentsLength = parents.length; i < parentsLength; i++) {
/* 368 */         Marker marker = parents[i];
/* 369 */         if (!first) {
/* 370 */           sb.append(", ");
/*     */         }
/* 372 */         first = false;
/* 373 */         sb.append(marker.getName());
/* 374 */         Marker[] p = (marker instanceof Log4jMarker) ? ((Log4jMarker)marker).parents : marker.getParents();
/* 375 */         if (p != null) {
/* 376 */           addParentInfo(sb, p);
/*     */         }
/*     */       } 
/* 379 */       sb.append(" ]");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\MarkerManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */