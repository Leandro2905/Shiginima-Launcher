/*    */ package joptsimple.util;
/*    */ 
/*    */ import java.text.DateFormat;
/*    */ import java.text.ParsePosition;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import joptsimple.ValueConversionException;
/*    */ import joptsimple.ValueConverter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DateConverter
/*    */   implements ValueConverter<Date>
/*    */ {
/*    */   private final DateFormat formatter;
/*    */   
/*    */   public DateConverter(DateFormat formatter) {
/* 51 */     if (formatter == null) {
/* 52 */       throw new NullPointerException("illegal null formatter");
/*    */     }
/* 54 */     this.formatter = formatter;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DateConverter datePattern(String pattern) {
/* 67 */     SimpleDateFormat formatter = new SimpleDateFormat(pattern);
/* 68 */     formatter.setLenient(false);
/*    */     
/* 70 */     return new DateConverter(formatter);
/*    */   }
/*    */   
/*    */   public Date convert(String value) {
/* 74 */     ParsePosition position = new ParsePosition(0);
/*    */     
/* 76 */     Date date = this.formatter.parse(value, position);
/* 77 */     if (position.getIndex() != value.length()) {
/* 78 */       throw new ValueConversionException(message(value));
/*    */     }
/* 80 */     return date;
/*    */   }
/*    */   
/*    */   public Class<Date> valueType() {
/* 84 */     return Date.class;
/*    */   }
/*    */   
/*    */   public String valuePattern() {
/* 88 */     return (this.formatter instanceof SimpleDateFormat) ? ((SimpleDateFormat)this.formatter).toPattern() : "";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private String message(String value) {
/* 94 */     String message = "Value [" + value + "] does not match date/time pattern";
/* 95 */     if (this.formatter instanceof SimpleDateFormat) {
/* 96 */       message = message + " [" + ((SimpleDateFormat)this.formatter).toPattern() + ']';
/*    */     }
/* 98 */     return message;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimpl\\util\DateConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */