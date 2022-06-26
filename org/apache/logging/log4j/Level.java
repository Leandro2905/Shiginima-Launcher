/*     */ package org.apache.logging.log4j;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.logging.log4j.spi.StandardLevel;
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
/*     */ public final class Level
/*     */   implements Comparable<Level>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1581082L;
/*  47 */   private static final ConcurrentMap<String, Level> levels = new ConcurrentHashMap<String, Level>();
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
/*  90 */   public static final Level OFF = new Level("OFF", StandardLevel.OFF.intLevel());
/*  91 */   public static final Level FATAL = new Level("FATAL", StandardLevel.FATAL.intLevel());
/*  92 */   public static final Level ERROR = new Level("ERROR", StandardLevel.ERROR.intLevel());
/*  93 */   public static final Level WARN = new Level("WARN", StandardLevel.WARN.intLevel());
/*  94 */   public static final Level INFO = new Level("INFO", StandardLevel.INFO.intLevel());
/*  95 */   public static final Level DEBUG = new Level("DEBUG", StandardLevel.DEBUG.intLevel());
/*  96 */   public static final Level TRACE = new Level("TRACE", StandardLevel.TRACE.intLevel());
/*  97 */   public static final Level ALL = new Level("ALL", StandardLevel.ALL.intLevel());
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final int intLevel;
/*     */   private final StandardLevel standardLevel;
/*     */   
/*     */   private Level(String name, int intLevel) {
/* 105 */     if (name == null || name.isEmpty()) {
/* 106 */       throw new IllegalArgumentException("Illegal null Level constant");
/*     */     }
/* 108 */     if (intLevel < 0) {
/* 109 */       throw new IllegalArgumentException("Illegal Level int less than zero.");
/*     */     }
/* 111 */     this.name = name;
/* 112 */     this.intLevel = intLevel;
/* 113 */     this.standardLevel = StandardLevel.getStandardLevel(intLevel);
/* 114 */     if (levels.putIfAbsent(name, this) != null) {
/* 115 */       throw new IllegalStateException("Level " + name + " has already been defined.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int intLevel() {
/* 125 */     return this.intLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardLevel getStandardLevel() {
/* 134 */     return this.standardLevel;
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
/*     */   public boolean isLessSpecificThan(Level level) {
/* 146 */     return (this.intLevel >= level.intLevel);
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
/*     */   public boolean isMoreSpecificThan(Level level) {
/* 158 */     return (this.intLevel <= level.intLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Level clone() throws CloneNotSupportedException {
/* 164 */     throw new CloneNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Level other) {
/* 169 */     return (this.intLevel < other.intLevel) ? -1 : ((this.intLevel > other.intLevel) ? 1 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 174 */     return (other instanceof Level && other == this);
/*     */   }
/*     */   
/*     */   public Class<Level> getDeclaringClass() {
/* 178 */     return Level.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 183 */     return this.name.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/* 192 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 197 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level forName(String name, int intValue) {
/* 208 */     Level level = levels.get(name);
/* 209 */     if (level != null) {
/* 210 */       return level;
/*     */     }
/*     */     try {
/* 213 */       return new Level(name, intValue);
/* 214 */     } catch (IllegalStateException ex) {
/*     */       
/* 216 */       return levels.get(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level getLevel(String name) {
/* 226 */     return levels.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level toLevel(String sArg) {
/* 237 */     return toLevel(sArg, DEBUG);
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
/*     */   public static Level toLevel(String name, Level defaultLevel) {
/* 250 */     if (name == null) {
/* 251 */       return defaultLevel;
/*     */     }
/* 253 */     Level level = levels.get(name.toUpperCase(Locale.ENGLISH));
/* 254 */     return (level == null) ? defaultLevel : level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level[] values() {
/* 262 */     Collection<Level> values = levels.values();
/* 263 */     return values.<Level>toArray(new Level[values.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level valueOf(String name) {
/* 274 */     if (name == null) {
/* 275 */       throw new NullPointerException("No level name given.");
/*     */     }
/* 277 */     String levelName = name.toUpperCase();
/* 278 */     if (levels.containsKey(levelName)) {
/* 279 */       return levels.get(levelName);
/*     */     }
/* 281 */     throw new IllegalArgumentException("Unknown level constant [" + levelName + "].");
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
/*     */   public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
/* 298 */     return Enum.valueOf(enumType, name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object readResolve() {
/* 303 */     return valueOf(this.name);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\Level.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */