/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import java.util.regex.Pattern;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ @Plugin(name = "replace", category = "Core", printObject = true)
/*    */ public final class RegexReplacement
/*    */ {
/* 33 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */ 
/*    */ 
/*    */   
/*    */   private final Pattern pattern;
/*    */ 
/*    */ 
/*    */   
/*    */   private final String substitution;
/*    */ 
/*    */ 
/*    */   
/*    */   private RegexReplacement(Pattern pattern, String substitution) {
/* 46 */     this.pattern = pattern;
/* 47 */     this.substitution = substitution;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String format(String msg) {
/* 56 */     return this.pattern.matcher(msg).replaceAll(this.substitution);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return "replace(regex=" + this.pattern.pattern() + ", replacement=" + this.substitution + ')';
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
/*    */   @PluginFactory
/*    */   public static RegexReplacement createRegexReplacement(@PluginAttribute("regex") Pattern regex, @PluginAttribute("replacement") String replacement) {
/* 74 */     if (regex == null) {
/* 75 */       LOGGER.error("A regular expression is required for replacement");
/* 76 */       return null;
/*    */     } 
/* 78 */     if (replacement == null) {
/* 79 */       LOGGER.error("A replacement string is required to perform replacement");
/*    */     }
/*    */     
/* 82 */     return new RegexReplacement(regex, replacement);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\RegexReplacement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */