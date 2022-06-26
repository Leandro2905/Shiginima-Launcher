/*     */ package joptsimple.internal;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Rows
/*     */ {
/*     */   private final int overallWidth;
/*     */   private final int columnSeparatorWidth;
/*  41 */   private final Set<Row> rows = new LinkedHashSet<Row>();
/*     */   private int widthOfWidestOption;
/*     */   private int widthOfWidestDescription;
/*     */   
/*     */   public Rows(int overallWidth, int columnSeparatorWidth) {
/*  46 */     this.overallWidth = overallWidth;
/*  47 */     this.columnSeparatorWidth = columnSeparatorWidth;
/*     */   }
/*     */   
/*     */   public void add(String option, String description) {
/*  51 */     add(new Row(option, description));
/*     */   }
/*     */   
/*     */   private void add(Row row) {
/*  55 */     this.rows.add(row);
/*  56 */     this.widthOfWidestOption = Math.max(this.widthOfWidestOption, row.option.length());
/*  57 */     this.widthOfWidestDescription = Math.max(this.widthOfWidestDescription, row.description.length());
/*     */   }
/*     */   
/*     */   private void reset() {
/*  61 */     this.rows.clear();
/*  62 */     this.widthOfWidestOption = 0;
/*  63 */     this.widthOfWidestDescription = 0;
/*     */   }
/*     */   
/*     */   public void fitToWidth() {
/*  67 */     Columns columns = new Columns(optionWidth(), descriptionWidth());
/*     */     
/*  69 */     Set<Row> fitted = new LinkedHashSet<Row>();
/*  70 */     for (Row each : this.rows) {
/*  71 */       fitted.addAll(columns.fit(each));
/*     */     }
/*  73 */     reset();
/*     */     
/*  75 */     for (Row each : fitted)
/*  76 */       add(each); 
/*     */   }
/*     */   
/*     */   public String render() {
/*  80 */     StringBuilder buffer = new StringBuilder();
/*     */     
/*  82 */     for (Row each : this.rows) {
/*  83 */       pad(buffer, each.option, optionWidth()).append(Strings.repeat(' ', this.columnSeparatorWidth));
/*  84 */       pad(buffer, each.description, descriptionWidth()).append(Strings.LINE_SEPARATOR);
/*     */     } 
/*     */     
/*  87 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   private int optionWidth() {
/*  91 */     return Math.min((this.overallWidth - this.columnSeparatorWidth) / 2, this.widthOfWidestOption);
/*     */   }
/*     */   
/*     */   private int descriptionWidth() {
/*  95 */     return Math.min((this.overallWidth - this.columnSeparatorWidth) / 2, this.widthOfWidestDescription);
/*     */   }
/*     */   
/*     */   private StringBuilder pad(StringBuilder buffer, String s, int length) {
/*  99 */     buffer.append(s).append(Strings.repeat(' ', length - s.length()));
/* 100 */     return buffer;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\internal\Rows.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */