/*      */ package org.apache.logging.log4j.core.lookup;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import org.apache.logging.log4j.core.LogEvent;
/*      */ import org.apache.logging.log4j.util.Strings;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class StrSubstitutor
/*      */ {
/*      */   public static final char DEFAULT_ESCAPE = '$';
/*  134 */   public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
/*      */ 
/*      */ 
/*      */   
/*  138 */   public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
/*      */ 
/*      */ 
/*      */   
/*  142 */   public static final StrMatcher DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":-");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int BUF_SIZE = 256;
/*      */ 
/*      */ 
/*      */   
/*      */   private char escapeChar;
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher prefixMatcher;
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher suffixMatcher;
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher valueDelimiterMatcher;
/*      */ 
/*      */ 
/*      */   
/*      */   private StrLookup variableResolver;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean enableSubstitutionInVariables;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor() {
/*  177 */     this((StrLookup)null, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(Map<String, String> valueMap) {
/*  186 */     this(new MapLookup(valueMap), DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(Map<String, String> valueMap, String prefix, String suffix) {
/*  198 */     this(new MapLookup(valueMap), prefix, suffix, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(Map<String, String> valueMap, String prefix, String suffix, char escape) {
/*  212 */     this(new MapLookup(valueMap), prefix, suffix, escape);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(Map<String, String> valueMap, String prefix, String suffix, char escape, String valueDelimiter) {
/*  227 */     this(new MapLookup(valueMap), prefix, suffix, escape, valueDelimiter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup variableResolver) {
/*  236 */     this(variableResolver, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup variableResolver, String prefix, String suffix, char escape) {
/*  250 */     setVariableResolver(variableResolver);
/*  251 */     setVariablePrefix(prefix);
/*  252 */     setVariableSuffix(suffix);
/*  253 */     setEscapeChar(escape);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup variableResolver, String prefix, String suffix, char escape, String valueDelimiter) {
/*  267 */     setVariableResolver(variableResolver);
/*  268 */     setVariablePrefix(prefix);
/*  269 */     setVariableSuffix(suffix);
/*  270 */     setEscapeChar(escape);
/*  271 */     setValueDelimiter(valueDelimiter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape) {
/*  286 */     this(variableResolver, prefixMatcher, suffixMatcher, escape, DEFAULT_VALUE_DELIMITER);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape, StrMatcher valueDelimiterMatcher) {
/*  301 */     setVariableResolver(variableResolver);
/*  302 */     setVariablePrefixMatcher(prefixMatcher);
/*  303 */     setVariableSuffixMatcher(suffixMatcher);
/*  304 */     setEscapeChar(escape);
/*  305 */     setValueDelimiterMatcher(valueDelimiterMatcher);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String replace(Object source, Map<String, String> valueMap) {
/*  318 */     return (new StrSubstitutor(valueMap)).replace(source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String replace(Object source, Map<String, String> valueMap, String prefix, String suffix) {
/*  335 */     return (new StrSubstitutor(valueMap, prefix, suffix)).replace(source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String replace(Object source, Properties valueProperties) {
/*  347 */     if (valueProperties == null) {
/*  348 */       return source.toString();
/*      */     }
/*  350 */     Map<String, String> valueMap = new HashMap<String, String>();
/*  351 */     Enumeration<?> propNames = valueProperties.propertyNames();
/*  352 */     while (propNames.hasMoreElements()) {
/*  353 */       String propName = (String)propNames.nextElement();
/*  354 */       String propValue = valueProperties.getProperty(propName);
/*  355 */       valueMap.put(propName, propValue);
/*      */     } 
/*  357 */     return replace(source, valueMap);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(String source) {
/*  369 */     return replace((LogEvent)null, source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, String source) {
/*  381 */     if (source == null) {
/*  382 */       return null;
/*      */     }
/*  384 */     StringBuilder buf = new StringBuilder(source);
/*  385 */     if (!substitute(event, buf, 0, source.length())) {
/*  386 */       return source;
/*      */     }
/*  388 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(String source, int offset, int length) {
/*  404 */     return replace((LogEvent)null, source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, String source, int offset, int length) {
/*  421 */     if (source == null) {
/*  422 */       return null;
/*      */     }
/*  424 */     StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
/*  425 */     if (!substitute(event, buf, 0, length)) {
/*  426 */       return source.substring(offset, offset + length);
/*      */     }
/*  428 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(char[] source) {
/*  441 */     return replace((LogEvent)null, source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, char[] source) {
/*  455 */     if (source == null) {
/*  456 */       return null;
/*      */     }
/*  458 */     StringBuilder buf = (new StringBuilder(source.length)).append(source);
/*  459 */     substitute(event, buf, 0, source.length);
/*  460 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(char[] source, int offset, int length) {
/*  477 */     return replace((LogEvent)null, source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, char[] source, int offset, int length) {
/*  495 */     if (source == null) {
/*  496 */       return null;
/*      */     }
/*  498 */     StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
/*  499 */     substitute(event, buf, 0, length);
/*  500 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(StringBuffer source) {
/*  513 */     return replace((LogEvent)null, source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, StringBuffer source) {
/*  527 */     if (source == null) {
/*  528 */       return null;
/*      */     }
/*  530 */     StringBuilder buf = (new StringBuilder(source.length())).append(source);
/*  531 */     substitute(event, buf, 0, buf.length());
/*  532 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(StringBuffer source, int offset, int length) {
/*  549 */     return replace((LogEvent)null, source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, StringBuffer source, int offset, int length) {
/*  567 */     if (source == null) {
/*  568 */       return null;
/*      */     }
/*  570 */     StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
/*  571 */     substitute(event, buf, 0, length);
/*  572 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(StringBuilder source) {
/*  585 */     return replace((LogEvent)null, source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, StringBuilder source) {
/*  599 */     if (source == null) {
/*  600 */       return null;
/*      */     }
/*  602 */     StringBuilder buf = (new StringBuilder(source.length())).append(source);
/*  603 */     substitute(event, buf, 0, buf.length());
/*  604 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(StringBuilder source, int offset, int length) {
/*  620 */     return replace((LogEvent)null, source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, StringBuilder source, int offset, int length) {
/*  638 */     if (source == null) {
/*  639 */       return null;
/*      */     }
/*  641 */     StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
/*  642 */     substitute(event, buf, 0, length);
/*  643 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(Object source) {
/*  656 */     return replace((LogEvent)null, source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, Object source) {
/*  669 */     if (source == null) {
/*  670 */       return null;
/*      */     }
/*  672 */     StringBuilder buf = (new StringBuilder()).append(source);
/*  673 */     substitute(event, buf, 0, buf.length());
/*  674 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(StringBuffer source) {
/*  687 */     if (source == null) {
/*  688 */       return false;
/*      */     }
/*  690 */     return replaceIn(source, 0, source.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(StringBuffer source, int offset, int length) {
/*  707 */     return replaceIn((LogEvent)null, source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(LogEvent event, StringBuffer source, int offset, int length) {
/*  725 */     if (source == null) {
/*  726 */       return false;
/*      */     }
/*  728 */     StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
/*  729 */     if (!substitute(event, buf, 0, length)) {
/*  730 */       return false;
/*      */     }
/*  732 */     source.replace(offset, offset + length, buf.toString());
/*  733 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(StringBuilder source) {
/*  745 */     return replaceIn(null, source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(LogEvent event, StringBuilder source) {
/*  758 */     if (source == null) {
/*  759 */       return false;
/*      */     }
/*  761 */     return substitute(event, source, 0, source.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(StringBuilder source, int offset, int length) {
/*  776 */     return replaceIn((LogEvent)null, source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(LogEvent event, StringBuilder source, int offset, int length) {
/*  793 */     if (source == null) {
/*  794 */       return false;
/*      */     }
/*  796 */     return substitute(event, source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean substitute(LogEvent event, StringBuilder buf, int offset, int length) {
/*  816 */     return (substitute(event, buf, offset, length, null) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int substitute(LogEvent event, StringBuilder buf, int offset, int length, List<String> priorVariables) {
/*  834 */     StrMatcher prefixMatcher = getVariablePrefixMatcher();
/*  835 */     StrMatcher suffixMatcher = getVariableSuffixMatcher();
/*  836 */     char escape = getEscapeChar();
/*  837 */     StrMatcher valueDelimiterMatcher = getValueDelimiterMatcher();
/*  838 */     boolean substitutionInVariablesEnabled = isEnableSubstitutionInVariables();
/*      */     
/*  840 */     boolean top = (priorVariables == null);
/*  841 */     boolean altered = false;
/*  842 */     int lengthChange = 0;
/*  843 */     char[] chars = getChars(buf);
/*  844 */     int bufEnd = offset + length;
/*  845 */     int pos = offset;
/*  846 */     while (pos < bufEnd) {
/*  847 */       int startMatchLen = prefixMatcher.isMatch(chars, pos, offset, bufEnd);
/*      */       
/*  849 */       if (startMatchLen == 0) {
/*  850 */         pos++;
/*      */         continue;
/*      */       } 
/*  853 */       if (pos > offset && chars[pos - 1] == escape) {
/*      */         
/*  855 */         buf.deleteCharAt(pos - 1);
/*  856 */         chars = getChars(buf);
/*  857 */         lengthChange--;
/*  858 */         altered = true;
/*  859 */         bufEnd--;
/*      */         continue;
/*      */       } 
/*  862 */       int startPos = pos;
/*  863 */       pos += startMatchLen;
/*  864 */       int endMatchLen = 0;
/*  865 */       int nestedVarCount = 0;
/*  866 */       while (pos < bufEnd) {
/*  867 */         if (substitutionInVariablesEnabled && (endMatchLen = prefixMatcher.isMatch(chars, pos, offset, bufEnd)) != 0) {
/*      */ 
/*      */ 
/*      */           
/*  871 */           nestedVarCount++;
/*  872 */           pos += endMatchLen;
/*      */           
/*      */           continue;
/*      */         } 
/*  876 */         endMatchLen = suffixMatcher.isMatch(chars, pos, offset, bufEnd);
/*      */         
/*  878 */         if (endMatchLen == 0) {
/*  879 */           pos++;
/*      */           continue;
/*      */         } 
/*  882 */         if (nestedVarCount == 0) {
/*  883 */           String varNameExpr = new String(chars, startPos + startMatchLen, pos - startPos - startMatchLen);
/*      */ 
/*      */           
/*  886 */           if (substitutionInVariablesEnabled) {
/*  887 */             StringBuilder bufName = new StringBuilder(varNameExpr);
/*  888 */             substitute(event, bufName, 0, bufName.length());
/*  889 */             varNameExpr = bufName.toString();
/*      */           } 
/*  891 */           pos += endMatchLen;
/*  892 */           int endPos = pos;
/*      */           
/*  894 */           String varName = varNameExpr;
/*  895 */           String varDefaultValue = null;
/*      */           
/*  897 */           if (valueDelimiterMatcher != null) {
/*  898 */             char[] varNameExprChars = varNameExpr.toCharArray();
/*  899 */             int valueDelimiterMatchLen = 0;
/*  900 */             for (int i = 0; i < varNameExprChars.length; i++) {
/*      */               
/*  902 */               if (!substitutionInVariablesEnabled && prefixMatcher.isMatch(varNameExprChars, i, i, varNameExprChars.length) != 0) {
/*      */                 break;
/*      */               }
/*      */               
/*  906 */               if ((valueDelimiterMatchLen = valueDelimiterMatcher.isMatch(varNameExprChars, i)) != 0) {
/*  907 */                 varName = varNameExpr.substring(0, i);
/*  908 */                 varDefaultValue = varNameExpr.substring(i + valueDelimiterMatchLen);
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/*  915 */           if (priorVariables == null) {
/*  916 */             priorVariables = new ArrayList<String>();
/*  917 */             priorVariables.add(new String(chars, offset, length + lengthChange));
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  922 */           checkCyclicSubstitution(varName, priorVariables);
/*  923 */           priorVariables.add(varName);
/*      */ 
/*      */           
/*  926 */           String varValue = resolveVariable(event, varName, buf, startPos, endPos);
/*      */           
/*  928 */           if (varValue == null) {
/*  929 */             varValue = varDefaultValue;
/*      */           }
/*  931 */           if (varValue != null) {
/*      */             
/*  933 */             int varLen = varValue.length();
/*  934 */             buf.replace(startPos, endPos, varValue);
/*  935 */             altered = true;
/*  936 */             int change = substitute(event, buf, startPos, varLen, priorVariables);
/*      */             
/*  938 */             change += varLen - endPos - startPos;
/*      */             
/*  940 */             pos += change;
/*  941 */             bufEnd += change;
/*  942 */             lengthChange += change;
/*  943 */             chars = getChars(buf);
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  948 */           priorVariables.remove(priorVariables.size() - 1);
/*      */           
/*      */           break;
/*      */         } 
/*  952 */         nestedVarCount--;
/*  953 */         pos += endMatchLen;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  960 */     if (top) {
/*  961 */       return altered ? 1 : 0;
/*      */     }
/*  963 */     return lengthChange;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkCyclicSubstitution(String varName, List<String> priorVariables) {
/*  973 */     if (!priorVariables.contains(varName)) {
/*      */       return;
/*      */     }
/*  976 */     StringBuilder buf = new StringBuilder(256);
/*  977 */     buf.append("Infinite loop in property interpolation of ");
/*  978 */     buf.append(priorVariables.remove(0));
/*  979 */     buf.append(": ");
/*  980 */     appendWithSeparators(buf, priorVariables, "->");
/*  981 */     throw new IllegalStateException(buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String resolveVariable(LogEvent event, String variableName, StringBuilder buf, int startPos, int endPos) {
/* 1004 */     StrLookup resolver = getVariableResolver();
/* 1005 */     if (resolver == null) {
/* 1006 */       return null;
/*      */     }
/* 1008 */     return resolver.lookup(event, variableName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char getEscapeChar() {
/* 1019 */     return this.escapeChar;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEscapeChar(char escapeCharacter) {
/* 1030 */     this.escapeChar = escapeCharacter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrMatcher getVariablePrefixMatcher() {
/* 1045 */     return this.prefixMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariablePrefixMatcher(StrMatcher prefixMatcher) {
/* 1060 */     if (prefixMatcher == null) {
/* 1061 */       throw new IllegalArgumentException("Variable prefix matcher must not be null!");
/*      */     }
/* 1063 */     this.prefixMatcher = prefixMatcher;
/* 1064 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariablePrefix(char prefix) {
/* 1078 */     return setVariablePrefixMatcher(StrMatcher.charMatcher(prefix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariablePrefix(String prefix) {
/* 1092 */     if (prefix == null) {
/* 1093 */       throw new IllegalArgumentException("Variable prefix must not be null!");
/*      */     }
/* 1095 */     return setVariablePrefixMatcher(StrMatcher.stringMatcher(prefix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrMatcher getVariableSuffixMatcher() {
/* 1110 */     return this.suffixMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariableSuffixMatcher(StrMatcher suffixMatcher) {
/* 1125 */     if (suffixMatcher == null) {
/* 1126 */       throw new IllegalArgumentException("Variable suffix matcher must not be null!");
/*      */     }
/* 1128 */     this.suffixMatcher = suffixMatcher;
/* 1129 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariableSuffix(char suffix) {
/* 1143 */     return setVariableSuffixMatcher(StrMatcher.charMatcher(suffix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariableSuffix(String suffix) {
/* 1157 */     if (suffix == null) {
/* 1158 */       throw new IllegalArgumentException("Variable suffix must not be null!");
/*      */     }
/* 1160 */     return setVariableSuffixMatcher(StrMatcher.stringMatcher(suffix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrMatcher getValueDelimiterMatcher() {
/* 1177 */     return this.valueDelimiterMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setValueDelimiterMatcher(StrMatcher valueDelimiterMatcher) {
/* 1194 */     this.valueDelimiterMatcher = valueDelimiterMatcher;
/* 1195 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setValueDelimiter(char valueDelimiter) {
/* 1209 */     return setValueDelimiterMatcher(StrMatcher.charMatcher(valueDelimiter));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setValueDelimiter(String valueDelimiter) {
/* 1226 */     if (Strings.isEmpty(valueDelimiter)) {
/* 1227 */       setValueDelimiterMatcher(null);
/* 1228 */       return this;
/*      */     } 
/* 1230 */     return setValueDelimiterMatcher(StrMatcher.stringMatcher(valueDelimiter));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrLookup getVariableResolver() {
/* 1241 */     return this.variableResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVariableResolver(StrLookup variableResolver) {
/* 1250 */     this.variableResolver = variableResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnableSubstitutionInVariables() {
/* 1261 */     return this.enableSubstitutionInVariables;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEnableSubstitutionInVariables(boolean enableSubstitutionInVariables) {
/* 1273 */     this.enableSubstitutionInVariables = enableSubstitutionInVariables;
/*      */   }
/*      */   
/*      */   private char[] getChars(StringBuilder sb) {
/* 1277 */     char[] chars = new char[sb.length()];
/* 1278 */     sb.getChars(0, sb.length(), chars, 0);
/* 1279 */     return chars;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void appendWithSeparators(StringBuilder sb, Iterable<?> iterable, String separator) {
/* 1292 */     if (iterable != null) {
/* 1293 */       separator = (separator == null) ? "" : separator;
/* 1294 */       Iterator<?> it = iterable.iterator();
/* 1295 */       while (it.hasNext()) {
/* 1296 */         sb.append(it.next());
/* 1297 */         if (it.hasNext()) {
/* 1298 */           sb.append(separator);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1306 */     return "StrSubstitutor(" + this.variableResolver.toString() + ')';
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\lookup\StrSubstitutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */