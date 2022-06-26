/*     */ package joptsimple.internal;
/*     */ 
/*     */ import java.text.BreakIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Columns
/*     */ {
/*     */   private static final int INDENT_WIDTH = 2;
/*     */   private final int optionWidth;
/*     */   private final int descriptionWidth;
/*     */   
/*     */   Columns(int optionWidth, int descriptionWidth) {
/*  47 */     this.optionWidth = optionWidth;
/*  48 */     this.descriptionWidth = descriptionWidth;
/*     */   }
/*     */   
/*     */   List<Row> fit(Row row) {
/*  52 */     List<String> options = piecesOf(row.option, this.optionWidth);
/*  53 */     List<String> descriptions = piecesOf(row.description, this.descriptionWidth);
/*     */     
/*  55 */     List<Row> rows = new ArrayList<Row>();
/*  56 */     for (int i = 0; i < Math.max(options.size(), descriptions.size()); i++) {
/*  57 */       rows.add(new Row(itemOrEmpty(options, i), itemOrEmpty(descriptions, i)));
/*     */     }
/*  59 */     return rows;
/*     */   }
/*     */   
/*     */   private static String itemOrEmpty(List<String> items, int index) {
/*  63 */     return (index >= items.size()) ? "" : items.get(index);
/*     */   }
/*     */   
/*     */   private List<String> piecesOf(String raw, int width) {
/*  67 */     List<String> pieces = new ArrayList<String>();
/*     */     
/*  69 */     for (String each : raw.trim().split(Strings.LINE_SEPARATOR)) {
/*  70 */       pieces.addAll(piecesOfEmbeddedLine(each, width));
/*     */     }
/*  72 */     return pieces;
/*     */   }
/*     */   
/*     */   private List<String> piecesOfEmbeddedLine(String line, int width) {
/*  76 */     List<String> pieces = new ArrayList<String>();
/*     */     
/*  78 */     BreakIterator words = BreakIterator.getLineInstance(Locale.US);
/*  79 */     words.setText(line);
/*     */     
/*  81 */     StringBuilder nextPiece = new StringBuilder();
/*     */     
/*  83 */     int start = words.first(); int end;
/*  84 */     for (end = words.next(); end != -1; start = end, end = words.next()) {
/*  85 */       nextPiece = processNextWord(line, nextPiece, start, end, width, pieces);
/*     */     }
/*  87 */     if (nextPiece.length() > 0) {
/*  88 */       pieces.add(nextPiece.toString());
/*     */     }
/*  90 */     return pieces;
/*     */   }
/*     */ 
/*     */   
/*     */   private StringBuilder processNextWord(String source, StringBuilder nextPiece, int start, int end, int width, List<String> pieces) {
/*  95 */     StringBuilder augmented = nextPiece;
/*     */     
/*  97 */     String word = source.substring(start, end);
/*  98 */     if (augmented.length() + word.length() > width) {
/*  99 */       pieces.add(augmented.toString().replaceAll("\\s+$", ""));
/* 100 */       augmented = (new StringBuilder(Strings.repeat(' ', 2))).append(word);
/*     */     } else {
/*     */       
/* 103 */       augmented.append(word);
/*     */     } 
/* 105 */     return augmented;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\internal\Columns.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */