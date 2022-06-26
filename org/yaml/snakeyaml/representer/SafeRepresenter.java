/*     */ package org.yaml.snakeyaml.representer;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.reader.StreamReader;
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
/*     */ class SafeRepresenter
/*     */   extends BaseRepresenter
/*     */ {
/*     */   protected Map<Class<? extends Object>, Tag> classTags;
/*  45 */   protected TimeZone timeZone = null;
/*     */   
/*     */   public SafeRepresenter() {
/*  48 */     this.nullRepresenter = new RepresentNull();
/*  49 */     this.representers.put(String.class, new RepresentString());
/*  50 */     this.representers.put(Boolean.class, new RepresentBoolean());
/*  51 */     this.representers.put(Character.class, new RepresentString());
/*  52 */     this.representers.put(byte[].class, new RepresentByteArray());
/*     */     
/*  54 */     Represent primitiveArray = new RepresentPrimitiveArray();
/*  55 */     this.representers.put(short[].class, primitiveArray);
/*  56 */     this.representers.put(int[].class, primitiveArray);
/*  57 */     this.representers.put(long[].class, primitiveArray);
/*  58 */     this.representers.put(float[].class, primitiveArray);
/*  59 */     this.representers.put(double[].class, primitiveArray);
/*  60 */     this.representers.put(char[].class, primitiveArray);
/*  61 */     this.representers.put(boolean[].class, primitiveArray);
/*     */     
/*  63 */     this.multiRepresenters.put(Number.class, new RepresentNumber());
/*  64 */     this.multiRepresenters.put(List.class, new RepresentList());
/*  65 */     this.multiRepresenters.put(Map.class, new RepresentMap());
/*  66 */     this.multiRepresenters.put(Set.class, new RepresentSet());
/*  67 */     this.multiRepresenters.put(Iterator.class, new RepresentIterator());
/*  68 */     this.multiRepresenters.put((new Object[0]).getClass(), new RepresentArray());
/*  69 */     this.multiRepresenters.put(Date.class, new RepresentDate());
/*  70 */     this.multiRepresenters.put(Enum.class, new RepresentEnum());
/*  71 */     this.multiRepresenters.put(Calendar.class, new RepresentDate());
/*  72 */     this.classTags = new HashMap<Class<? extends Object>, Tag>();
/*     */   }
/*     */   
/*     */   protected Tag getTag(Class<?> clazz, Tag defaultTag) {
/*  76 */     if (this.classTags.containsKey(clazz)) {
/*  77 */       return this.classTags.get(clazz);
/*     */     }
/*  79 */     return defaultTag;
/*     */   }
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
/*     */   public Tag addClassTag(Class<? extends Object> clazz, String tag) {
/*  95 */     return addClassTag(clazz, new Tag(tag));
/*     */   }
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
/*     */   public Tag addClassTag(Class<? extends Object> clazz, Tag tag) {
/* 109 */     if (tag == null) {
/* 110 */       throw new NullPointerException("Tag must be provided.");
/*     */     }
/* 112 */     return this.classTags.put(clazz, tag);
/*     */   }
/*     */   
/*     */   protected class RepresentNull implements Represent {
/*     */     public Node representData(Object data) {
/* 117 */       return SafeRepresenter.this.representScalar(Tag.NULL, "null");
/*     */     }
/*     */   }
/*     */   
/* 121 */   public static Pattern MULTILINE_PATTERN = Pattern.compile("\n|| | ");
/*     */   
/*     */   protected class RepresentString implements Represent {
/*     */     public Node representData(Object data) {
/* 125 */       Tag tag = Tag.STR;
/* 126 */       Character style = null;
/* 127 */       String value = data.toString();
/* 128 */       if (StreamReader.NON_PRINTABLE.matcher(value).find()) {
/* 129 */         char[] binary; tag = Tag.BINARY;
/*     */         
/*     */         try {
/* 132 */           binary = Base64Coder.encode(value.getBytes("UTF-8"));
/* 133 */         } catch (UnsupportedEncodingException e) {
/* 134 */           throw new YAMLException(e);
/*     */         } 
/* 136 */         value = String.valueOf(binary);
/* 137 */         style = Character.valueOf('|');
/*     */       } 
/*     */ 
/*     */       
/* 141 */       if (SafeRepresenter.this.defaultScalarStyle == null && SafeRepresenter.MULTILINE_PATTERN.matcher(value).find()) {
/* 142 */         style = Character.valueOf('|');
/*     */       }
/* 144 */       return SafeRepresenter.this.representScalar(tag, value, style);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentBoolean implements Represent {
/*     */     public Node representData(Object data) {
/*     */       String value;
/* 151 */       if (Boolean.TRUE.equals(data)) {
/* 152 */         value = "true";
/*     */       } else {
/* 154 */         value = "false";
/*     */       } 
/* 156 */       return SafeRepresenter.this.representScalar(Tag.BOOL, value);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentNumber implements Represent {
/*     */     public Node representData(Object data) {
/*     */       Tag tag;
/*     */       String value;
/* 164 */       if (data instanceof Byte || data instanceof Short || data instanceof Integer || data instanceof Long || data instanceof java.math.BigInteger) {
/*     */         
/* 166 */         tag = Tag.INT;
/* 167 */         value = data.toString();
/*     */       } else {
/* 169 */         Number number = (Number)data;
/* 170 */         tag = Tag.FLOAT;
/* 171 */         if (number.equals(Double.valueOf(Double.NaN))) {
/* 172 */           value = ".NaN";
/* 173 */         } else if (number.equals(Double.valueOf(Double.POSITIVE_INFINITY))) {
/* 174 */           value = ".inf";
/* 175 */         } else if (number.equals(Double.valueOf(Double.NEGATIVE_INFINITY))) {
/* 176 */           value = "-.inf";
/*     */         } else {
/* 178 */           value = number.toString();
/*     */         } 
/*     */       } 
/* 181 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), tag), value);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentList
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/* 188 */       return SafeRepresenter.this.representSequence(SafeRepresenter.this.getTag(data.getClass(), Tag.SEQ), (List)data, null);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentIterator
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/* 195 */       Iterator<Object> iter = (Iterator<Object>)data;
/* 196 */       return SafeRepresenter.this.representSequence(SafeRepresenter.this.getTag(data.getClass(), Tag.SEQ), new SafeRepresenter.IteratorWrapper(iter), null);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class IteratorWrapper
/*     */     implements Iterable<Object> {
/*     */     private Iterator<Object> iter;
/*     */     
/*     */     public IteratorWrapper(Iterator<Object> iter) {
/* 205 */       this.iter = iter;
/*     */     }
/*     */     
/*     */     public Iterator<Object> iterator() {
/* 209 */       return this.iter;
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentArray implements Represent {
/*     */     public Node representData(Object data) {
/* 215 */       Object[] array = (Object[])data;
/* 216 */       List<Object> list = Arrays.asList(array);
/* 217 */       return SafeRepresenter.this.representSequence(Tag.SEQ, list, null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class RepresentPrimitiveArray
/*     */     implements Represent
/*     */   {
/*     */     public Node representData(Object data) {
/* 228 */       Class<?> type = data.getClass().getComponentType();
/*     */       
/* 230 */       if (byte.class == type)
/* 231 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asByteList(data), null); 
/* 232 */       if (short.class == type)
/* 233 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asShortList(data), null); 
/* 234 */       if (int.class == type)
/* 235 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asIntList(data), null); 
/* 236 */       if (long.class == type)
/* 237 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asLongList(data), null); 
/* 238 */       if (float.class == type)
/* 239 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asFloatList(data), null); 
/* 240 */       if (double.class == type)
/* 241 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asDoubleList(data), null); 
/* 242 */       if (char.class == type)
/* 243 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asCharList(data), null); 
/* 244 */       if (boolean.class == type) {
/* 245 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asBooleanList(data), null);
/*     */       }
/*     */       
/* 248 */       throw new YAMLException("Unexpected primitive '" + type.getCanonicalName() + "'");
/*     */     }
/*     */ 
/*     */     
/*     */     private List<Byte> asByteList(Object in) {
/* 253 */       byte[] array = (byte[])in;
/* 254 */       List<Byte> list = new ArrayList<Byte>(array.length);
/* 255 */       for (int i = 0; i < array.length; i++)
/* 256 */         list.add(Byte.valueOf(array[i])); 
/* 257 */       return list;
/*     */     }
/*     */     
/*     */     private List<Short> asShortList(Object in) {
/* 261 */       short[] array = (short[])in;
/* 262 */       List<Short> list = new ArrayList<Short>(array.length);
/* 263 */       for (int i = 0; i < array.length; i++)
/* 264 */         list.add(Short.valueOf(array[i])); 
/* 265 */       return list;
/*     */     }
/*     */     
/*     */     private List<Integer> asIntList(Object in) {
/* 269 */       int[] array = (int[])in;
/* 270 */       List<Integer> list = new ArrayList<Integer>(array.length);
/* 271 */       for (int i = 0; i < array.length; i++)
/* 272 */         list.add(Integer.valueOf(array[i])); 
/* 273 */       return list;
/*     */     }
/*     */     
/*     */     private List<Long> asLongList(Object in) {
/* 277 */       long[] array = (long[])in;
/* 278 */       List<Long> list = new ArrayList<Long>(array.length);
/* 279 */       for (int i = 0; i < array.length; i++)
/* 280 */         list.add(Long.valueOf(array[i])); 
/* 281 */       return list;
/*     */     }
/*     */     
/*     */     private List<Float> asFloatList(Object in) {
/* 285 */       float[] array = (float[])in;
/* 286 */       List<Float> list = new ArrayList<Float>(array.length);
/* 287 */       for (int i = 0; i < array.length; i++)
/* 288 */         list.add(Float.valueOf(array[i])); 
/* 289 */       return list;
/*     */     }
/*     */     
/*     */     private List<Double> asDoubleList(Object in) {
/* 293 */       double[] array = (double[])in;
/* 294 */       List<Double> list = new ArrayList<Double>(array.length);
/* 295 */       for (int i = 0; i < array.length; i++)
/* 296 */         list.add(Double.valueOf(array[i])); 
/* 297 */       return list;
/*     */     }
/*     */     
/*     */     private List<Character> asCharList(Object in) {
/* 301 */       char[] array = (char[])in;
/* 302 */       List<Character> list = new ArrayList<Character>(array.length);
/* 303 */       for (int i = 0; i < array.length; i++)
/* 304 */         list.add(Character.valueOf(array[i])); 
/* 305 */       return list;
/*     */     }
/*     */     
/*     */     private List<Boolean> asBooleanList(Object in) {
/* 309 */       boolean[] array = (boolean[])in;
/* 310 */       List<Boolean> list = new ArrayList<Boolean>(array.length);
/* 311 */       for (int i = 0; i < array.length; i++)
/* 312 */         list.add(Boolean.valueOf(array[i])); 
/* 313 */       return list;
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentMap
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/* 320 */       return SafeRepresenter.this.representMapping(SafeRepresenter.this.getTag(data.getClass(), Tag.MAP), (Map<?, ?>)data, null);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentSet
/*     */     implements Represent
/*     */   {
/*     */     public Node representData(Object data) {
/* 328 */       Map<Object, Object> value = new LinkedHashMap<Object, Object>();
/* 329 */       Set<Object> set = (Set<Object>)data;
/* 330 */       for (Object key : set) {
/* 331 */         value.put(key, null);
/*     */       }
/* 333 */       return SafeRepresenter.this.representMapping(SafeRepresenter.this.getTag(data.getClass(), Tag.SET), value, null);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentDate
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/*     */       Calendar calendar;
/* 341 */       if (data instanceof Calendar) {
/* 342 */         calendar = (Calendar)data;
/*     */       } else {
/* 344 */         calendar = Calendar.getInstance((SafeRepresenter.this.getTimeZone() == null) ? TimeZone.getTimeZone("UTC") : SafeRepresenter.this.timeZone);
/*     */         
/* 346 */         calendar.setTime((Date)data);
/*     */       } 
/* 348 */       int years = calendar.get(1);
/* 349 */       int months = calendar.get(2) + 1;
/* 350 */       int days = calendar.get(5);
/* 351 */       int hour24 = calendar.get(11);
/* 352 */       int minutes = calendar.get(12);
/* 353 */       int seconds = calendar.get(13);
/* 354 */       int millis = calendar.get(14);
/* 355 */       StringBuilder buffer = new StringBuilder(String.valueOf(years));
/* 356 */       while (buffer.length() < 4)
/*     */       {
/* 358 */         buffer.insert(0, "0");
/*     */       }
/* 360 */       buffer.append("-");
/* 361 */       if (months < 10) {
/* 362 */         buffer.append("0");
/*     */       }
/* 364 */       buffer.append(String.valueOf(months));
/* 365 */       buffer.append("-");
/* 366 */       if (days < 10) {
/* 367 */         buffer.append("0");
/*     */       }
/* 369 */       buffer.append(String.valueOf(days));
/* 370 */       buffer.append("T");
/* 371 */       if (hour24 < 10) {
/* 372 */         buffer.append("0");
/*     */       }
/* 374 */       buffer.append(String.valueOf(hour24));
/* 375 */       buffer.append(":");
/* 376 */       if (minutes < 10) {
/* 377 */         buffer.append("0");
/*     */       }
/* 379 */       buffer.append(String.valueOf(minutes));
/* 380 */       buffer.append(":");
/* 381 */       if (seconds < 10) {
/* 382 */         buffer.append("0");
/*     */       }
/* 384 */       buffer.append(String.valueOf(seconds));
/* 385 */       if (millis > 0) {
/* 386 */         if (millis < 10) {
/* 387 */           buffer.append(".00");
/* 388 */         } else if (millis < 100) {
/* 389 */           buffer.append(".0");
/*     */         } else {
/* 391 */           buffer.append(".");
/*     */         } 
/* 393 */         buffer.append(String.valueOf(millis));
/*     */       } 
/* 395 */       if (TimeZone.getTimeZone("UTC").equals(calendar.getTimeZone())) {
/* 396 */         buffer.append("Z");
/*     */       } else {
/*     */         
/* 399 */         int gmtOffset = calendar.getTimeZone().getOffset(calendar.get(0), calendar.get(1), calendar.get(2), calendar.get(5), calendar.get(7), calendar.get(14));
/*     */ 
/*     */ 
/*     */         
/* 403 */         int minutesOffset = gmtOffset / 60000;
/* 404 */         int hoursOffset = minutesOffset / 60;
/* 405 */         int partOfHour = minutesOffset % 60;
/* 406 */         buffer.append(((hoursOffset > 0) ? "+" : "") + hoursOffset + ":" + ((partOfHour < 10) ? ("0" + partOfHour) : (String)Integer.valueOf(partOfHour)));
/*     */       } 
/*     */       
/* 409 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), Tag.TIMESTAMP), buffer.toString(), null);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentEnum implements Represent {
/*     */     public Node representData(Object data) {
/* 415 */       Tag tag = new Tag(data.getClass());
/* 416 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), tag), ((Enum)data).name());
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentByteArray implements Represent {
/*     */     public Node representData(Object data) {
/* 422 */       char[] binary = Base64Coder.encode((byte[])data);
/* 423 */       return SafeRepresenter.this.representScalar(Tag.BINARY, String.valueOf(binary), Character.valueOf('|'));
/*     */     }
/*     */   }
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 428 */     return this.timeZone;
/*     */   }
/*     */   
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 432 */     this.timeZone = timeZone;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\representer\SafeRepresenter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */