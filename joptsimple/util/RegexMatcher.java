/*    */ package joptsimple.util;
/*    */ 
/*    */ import java.util.regex.Pattern;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RegexMatcher
/*    */   implements ValueConverter<String>
/*    */ {
/*    */   private final Pattern pattern;
/*    */   
/*    */   public RegexMatcher(String pattern, int flags) {
/* 53 */     this.pattern = Pattern.compile(pattern, flags);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ValueConverter<String> regex(String pattern) {
/* 64 */     return new RegexMatcher(pattern, 0);
/*    */   }
/*    */   
/*    */   public String convert(String value) {
/* 68 */     if (!this.pattern.matcher(value).matches()) {
/* 69 */       throw new ValueConversionException("Value [" + value + "] did not match regex [" + this.pattern.pattern() + ']');
/*    */     }
/*    */ 
/*    */     
/* 73 */     return value;
/*    */   }
/*    */   
/*    */   public Class<String> valueType() {
/* 77 */     return String.class;
/*    */   }
/*    */   
/*    */   public String valuePattern() {
/* 81 */     return this.pattern.pattern();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimpl\\util\RegexMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */