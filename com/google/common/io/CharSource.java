/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CharSource
/*     */ {
/*     */   public abstract Reader openStream() throws IOException;
/*     */   
/*     */   public BufferedReader openBufferedStream() throws IOException {
/*  91 */     Reader reader = openStream();
/*  92 */     return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
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
/*     */   public long copyTo(Appendable appendable) throws IOException {
/* 105 */     Preconditions.checkNotNull(appendable);
/*     */     
/* 107 */     Closer closer = Closer.create();
/*     */     try {
/* 109 */       Reader reader = closer.<Reader>register(openStream());
/* 110 */       return CharStreams.copy(reader, appendable);
/* 111 */     } catch (Throwable e) {
/* 112 */       throw closer.rethrow(e);
/*     */     } finally {
/* 114 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long copyTo(CharSink sink) throws IOException {
/* 125 */     Preconditions.checkNotNull(sink);
/*     */     
/* 127 */     Closer closer = Closer.create();
/*     */     try {
/* 129 */       Reader reader = closer.<Reader>register(openStream());
/* 130 */       Writer writer = closer.<Writer>register(sink.openStream());
/* 131 */       return CharStreams.copy(reader, writer);
/* 132 */     } catch (Throwable e) {
/* 133 */       throw closer.rethrow(e);
/*     */     } finally {
/* 135 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String read() throws IOException {
/* 145 */     Closer closer = Closer.create();
/*     */     try {
/* 147 */       Reader reader = closer.<Reader>register(openStream());
/* 148 */       return CharStreams.toString(reader);
/* 149 */     } catch (Throwable e) {
/* 150 */       throw closer.rethrow(e);
/*     */     } finally {
/* 152 */       closer.close();
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
/*     */   @Nullable
/*     */   public String readFirstLine() throws IOException {
/* 166 */     Closer closer = Closer.create();
/*     */     try {
/* 168 */       BufferedReader reader = closer.<BufferedReader>register(openBufferedStream());
/* 169 */       return reader.readLine();
/* 170 */     } catch (Throwable e) {
/* 171 */       throw closer.rethrow(e);
/*     */     } finally {
/* 173 */       closer.close();
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
/*     */   public ImmutableList<String> readLines() throws IOException {
/* 188 */     Closer closer = Closer.create();
/*     */     try {
/* 190 */       BufferedReader reader = closer.<BufferedReader>register(openBufferedStream());
/* 191 */       List<String> result = Lists.newArrayList();
/*     */       String line;
/* 193 */       while ((line = reader.readLine()) != null) {
/* 194 */         result.add(line);
/*     */       }
/* 196 */       return ImmutableList.copyOf(result);
/* 197 */     } catch (Throwable e) {
/* 198 */       throw closer.rethrow(e);
/*     */     } finally {
/* 200 */       closer.close();
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
/*     */   
/*     */   @Beta
/*     */   public <T> T readLines(LineProcessor<T> processor) throws IOException {
/* 220 */     Preconditions.checkNotNull(processor);
/*     */     
/* 222 */     Closer closer = Closer.create();
/*     */     try {
/* 224 */       Reader reader = closer.<Reader>register(openStream());
/* 225 */       return (T)CharStreams.readLines(reader, (LineProcessor)processor);
/* 226 */     } catch (Throwable e) {
/* 227 */       throw closer.rethrow(e);
/*     */     } finally {
/* 229 */       closer.close();
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
/*     */   public boolean isEmpty() throws IOException {
/* 241 */     Closer closer = Closer.create();
/*     */     try {
/* 243 */       Reader reader = closer.<Reader>register(openStream());
/* 244 */       return (reader.read() == -1);
/* 245 */     } catch (Throwable e) {
/* 246 */       throw closer.rethrow(e);
/*     */     } finally {
/* 248 */       closer.close();
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
/*     */   public static CharSource concat(Iterable<? extends CharSource> sources) {
/* 264 */     return new ConcatenatedCharSource(sources);
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
/*     */   public static CharSource concat(Iterator<? extends CharSource> sources) {
/* 286 */     return concat((Iterable<? extends CharSource>)ImmutableList.copyOf(sources));
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
/*     */   public static CharSource concat(CharSource... sources) {
/* 302 */     return concat((Iterable<? extends CharSource>)ImmutableList.copyOf((Object[])sources));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSource wrap(CharSequence charSequence) {
/* 313 */     return new CharSequenceCharSource(charSequence);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSource empty() {
/* 322 */     return EmptyCharSource.INSTANCE;
/*     */   }
/*     */   
/*     */   private static class CharSequenceCharSource
/*     */     extends CharSource {
/* 327 */     private static final Splitter LINE_SPLITTER = Splitter.on(Pattern.compile("\r\n|\n|\r"));
/*     */     
/*     */     private final CharSequence seq;
/*     */ 
/*     */     
/*     */     protected CharSequenceCharSource(CharSequence seq) {
/* 333 */       this.seq = (CharSequence)Preconditions.checkNotNull(seq);
/*     */     }
/*     */ 
/*     */     
/*     */     public Reader openStream() {
/* 338 */       return new CharSequenceReader(this.seq);
/*     */     }
/*     */ 
/*     */     
/*     */     public String read() {
/* 343 */       return this.seq.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 348 */       return (this.seq.length() == 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Iterable<String> lines() {
/* 357 */       return new Iterable<String>()
/*     */         {
/*     */           public Iterator<String> iterator() {
/* 360 */             return (Iterator<String>)new AbstractIterator<String>() {
/* 361 */                 Iterator<String> lines = CharSource.CharSequenceCharSource.LINE_SPLITTER.split(CharSource.CharSequenceCharSource.this.seq).iterator();
/*     */ 
/*     */                 
/*     */                 protected String computeNext() {
/* 365 */                   if (this.lines.hasNext()) {
/* 366 */                     String next = this.lines.next();
/*     */                     
/* 368 */                     if (this.lines.hasNext() || !next.isEmpty()) {
/* 369 */                       return next;
/*     */                     }
/*     */                   } 
/* 372 */                   return (String)endOfData();
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public String readFirstLine() {
/* 381 */       Iterator<String> lines = lines().iterator();
/* 382 */       return lines.hasNext() ? lines.next() : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<String> readLines() {
/* 387 */       return ImmutableList.copyOf(lines());
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T readLines(LineProcessor<T> processor) throws IOException {
/* 392 */       for (String line : lines()) {
/* 393 */         if (!processor.processLine(line)) {
/*     */           break;
/*     */         }
/*     */       } 
/* 397 */       return processor.getResult();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 402 */       String str = String.valueOf(String.valueOf(Ascii.truncate(this.seq, 30, "..."))); return (new StringBuilder(17 + str.length())).append("CharSource.wrap(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EmptyCharSource
/*     */     extends CharSequenceCharSource {
/* 408 */     private static final EmptyCharSource INSTANCE = new EmptyCharSource();
/*     */     
/*     */     private EmptyCharSource() {
/* 411 */       super("");
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 416 */       return "CharSource.empty()";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ConcatenatedCharSource
/*     */     extends CharSource {
/*     */     private final Iterable<? extends CharSource> sources;
/*     */     
/*     */     ConcatenatedCharSource(Iterable<? extends CharSource> sources) {
/* 425 */       this.sources = (Iterable<? extends CharSource>)Preconditions.checkNotNull(sources);
/*     */     }
/*     */ 
/*     */     
/*     */     public Reader openStream() throws IOException {
/* 430 */       return new MultiReader(this.sources.iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() throws IOException {
/* 435 */       for (CharSource source : this.sources) {
/* 436 */         if (!source.isEmpty()) {
/* 437 */           return false;
/*     */         }
/*     */       } 
/* 440 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 445 */       String str = String.valueOf(String.valueOf(this.sources)); return (new StringBuilder(19 + str.length())).append("CharSource.concat(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\io\CharSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */