/*     */ package org.apache.commons.lang3.builder;
/*     */ 
/*     */ import org.apache.commons.lang3.ClassUtils;
/*     */ import org.apache.commons.lang3.SystemUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MultilineRecursiveToStringStyle
/*     */   extends RecursiveToStringStyle
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  75 */   private int indent = 2;
/*     */ 
/*     */   
/*  78 */   private int spaces = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultilineRecursiveToStringStyle() {
/*  85 */     resetIndent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resetIndent() {
/*  93 */     setArrayStart("{" + SystemUtils.LINE_SEPARATOR + spacer(this.spaces));
/*  94 */     setArraySeparator("," + SystemUtils.LINE_SEPARATOR + spacer(this.spaces));
/*  95 */     setArrayEnd(SystemUtils.LINE_SEPARATOR + spacer(this.spaces - this.indent) + "}");
/*     */     
/*  97 */     setContentStart("[" + SystemUtils.LINE_SEPARATOR + spacer(this.spaces));
/*  98 */     setFieldSeparator("," + SystemUtils.LINE_SEPARATOR + spacer(this.spaces));
/*  99 */     setContentEnd(SystemUtils.LINE_SEPARATOR + spacer(this.spaces - this.indent) + "]");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StringBuilder spacer(int spaces) {
/* 109 */     StringBuilder sb = new StringBuilder();
/* 110 */     for (int i = 0; i < spaces; i++) {
/* 111 */       sb.append(" ");
/*     */     }
/* 113 */     return sb;
/*     */   }
/*     */ 
/*     */   
/*     */   public void appendDetail(StringBuffer buffer, String fieldName, Object value) {
/* 118 */     if (!ClassUtils.isPrimitiveWrapper(value.getClass()) && !String.class.equals(value.getClass()) && accept(value.getClass())) {
/*     */       
/* 120 */       this.spaces += this.indent;
/* 121 */       resetIndent();
/* 122 */       buffer.append(ReflectionToStringBuilder.toString(value, this));
/* 123 */       this.spaces -= this.indent;
/* 124 */       resetIndent();
/*     */     } else {
/* 126 */       super.appendDetail(buffer, fieldName, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, Object[] array) {
/* 132 */     this.spaces += this.indent;
/* 133 */     resetIndent();
/* 134 */     super.appendDetail(buffer, fieldName, array);
/* 135 */     this.spaces -= this.indent;
/* 136 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void reflectionAppendArrayDetail(StringBuffer buffer, String fieldName, Object array) {
/* 141 */     this.spaces += this.indent;
/* 142 */     resetIndent();
/* 143 */     super.appendDetail(buffer, fieldName, array);
/* 144 */     this.spaces -= this.indent;
/* 145 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, long[] array) {
/* 150 */     this.spaces += this.indent;
/* 151 */     resetIndent();
/* 152 */     super.appendDetail(buffer, fieldName, array);
/* 153 */     this.spaces -= this.indent;
/* 154 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, int[] array) {
/* 159 */     this.spaces += this.indent;
/* 160 */     resetIndent();
/* 161 */     super.appendDetail(buffer, fieldName, array);
/* 162 */     this.spaces -= this.indent;
/* 163 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, short[] array) {
/* 168 */     this.spaces += this.indent;
/* 169 */     resetIndent();
/* 170 */     super.appendDetail(buffer, fieldName, array);
/* 171 */     this.spaces -= this.indent;
/* 172 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, byte[] array) {
/* 177 */     this.spaces += this.indent;
/* 178 */     resetIndent();
/* 179 */     super.appendDetail(buffer, fieldName, array);
/* 180 */     this.spaces -= this.indent;
/* 181 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, char[] array) {
/* 186 */     this.spaces += this.indent;
/* 187 */     resetIndent();
/* 188 */     super.appendDetail(buffer, fieldName, array);
/* 189 */     this.spaces -= this.indent;
/* 190 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, double[] array) {
/* 195 */     this.spaces += this.indent;
/* 196 */     resetIndent();
/* 197 */     super.appendDetail(buffer, fieldName, array);
/* 198 */     this.spaces -= this.indent;
/* 199 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, float[] array) {
/* 204 */     this.spaces += this.indent;
/* 205 */     resetIndent();
/* 206 */     super.appendDetail(buffer, fieldName, array);
/* 207 */     this.spaces -= this.indent;
/* 208 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, boolean[] array) {
/* 213 */     this.spaces += this.indent;
/* 214 */     resetIndent();
/* 215 */     super.appendDetail(buffer, fieldName, array);
/* 216 */     this.spaces -= this.indent;
/* 217 */     resetIndent();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\builder\MultilineRecursiveToStringStyle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */