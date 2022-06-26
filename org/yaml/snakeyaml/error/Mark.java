/*     */ package org.yaml.snakeyaml.error;
/*     */ 
/*     */ import org.yaml.snakeyaml.scanner.Constant;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Mark
/*     */ {
/*     */   private String name;
/*     */   private int index;
/*     */   private int line;
/*     */   private int column;
/*     */   private String buffer;
/*     */   private int pointer;
/*     */   
/*     */   public Mark(String name, int index, int line, int column, String buffer, int pointer) {
/*  34 */     this.name = name;
/*  35 */     this.index = index;
/*  36 */     this.line = line;
/*  37 */     this.column = column;
/*  38 */     this.buffer = buffer;
/*  39 */     this.pointer = pointer;
/*     */   }
/*     */   
/*     */   private boolean isLineBreak(char ch) {
/*  43 */     return Constant.NULL_OR_LINEBR.has(ch);
/*     */   }
/*     */   
/*     */   public String get_snippet(int indent, int max_length) {
/*  47 */     if (this.buffer == null) {
/*  48 */       return null;
/*     */     }
/*  50 */     float half = (max_length / 2 - 1);
/*  51 */     int start = this.pointer;
/*  52 */     String head = "";
/*  53 */     while (start > 0 && !isLineBreak(this.buffer.charAt(start - 1))) {
/*  54 */       start--;
/*  55 */       if ((this.pointer - start) > half) {
/*  56 */         head = " ... ";
/*  57 */         start += 5;
/*     */         break;
/*     */       } 
/*     */     } 
/*  61 */     String tail = "";
/*  62 */     int end = this.pointer;
/*  63 */     while (end < this.buffer.length() && !isLineBreak(this.buffer.charAt(end))) {
/*  64 */       end++;
/*  65 */       if ((end - this.pointer) > half) {
/*  66 */         tail = " ... ";
/*  67 */         end -= 5;
/*     */         break;
/*     */       } 
/*     */     } 
/*  71 */     String snippet = this.buffer.substring(start, end);
/*  72 */     StringBuilder result = new StringBuilder(); int i;
/*  73 */     for (i = 0; i < indent; i++) {
/*  74 */       result.append(" ");
/*     */     }
/*  76 */     result.append(head);
/*  77 */     result.append(snippet);
/*  78 */     result.append(tail);
/*  79 */     result.append("\n");
/*  80 */     for (i = 0; i < indent + this.pointer - start + head.length(); i++) {
/*  81 */       result.append(" ");
/*     */     }
/*  83 */     result.append("^");
/*  84 */     return result.toString();
/*     */   }
/*     */   
/*     */   public String get_snippet() {
/*  88 */     return get_snippet(4, 75);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  93 */     String snippet = get_snippet();
/*  94 */     StringBuilder where = new StringBuilder(" in ");
/*  95 */     where.append(this.name);
/*  96 */     where.append(", line ");
/*  97 */     where.append(this.line + 1);
/*  98 */     where.append(", column ");
/*  99 */     where.append(this.column + 1);
/* 100 */     if (snippet != null) {
/* 101 */       where.append(":\n");
/* 102 */       where.append(snippet);
/*     */     } 
/* 104 */     return where.toString();
/*     */   }
/*     */   
/*     */   public String getName() {
/* 108 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLine() {
/* 115 */     return this.line;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumn() {
/* 122 */     return this.column;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIndex() {
/* 129 */     return this.index;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\error\Mark.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */