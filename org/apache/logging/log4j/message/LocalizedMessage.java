/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalizedMessage
/*     */   implements Message, LoggerNameAwareMessage
/*     */ {
/*     */   private static final long serialVersionUID = 3893703791567290742L;
/*     */   private String baseName;
/*     */   private transient ResourceBundle resourceBundle;
/*     */   private final Locale locale;
/*  47 */   private transient StatusLogger logger = StatusLogger.getLogger();
/*     */   
/*     */   private String loggerName;
/*     */   
/*     */   private String key;
/*     */   
/*     */   private String[] stringArgs;
/*     */   
/*     */   private transient Object[] argArray;
/*     */   
/*     */   private String formattedMessage;
/*     */   
/*     */   private transient Throwable throwable;
/*     */ 
/*     */   
/*     */   public LocalizedMessage(String messagePattern, Object[] arguments) {
/*  63 */     this((ResourceBundle)null, (Locale)null, messagePattern, arguments);
/*     */   }
/*     */   
/*     */   public LocalizedMessage(String baseName, String key, Object[] arguments) {
/*  67 */     this(baseName, (Locale)null, key, arguments);
/*     */   }
/*     */   
/*     */   public LocalizedMessage(ResourceBundle bundle, String key, Object[] arguments) {
/*  71 */     this(bundle, (Locale)null, key, arguments);
/*     */   }
/*     */   
/*     */   public LocalizedMessage(String baseName, Locale locale, String key, Object[] arguments) {
/*  75 */     this.key = key;
/*  76 */     this.argArray = arguments;
/*  77 */     this.throwable = null;
/*  78 */     this.baseName = baseName;
/*  79 */     this.resourceBundle = null;
/*  80 */     this.locale = locale;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalizedMessage(ResourceBundle bundle, Locale locale, String key, Object[] arguments) {
/*  85 */     this.key = key;
/*  86 */     this.argArray = arguments;
/*  87 */     this.throwable = null;
/*  88 */     this.baseName = null;
/*  89 */     this.resourceBundle = bundle;
/*  90 */     this.locale = locale;
/*     */   }
/*     */   
/*     */   public LocalizedMessage(Locale locale, String key, Object[] arguments) {
/*  94 */     this((ResourceBundle)null, locale, key, arguments);
/*     */   }
/*     */   
/*     */   public LocalizedMessage(String messagePattern, Object arg) {
/*  98 */     this((ResourceBundle)null, (Locale)null, messagePattern, new Object[] { arg });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(String baseName, String key, Object arg) {
/* 102 */     this(baseName, (Locale)null, key, new Object[] { arg });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(ResourceBundle bundle, String key, Object arg) {
/* 106 */     this(bundle, (Locale)null, key, new Object[] { arg });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(String baseName, Locale locale, String key, Object arg) {
/* 110 */     this(baseName, locale, key, new Object[] { arg });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(ResourceBundle bundle, Locale locale, String key, Object arg) {
/* 114 */     this(bundle, locale, key, new Object[] { arg });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(Locale locale, String key, Object arg) {
/* 118 */     this((ResourceBundle)null, locale, key, new Object[] { arg });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(String messagePattern, Object arg1, Object arg2) {
/* 122 */     this((ResourceBundle)null, (Locale)null, messagePattern, new Object[] { arg1, arg2 });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(String baseName, String key, Object arg1, Object arg2) {
/* 126 */     this(baseName, (Locale)null, key, new Object[] { arg1, arg2 });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(ResourceBundle bundle, String key, Object arg1, Object arg2) {
/* 130 */     this(bundle, (Locale)null, key, new Object[] { arg1, arg2 });
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalizedMessage(String baseName, Locale locale, String key, Object arg1, Object arg2) {
/* 135 */     this(baseName, locale, key, new Object[] { arg1, arg2 });
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalizedMessage(ResourceBundle bundle, Locale locale, String key, Object arg1, Object arg2) {
/* 140 */     this(bundle, locale, key, new Object[] { arg1, arg2 });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(Locale locale, String key, Object arg1, Object arg2) {
/* 144 */     this((ResourceBundle)null, locale, key, new Object[] { arg1, arg2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLoggerName(String name) {
/* 153 */     this.loggerName = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLoggerName() {
/* 162 */     return this.loggerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 171 */     if (this.formattedMessage != null) {
/* 172 */       return this.formattedMessage;
/*     */     }
/* 174 */     ResourceBundle bundle = this.resourceBundle;
/* 175 */     if (bundle == null) {
/* 176 */       if (this.baseName != null) {
/* 177 */         bundle = getResourceBundle(this.baseName, this.locale, false);
/*     */       } else {
/* 179 */         bundle = getResourceBundle(this.loggerName, this.locale, true);
/*     */       } 
/*     */     }
/* 182 */     String myKey = getFormat();
/* 183 */     String msgPattern = (bundle == null || !bundle.containsKey(myKey)) ? myKey : bundle.getString(myKey);
/*     */     
/* 185 */     Object[] array = (this.argArray == null) ? (Object[])this.stringArgs : this.argArray;
/* 186 */     FormattedMessage msg = new FormattedMessage(msgPattern, array);
/* 187 */     this.formattedMessage = msg.getFormattedMessage();
/* 188 */     this.throwable = msg.getThrowable();
/* 189 */     return this.formattedMessage;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 194 */     return this.key;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 199 */     if (this.argArray != null) {
/* 200 */       return this.argArray;
/*     */     }
/* 202 */     return (Object[])this.stringArgs;
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 207 */     return this.throwable;
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
/*     */   protected ResourceBundle getResourceBundle(String rbBaseName, Locale resourceBundleLocale, boolean loop) {
/* 220 */     ResourceBundle rb = null;
/*     */     
/* 222 */     if (rbBaseName == null) {
/* 223 */       return null;
/*     */     }
/*     */     try {
/* 226 */       if (resourceBundleLocale != null) {
/* 227 */         rb = ResourceBundle.getBundle(rbBaseName, resourceBundleLocale);
/*     */       } else {
/* 229 */         rb = ResourceBundle.getBundle(rbBaseName);
/*     */       } 
/* 231 */     } catch (MissingResourceException ex) {
/* 232 */       if (!loop) {
/* 233 */         this.logger.debug("Unable to locate ResourceBundle " + rbBaseName);
/* 234 */         return null;
/*     */       } 
/*     */     } 
/*     */     
/* 238 */     String substr = rbBaseName;
/*     */     int i;
/* 240 */     while (rb == null && (i = substr.lastIndexOf('.')) > 0) {
/* 241 */       substr = substr.substring(0, i);
/*     */       try {
/* 243 */         if (resourceBundleLocale != null) {
/* 244 */           rb = ResourceBundle.getBundle(substr, resourceBundleLocale); continue;
/*     */         } 
/* 246 */         rb = ResourceBundle.getBundle(substr);
/*     */       }
/* 248 */       catch (MissingResourceException ex) {
/* 249 */         this.logger.debug("Unable to locate ResourceBundle " + substr);
/*     */       } 
/*     */     } 
/* 252 */     return rb;
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 256 */     out.defaultWriteObject();
/* 257 */     getFormattedMessage();
/* 258 */     out.writeUTF(this.formattedMessage);
/* 259 */     out.writeUTF(this.key);
/* 260 */     out.writeUTF(this.baseName);
/* 261 */     out.writeInt(this.argArray.length);
/* 262 */     this.stringArgs = new String[this.argArray.length];
/* 263 */     int i = 0;
/* 264 */     for (Object obj : this.argArray) {
/* 265 */       this.stringArgs[i] = obj.toString();
/* 266 */       i++;
/*     */     } 
/* 268 */     out.writeObject(this.stringArgs);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 272 */     in.defaultReadObject();
/* 273 */     this.formattedMessage = in.readUTF();
/* 274 */     this.key = in.readUTF();
/* 275 */     this.baseName = in.readUTF();
/* 276 */     int length = in.readInt();
/* 277 */     this.stringArgs = (String[])in.readObject();
/* 278 */     this.logger = StatusLogger.getLogger();
/* 279 */     this.resourceBundle = null;
/* 280 */     this.argArray = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\message\LocalizedMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */