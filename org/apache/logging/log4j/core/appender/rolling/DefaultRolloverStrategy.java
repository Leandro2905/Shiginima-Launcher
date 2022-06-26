/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.Action;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.FileRenameAction;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.GzCompressAction;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.ZipCompressAction;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.core.util.Integers;
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
/*     */ @Plugin(name = "DefaultRolloverStrategy", category = "Core", printObject = true)
/*     */ public class DefaultRolloverStrategy
/*     */   implements RolloverStrategy
/*     */ {
/*     */   private static final String EXT_ZIP = ".zip";
/*     */   private static final String EXT_GZIP = ".gz";
/*  86 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static final int MIN_WINDOW_SIZE = 1;
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_WINDOW_SIZE = 7;
/*     */ 
/*     */   
/*     */   private final int maxIndex;
/*     */   
/*     */   private final int minIndex;
/*     */   
/*     */   private final boolean useMax;
/*     */   
/*     */   private final StrSubstitutor subst;
/*     */   
/*     */   private final int compressionLevel;
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static DefaultRolloverStrategy createStrategy(@PluginAttribute("max") String max, @PluginAttribute("min") String min, @PluginAttribute("fileIndex") String fileIndex, @PluginAttribute("compressionLevel") String compressionLevelStr, @PluginConfiguration Configuration config) {
/* 108 */     boolean useMax = (fileIndex == null) ? true : fileIndex.equalsIgnoreCase("max");
/* 109 */     int minIndex = 1;
/* 110 */     if (min != null) {
/* 111 */       minIndex = Integer.parseInt(min);
/* 112 */       if (minIndex < 1) {
/* 113 */         LOGGER.error("Minimum window size too small. Limited to 1");
/* 114 */         minIndex = 1;
/*     */       } 
/*     */     } 
/* 117 */     int maxIndex = 7;
/* 118 */     if (max != null) {
/* 119 */       maxIndex = Integer.parseInt(max);
/* 120 */       if (maxIndex < minIndex) {
/* 121 */         maxIndex = (minIndex < 7) ? 7 : minIndex;
/* 122 */         LOGGER.error("Maximum window size must be greater than the minimum windows size. Set to " + maxIndex);
/*     */       } 
/*     */     } 
/* 125 */     int compressionLevel = Integers.parseInt(compressionLevelStr, -1);
/* 126 */     return new DefaultRolloverStrategy(minIndex, maxIndex, useMax, compressionLevel, config.getStrSubstitutor());
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected DefaultRolloverStrategy(int minIndex, int maxIndex, boolean useMax, int compressionLevel, StrSubstitutor subst) {
/* 148 */     this.minIndex = minIndex;
/* 149 */     this.maxIndex = maxIndex;
/* 150 */     this.useMax = useMax;
/* 151 */     this.compressionLevel = compressionLevel;
/* 152 */     this.subst = subst;
/*     */   }
/*     */   
/*     */   public int getCompressionLevel() {
/* 156 */     return this.compressionLevel;
/*     */   }
/*     */   
/*     */   public int getMaxIndex() {
/* 160 */     return this.maxIndex;
/*     */   }
/*     */   
/*     */   public int getMinIndex() {
/* 164 */     return this.minIndex;
/*     */   }
/*     */   
/*     */   private int purge(int lowIndex, int highIndex, RollingFileManager manager) {
/* 168 */     return this.useMax ? purgeAscending(lowIndex, highIndex, manager) : purgeDescending(lowIndex, highIndex, manager);
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
/*     */   private int purgeAscending(int lowIndex, int highIndex, RollingFileManager manager) {
/* 182 */     int suffixLength = 0;
/*     */     
/* 184 */     List<FileRenameAction> renames = new ArrayList<FileRenameAction>();
/* 185 */     StringBuilder buf = new StringBuilder();
/*     */ 
/*     */     
/* 188 */     manager.getPatternProcessor().formatFileName(this.subst, buf, Integer.valueOf(highIndex));
/*     */     
/* 190 */     String highFilename = this.subst.replace(buf);
/*     */     
/* 192 */     if (highFilename.endsWith(".gz")) {
/* 193 */       suffixLength = ".gz".length();
/* 194 */     } else if (highFilename.endsWith(".zip")) {
/* 195 */       suffixLength = ".zip".length();
/*     */     } 
/*     */     
/* 198 */     int maxIndex = 0;
/*     */     int i;
/* 200 */     for (i = highIndex; i >= lowIndex; i--) {
/* 201 */       File toRename = new File(highFilename);
/* 202 */       if (i == highIndex && toRename.exists()) {
/* 203 */         maxIndex = highIndex;
/* 204 */       } else if (maxIndex == 0 && toRename.exists()) {
/* 205 */         maxIndex = i + 1;
/*     */         
/*     */         break;
/*     */       } 
/* 209 */       boolean isBase = false;
/*     */       
/* 211 */       if (suffixLength > 0) {
/* 212 */         File toRenameBase = new File(highFilename.substring(0, highFilename.length() - suffixLength));
/*     */ 
/*     */         
/* 215 */         if (toRename.exists()) {
/* 216 */           if (toRenameBase.exists()) {
/* 217 */             LOGGER.debug("DefaultRolloverStrategy.purgeAscending deleting {} base of {}.", new Object[] { toRenameBase, toRename });
/*     */             
/* 219 */             toRenameBase.delete();
/*     */           } 
/*     */         } else {
/* 222 */           toRename = toRenameBase;
/* 223 */           isBase = true;
/*     */         } 
/*     */       } 
/*     */       
/* 227 */       if (toRename.exists()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 232 */         if (i == lowIndex) {
/* 233 */           LOGGER.debug("DefaultRolloverStrategy.purgeAscending deleting {} at low index {}: all slots full.", new Object[] { toRename, Integer.valueOf(i) });
/*     */           
/* 235 */           if (!toRename.delete()) {
/* 236 */             return -1;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */ 
/*     */         
/* 245 */         buf.setLength(0);
/*     */         
/* 247 */         manager.getPatternProcessor().formatFileName(this.subst, buf, Integer.valueOf(i - 1));
/*     */         
/* 249 */         String lowFilename = this.subst.replace(buf);
/* 250 */         String renameTo = lowFilename;
/*     */         
/* 252 */         if (isBase) {
/* 253 */           renameTo = lowFilename.substring(0, lowFilename.length() - suffixLength);
/*     */         }
/*     */         
/* 256 */         renames.add(new FileRenameAction(toRename, new File(renameTo), true));
/* 257 */         highFilename = lowFilename;
/*     */       } else {
/* 259 */         buf.setLength(0);
/*     */         
/* 261 */         manager.getPatternProcessor().formatFileName(this.subst, buf, Integer.valueOf(i - 1));
/*     */         
/* 263 */         highFilename = this.subst.replace(buf);
/*     */       } 
/*     */     } 
/* 266 */     if (maxIndex == 0) {
/* 267 */       maxIndex = lowIndex;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 273 */     for (i = renames.size() - 1; i >= 0; i--) {
/* 274 */       Action action = (Action)renames.get(i);
/*     */       try {
/* 276 */         LOGGER.debug("DefaultRolloverStrategy.purgeAscending executing {} of {}: {}", new Object[] { Integer.valueOf(i), Integer.valueOf(renames.size()), action });
/*     */         
/* 278 */         if (!action.execute()) {
/* 279 */           return -1;
/*     */         }
/* 281 */       } catch (Exception ex) {
/* 282 */         LOGGER.warn("Exception during purge in RollingFileAppender", ex);
/* 283 */         return -1;
/*     */       } 
/*     */     } 
/* 286 */     return maxIndex;
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
/*     */   private int purgeDescending(int lowIndex, int highIndex, RollingFileManager manager) {
/* 299 */     int suffixLength = 0;
/*     */     
/* 301 */     List<FileRenameAction> renames = new ArrayList<FileRenameAction>();
/* 302 */     StringBuilder buf = new StringBuilder();
/*     */ 
/*     */     
/* 305 */     manager.getPatternProcessor().formatFileName(this.subst, buf, Integer.valueOf(lowIndex));
/*     */     
/* 307 */     String lowFilename = this.subst.replace(buf);
/*     */     
/* 309 */     if (lowFilename.endsWith(".gz")) {
/* 310 */       suffixLength = ".gz".length();
/* 311 */     } else if (lowFilename.endsWith(".zip")) {
/* 312 */       suffixLength = ".zip".length();
/*     */     } 
/*     */     int i;
/* 315 */     for (i = lowIndex; i <= highIndex; ) {
/* 316 */       File toRename = new File(lowFilename);
/* 317 */       boolean isBase = false;
/*     */       
/* 319 */       if (suffixLength > 0) {
/* 320 */         File toRenameBase = new File(lowFilename.substring(0, lowFilename.length() - suffixLength));
/*     */ 
/*     */         
/* 323 */         if (toRename.exists()) {
/* 324 */           if (toRenameBase.exists()) {
/* 325 */             LOGGER.debug("DefaultRolloverStrategy.purgeDescending deleting {} base of {}.", new Object[] { toRenameBase, toRename });
/*     */             
/* 327 */             toRenameBase.delete();
/*     */           } 
/*     */         } else {
/* 330 */           toRename = toRenameBase;
/* 331 */           isBase = true;
/*     */         } 
/*     */       } 
/*     */       
/* 335 */       if (toRename.exists()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 340 */         if (i == highIndex) {
/* 341 */           LOGGER.debug("DefaultRolloverStrategy.purgeDescending deleting {} at high index {}: all slots full.", new Object[] { toRename, Integer.valueOf(i) });
/*     */           
/* 343 */           if (!toRename.delete()) {
/* 344 */             return -1;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */ 
/*     */         
/* 353 */         buf.setLength(0);
/*     */         
/* 355 */         manager.getPatternProcessor().formatFileName(this.subst, buf, Integer.valueOf(i + 1));
/*     */         
/* 357 */         String highFilename = this.subst.replace(buf);
/* 358 */         String renameTo = highFilename;
/*     */         
/* 360 */         if (isBase) {
/* 361 */           renameTo = highFilename.substring(0, highFilename.length() - suffixLength);
/*     */         }
/*     */         
/* 364 */         renames.add(new FileRenameAction(toRename, new File(renameTo), true));
/* 365 */         lowFilename = highFilename;
/*     */ 
/*     */ 
/*     */         
/*     */         i++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 374 */     for (i = renames.size() - 1; i >= 0; i--) {
/* 375 */       Action action = (Action)renames.get(i);
/*     */       try {
/* 377 */         LOGGER.debug("DefaultRolloverStrategy.purgeDescending executing {} of {}: {}", new Object[] { Integer.valueOf(i), Integer.valueOf(renames.size()), action });
/*     */         
/* 379 */         if (!action.execute()) {
/* 380 */           return -1;
/*     */         }
/* 382 */       } catch (Exception ex) {
/* 383 */         LOGGER.warn("Exception during purge in RollingFileAppender", ex);
/* 384 */         return -1;
/*     */       } 
/*     */     } 
/*     */     
/* 388 */     return lowIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RolloverDescription rollover(RollingFileManager manager) throws SecurityException {
/*     */     ZipCompressAction zipCompressAction;
/* 399 */     if (this.maxIndex < 0) {
/* 400 */       return null;
/*     */     }
/* 402 */     long start = System.nanoTime();
/* 403 */     int fileIndex = purge(this.minIndex, this.maxIndex, manager);
/* 404 */     if (fileIndex < 0) {
/* 405 */       return null;
/*     */     }
/* 407 */     if (LOGGER.isTraceEnabled()) {
/* 408 */       double duration = (System.nanoTime() - start) / 1.0E9D;
/* 409 */       LOGGER.trace("DefaultRolloverStrategy.purge() took {} seconds", new Object[] { Double.valueOf(duration) });
/*     */     } 
/* 411 */     StringBuilder buf = new StringBuilder(255);
/* 412 */     manager.getPatternProcessor().formatFileName(this.subst, buf, Integer.valueOf(fileIndex));
/* 413 */     String currentFileName = manager.getFileName();
/*     */     
/* 415 */     String renameTo = buf.toString();
/* 416 */     String compressedName = renameTo;
/* 417 */     Action compressAction = null;
/*     */     
/* 419 */     if (renameTo.endsWith(".gz")) {
/* 420 */       renameTo = renameTo.substring(0, renameTo.length() - ".gz".length());
/* 421 */       GzCompressAction gzCompressAction = new GzCompressAction(new File(renameTo), new File(compressedName), true);
/* 422 */     } else if (renameTo.endsWith(".zip")) {
/* 423 */       renameTo = renameTo.substring(0, renameTo.length() - ".zip".length());
/* 424 */       zipCompressAction = new ZipCompressAction(new File(renameTo), new File(compressedName), true, this.compressionLevel);
/*     */     } 
/*     */ 
/*     */     
/* 428 */     FileRenameAction renameAction = new FileRenameAction(new File(currentFileName), new File(renameTo), false);
/*     */ 
/*     */     
/* 431 */     return new RolloverDescriptionImpl(currentFileName, false, (Action)renameAction, (Action)zipCompressAction);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 436 */     return "DefaultRolloverStrategy(min=" + this.minIndex + ", max=" + this.maxIndex + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\rolling\DefaultRolloverStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */