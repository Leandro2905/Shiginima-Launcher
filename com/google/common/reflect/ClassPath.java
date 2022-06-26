/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.FluentIterable;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.ImmutableSortedSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.Comparator;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
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
/*     */ @Beta
/*     */ public final class ClassPath
/*     */ {
/*  60 */   private static final Logger logger = Logger.getLogger(ClassPath.class.getName());
/*     */   
/*  62 */   private static final Predicate<ClassInfo> IS_TOP_LEVEL = new Predicate<ClassInfo>() {
/*     */       public boolean apply(ClassPath.ClassInfo info) {
/*  64 */         return (info.className.indexOf('$') == -1);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  69 */   private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();
/*     */   
/*     */   private static final String CLASS_FILE_NAME_EXTENSION = ".class";
/*     */   
/*     */   private final ImmutableSet<ResourceInfo> resources;
/*     */ 
/*     */   
/*     */   private ClassPath(ImmutableSet<ResourceInfo> resources) {
/*  77 */     this.resources = resources;
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
/*     */   public static ClassPath from(ClassLoader classloader) throws IOException {
/*  90 */     Scanner scanner = new Scanner();
/*  91 */     for (Map.Entry<URI, ClassLoader> entry : (Iterable<Map.Entry<URI, ClassLoader>>)getClassPathEntries(classloader).entrySet()) {
/*  92 */       scanner.scan(entry.getKey(), entry.getValue());
/*     */     }
/*  94 */     return new ClassPath((ImmutableSet<ResourceInfo>)scanner.getResources());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ResourceInfo> getResources() {
/* 102 */     return this.resources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getAllClasses() {
/* 111 */     return FluentIterable.from((Iterable)this.resources).filter(ClassInfo.class).toSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClasses() {
/* 116 */     return FluentIterable.from((Iterable)this.resources).filter(ClassInfo.class).filter(IS_TOP_LEVEL).toSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClasses(String packageName) {
/* 121 */     Preconditions.checkNotNull(packageName);
/* 122 */     ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
/* 123 */     for (ClassInfo classInfo : getTopLevelClasses()) {
/* 124 */       if (classInfo.getPackageName().equals(packageName)) {
/* 125 */         builder.add(classInfo);
/*     */       }
/*     */     } 
/* 128 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(String packageName) {
/* 136 */     Preconditions.checkNotNull(packageName);
/* 137 */     String str1 = String.valueOf(String.valueOf(packageName)), packagePrefix = (new StringBuilder(1 + str1.length())).append(str1).append(".").toString();
/* 138 */     ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
/* 139 */     for (ClassInfo classInfo : getTopLevelClasses()) {
/* 140 */       if (classInfo.getName().startsWith(packagePrefix)) {
/* 141 */         builder.add(classInfo);
/*     */       }
/*     */     } 
/* 144 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static class ResourceInfo
/*     */   {
/*     */     private final String resourceName;
/*     */ 
/*     */     
/*     */     final ClassLoader loader;
/*     */ 
/*     */     
/*     */     static ResourceInfo of(String resourceName, ClassLoader loader) {
/* 159 */       if (resourceName.endsWith(".class")) {
/* 160 */         return new ClassPath.ClassInfo(resourceName, loader);
/*     */       }
/* 162 */       return new ResourceInfo(resourceName, loader);
/*     */     }
/*     */ 
/*     */     
/*     */     ResourceInfo(String resourceName, ClassLoader loader) {
/* 167 */       this.resourceName = (String)Preconditions.checkNotNull(resourceName);
/* 168 */       this.loader = (ClassLoader)Preconditions.checkNotNull(loader);
/*     */     }
/*     */ 
/*     */     
/*     */     public final URL url() {
/* 173 */       return (URL)Preconditions.checkNotNull(this.loader.getResource(this.resourceName), "Failed to load resource: %s", new Object[] { this.resourceName });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final String getResourceName() {
/* 179 */       return this.resourceName;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 183 */       return this.resourceName.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj) {
/* 187 */       if (obj instanceof ResourceInfo) {
/* 188 */         ResourceInfo that = (ResourceInfo)obj;
/* 189 */         return (this.resourceName.equals(that.resourceName) && this.loader == that.loader);
/*     */       } 
/*     */       
/* 192 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 197 */       return this.resourceName;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static final class ClassInfo
/*     */     extends ResourceInfo
/*     */   {
/*     */     private final String className;
/*     */ 
/*     */     
/*     */     ClassInfo(String resourceName, ClassLoader loader) {
/* 211 */       super(resourceName, loader);
/* 212 */       this.className = ClassPath.getClassName(resourceName);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getPackageName() {
/* 222 */       return Reflection.getPackageName(this.className);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getSimpleName() {
/* 232 */       int lastDollarSign = this.className.lastIndexOf('$');
/* 233 */       if (lastDollarSign != -1) {
/* 234 */         String innerClassName = this.className.substring(lastDollarSign + 1);
/*     */ 
/*     */         
/* 237 */         return CharMatcher.DIGIT.trimLeadingFrom(innerClassName);
/*     */       } 
/* 239 */       String packageName = getPackageName();
/* 240 */       if (packageName.isEmpty()) {
/* 241 */         return this.className;
/*     */       }
/*     */ 
/*     */       
/* 245 */       return this.className.substring(packageName.length() + 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 255 */       return this.className;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Class<?> load() {
/*     */       try {
/* 266 */         return this.loader.loadClass(this.className);
/* 267 */       } catch (ClassNotFoundException e) {
/*     */         
/* 269 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public String toString() {
/* 274 */       return this.className;
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static ImmutableMap<URI, ClassLoader> getClassPathEntries(ClassLoader classloader) {
/* 280 */     LinkedHashMap<URI, ClassLoader> entries = Maps.newLinkedHashMap();
/*     */     
/* 282 */     ClassLoader parent = classloader.getParent();
/* 283 */     if (parent != null) {
/* 284 */       entries.putAll((Map<? extends URI, ? extends ClassLoader>)getClassPathEntries(parent));
/*     */     }
/* 286 */     if (classloader instanceof URLClassLoader) {
/* 287 */       URLClassLoader urlClassLoader = (URLClassLoader)classloader;
/* 288 */       for (URL entry : urlClassLoader.getURLs()) {
/*     */         URI uri;
/*     */         try {
/* 291 */           uri = entry.toURI();
/* 292 */         } catch (URISyntaxException e) {
/* 293 */           throw new IllegalArgumentException(e);
/*     */         } 
/* 295 */         if (!entries.containsKey(uri)) {
/* 296 */           entries.put(uri, classloader);
/*     */         }
/*     */       } 
/*     */     } 
/* 300 */     return ImmutableMap.copyOf(entries);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class Scanner {
/* 305 */     private final ImmutableSortedSet.Builder<ClassPath.ResourceInfo> resources = new ImmutableSortedSet.Builder((Comparator)Ordering.usingToString());
/*     */     
/* 307 */     private final Set<URI> scannedUris = Sets.newHashSet();
/*     */     
/*     */     ImmutableSortedSet<ClassPath.ResourceInfo> getResources() {
/* 310 */       return this.resources.build();
/*     */     }
/*     */     
/*     */     void scan(URI uri, ClassLoader classloader) throws IOException {
/* 314 */       if (uri.getScheme().equals("file") && this.scannedUris.add(uri)) {
/* 315 */         scanFrom(new File(uri), classloader);
/*     */       }
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     void scanFrom(File file, ClassLoader classloader) throws IOException {
/* 321 */       if (!file.exists()) {
/*     */         return;
/*     */       }
/* 324 */       if (file.isDirectory()) {
/* 325 */         scanDirectory(file, classloader);
/*     */       } else {
/* 327 */         scanJar(file, classloader);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void scanDirectory(File directory, ClassLoader classloader) throws IOException {
/* 332 */       scanDirectory(directory, classloader, "", ImmutableSet.of());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void scanDirectory(File directory, ClassLoader classloader, String packagePrefix, ImmutableSet<File> ancestors) throws IOException {
/* 338 */       File canonical = directory.getCanonicalFile();
/* 339 */       if (ancestors.contains(canonical)) {
/*     */         return;
/*     */       }
/*     */       
/* 343 */       File[] files = directory.listFiles();
/* 344 */       if (files == null) {
/* 345 */         String str = String.valueOf(String.valueOf(directory)); ClassPath.logger.warning((new StringBuilder(22 + str.length())).append("Cannot read directory ").append(str).toString());
/*     */         
/*     */         return;
/*     */       } 
/* 349 */       ImmutableSet<File> newAncestors = ImmutableSet.builder().addAll((Iterable)ancestors).add(canonical).build();
/*     */ 
/*     */ 
/*     */       
/* 353 */       for (File f : files) {
/* 354 */         String name = f.getName();
/* 355 */         if (f.isDirectory()) {
/* 356 */           String str1 = String.valueOf(String.valueOf(packagePrefix)), str2 = String.valueOf(String.valueOf(name)); scanDirectory(f, classloader, (new StringBuilder(1 + str1.length() + str2.length())).append(str1).append(str2).append("/").toString(), newAncestors);
/*     */         } else {
/* 358 */           String.valueOf(name); String resourceName = (String.valueOf(name).length() != 0) ? String.valueOf(packagePrefix).concat(String.valueOf(name)) : new String(String.valueOf(packagePrefix));
/* 359 */           if (!resourceName.equals("META-INF/MANIFEST.MF")) {
/* 360 */             this.resources.add(ClassPath.ResourceInfo.of(resourceName, classloader));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void scanJar(File file, ClassLoader classloader) throws IOException {
/*     */       JarFile jarFile;
/*     */       try {
/* 369 */         jarFile = new JarFile(file);
/* 370 */       } catch (IOException e) {
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 375 */         for (URI uri : getClassPathFromManifest(file, jarFile.getManifest())) {
/* 376 */           scan(uri, classloader);
/*     */         }
/* 378 */         Enumeration<JarEntry> entries = jarFile.entries();
/* 379 */         while (entries.hasMoreElements()) {
/* 380 */           JarEntry entry = entries.nextElement();
/* 381 */           if (entry.isDirectory() || entry.getName().equals("META-INF/MANIFEST.MF")) {
/*     */             continue;
/*     */           }
/* 384 */           this.resources.add(ClassPath.ResourceInfo.of(entry.getName(), classloader));
/*     */         } 
/*     */       } finally {
/*     */         try {
/* 388 */           jarFile.close();
/* 389 */         } catch (IOException ignored) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     static ImmutableSet<URI> getClassPathFromManifest(File jarFile, @Nullable Manifest manifest) {
/* 401 */       if (manifest == null) {
/* 402 */         return ImmutableSet.of();
/*     */       }
/* 404 */       ImmutableSet.Builder<URI> builder = ImmutableSet.builder();
/* 405 */       String classpathAttribute = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
/*     */       
/* 407 */       if (classpathAttribute != null) {
/* 408 */         for (String path : ClassPath.CLASS_PATH_ATTRIBUTE_SEPARATOR.split(classpathAttribute)) {
/*     */           URI uri;
/*     */           try {
/* 411 */             uri = getClassPathEntry(jarFile, path);
/* 412 */           } catch (URISyntaxException e) {
/*     */             
/* 414 */             String.valueOf(path); ClassPath.logger.warning((String.valueOf(path).length() != 0) ? "Invalid Class-Path entry: ".concat(String.valueOf(path)) : new String("Invalid Class-Path entry: "));
/*     */             continue;
/*     */           } 
/* 417 */           builder.add(uri);
/*     */         } 
/*     */       }
/* 420 */       return builder.build();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     static URI getClassPathEntry(File jarFile, String path) throws URISyntaxException {
/* 431 */       URI uri = new URI(path);
/* 432 */       if (uri.isAbsolute()) {
/* 433 */         return uri;
/*     */       }
/* 435 */       return (new File(jarFile.getParentFile(), path.replace('/', File.separatorChar))).toURI();
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static String getClassName(String filename) {
/* 441 */     int classNameEnd = filename.length() - ".class".length();
/* 442 */     return filename.substring(0, classNameEnd).replace('/', '.');
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\reflect\ClassPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */