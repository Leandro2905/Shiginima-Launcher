/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ public final class PropertiesUtil
/*     */ {
/*  36 */   private static final PropertiesUtil LOG4J_PROPERTIES = new PropertiesUtil("log4j2.component.properties");
/*     */   
/*  38 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final Properties props;
/*     */   
/*     */   public PropertiesUtil(Properties props) {
/*  43 */     this.props = props;
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
/*     */   static Properties loadClose(InputStream in, Object source) {
/*  58 */     Properties props = new Properties();
/*  59 */     if (null != in) {
/*     */       try {
/*  61 */         props.load(in);
/*  62 */       } catch (IOException e) {
/*  63 */         LOGGER.error("Unable to read {}", new Object[] { source, e });
/*     */       } finally {
/*     */         try {
/*  66 */           in.close();
/*  67 */         } catch (IOException e) {
/*  68 */           LOGGER.error("Unable to close {}", new Object[] { source, e });
/*     */         } 
/*     */       } 
/*     */     }
/*  72 */     return props;
/*     */   }
/*     */   
/*     */   public PropertiesUtil(String propsLocn) {
/*  76 */     ClassLoader loader = LoaderUtil.getThreadContextClassLoader();
/*     */ 
/*     */     
/*  79 */     Properties properties = new Properties();
/*     */     try {
/*  81 */       Enumeration<URL> enumeration = loader.getResources(propsLocn);
/*  82 */       while (enumeration.hasMoreElements()) {
/*  83 */         URL url = enumeration.nextElement();
/*  84 */         InputStream in = url.openStream();
/*     */         try {
/*  86 */           properties.load(in);
/*  87 */         } catch (IOException ioe) {
/*  88 */           LOGGER.error("Unable to read {}", new Object[] { url.toString() });
/*     */         } finally {
/*     */           try {
/*  91 */             in.close();
/*  92 */           } catch (IOException ioe) {
/*  93 */             LOGGER.error("Unable to close {}", new Object[] { url.toString(), ioe });
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       } 
/*  99 */     } catch (IOException ioe) {
/* 100 */       LOGGER.error("Unable to access {}", new Object[] { propsLocn, ioe });
/*     */     } 
/* 102 */     this.props = properties;
/*     */   }
/*     */   
/*     */   public static PropertiesUtil getProperties() {
/* 106 */     return LOG4J_PROPERTIES;
/*     */   }
/*     */   
/*     */   public String getStringProperty(String name) {
/* 110 */     String prop = null;
/*     */     try {
/* 112 */       prop = System.getProperty(name);
/* 113 */     } catch (SecurityException ignored) {}
/*     */ 
/*     */     
/* 116 */     return (prop == null) ? this.props.getProperty(name) : prop;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIntegerProperty(String name, int defaultValue) {
/* 121 */     String prop = null;
/*     */     try {
/* 123 */       prop = System.getProperty(name);
/* 124 */     } catch (SecurityException ignored) {}
/*     */ 
/*     */     
/* 127 */     if (prop == null) {
/* 128 */       prop = this.props.getProperty(name);
/*     */     }
/* 130 */     if (prop != null) {
/*     */       try {
/* 132 */         return Integer.parseInt(prop);
/* 133 */       } catch (Exception ex) {
/* 134 */         return defaultValue;
/*     */       } 
/*     */     }
/* 137 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLongProperty(String name, long defaultValue) {
/* 142 */     String prop = null;
/*     */     try {
/* 144 */       prop = System.getProperty(name);
/* 145 */     } catch (SecurityException ignored) {}
/*     */ 
/*     */     
/* 148 */     if (prop == null) {
/* 149 */       prop = this.props.getProperty(name);
/*     */     }
/* 151 */     if (prop != null) {
/*     */       try {
/* 153 */         return Long.parseLong(prop);
/* 154 */       } catch (Exception ex) {
/* 155 */         return defaultValue;
/*     */       } 
/*     */     }
/* 158 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public String getStringProperty(String name, String defaultValue) {
/* 162 */     String prop = getStringProperty(name);
/* 163 */     return (prop == null) ? defaultValue : prop;
/*     */   }
/*     */   
/*     */   public boolean getBooleanProperty(String name) {
/* 167 */     return getBooleanProperty(name, false);
/*     */   }
/*     */   
/*     */   public boolean getBooleanProperty(String name, boolean defaultValue) {
/* 171 */     String prop = getStringProperty(name);
/* 172 */     return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Properties getSystemProperties() {
/*     */     try {
/* 181 */       return new Properties(System.getProperties());
/* 182 */     } catch (SecurityException ex) {
/* 183 */       LOGGER.error("Unable to access system properties.", ex);
/*     */       
/* 185 */       return new Properties();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4\\util\PropertiesUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */