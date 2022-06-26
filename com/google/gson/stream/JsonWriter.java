/*     */ package com.google.gson.stream;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.Flushable;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonWriter
/*     */   implements Closeable, Flushable
/*     */ {
/* 145 */   private static final String[] REPLACEMENT_CHARS = new String[128]; static {
/* 146 */     for (int i = 0; i <= 31; i++) {
/* 147 */       REPLACEMENT_CHARS[i] = String.format("\\u%04x", new Object[] { Integer.valueOf(i) });
/*     */     } 
/* 149 */     REPLACEMENT_CHARS[34] = "\\\"";
/* 150 */     REPLACEMENT_CHARS[92] = "\\\\";
/* 151 */     REPLACEMENT_CHARS[9] = "\\t";
/* 152 */     REPLACEMENT_CHARS[8] = "\\b";
/* 153 */     REPLACEMENT_CHARS[10] = "\\n";
/* 154 */     REPLACEMENT_CHARS[13] = "\\r";
/* 155 */     REPLACEMENT_CHARS[12] = "\\f";
/* 156 */   } private static final String[] HTML_SAFE_REPLACEMENT_CHARS = (String[])REPLACEMENT_CHARS.clone(); static {
/* 157 */     HTML_SAFE_REPLACEMENT_CHARS[60] = "\\u003c";
/* 158 */     HTML_SAFE_REPLACEMENT_CHARS[62] = "\\u003e";
/* 159 */     HTML_SAFE_REPLACEMENT_CHARS[38] = "\\u0026";
/* 160 */     HTML_SAFE_REPLACEMENT_CHARS[61] = "\\u003d";
/* 161 */     HTML_SAFE_REPLACEMENT_CHARS[39] = "\\u0027";
/*     */   }
/*     */ 
/*     */   
/*     */   private final Writer out;
/*     */   
/* 167 */   private int[] stack = new int[32];
/* 168 */   private int stackSize = 0; private String indent;
/*     */   public JsonWriter(Writer out) {
/* 170 */     push(6);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     this.separator = ":";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     this.serializeNulls = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     if (out == null) {
/* 199 */       throw new NullPointerException("out == null");
/*     */     }
/* 201 */     this.out = out;
/*     */   }
/*     */ 
/*     */   
/*     */   private String separator;
/*     */   
/*     */   private boolean lenient;
/*     */   private boolean htmlSafe;
/*     */   private String deferredName;
/*     */   private boolean serializeNulls;
/*     */   
/*     */   public final void setIndent(String indent) {
/* 213 */     if (indent.length() == 0) {
/* 214 */       this.indent = null;
/* 215 */       this.separator = ":";
/*     */     } else {
/* 217 */       this.indent = indent;
/* 218 */       this.separator = ": ";
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
/*     */   public final void setLenient(boolean lenient) {
/* 235 */     this.lenient = lenient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLenient() {
/* 242 */     return this.lenient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setHtmlSafe(boolean htmlSafe) {
/* 253 */     this.htmlSafe = htmlSafe;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isHtmlSafe() {
/* 261 */     return this.htmlSafe;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSerializeNulls(boolean serializeNulls) {
/* 269 */     this.serializeNulls = serializeNulls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean getSerializeNulls() {
/* 277 */     return this.serializeNulls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter beginArray() throws IOException {
/* 287 */     writeDeferredName();
/* 288 */     return open(1, "[");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter endArray() throws IOException {
/* 297 */     return close(1, 2, "]");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter beginObject() throws IOException {
/* 307 */     writeDeferredName();
/* 308 */     return open(3, "{");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter endObject() throws IOException {
/* 317 */     return close(3, 5, "}");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JsonWriter open(int empty, String openBracket) throws IOException {
/* 325 */     beforeValue(true);
/* 326 */     push(empty);
/* 327 */     this.out.write(openBracket);
/* 328 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JsonWriter close(int empty, int nonempty, String closeBracket) throws IOException {
/* 337 */     int context = peek();
/* 338 */     if (context != nonempty && context != empty) {
/* 339 */       throw new IllegalStateException("Nesting problem.");
/*     */     }
/* 341 */     if (this.deferredName != null) {
/* 342 */       throw new IllegalStateException("Dangling name: " + this.deferredName);
/*     */     }
/*     */     
/* 345 */     this.stackSize--;
/* 346 */     if (context == nonempty) {
/* 347 */       newline();
/*     */     }
/* 349 */     this.out.write(closeBracket);
/* 350 */     return this;
/*     */   }
/*     */   
/*     */   private void push(int newTop) {
/* 354 */     if (this.stackSize == this.stack.length) {
/* 355 */       int[] newStack = new int[this.stackSize * 2];
/* 356 */       System.arraycopy(this.stack, 0, newStack, 0, this.stackSize);
/* 357 */       this.stack = newStack;
/*     */     } 
/* 359 */     this.stack[this.stackSize++] = newTop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int peek() {
/* 366 */     if (this.stackSize == 0) {
/* 367 */       throw new IllegalStateException("JsonWriter is closed.");
/*     */     }
/* 369 */     return this.stack[this.stackSize - 1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void replaceTop(int topOfStack) {
/* 376 */     this.stack[this.stackSize - 1] = topOfStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter name(String name) throws IOException {
/* 386 */     if (name == null) {
/* 387 */       throw new NullPointerException("name == null");
/*     */     }
/* 389 */     if (this.deferredName != null) {
/* 390 */       throw new IllegalStateException();
/*     */     }
/* 392 */     if (this.stackSize == 0) {
/* 393 */       throw new IllegalStateException("JsonWriter is closed.");
/*     */     }
/* 395 */     this.deferredName = name;
/* 396 */     return this;
/*     */   }
/*     */   
/*     */   private void writeDeferredName() throws IOException {
/* 400 */     if (this.deferredName != null) {
/* 401 */       beforeName();
/* 402 */       string(this.deferredName);
/* 403 */       this.deferredName = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter value(String value) throws IOException {
/* 414 */     if (value == null) {
/* 415 */       return nullValue();
/*     */     }
/* 417 */     writeDeferredName();
/* 418 */     beforeValue(false);
/* 419 */     string(value);
/* 420 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter nullValue() throws IOException {
/* 429 */     if (this.deferredName != null) {
/* 430 */       if (this.serializeNulls) {
/* 431 */         writeDeferredName();
/*     */       } else {
/* 433 */         this.deferredName = null;
/* 434 */         return this;
/*     */       } 
/*     */     }
/* 437 */     beforeValue(false);
/* 438 */     this.out.write("null");
/* 439 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter value(boolean value) throws IOException {
/* 448 */     writeDeferredName();
/* 449 */     beforeValue(false);
/* 450 */     this.out.write(value ? "true" : "false");
/* 451 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter value(double value) throws IOException {
/* 462 */     if (Double.isNaN(value) || Double.isInfinite(value)) {
/* 463 */       throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
/*     */     }
/* 465 */     writeDeferredName();
/* 466 */     beforeValue(false);
/* 467 */     this.out.append(Double.toString(value));
/* 468 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter value(long value) throws IOException {
/* 477 */     writeDeferredName();
/* 478 */     beforeValue(false);
/* 479 */     this.out.write(Long.toString(value));
/* 480 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter value(Number value) throws IOException {
/* 491 */     if (value == null) {
/* 492 */       return nullValue();
/*     */     }
/*     */     
/* 495 */     writeDeferredName();
/* 496 */     String string = value.toString();
/* 497 */     if (!this.lenient && (string.equals("-Infinity") || string.equals("Infinity") || string.equals("NaN")))
/*     */     {
/* 499 */       throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
/*     */     }
/* 501 */     beforeValue(false);
/* 502 */     this.out.append(string);
/* 503 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 511 */     if (this.stackSize == 0) {
/* 512 */       throw new IllegalStateException("JsonWriter is closed.");
/*     */     }
/* 514 */     this.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 523 */     this.out.close();
/*     */     
/* 525 */     int size = this.stackSize;
/* 526 */     if (size > 1 || (size == 1 && this.stack[size - 1] != 7)) {
/* 527 */       throw new IOException("Incomplete document");
/*     */     }
/* 529 */     this.stackSize = 0;
/*     */   }
/*     */   
/*     */   private void string(String value) throws IOException {
/* 533 */     String[] replacements = this.htmlSafe ? HTML_SAFE_REPLACEMENT_CHARS : REPLACEMENT_CHARS;
/* 534 */     this.out.write("\"");
/* 535 */     int last = 0;
/* 536 */     int length = value.length();
/* 537 */     for (int i = 0; i < length; i++) {
/* 538 */       String replacement; char c = value.charAt(i);
/*     */       
/* 540 */       if (c < '') {
/* 541 */         replacement = replacements[c];
/* 542 */         if (replacement == null) {
/*     */           continue;
/*     */         }
/* 545 */       } else if (c == ' ') {
/* 546 */         replacement = "\\u2028";
/* 547 */       } else if (c == ' ') {
/* 548 */         replacement = "\\u2029";
/*     */       } else {
/*     */         continue;
/*     */       } 
/* 552 */       if (last < i) {
/* 553 */         this.out.write(value, last, i - last);
/*     */       }
/* 555 */       this.out.write(replacement);
/* 556 */       last = i + 1; continue;
/*     */     } 
/* 558 */     if (last < length) {
/* 559 */       this.out.write(value, last, length - last);
/*     */     }
/* 561 */     this.out.write("\"");
/*     */   }
/*     */   
/*     */   private void newline() throws IOException {
/* 565 */     if (this.indent == null) {
/*     */       return;
/*     */     }
/*     */     
/* 569 */     this.out.write("\n");
/* 570 */     for (int i = 1, size = this.stackSize; i < size; i++) {
/* 571 */       this.out.write(this.indent);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void beforeName() throws IOException {
/* 580 */     int context = peek();
/* 581 */     if (context == 5) {
/* 582 */       this.out.write(44);
/* 583 */     } else if (context != 3) {
/* 584 */       throw new IllegalStateException("Nesting problem.");
/*     */     } 
/* 586 */     newline();
/* 587 */     replaceTop(4);
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
/*     */   private void beforeValue(boolean root) throws IOException {
/* 600 */     switch (peek()) {
/*     */       case 7:
/* 602 */         if (!this.lenient) {
/* 603 */           throw new IllegalStateException("JSON must have only one top-level value.");
/*     */         }
/*     */ 
/*     */       
/*     */       case 6:
/* 608 */         if (!this.lenient && !root) {
/* 609 */           throw new IllegalStateException("JSON must start with an array or an object.");
/*     */         }
/*     */         
/* 612 */         replaceTop(7);
/*     */         return;
/*     */       
/*     */       case 1:
/* 616 */         replaceTop(2);
/* 617 */         newline();
/*     */         return;
/*     */       
/*     */       case 2:
/* 621 */         this.out.append(',');
/* 622 */         newline();
/*     */         return;
/*     */       
/*     */       case 4:
/* 626 */         this.out.append(this.separator);
/* 627 */         replaceTop(5);
/*     */         return;
/*     */     } 
/*     */     
/* 631 */     throw new IllegalStateException("Nesting problem.");
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\gson\stream\JsonWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */