/*     */ package org.apache.logging.log4j.core.config.plugins.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarInputStream;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.util.Charsets;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.osgi.framework.FrameworkUtil;
/*     */ import org.osgi.framework.wiring.BundleWiring;
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
/*     */ public class ResolverUtil
/*     */ {
/*  78 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static final String VFSZIP = "vfszip";
/*     */   
/*     */   private static final String BUNDLE_RESOURCE = "bundleresource";
/*     */   
/*  85 */   private final Set<Class<?>> classMatches = new HashSet<Class<?>>();
/*     */ 
/*     */   
/*  88 */   private final Set<URI> resourceMatches = new HashSet<URI>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassLoader classloader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Class<?>> getClasses() {
/* 103 */     return this.classMatches;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<URI> getResources() {
/* 111 */     return this.resourceMatches;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassLoader getClassLoader() {
/* 122 */     return (this.classloader != null) ? this.classloader : (this.classloader = Loader.getClassLoader(ResolverUtil.class, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassLoader(ClassLoader classloader) {
/* 131 */     this.classloader = classloader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void find(Test test, String... packageNames) {
/* 141 */     if (packageNames == null) {
/*     */       return;
/*     */     }
/*     */     
/* 145 */     for (String pkg : packageNames) {
/* 146 */       findInPackage(test, pkg);
/*     */     }
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
/*     */   public void findInPackage(Test test, String packageName) {
/*     */     Enumeration<URL> urls;
/* 161 */     packageName = packageName.replace('.', '/');
/* 162 */     ClassLoader loader = getClassLoader();
/*     */ 
/*     */     
/*     */     try {
/* 166 */       urls = loader.getResources(packageName);
/* 167 */     } catch (IOException ioe) {
/* 168 */       LOGGER.warn("Could not read package: " + packageName, ioe);
/*     */       
/*     */       return;
/*     */     } 
/* 172 */     while (urls.hasMoreElements()) {
/*     */       try {
/* 174 */         URL url = urls.nextElement();
/* 175 */         String urlPath = extractPath(url);
/*     */         
/* 177 */         LOGGER.info("Scanning for classes in [" + urlPath + "] matching criteria: " + test);
/*     */         
/* 179 */         if ("vfszip".equals(url.getProtocol())) {
/* 180 */           String path = urlPath.substring(0, urlPath.length() - packageName.length() - 2);
/* 181 */           URL newURL = new URL(url.getProtocol(), url.getHost(), path);
/*     */           
/* 183 */           JarInputStream stream = new JarInputStream(newURL.openStream());
/*     */           try {
/* 185 */             loadImplementationsInJar(test, packageName, path, stream);
/*     */           } finally {
/* 187 */             close(stream, newURL);
/*     */           }  continue;
/* 189 */         }  if ("bundleresource".equals(url.getProtocol())) {
/* 190 */           loadImplementationsInBundle(test, packageName); continue;
/*     */         } 
/* 192 */         File file = new File(urlPath);
/* 193 */         if (file.isDirectory()) {
/* 194 */           loadImplementationsInDirectory(test, packageName, file); continue;
/*     */         } 
/* 196 */         loadImplementationsInJar(test, packageName, file);
/*     */       
/*     */       }
/* 199 */       catch (IOException ioe) {
/* 200 */         LOGGER.warn("could not read entries", ioe);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   String extractPath(URL url) throws UnsupportedEncodingException {
/* 206 */     String urlPath = url.getPath();
/*     */ 
/*     */ 
/*     */     
/* 210 */     if (urlPath.startsWith("jar:")) {
/* 211 */       urlPath = urlPath.substring(4);
/*     */     }
/*     */     
/* 214 */     if (urlPath.startsWith("file:")) {
/* 215 */       urlPath = urlPath.substring(5);
/*     */     }
/*     */     
/* 218 */     if (urlPath.indexOf('!') > 0) {
/* 219 */       urlPath = urlPath.substring(0, urlPath.indexOf('!'));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 224 */     String protocol = url.getProtocol();
/* 225 */     List<String> neverDecode = Arrays.asList(new String[] { "vfszip", "bundleresource" });
/* 226 */     if (neverDecode.contains(protocol)) {
/* 227 */       return urlPath;
/*     */     }
/* 229 */     if ((new File(urlPath)).exists())
/*     */     {
/* 231 */       return urlPath;
/*     */     }
/* 233 */     urlPath = URLDecoder.decode(urlPath, Charsets.UTF_8.name());
/* 234 */     return urlPath;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadImplementationsInBundle(Test test, String packageName) {
/* 240 */     BundleWiring wiring = (BundleWiring)FrameworkUtil.getBundle(ResolverUtil.class).adapt(BundleWiring.class);
/*     */ 
/*     */     
/* 243 */     Collection<String> list = wiring.listResources(packageName, "*.class", 1);
/*     */     
/* 245 */     for (String name : list) {
/* 246 */       addIfMatching(test, name);
/*     */     }
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
/*     */   private void loadImplementationsInDirectory(Test test, String parent, File location) {
/* 264 */     File[] files = location.listFiles();
/* 265 */     if (files == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 270 */     for (File file : files) {
/* 271 */       StringBuilder builder = new StringBuilder();
/* 272 */       builder.append(parent).append('/').append(file.getName());
/* 273 */       String packageOrClass = (parent == null) ? file.getName() : builder.toString();
/*     */       
/* 275 */       if (file.isDirectory()) {
/* 276 */         loadImplementationsInDirectory(test, packageOrClass, file);
/* 277 */       } else if (isTestApplicable(test, file.getName())) {
/* 278 */         addIfMatching(test, packageOrClass);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isTestApplicable(Test test, String path) {
/* 284 */     return (test.doesMatchResource() || (path.endsWith(".class") && test.doesMatchClass()));
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
/*     */   private void loadImplementationsInJar(Test test, String parent, File jarFile) {
/* 298 */     JarInputStream jarStream = null;
/*     */     try {
/* 300 */       jarStream = new JarInputStream(new FileInputStream(jarFile));
/* 301 */       loadImplementationsInJar(test, parent, jarFile.getPath(), jarStream);
/* 302 */     } catch (FileNotFoundException ex) {
/* 303 */       LOGGER.error("Could not search jar file '" + jarFile + "' for classes matching criteria: " + test + " file not found", ex);
/*     */     }
/* 305 */     catch (IOException ioe) {
/* 306 */       LOGGER.error("Could not search jar file '" + jarFile + "' for classes matching criteria: " + test + " due to an IOException", ioe);
/*     */     } finally {
/*     */       
/* 309 */       close(jarStream, jarFile);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void close(JarInputStream jarStream, Object source) {
/* 318 */     if (jarStream != null) {
/*     */       try {
/* 320 */         jarStream.close();
/* 321 */       } catch (IOException e) {
/* 322 */         LOGGER.error("Error closing JAR file stream for {}", new Object[] { source, e });
/*     */       } 
/*     */     }
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
/*     */   private void loadImplementationsInJar(Test test, String parent, String path, JarInputStream stream) {
/*     */     try {
/*     */       JarEntry entry;
/* 342 */       while ((entry = stream.getNextJarEntry()) != null) {
/* 343 */         String name = entry.getName();
/* 344 */         if (!entry.isDirectory() && name.startsWith(parent) && isTestApplicable(test, name)) {
/* 345 */           addIfMatching(test, name);
/*     */         }
/*     */       } 
/* 348 */     } catch (IOException ioe) {
/* 349 */       LOGGER.error("Could not search jar file '" + path + "' for classes matching criteria: " + test + " due to an IOException", ioe);
/*     */     } 
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
/*     */   protected void addIfMatching(Test test, String fqn) {
/*     */     try {
/* 363 */       ClassLoader loader = getClassLoader();
/* 364 */       if (test.doesMatchClass()) {
/* 365 */         String externalName = fqn.substring(0, fqn.indexOf('.')).replace('/', '.');
/* 366 */         if (LOGGER.isDebugEnabled()) {
/* 367 */           LOGGER.debug("Checking to see if class " + externalName + " matches criteria [" + test + ']');
/*     */         }
/*     */         
/* 370 */         Class<?> type = loader.loadClass(externalName);
/* 371 */         if (test.matches(type)) {
/* 372 */           this.classMatches.add(type);
/*     */         }
/*     */       } 
/* 375 */       if (test.doesMatchResource()) {
/* 376 */         URL url = loader.getResource(fqn);
/* 377 */         if (url == null) {
/* 378 */           url = loader.getResource(fqn.substring(1));
/*     */         }
/* 380 */         if (url != null && test.matches(url.toURI())) {
/* 381 */           this.resourceMatches.add(url.toURI());
/*     */         }
/*     */       } 
/* 384 */     } catch (Throwable t) {
/* 385 */       LOGGER.warn("Could not examine class '" + fqn, t);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface Test {
/*     */     boolean matches(Class<?> param1Class);
/*     */     
/*     */     boolean matches(URI param1URI);
/*     */     
/*     */     boolean doesMatchClass();
/*     */     
/*     */     boolean doesMatchResource();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugin\\util\ResolverUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */