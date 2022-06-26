/*      */ package org.apache.commons.lang3.text;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   
/*  143 */   public static final StrMatcher DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":-");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private char escapeChar;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher prefixMatcher;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher suffixMatcher;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher valueDelimiterMatcher;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StrLookup<?> variableResolver;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean enableSubstitutionInVariables;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> String replace(Object source, Map<String, V> valueMap) {
/*  181 */     return (new StrSubstitutor(valueMap)).replace(source);
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
/*      */   public static <V> String replace(Object source, Map<String, V> valueMap, String prefix, String suffix) {
/*  198 */     return (new StrSubstitutor(valueMap, prefix, suffix)).replace(source);
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
/*  210 */     if (valueProperties == null) {
/*  211 */       return source.toString();
/*      */     }
/*  213 */     Map<String, String> valueMap = new HashMap<String, String>();
/*  214 */     Enumeration<?> propNames = valueProperties.propertyNames();
/*  215 */     while (propNames.hasMoreElements()) {
/*  216 */       String propName = (String)propNames.nextElement();
/*  217 */       String propValue = valueProperties.getProperty(propName);
/*  218 */       valueMap.put(propName, propValue);
/*      */     } 
/*  220 */     return replace(source, valueMap);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String replaceSystemProperties(Object source) {
/*  231 */     return (new StrSubstitutor(StrLookup.systemPropertiesLookup())).replace(source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor() {
/*  240 */     this((StrLookup)null, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <V> StrSubstitutor(Map<String, V> valueMap) {
/*  251 */     this(StrLookup.mapLookup(valueMap), DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
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
/*      */   public <V> StrSubstitutor(Map<String, V> valueMap, String prefix, String suffix) {
/*  264 */     this(StrLookup.mapLookup(valueMap), prefix, suffix, '$');
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
/*      */   public <V> StrSubstitutor(Map<String, V> valueMap, String prefix, String suffix, char escape) {
/*  279 */     this(StrLookup.mapLookup(valueMap), prefix, suffix, escape);
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
/*      */   public <V> StrSubstitutor(Map<String, V> valueMap, String prefix, String suffix, char escape, String valueDelimiter) {
/*  296 */     this(StrLookup.mapLookup(valueMap), prefix, suffix, escape, valueDelimiter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup<?> variableResolver) {
/*  305 */     this(variableResolver, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
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
/*      */   public StrSubstitutor(StrLookup<?> variableResolver, String prefix, String suffix, char escape) {
/*  319 */     setVariableResolver(variableResolver);
/*  320 */     setVariablePrefix(prefix);
/*  321 */     setVariableSuffix(suffix);
/*  322 */     setEscapeChar(escape);
/*  323 */     setValueDelimiterMatcher(DEFAULT_VALUE_DELIMITER);
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
/*      */   public StrSubstitutor(StrLookup<?> variableResolver, String prefix, String suffix, char escape, String valueDelimiter) {
/*  339 */     setVariableResolver(variableResolver);
/*  340 */     setVariablePrefix(prefix);
/*  341 */     setVariableSuffix(suffix);
/*  342 */     setEscapeChar(escape);
/*  343 */     setValueDelimiter(valueDelimiter);
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
/*      */   public StrSubstitutor(StrLookup<?> variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape) {
/*  358 */     this(variableResolver, prefixMatcher, suffixMatcher, escape, DEFAULT_VALUE_DELIMITER);
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
/*      */   public StrSubstitutor(StrLookup<?> variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape, StrMatcher valueDelimiterMatcher) {
/*  375 */     setVariableResolver(variableResolver);
/*  376 */     setVariablePrefixMatcher(prefixMatcher);
/*  377 */     setVariableSuffixMatcher(suffixMatcher);
/*  378 */     setEscapeChar(escape);
/*  379 */     setValueDelimiterMatcher(valueDelimiterMatcher);
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
/*  391 */     if (source == null) {
/*  392 */       return null;
/*      */     }
/*  394 */     StrBuilder buf = new StrBuilder(source);
/*  395 */     if (!substitute(buf, 0, source.length())) {
/*  396 */       return source;
/*      */     }
/*  398 */     return buf.toString();
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
/*  414 */     if (source == null) {
/*  415 */       return null;
/*      */     }
/*  417 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  418 */     if (!substitute(buf, 0, length)) {
/*  419 */       return source.substring(offset, offset + length);
/*      */     }
/*  421 */     return buf.toString();
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
/*  434 */     if (source == null) {
/*  435 */       return null;
/*      */     }
/*  437 */     StrBuilder buf = (new StrBuilder(source.length)).append(source);
/*  438 */     substitute(buf, 0, source.length);
/*  439 */     return buf.toString();
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
/*  456 */     if (source == null) {
/*  457 */       return null;
/*      */     }
/*  459 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  460 */     substitute(buf, 0, length);
/*  461 */     return buf.toString();
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
/*  474 */     if (source == null) {
/*  475 */       return null;
/*      */     }
/*  477 */     StrBuilder buf = (new StrBuilder(source.length())).append(source);
/*  478 */     substitute(buf, 0, buf.length());
/*  479 */     return buf.toString();
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
/*  496 */     if (source == null) {
/*  497 */       return null;
/*      */     }
/*  499 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  500 */     substitute(buf, 0, length);
/*  501 */     return buf.toString();
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
/*      */   public String replace(CharSequence source) {
/*  514 */     if (source == null) {
/*  515 */       return null;
/*      */     }
/*  517 */     return replace(source, 0, source.length());
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
/*      */   public String replace(CharSequence source, int offset, int length) {
/*  535 */     if (source == null) {
/*  536 */       return null;
/*      */     }
/*  538 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  539 */     substitute(buf, 0, length);
/*  540 */     return buf.toString();
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
/*      */   public String replace(StrBuilder source) {
/*  553 */     if (source == null) {
/*  554 */       return null;
/*      */     }
/*  556 */     StrBuilder buf = (new StrBuilder(source.length())).append(source);
/*  557 */     substitute(buf, 0, buf.length());
/*  558 */     return buf.toString();
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
/*      */   public String replace(StrBuilder source, int offset, int length) {
/*  575 */     if (source == null) {
/*  576 */       return null;
/*      */     }
/*  578 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  579 */     substitute(buf, 0, length);
/*  580 */     return buf.toString();
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
/*  593 */     if (source == null) {
/*  594 */       return null;
/*      */     }
/*  596 */     StrBuilder buf = (new StrBuilder()).append(source);
/*  597 */     substitute(buf, 0, buf.length());
/*  598 */     return buf.toString();
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
/*  611 */     if (source == null) {
/*  612 */       return false;
/*      */     }
/*  614 */     return replaceIn(source, 0, source.length());
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
/*  631 */     if (source == null) {
/*  632 */       return false;
/*      */     }
/*  634 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  635 */     if (!substitute(buf, 0, length)) {
/*  636 */       return false;
/*      */     }
/*  638 */     source.replace(offset, offset + length, buf.toString());
/*  639 */     return true;
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
/*      */   public boolean replaceIn(StringBuilder source) {
/*  653 */     if (source == null) {
/*  654 */       return false;
/*      */     }
/*  656 */     return replaceIn(source, 0, source.length());
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
/*      */   public boolean replaceIn(StringBuilder source, int offset, int length) {
/*  674 */     if (source == null) {
/*  675 */       return false;
/*      */     }
/*  677 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  678 */     if (!substitute(buf, 0, length)) {
/*  679 */       return false;
/*      */     }
/*  681 */     source.replace(offset, offset + length, buf.toString());
/*  682 */     return true;
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
/*      */   public boolean replaceIn(StrBuilder source) {
/*  694 */     if (source == null) {
/*  695 */       return false;
/*      */     }
/*  697 */     return substitute(source, 0, source.length());
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
/*      */   public boolean replaceIn(StrBuilder source, int offset, int length) {
/*  713 */     if (source == null) {
/*  714 */       return false;
/*      */     }
/*  716 */     return substitute(source, offset, length);
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
/*      */   protected boolean substitute(StrBuilder buf, int offset, int length) {
/*  735 */     return (substitute(buf, offset, length, null) > 0);
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
/*      */   private int substitute(StrBuilder buf, int offset, int length, List<String> priorVariables) {
/*  751 */     StrMatcher pfxMatcher = getVariablePrefixMatcher();
/*  752 */     StrMatcher suffMatcher = getVariableSuffixMatcher();
/*  753 */     char escape = getEscapeChar();
/*  754 */     StrMatcher valueDelimMatcher = getValueDelimiterMatcher();
/*  755 */     boolean substitutionInVariablesEnabled = isEnableSubstitutionInVariables();
/*      */     
/*  757 */     boolean top = (priorVariables == null);
/*  758 */     boolean altered = false;
/*  759 */     int lengthChange = 0;
/*  760 */     char[] chars = buf.buffer;
/*  761 */     int bufEnd = offset + length;
/*  762 */     int pos = offset;
/*  763 */     while (pos < bufEnd) {
/*  764 */       int startMatchLen = pfxMatcher.isMatch(chars, pos, offset, bufEnd);
/*      */       
/*  766 */       if (startMatchLen == 0) {
/*  767 */         pos++;
/*      */         continue;
/*      */       } 
/*  770 */       if (pos > offset && chars[pos - 1] == escape) {
/*      */         
/*  772 */         buf.deleteCharAt(pos - 1);
/*  773 */         chars = buf.buffer;
/*  774 */         lengthChange--;
/*  775 */         altered = true;
/*  776 */         bufEnd--;
/*      */         continue;
/*      */       } 
/*  779 */       int startPos = pos;
/*  780 */       pos += startMatchLen;
/*  781 */       int endMatchLen = 0;
/*  782 */       int nestedVarCount = 0;
/*  783 */       while (pos < bufEnd) {
/*  784 */         if (substitutionInVariablesEnabled && (endMatchLen = pfxMatcher.isMatch(chars, pos, offset, bufEnd)) != 0) {
/*      */ 
/*      */ 
/*      */           
/*  788 */           nestedVarCount++;
/*  789 */           pos += endMatchLen;
/*      */           
/*      */           continue;
/*      */         } 
/*  793 */         endMatchLen = suffMatcher.isMatch(chars, pos, offset, bufEnd);
/*      */         
/*  795 */         if (endMatchLen == 0) {
/*  796 */           pos++;
/*      */           continue;
/*      */         } 
/*  799 */         if (nestedVarCount == 0) {
/*  800 */           String varNameExpr = new String(chars, startPos + startMatchLen, pos - startPos - startMatchLen);
/*      */ 
/*      */           
/*  803 */           if (substitutionInVariablesEnabled) {
/*  804 */             StrBuilder bufName = new StrBuilder(varNameExpr);
/*  805 */             substitute(bufName, 0, bufName.length());
/*  806 */             varNameExpr = bufName.toString();
/*      */           } 
/*  808 */           pos += endMatchLen;
/*  809 */           int endPos = pos;
/*      */           
/*  811 */           String varName = varNameExpr;
/*  812 */           String varDefaultValue = null;
/*      */           
/*  814 */           if (valueDelimMatcher != null) {
/*  815 */             char[] varNameExprChars = varNameExpr.toCharArray();
/*  816 */             int valueDelimiterMatchLen = 0;
/*  817 */             for (int i = 0; i < varNameExprChars.length; i++) {
/*      */               
/*  819 */               if (!substitutionInVariablesEnabled && pfxMatcher.isMatch(varNameExprChars, i, i, varNameExprChars.length) != 0) {
/*      */                 break;
/*      */               }
/*      */               
/*  823 */               if ((valueDelimiterMatchLen = valueDelimMatcher.isMatch(varNameExprChars, i)) != 0) {
/*  824 */                 varName = varNameExpr.substring(0, i);
/*  825 */                 varDefaultValue = varNameExpr.substring(i + valueDelimiterMatchLen);
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/*  832 */           if (priorVariables == null) {
/*  833 */             priorVariables = new ArrayList<String>();
/*  834 */             priorVariables.add(new String(chars, offset, length));
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  839 */           checkCyclicSubstitution(varName, priorVariables);
/*  840 */           priorVariables.add(varName);
/*      */ 
/*      */           
/*  843 */           String varValue = resolveVariable(varName, buf, startPos, endPos);
/*      */           
/*  845 */           if (varValue == null) {
/*  846 */             varValue = varDefaultValue;
/*      */           }
/*  848 */           if (varValue != null) {
/*      */             
/*  850 */             int varLen = varValue.length();
/*  851 */             buf.replace(startPos, endPos, varValue);
/*  852 */             altered = true;
/*  853 */             int change = substitute(buf, startPos, varLen, priorVariables);
/*      */             
/*  855 */             change = change + varLen - endPos - startPos;
/*      */             
/*  857 */             pos += change;
/*  858 */             bufEnd += change;
/*  859 */             lengthChange += change;
/*  860 */             chars = buf.buffer;
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  865 */           priorVariables.remove(priorVariables.size() - 1);
/*      */           
/*      */           break;
/*      */         } 
/*  869 */         nestedVarCount--;
/*  870 */         pos += endMatchLen;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  876 */     if (top) {
/*  877 */       return altered ? 1 : 0;
/*      */     }
/*  879 */     return lengthChange;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkCyclicSubstitution(String varName, List<String> priorVariables) {
/*  889 */     if (!priorVariables.contains(varName)) {
/*      */       return;
/*      */     }
/*  892 */     StrBuilder buf = new StrBuilder(256);
/*  893 */     buf.append("Infinite loop in property interpolation of ");
/*  894 */     buf.append(priorVariables.remove(0));
/*  895 */     buf.append(": ");
/*  896 */     buf.appendWithSeparators(priorVariables, "->");
/*  897 */     throw new IllegalStateException(buf.toString());
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
/*      */   protected String resolveVariable(String variableName, StrBuilder buf, int startPos, int endPos) {
/*  918 */     StrLookup<?> resolver = getVariableResolver();
/*  919 */     if (resolver == null) {
/*  920 */       return null;
/*      */     }
/*  922 */     return resolver.lookup(variableName);
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
/*  933 */     return this.escapeChar;
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
/*  944 */     this.escapeChar = escapeCharacter;
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
/*  959 */     return this.prefixMatcher;
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
/*  974 */     if (prefixMatcher == null) {
/*  975 */       throw new IllegalArgumentException("Variable prefix matcher must not be null!");
/*      */     }
/*  977 */     this.prefixMatcher = prefixMatcher;
/*  978 */     return this;
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
/*  992 */     return setVariablePrefixMatcher(StrMatcher.charMatcher(prefix));
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
/* 1006 */     if (prefix == null) {
/* 1007 */       throw new IllegalArgumentException("Variable prefix must not be null!");
/*      */     }
/* 1009 */     return setVariablePrefixMatcher(StrMatcher.stringMatcher(prefix));
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
/* 1024 */     return this.suffixMatcher;
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
/* 1039 */     if (suffixMatcher == null) {
/* 1040 */       throw new IllegalArgumentException("Variable suffix matcher must not be null!");
/*      */     }
/* 1042 */     this.suffixMatcher = suffixMatcher;
/* 1043 */     return this;
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
/* 1057 */     return setVariableSuffixMatcher(StrMatcher.charMatcher(suffix));
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
/* 1071 */     if (suffix == null) {
/* 1072 */       throw new IllegalArgumentException("Variable suffix must not be null!");
/*      */     }
/* 1074 */     return setVariableSuffixMatcher(StrMatcher.stringMatcher(suffix));
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
/*      */   public StrMatcher getValueDelimiterMatcher() {
/* 1092 */     return this.valueDelimiterMatcher;
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
/*      */   public StrSubstitutor setValueDelimiterMatcher(StrMatcher valueDelimiterMatcher) {
/* 1110 */     this.valueDelimiterMatcher = valueDelimiterMatcher;
/* 1111 */     return this;
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
/*      */   public StrSubstitutor setValueDelimiter(char valueDelimiter) {
/* 1126 */     return setValueDelimiterMatcher(StrMatcher.charMatcher(valueDelimiter));
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
/*      */   public StrSubstitutor setValueDelimiter(String valueDelimiter) {
/* 1144 */     if (StringUtils.isEmpty(valueDelimiter)) {
/* 1145 */       setValueDelimiterMatcher(null);
/* 1146 */       return this;
/*      */     } 
/* 1148 */     return setValueDelimiterMatcher(StrMatcher.stringMatcher(valueDelimiter));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrLookup<?> getVariableResolver() {
/* 1159 */     return this.variableResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVariableResolver(StrLookup<?> variableResolver) {
/* 1168 */     this.variableResolver = variableResolver;
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
/*      */   public boolean isEnableSubstitutionInVariables() {
/* 1180 */     return this.enableSubstitutionInVariables;
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
/*      */   public void setEnableSubstitutionInVariables(boolean enableSubstitutionInVariables) {
/* 1194 */     this.enableSubstitutionInVariables = enableSubstitutionInVariables;
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\text\StrSubstitutor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */