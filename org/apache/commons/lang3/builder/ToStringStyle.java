/*      */ package org.apache.commons.lang3.builder;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.Collection;
/*      */ import java.util.Map;
/*      */ import java.util.WeakHashMap;
/*      */ import org.apache.commons.lang3.ClassUtils;
/*      */ import org.apache.commons.lang3.ObjectUtils;
/*      */ import org.apache.commons.lang3.SystemUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class ToStringStyle
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -2587890625525655916L;
/*   81 */   public static final ToStringStyle DEFAULT_STYLE = new DefaultToStringStyle();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   95 */   public static final ToStringStyle MULTI_LINE_STYLE = new MultiLineToStringStyle();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  106 */   public static final ToStringStyle NO_FIELD_NAMES_STYLE = new NoFieldNameToStringStyle();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  118 */   public static final ToStringStyle SHORT_PREFIX_STYLE = new ShortPrefixToStringStyle();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  128 */   public static final ToStringStyle SIMPLE_STYLE = new SimpleToStringStyle();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  140 */   public static final ToStringStyle NO_CLASS_NAME_STYLE = new NoClassNameToStringStyle();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  159 */   public static final ToStringStyle JSON_STYLE = new JsonToStringStyle();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  167 */   private static final ThreadLocal<WeakHashMap<Object, Object>> REGISTRY = new ThreadLocal<WeakHashMap<Object, Object>>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Map<Object, Object> getRegistry() {
/*  188 */     return REGISTRY.get();
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
/*      */   static boolean isRegistered(Object value) {
/*  203 */     Map<Object, Object> m = getRegistry();
/*  204 */     return (m != null && m.containsKey(value));
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
/*      */   static void register(Object value) {
/*  217 */     if (value != null) {
/*  218 */       Map<Object, Object> m = getRegistry();
/*  219 */       if (m == null) {
/*  220 */         REGISTRY.set(new WeakHashMap<Object, Object>());
/*      */       }
/*  222 */       getRegistry().put(value, null);
/*      */     } 
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
/*      */   static void unregister(Object value) {
/*  239 */     if (value != null) {
/*  240 */       Map<Object, Object> m = getRegistry();
/*  241 */       if (m != null) {
/*  242 */         m.remove(value);
/*  243 */         if (m.isEmpty()) {
/*  244 */           REGISTRY.remove();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useFieldNames = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useClassName = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useShortClassName = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useIdentityHashCode = true;
/*      */ 
/*      */ 
/*      */   
/*  273 */   private String contentStart = "[";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  278 */   private String contentEnd = "]";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  283 */   private String fieldNameValueSeparator = "=";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean fieldSeparatorAtStart = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean fieldSeparatorAtEnd = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  298 */   private String fieldSeparator = ",";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  303 */   private String arrayStart = "{";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  308 */   private String arraySeparator = ",";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean arrayContentDetail = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  318 */   private String arrayEnd = "}";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean defaultFullDetail = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  329 */   private String nullText = "<null>";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  334 */   private String sizeStartText = "<size=";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  339 */   private String sizeEndText = ">";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  344 */   private String summaryObjectStartText = "<";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  349 */   private String summaryObjectEndText = ">";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void appendSuper(StringBuffer buffer, String superToString) {
/*  373 */     appendToString(buffer, superToString);
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
/*      */   public void appendToString(StringBuffer buffer, String toString) {
/*  387 */     if (toString != null) {
/*  388 */       int pos1 = toString.indexOf(this.contentStart) + this.contentStart.length();
/*  389 */       int pos2 = toString.lastIndexOf(this.contentEnd);
/*  390 */       if (pos1 != pos2 && pos1 >= 0 && pos2 >= 0) {
/*  391 */         String data = toString.substring(pos1, pos2);
/*  392 */         if (this.fieldSeparatorAtStart) {
/*  393 */           removeLastFieldSeparator(buffer);
/*      */         }
/*  395 */         buffer.append(data);
/*  396 */         appendFieldSeparator(buffer);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void appendStart(StringBuffer buffer, Object object) {
/*  408 */     if (object != null) {
/*  409 */       appendClassName(buffer, object);
/*  410 */       appendIdentityHashCode(buffer, object);
/*  411 */       appendContentStart(buffer);
/*  412 */       if (this.fieldSeparatorAtStart) {
/*  413 */         appendFieldSeparator(buffer);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void appendEnd(StringBuffer buffer, Object object) {
/*  426 */     if (!this.fieldSeparatorAtEnd) {
/*  427 */       removeLastFieldSeparator(buffer);
/*      */     }
/*  429 */     appendContentEnd(buffer);
/*  430 */     unregister(object);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeLastFieldSeparator(StringBuffer buffer) {
/*  440 */     int len = buffer.length();
/*  441 */     int sepLen = this.fieldSeparator.length();
/*  442 */     if (len > 0 && sepLen > 0 && len >= sepLen) {
/*  443 */       boolean match = true;
/*  444 */       for (int i = 0; i < sepLen; i++) {
/*  445 */         if (buffer.charAt(len - 1 - i) != this.fieldSeparator.charAt(sepLen - 1 - i)) {
/*  446 */           match = false;
/*      */           break;
/*      */         } 
/*      */       } 
/*  450 */       if (match) {
/*  451 */         buffer.setLength(len - sepLen);
/*      */       }
/*      */     } 
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
/*      */   public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {
/*  470 */     appendFieldStart(buffer, fieldName);
/*      */     
/*  472 */     if (value == null) {
/*  473 */       appendNullText(buffer, fieldName);
/*      */     } else {
/*      */       
/*  476 */       appendInternal(buffer, fieldName, value, isFullDetail(fullDetail));
/*      */     } 
/*      */     
/*  479 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendInternal(StringBuffer buffer, String fieldName, Object value, boolean detail) {
/*  502 */     if (isRegistered(value) && !(value instanceof Number) && !(value instanceof Boolean) && !(value instanceof Character)) {
/*      */       
/*  504 */       appendCyclicObject(buffer, fieldName, value);
/*      */       
/*      */       return;
/*      */     } 
/*  508 */     register(value);
/*      */     
/*      */     try {
/*  511 */       if (value instanceof Collection) {
/*  512 */         if (detail) {
/*  513 */           appendDetail(buffer, fieldName, (Collection)value);
/*      */         } else {
/*  515 */           appendSummarySize(buffer, fieldName, ((Collection)value).size());
/*      */         }
/*      */       
/*  518 */       } else if (value instanceof Map) {
/*  519 */         if (detail) {
/*  520 */           appendDetail(buffer, fieldName, (Map<?, ?>)value);
/*      */         } else {
/*  522 */           appendSummarySize(buffer, fieldName, ((Map)value).size());
/*      */         }
/*      */       
/*  525 */       } else if (value instanceof long[]) {
/*  526 */         if (detail) {
/*  527 */           appendDetail(buffer, fieldName, (long[])value);
/*      */         } else {
/*  529 */           appendSummary(buffer, fieldName, (long[])value);
/*      */         }
/*      */       
/*  532 */       } else if (value instanceof int[]) {
/*  533 */         if (detail) {
/*  534 */           appendDetail(buffer, fieldName, (int[])value);
/*      */         } else {
/*  536 */           appendSummary(buffer, fieldName, (int[])value);
/*      */         }
/*      */       
/*  539 */       } else if (value instanceof short[]) {
/*  540 */         if (detail) {
/*  541 */           appendDetail(buffer, fieldName, (short[])value);
/*      */         } else {
/*  543 */           appendSummary(buffer, fieldName, (short[])value);
/*      */         }
/*      */       
/*  546 */       } else if (value instanceof byte[]) {
/*  547 */         if (detail) {
/*  548 */           appendDetail(buffer, fieldName, (byte[])value);
/*      */         } else {
/*  550 */           appendSummary(buffer, fieldName, (byte[])value);
/*      */         }
/*      */       
/*  553 */       } else if (value instanceof char[]) {
/*  554 */         if (detail) {
/*  555 */           appendDetail(buffer, fieldName, (char[])value);
/*      */         } else {
/*  557 */           appendSummary(buffer, fieldName, (char[])value);
/*      */         }
/*      */       
/*  560 */       } else if (value instanceof double[]) {
/*  561 */         if (detail) {
/*  562 */           appendDetail(buffer, fieldName, (double[])value);
/*      */         } else {
/*  564 */           appendSummary(buffer, fieldName, (double[])value);
/*      */         }
/*      */       
/*  567 */       } else if (value instanceof float[]) {
/*  568 */         if (detail) {
/*  569 */           appendDetail(buffer, fieldName, (float[])value);
/*      */         } else {
/*  571 */           appendSummary(buffer, fieldName, (float[])value);
/*      */         }
/*      */       
/*  574 */       } else if (value instanceof boolean[]) {
/*  575 */         if (detail) {
/*  576 */           appendDetail(buffer, fieldName, (boolean[])value);
/*      */         } else {
/*  578 */           appendSummary(buffer, fieldName, (boolean[])value);
/*      */         }
/*      */       
/*  581 */       } else if (value.getClass().isArray()) {
/*  582 */         if (detail) {
/*  583 */           appendDetail(buffer, fieldName, (Object[])value);
/*      */         } else {
/*  585 */           appendSummary(buffer, fieldName, (Object[])value);
/*      */         }
/*      */       
/*      */       }
/*  589 */       else if (detail) {
/*  590 */         appendDetail(buffer, fieldName, value);
/*      */       } else {
/*  592 */         appendSummary(buffer, fieldName, value);
/*      */       } 
/*      */     } finally {
/*      */       
/*  596 */       unregister(value);
/*      */     } 
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
/*      */   protected void appendCyclicObject(StringBuffer buffer, String fieldName, Object value) {
/*  613 */     ObjectUtils.identityToString(buffer, value);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
/*  626 */     buffer.append(value);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, Collection<?> coll) {
/*  638 */     buffer.append(coll);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, Map<?, ?> map) {
/*  650 */     buffer.append(map);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, Object value) {
/*  663 */     buffer.append(this.summaryObjectStartText);
/*  664 */     buffer.append(getShortClassName(value.getClass()));
/*  665 */     buffer.append(this.summaryObjectEndText);
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
/*      */   public void append(StringBuffer buffer, String fieldName, long value) {
/*  679 */     appendFieldStart(buffer, fieldName);
/*  680 */     appendDetail(buffer, fieldName, value);
/*  681 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, long value) {
/*  693 */     buffer.append(value);
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
/*      */   public void append(StringBuffer buffer, String fieldName, int value) {
/*  707 */     appendFieldStart(buffer, fieldName);
/*  708 */     appendDetail(buffer, fieldName, value);
/*  709 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, int value) {
/*  721 */     buffer.append(value);
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
/*      */   public void append(StringBuffer buffer, String fieldName, short value) {
/*  735 */     appendFieldStart(buffer, fieldName);
/*  736 */     appendDetail(buffer, fieldName, value);
/*  737 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, short value) {
/*  749 */     buffer.append(value);
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
/*      */   public void append(StringBuffer buffer, String fieldName, byte value) {
/*  763 */     appendFieldStart(buffer, fieldName);
/*  764 */     appendDetail(buffer, fieldName, value);
/*  765 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, byte value) {
/*  777 */     buffer.append(value);
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
/*      */   public void append(StringBuffer buffer, String fieldName, char value) {
/*  791 */     appendFieldStart(buffer, fieldName);
/*  792 */     appendDetail(buffer, fieldName, value);
/*  793 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, char value) {
/*  805 */     buffer.append(value);
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
/*      */   public void append(StringBuffer buffer, String fieldName, double value) {
/*  819 */     appendFieldStart(buffer, fieldName);
/*  820 */     appendDetail(buffer, fieldName, value);
/*  821 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, double value) {
/*  833 */     buffer.append(value);
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
/*      */   public void append(StringBuffer buffer, String fieldName, float value) {
/*  847 */     appendFieldStart(buffer, fieldName);
/*  848 */     appendDetail(buffer, fieldName, value);
/*  849 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, float value) {
/*  861 */     buffer.append(value);
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
/*      */   public void append(StringBuffer buffer, String fieldName, boolean value) {
/*  875 */     appendFieldStart(buffer, fieldName);
/*  876 */     appendDetail(buffer, fieldName, value);
/*  877 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, boolean value) {
/*  889 */     buffer.append(value);
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
/*      */   public void append(StringBuffer buffer, String fieldName, Object[] array, Boolean fullDetail) {
/*  903 */     appendFieldStart(buffer, fieldName);
/*      */     
/*  905 */     if (array == null) {
/*  906 */       appendNullText(buffer, fieldName);
/*      */     }
/*  908 */     else if (isFullDetail(fullDetail)) {
/*  909 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/*  912 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/*  915 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, Object[] array) {
/*  930 */     buffer.append(this.arrayStart);
/*  931 */     for (int i = 0; i < array.length; i++) {
/*  932 */       Object item = array[i];
/*  933 */       if (i > 0) {
/*  934 */         buffer.append(this.arraySeparator);
/*      */       }
/*  936 */       if (item == null) {
/*  937 */         appendNullText(buffer, fieldName);
/*      */       } else {
/*      */         
/*  940 */         appendInternal(buffer, fieldName, item, this.arrayContentDetail);
/*      */       } 
/*      */     } 
/*  943 */     buffer.append(this.arrayEnd);
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
/*      */   protected void reflectionAppendArrayDetail(StringBuffer buffer, String fieldName, Object array) {
/*  956 */     buffer.append(this.arrayStart);
/*  957 */     int length = Array.getLength(array);
/*  958 */     for (int i = 0; i < length; i++) {
/*  959 */       Object item = Array.get(array, i);
/*  960 */       if (i > 0) {
/*  961 */         buffer.append(this.arraySeparator);
/*      */       }
/*  963 */       if (item == null) {
/*  964 */         appendNullText(buffer, fieldName);
/*      */       } else {
/*      */         
/*  967 */         appendInternal(buffer, fieldName, item, this.arrayContentDetail);
/*      */       } 
/*      */     } 
/*  970 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, Object[] array) {
/*  983 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   public void append(StringBuffer buffer, String fieldName, long[] array, Boolean fullDetail) {
/*  999 */     appendFieldStart(buffer, fieldName);
/*      */     
/* 1001 */     if (array == null) {
/* 1002 */       appendNullText(buffer, fieldName);
/*      */     }
/* 1004 */     else if (isFullDetail(fullDetail)) {
/* 1005 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/* 1008 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/* 1011 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, long[] array) {
/* 1024 */     buffer.append(this.arrayStart);
/* 1025 */     for (int i = 0; i < array.length; i++) {
/* 1026 */       if (i > 0) {
/* 1027 */         buffer.append(this.arraySeparator);
/*      */       }
/* 1029 */       appendDetail(buffer, fieldName, array[i]);
/*      */     } 
/* 1031 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, long[] array) {
/* 1044 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   public void append(StringBuffer buffer, String fieldName, int[] array, Boolean fullDetail) {
/* 1060 */     appendFieldStart(buffer, fieldName);
/*      */     
/* 1062 */     if (array == null) {
/* 1063 */       appendNullText(buffer, fieldName);
/*      */     }
/* 1065 */     else if (isFullDetail(fullDetail)) {
/* 1066 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/* 1069 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/* 1072 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, int[] array) {
/* 1085 */     buffer.append(this.arrayStart);
/* 1086 */     for (int i = 0; i < array.length; i++) {
/* 1087 */       if (i > 0) {
/* 1088 */         buffer.append(this.arraySeparator);
/*      */       }
/* 1090 */       appendDetail(buffer, fieldName, array[i]);
/*      */     } 
/* 1092 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, int[] array) {
/* 1105 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   public void append(StringBuffer buffer, String fieldName, short[] array, Boolean fullDetail) {
/* 1121 */     appendFieldStart(buffer, fieldName);
/*      */     
/* 1123 */     if (array == null) {
/* 1124 */       appendNullText(buffer, fieldName);
/*      */     }
/* 1126 */     else if (isFullDetail(fullDetail)) {
/* 1127 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/* 1130 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/* 1133 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, short[] array) {
/* 1146 */     buffer.append(this.arrayStart);
/* 1147 */     for (int i = 0; i < array.length; i++) {
/* 1148 */       if (i > 0) {
/* 1149 */         buffer.append(this.arraySeparator);
/*      */       }
/* 1151 */       appendDetail(buffer, fieldName, array[i]);
/*      */     } 
/* 1153 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, short[] array) {
/* 1166 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   public void append(StringBuffer buffer, String fieldName, byte[] array, Boolean fullDetail) {
/* 1182 */     appendFieldStart(buffer, fieldName);
/*      */     
/* 1184 */     if (array == null) {
/* 1185 */       appendNullText(buffer, fieldName);
/*      */     }
/* 1187 */     else if (isFullDetail(fullDetail)) {
/* 1188 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/* 1191 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/* 1194 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, byte[] array) {
/* 1207 */     buffer.append(this.arrayStart);
/* 1208 */     for (int i = 0; i < array.length; i++) {
/* 1209 */       if (i > 0) {
/* 1210 */         buffer.append(this.arraySeparator);
/*      */       }
/* 1212 */       appendDetail(buffer, fieldName, array[i]);
/*      */     } 
/* 1214 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, byte[] array) {
/* 1227 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   public void append(StringBuffer buffer, String fieldName, char[] array, Boolean fullDetail) {
/* 1243 */     appendFieldStart(buffer, fieldName);
/*      */     
/* 1245 */     if (array == null) {
/* 1246 */       appendNullText(buffer, fieldName);
/*      */     }
/* 1248 */     else if (isFullDetail(fullDetail)) {
/* 1249 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/* 1252 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/* 1255 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, char[] array) {
/* 1268 */     buffer.append(this.arrayStart);
/* 1269 */     for (int i = 0; i < array.length; i++) {
/* 1270 */       if (i > 0) {
/* 1271 */         buffer.append(this.arraySeparator);
/*      */       }
/* 1273 */       appendDetail(buffer, fieldName, array[i]);
/*      */     } 
/* 1275 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, char[] array) {
/* 1288 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   public void append(StringBuffer buffer, String fieldName, double[] array, Boolean fullDetail) {
/* 1304 */     appendFieldStart(buffer, fieldName);
/*      */     
/* 1306 */     if (array == null) {
/* 1307 */       appendNullText(buffer, fieldName);
/*      */     }
/* 1309 */     else if (isFullDetail(fullDetail)) {
/* 1310 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/* 1313 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/* 1316 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, double[] array) {
/* 1329 */     buffer.append(this.arrayStart);
/* 1330 */     for (int i = 0; i < array.length; i++) {
/* 1331 */       if (i > 0) {
/* 1332 */         buffer.append(this.arraySeparator);
/*      */       }
/* 1334 */       appendDetail(buffer, fieldName, array[i]);
/*      */     } 
/* 1336 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, double[] array) {
/* 1349 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   public void append(StringBuffer buffer, String fieldName, float[] array, Boolean fullDetail) {
/* 1365 */     appendFieldStart(buffer, fieldName);
/*      */     
/* 1367 */     if (array == null) {
/* 1368 */       appendNullText(buffer, fieldName);
/*      */     }
/* 1370 */     else if (isFullDetail(fullDetail)) {
/* 1371 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/* 1374 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/* 1377 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, float[] array) {
/* 1390 */     buffer.append(this.arrayStart);
/* 1391 */     for (int i = 0; i < array.length; i++) {
/* 1392 */       if (i > 0) {
/* 1393 */         buffer.append(this.arraySeparator);
/*      */       }
/* 1395 */       appendDetail(buffer, fieldName, array[i]);
/*      */     } 
/* 1397 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, float[] array) {
/* 1410 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   public void append(StringBuffer buffer, String fieldName, boolean[] array, Boolean fullDetail) {
/* 1426 */     appendFieldStart(buffer, fieldName);
/*      */     
/* 1428 */     if (array == null) {
/* 1429 */       appendNullText(buffer, fieldName);
/*      */     }
/* 1431 */     else if (isFullDetail(fullDetail)) {
/* 1432 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/* 1435 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/* 1438 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, boolean[] array) {
/* 1451 */     buffer.append(this.arrayStart);
/* 1452 */     for (int i = 0; i < array.length; i++) {
/* 1453 */       if (i > 0) {
/* 1454 */         buffer.append(this.arraySeparator);
/*      */       }
/* 1456 */       appendDetail(buffer, fieldName, array[i]);
/*      */     } 
/* 1458 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, boolean[] array) {
/* 1471 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   protected void appendClassName(StringBuffer buffer, Object object) {
/* 1483 */     if (this.useClassName && object != null) {
/* 1484 */       register(object);
/* 1485 */       if (this.useShortClassName) {
/* 1486 */         buffer.append(getShortClassName(object.getClass()));
/*      */       } else {
/* 1488 */         buffer.append(object.getClass().getName());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void appendIdentityHashCode(StringBuffer buffer, Object object) {
/* 1500 */     if (isUseIdentityHashCode() && object != null) {
/* 1501 */       register(object);
/* 1502 */       buffer.append('@');
/* 1503 */       buffer.append(Integer.toHexString(System.identityHashCode(object)));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void appendContentStart(StringBuffer buffer) {
/* 1513 */     buffer.append(this.contentStart);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void appendContentEnd(StringBuffer buffer) {
/* 1522 */     buffer.append(this.contentEnd);
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
/*      */   protected void appendNullText(StringBuffer buffer, String fieldName) {
/* 1534 */     buffer.append(this.nullText);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void appendFieldSeparator(StringBuffer buffer) {
/* 1543 */     buffer.append(this.fieldSeparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void appendFieldStart(StringBuffer buffer, String fieldName) {
/* 1553 */     if (this.useFieldNames && fieldName != null) {
/* 1554 */       buffer.append(fieldName);
/* 1555 */       buffer.append(this.fieldNameValueSeparator);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void appendFieldEnd(StringBuffer buffer, String fieldName) {
/* 1566 */     appendFieldSeparator(buffer);
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
/*      */   protected void appendSummarySize(StringBuffer buffer, String fieldName, int size) {
/* 1585 */     buffer.append(this.sizeStartText);
/* 1586 */     buffer.append(size);
/* 1587 */     buffer.append(this.sizeEndText);
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
/*      */   protected boolean isFullDetail(Boolean fullDetailRequest) {
/* 1605 */     if (fullDetailRequest == null) {
/* 1606 */       return this.defaultFullDetail;
/*      */     }
/* 1608 */     return fullDetailRequest.booleanValue();
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
/*      */   protected String getShortClassName(Class<?> cls) {
/* 1621 */     return ClassUtils.getShortClassName(cls);
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
/*      */   protected boolean isUseClassName() {
/* 1635 */     return this.useClassName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setUseClassName(boolean useClassName) {
/* 1644 */     this.useClassName = useClassName;
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
/*      */   protected boolean isUseShortClassName() {
/* 1656 */     return this.useShortClassName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setUseShortClassName(boolean useShortClassName) {
/* 1666 */     this.useShortClassName = useShortClassName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isUseIdentityHashCode() {
/* 1677 */     return this.useIdentityHashCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setUseIdentityHashCode(boolean useIdentityHashCode) {
/* 1686 */     this.useIdentityHashCode = useIdentityHashCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isUseFieldNames() {
/* 1697 */     return this.useFieldNames;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setUseFieldNames(boolean useFieldNames) {
/* 1706 */     this.useFieldNames = useFieldNames;
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
/*      */   protected boolean isDefaultFullDetail() {
/* 1718 */     return this.defaultFullDetail;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setDefaultFullDetail(boolean defaultFullDetail) {
/* 1728 */     this.defaultFullDetail = defaultFullDetail;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isArrayContentDetail() {
/* 1739 */     return this.arrayContentDetail;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setArrayContentDetail(boolean arrayContentDetail) {
/* 1748 */     this.arrayContentDetail = arrayContentDetail;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getArrayStart() {
/* 1759 */     return this.arrayStart;
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
/*      */   protected void setArrayStart(String arrayStart) {
/* 1771 */     if (arrayStart == null) {
/* 1772 */       arrayStart = "";
/*      */     }
/* 1774 */     this.arrayStart = arrayStart;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getArrayEnd() {
/* 1785 */     return this.arrayEnd;
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
/*      */   protected void setArrayEnd(String arrayEnd) {
/* 1797 */     if (arrayEnd == null) {
/* 1798 */       arrayEnd = "";
/*      */     }
/* 1800 */     this.arrayEnd = arrayEnd;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getArraySeparator() {
/* 1811 */     return this.arraySeparator;
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
/*      */   protected void setArraySeparator(String arraySeparator) {
/* 1823 */     if (arraySeparator == null) {
/* 1824 */       arraySeparator = "";
/*      */     }
/* 1826 */     this.arraySeparator = arraySeparator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getContentStart() {
/* 1837 */     return this.contentStart;
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
/*      */   protected void setContentStart(String contentStart) {
/* 1849 */     if (contentStart == null) {
/* 1850 */       contentStart = "";
/*      */     }
/* 1852 */     this.contentStart = contentStart;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getContentEnd() {
/* 1863 */     return this.contentEnd;
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
/*      */   protected void setContentEnd(String contentEnd) {
/* 1875 */     if (contentEnd == null) {
/* 1876 */       contentEnd = "";
/*      */     }
/* 1878 */     this.contentEnd = contentEnd;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getFieldNameValueSeparator() {
/* 1889 */     return this.fieldNameValueSeparator;
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
/*      */   protected void setFieldNameValueSeparator(String fieldNameValueSeparator) {
/* 1901 */     if (fieldNameValueSeparator == null) {
/* 1902 */       fieldNameValueSeparator = "";
/*      */     }
/* 1904 */     this.fieldNameValueSeparator = fieldNameValueSeparator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getFieldSeparator() {
/* 1915 */     return this.fieldSeparator;
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
/*      */   protected void setFieldSeparator(String fieldSeparator) {
/* 1927 */     if (fieldSeparator == null) {
/* 1928 */       fieldSeparator = "";
/*      */     }
/* 1930 */     this.fieldSeparator = fieldSeparator;
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
/*      */   protected boolean isFieldSeparatorAtStart() {
/* 1943 */     return this.fieldSeparatorAtStart;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setFieldSeparatorAtStart(boolean fieldSeparatorAtStart) {
/* 1954 */     this.fieldSeparatorAtStart = fieldSeparatorAtStart;
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
/*      */   protected boolean isFieldSeparatorAtEnd() {
/* 1967 */     return this.fieldSeparatorAtEnd;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setFieldSeparatorAtEnd(boolean fieldSeparatorAtEnd) {
/* 1978 */     this.fieldSeparatorAtEnd = fieldSeparatorAtEnd;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getNullText() {
/* 1989 */     return this.nullText;
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
/*      */   protected void setNullText(String nullText) {
/* 2001 */     if (nullText == null) {
/* 2002 */       nullText = "";
/*      */     }
/* 2004 */     this.nullText = nullText;
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
/*      */   protected String getSizeStartText() {
/* 2018 */     return this.sizeStartText;
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
/*      */   protected void setSizeStartText(String sizeStartText) {
/* 2033 */     if (sizeStartText == null) {
/* 2034 */       sizeStartText = "";
/*      */     }
/* 2036 */     this.sizeStartText = sizeStartText;
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
/*      */   protected String getSizeEndText() {
/* 2050 */     return this.sizeEndText;
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
/*      */   protected void setSizeEndText(String sizeEndText) {
/* 2065 */     if (sizeEndText == null) {
/* 2066 */       sizeEndText = "";
/*      */     }
/* 2068 */     this.sizeEndText = sizeEndText;
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
/*      */   protected String getSummaryObjectStartText() {
/* 2082 */     return this.summaryObjectStartText;
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
/*      */   protected void setSummaryObjectStartText(String summaryObjectStartText) {
/* 2097 */     if (summaryObjectStartText == null) {
/* 2098 */       summaryObjectStartText = "";
/*      */     }
/* 2100 */     this.summaryObjectStartText = summaryObjectStartText;
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
/*      */   protected String getSummaryObjectEndText() {
/* 2114 */     return this.summaryObjectEndText;
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
/*      */   protected void setSummaryObjectEndText(String summaryObjectEndText) {
/* 2129 */     if (summaryObjectEndText == null) {
/* 2130 */       summaryObjectEndText = "";
/*      */     }
/* 2132 */     this.summaryObjectEndText = summaryObjectEndText;
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
/*      */   private static final class DefaultToStringStyle
/*      */     extends ToStringStyle
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object readResolve() {
/* 2167 */       return ToStringStyle.DEFAULT_STYLE;
/*      */     }
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
/*      */   private static final class NoFieldNameToStringStyle
/*      */     extends ToStringStyle
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     NoFieldNameToStringStyle() {
/* 2192 */       setUseFieldNames(false);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object readResolve() {
/* 2201 */       return ToStringStyle.NO_FIELD_NAMES_STYLE;
/*      */     }
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
/*      */   private static final class ShortPrefixToStringStyle
/*      */     extends ToStringStyle
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ShortPrefixToStringStyle() {
/* 2226 */       setUseShortClassName(true);
/* 2227 */       setUseIdentityHashCode(false);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object readResolve() {
/* 2235 */       return ToStringStyle.SHORT_PREFIX_STYLE;
/*      */     }
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
/*      */   private static final class SimpleToStringStyle
/*      */     extends ToStringStyle
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     SimpleToStringStyle() {
/* 2260 */       setUseClassName(false);
/* 2261 */       setUseIdentityHashCode(false);
/* 2262 */       setUseFieldNames(false);
/* 2263 */       setContentStart("");
/* 2264 */       setContentEnd("");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object readResolve() {
/* 2272 */       return ToStringStyle.SIMPLE_STYLE;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class MultiLineToStringStyle
/*      */     extends ToStringStyle
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     MultiLineToStringStyle() {
/* 2296 */       setContentStart("[");
/* 2297 */       setFieldSeparator(SystemUtils.LINE_SEPARATOR + "  ");
/* 2298 */       setFieldSeparatorAtStart(true);
/* 2299 */       setContentEnd(SystemUtils.LINE_SEPARATOR + "]");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object readResolve() {
/* 2308 */       return ToStringStyle.MULTI_LINE_STYLE;
/*      */     }
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
/*      */   private static final class NoClassNameToStringStyle
/*      */     extends ToStringStyle
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     NoClassNameToStringStyle() {
/* 2333 */       setUseClassName(false);
/* 2334 */       setUseIdentityHashCode(false);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object readResolve() {
/* 2343 */       return ToStringStyle.NO_CLASS_NAME_STYLE;
/*      */     }
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
/*      */   private static final class JsonToStringStyle
/*      */     extends ToStringStyle
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2367 */     private String FIELD_NAME_PREFIX = "\"";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     JsonToStringStyle() {
/* 2381 */       setUseClassName(false);
/* 2382 */       setUseIdentityHashCode(false);
/*      */       
/* 2384 */       setContentStart("{");
/* 2385 */       setContentEnd("}");
/*      */       
/* 2387 */       setArrayStart("[");
/* 2388 */       setArrayEnd("]");
/*      */       
/* 2390 */       setFieldSeparator(",");
/* 2391 */       setFieldNameValueSeparator(":");
/*      */       
/* 2393 */       setNullText("null");
/*      */       
/* 2395 */       setSummaryObjectStartText("\"<");
/* 2396 */       setSummaryObjectEndText(">\"");
/*      */       
/* 2398 */       setSizeStartText("\"<size=");
/* 2399 */       setSizeEndText(">\"");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, Object[] array, Boolean fullDetail) {
/* 2406 */       if (fieldName == null) {
/* 2407 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2410 */       if (!isFullDetail(fullDetail)) {
/* 2411 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2415 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, long[] array, Boolean fullDetail) {
/* 2422 */       if (fieldName == null) {
/* 2423 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2426 */       if (!isFullDetail(fullDetail)) {
/* 2427 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2431 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, int[] array, Boolean fullDetail) {
/* 2438 */       if (fieldName == null) {
/* 2439 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2442 */       if (!isFullDetail(fullDetail)) {
/* 2443 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2447 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, short[] array, Boolean fullDetail) {
/* 2454 */       if (fieldName == null) {
/* 2455 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2458 */       if (!isFullDetail(fullDetail)) {
/* 2459 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2463 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, byte[] array, Boolean fullDetail) {
/* 2470 */       if (fieldName == null) {
/* 2471 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2474 */       if (!isFullDetail(fullDetail)) {
/* 2475 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2479 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, char[] array, Boolean fullDetail) {
/* 2486 */       if (fieldName == null) {
/* 2487 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2490 */       if (!isFullDetail(fullDetail)) {
/* 2491 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2495 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, double[] array, Boolean fullDetail) {
/* 2502 */       if (fieldName == null) {
/* 2503 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2506 */       if (!isFullDetail(fullDetail)) {
/* 2507 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2511 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, float[] array, Boolean fullDetail) {
/* 2518 */       if (fieldName == null) {
/* 2519 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2522 */       if (!isFullDetail(fullDetail)) {
/* 2523 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2527 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, boolean[] array, Boolean fullDetail) {
/* 2534 */       if (fieldName == null) {
/* 2535 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2538 */       if (!isFullDetail(fullDetail)) {
/* 2539 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2543 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {
/* 2550 */       if (fieldName == null) {
/* 2551 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2554 */       if (!isFullDetail(fullDetail)) {
/* 2555 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2559 */       super.append(buffer, fieldName, value, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
/* 2565 */       if (value == null) {
/*      */         
/* 2567 */         appendNullText(buffer, fieldName);
/*      */         
/*      */         return;
/*      */       } 
/* 2571 */       if (value.getClass() == String.class) {
/*      */         
/* 2573 */         appendValueAsString(buffer, (String)value);
/*      */         
/*      */         return;
/*      */       } 
/* 2577 */       buffer.append(value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void appendValueAsString(StringBuffer buffer, String value) {
/* 2587 */       buffer.append("\"" + value + "\"");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     protected void appendFieldStart(StringBuffer buffer, String fieldName) {
/* 2593 */       if (fieldName == null) {
/* 2594 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2598 */       super.appendFieldStart(buffer, this.FIELD_NAME_PREFIX + fieldName + this.FIELD_NAME_PREFIX);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object readResolve() {
/* 2610 */       return ToStringStyle.JSON_STYLE;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\builder\ToStringStyle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */