/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.IOException;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.annotation.CheckReturnValue;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public class Joiner
/*     */ {
/*     */   private final String separator;
/*     */   
/*     */   public static Joiner on(String separator) {
/*  71 */     return new Joiner(separator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Joiner on(char separator) {
/*  78 */     return new Joiner(String.valueOf(separator));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Joiner(String separator) {
/*  84 */     this.separator = Preconditions.<String>checkNotNull(separator);
/*     */   }
/*     */   
/*     */   private Joiner(Joiner prototype) {
/*  88 */     this.separator = prototype.separator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException {
/*  96 */     return appendTo(appendable, parts.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
/* 106 */     Preconditions.checkNotNull(appendable);
/* 107 */     if (parts.hasNext()) {
/* 108 */       appendable.append(toString(parts.next()));
/* 109 */       while (parts.hasNext()) {
/* 110 */         appendable.append(this.separator);
/* 111 */         appendable.append(toString(parts.next()));
/*     */       } 
/*     */     } 
/* 114 */     return appendable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <A extends Appendable> A appendTo(A appendable, Object[] parts) throws IOException {
/* 122 */     return appendTo(appendable, Arrays.asList(parts));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <A extends Appendable> A appendTo(A appendable, @Nullable Object first, @Nullable Object second, Object... rest) throws IOException {
/* 131 */     return appendTo(appendable, iterable(first, second, rest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final StringBuilder appendTo(StringBuilder builder, Iterable<?> parts) {
/* 140 */     return appendTo(builder, parts.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final StringBuilder appendTo(StringBuilder builder, Iterator<?> parts) {
/*     */     try {
/* 152 */       appendTo(builder, parts);
/* 153 */     } catch (IOException impossible) {
/* 154 */       throw new AssertionError(impossible);
/*     */     } 
/* 156 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final StringBuilder appendTo(StringBuilder builder, Object[] parts) {
/* 165 */     return appendTo(builder, Arrays.asList(parts));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final StringBuilder appendTo(StringBuilder builder, @Nullable Object first, @Nullable Object second, Object... rest) {
/* 175 */     return appendTo(builder, iterable(first, second, rest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(Iterable<?> parts) {
/* 183 */     return join(parts.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(Iterator<?> parts) {
/* 193 */     return appendTo(new StringBuilder(), parts).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(Object[] parts) {
/* 201 */     return join(Arrays.asList(parts));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(@Nullable Object first, @Nullable Object second, Object... rest) {
/* 209 */     return join(iterable(first, second, rest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public Joiner useForNull(final String nullText) {
/* 218 */     Preconditions.checkNotNull(nullText);
/* 219 */     return new Joiner(this) {
/*     */         CharSequence toString(@Nullable Object part) {
/* 221 */           return (part == null) ? nullText : Joiner.this.toString(part);
/*     */         }
/*     */         
/*     */         public Joiner useForNull(String nullText) {
/* 225 */           throw new UnsupportedOperationException("already specified useForNull");
/*     */         }
/*     */         
/*     */         public Joiner skipNulls() {
/* 229 */           throw new UnsupportedOperationException("already specified useForNull");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public Joiner skipNulls() {
/* 240 */     return new Joiner(this)
/*     */       {
/*     */         public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
/* 243 */           Preconditions.checkNotNull(appendable, "appendable");
/* 244 */           Preconditions.checkNotNull(parts, "parts");
/* 245 */           while (parts.hasNext()) {
/* 246 */             Object part = parts.next();
/* 247 */             if (part != null) {
/* 248 */               appendable.append(Joiner.this.toString(part));
/*     */               break;
/*     */             } 
/*     */           } 
/* 252 */           while (parts.hasNext()) {
/* 253 */             Object part = parts.next();
/* 254 */             if (part != null) {
/* 255 */               appendable.append(Joiner.this.separator);
/* 256 */               appendable.append(Joiner.this.toString(part));
/*     */             } 
/*     */           } 
/* 259 */           return appendable;
/*     */         }
/*     */         
/*     */         public Joiner useForNull(String nullText) {
/* 263 */           throw new UnsupportedOperationException("already specified skipNulls");
/*     */         }
/*     */         
/*     */         public Joiner.MapJoiner withKeyValueSeparator(String kvs) {
/* 267 */           throw new UnsupportedOperationException("can't use .skipNulls() with maps");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public MapJoiner withKeyValueSeparator(String keyValueSeparator) {
/* 278 */     return new MapJoiner(this, keyValueSeparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class MapJoiner
/*     */   {
/*     */     private final Joiner joiner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String keyValueSeparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private MapJoiner(Joiner joiner, String keyValueSeparator) {
/* 304 */       this.joiner = joiner;
/* 305 */       this.keyValueSeparator = Preconditions.<String>checkNotNull(keyValueSeparator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <A extends Appendable> A appendTo(A appendable, Map<?, ?> map) throws IOException {
/* 313 */       return appendTo(appendable, map.entrySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StringBuilder appendTo(StringBuilder builder, Map<?, ?> map) {
/* 322 */       return appendTo(builder, map.entrySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String join(Map<?, ?> map) {
/* 330 */       return join(map.entrySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     public <A extends Appendable> A appendTo(A appendable, Iterable<? extends Map.Entry<?, ?>> entries) throws IOException {
/* 342 */       return appendTo(appendable, entries.iterator());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     public <A extends Appendable> A appendTo(A appendable, Iterator<? extends Map.Entry<?, ?>> parts) throws IOException {
/* 354 */       Preconditions.checkNotNull(appendable);
/* 355 */       if (parts.hasNext()) {
/* 356 */         Map.Entry<?, ?> entry = parts.next();
/* 357 */         appendable.append(this.joiner.toString(entry.getKey()));
/* 358 */         appendable.append(this.keyValueSeparator);
/* 359 */         appendable.append(this.joiner.toString(entry.getValue()));
/* 360 */         while (parts.hasNext()) {
/* 361 */           appendable.append(this.joiner.separator);
/* 362 */           Map.Entry<?, ?> e = parts.next();
/* 363 */           appendable.append(this.joiner.toString(e.getKey()));
/* 364 */           appendable.append(this.keyValueSeparator);
/* 365 */           appendable.append(this.joiner.toString(e.getValue()));
/*     */         } 
/*     */       } 
/* 368 */       return appendable;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     public StringBuilder appendTo(StringBuilder builder, Iterable<? extends Map.Entry<?, ?>> entries) {
/* 380 */       return appendTo(builder, entries.iterator());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     public StringBuilder appendTo(StringBuilder builder, Iterator<? extends Map.Entry<?, ?>> entries) {
/*     */       try {
/* 393 */         appendTo(builder, entries);
/* 394 */       } catch (IOException impossible) {
/* 395 */         throw new AssertionError(impossible);
/*     */       } 
/* 397 */       return builder;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     public String join(Iterable<? extends Map.Entry<?, ?>> entries) {
/* 408 */       return join(entries.iterator());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     public String join(Iterator<? extends Map.Entry<?, ?>> entries) {
/* 419 */       return appendTo(new StringBuilder(), entries).toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckReturnValue
/*     */     public MapJoiner useForNull(String nullText) {
/* 428 */       return new MapJoiner(this.joiner.useForNull(nullText), this.keyValueSeparator);
/*     */     }
/*     */   }
/*     */   
/*     */   CharSequence toString(Object part) {
/* 433 */     Preconditions.checkNotNull(part);
/* 434 */     return (part instanceof CharSequence) ? (CharSequence)part : part.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static Iterable<Object> iterable(final Object first, final Object second, final Object[] rest) {
/* 439 */     Preconditions.checkNotNull(rest);
/* 440 */     return new AbstractList() {
/*     */         public int size() {
/* 442 */           return rest.length + 2;
/*     */         }
/*     */         
/*     */         public Object get(int index) {
/* 446 */           switch (index) {
/*     */             case 0:
/* 448 */               return first;
/*     */             case 1:
/* 450 */               return second;
/*     */           } 
/* 452 */           return rest[index - 2];
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\Joiner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */