/*     */ package com.google.gson.internal.bind;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonIOException;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.google.gson.TypeAdapter;
/*     */ import com.google.gson.TypeAdapterFactory;
/*     */ import com.google.gson.annotations.SerializedName;
/*     */ import com.google.gson.internal.LazilyParsedNumber;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.google.gson.stream.JsonToken;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.net.InetAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.BitSet;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.UUID;
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
/*     */ public final class TypeAdapters
/*     */ {
/*  61 */   public static final TypeAdapter<Class> CLASS = new TypeAdapter<Class>()
/*     */     {
/*     */       public void write(JsonWriter out, Class value) throws IOException {
/*  64 */         if (value == null) {
/*  65 */           out.nullValue();
/*     */         } else {
/*  67 */           throw new UnsupportedOperationException("Attempted to serialize java.lang.Class: " + value.getName() + ". Forgot to register a type adapter?");
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public Class read(JsonReader in) throws IOException {
/*  73 */         if (in.peek() == JsonToken.NULL) {
/*  74 */           in.nextNull();
/*  75 */           return null;
/*     */         } 
/*  77 */         throw new UnsupportedOperationException("Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  82 */   public static final TypeAdapterFactory CLASS_FACTORY = newFactory(Class.class, CLASS);
/*     */   
/*  84 */   public static final TypeAdapter<BitSet> BIT_SET = new TypeAdapter<BitSet>() {
/*     */       public BitSet read(JsonReader in) throws IOException {
/*  86 */         if (in.peek() == JsonToken.NULL) {
/*  87 */           in.nextNull();
/*  88 */           return null;
/*     */         } 
/*     */         
/*  91 */         BitSet bitset = new BitSet();
/*  92 */         in.beginArray();
/*  93 */         int i = 0;
/*  94 */         JsonToken tokenType = in.peek();
/*  95 */         while (tokenType != JsonToken.END_ARRAY) {
/*     */           boolean set; String stringValue;
/*  97 */           switch (tokenType) {
/*     */             case NUMBER:
/*  99 */               set = (in.nextInt() != 0);
/*     */               break;
/*     */             case BOOLEAN:
/* 102 */               set = in.nextBoolean();
/*     */               break;
/*     */             case STRING:
/* 105 */               stringValue = in.nextString();
/*     */               try {
/* 107 */                 set = (Integer.parseInt(stringValue) != 0);
/* 108 */               } catch (NumberFormatException e) {
/* 109 */                 throw new JsonSyntaxException("Error: Expecting: bitset number value (1, 0), Found: " + stringValue);
/*     */               } 
/*     */               break;
/*     */             
/*     */             default:
/* 114 */               throw new JsonSyntaxException("Invalid bitset value type: " + tokenType);
/*     */           } 
/* 116 */           if (set) {
/* 117 */             bitset.set(i);
/*     */           }
/* 119 */           i++;
/* 120 */           tokenType = in.peek();
/*     */         } 
/* 122 */         in.endArray();
/* 123 */         return bitset;
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, BitSet src) throws IOException {
/* 127 */         if (src == null) {
/* 128 */           out.nullValue();
/*     */           
/*     */           return;
/*     */         } 
/* 132 */         out.beginArray();
/* 133 */         for (int i = 0; i < src.length(); i++) {
/* 134 */           int value = src.get(i) ? 1 : 0;
/* 135 */           out.value(value);
/*     */         } 
/* 137 */         out.endArray();
/*     */       }
/*     */     };
/*     */   
/* 141 */   public static final TypeAdapterFactory BIT_SET_FACTORY = newFactory(BitSet.class, BIT_SET);
/*     */   
/* 143 */   public static final TypeAdapter<Boolean> BOOLEAN = new TypeAdapter<Boolean>()
/*     */     {
/*     */       public Boolean read(JsonReader in) throws IOException {
/* 146 */         if (in.peek() == JsonToken.NULL) {
/* 147 */           in.nextNull();
/* 148 */           return null;
/* 149 */         }  if (in.peek() == JsonToken.STRING)
/*     */         {
/* 151 */           return Boolean.valueOf(Boolean.parseBoolean(in.nextString()));
/*     */         }
/* 153 */         return Boolean.valueOf(in.nextBoolean());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Boolean value) throws IOException {
/* 157 */         if (value == null) {
/* 158 */           out.nullValue();
/*     */           return;
/*     */         } 
/* 161 */         out.value(value.booleanValue());
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   public static final TypeAdapter<Boolean> BOOLEAN_AS_STRING = new TypeAdapter<Boolean>() {
/*     */       public Boolean read(JsonReader in) throws IOException {
/* 171 */         if (in.peek() == JsonToken.NULL) {
/* 172 */           in.nextNull();
/* 173 */           return null;
/*     */         } 
/* 175 */         return Boolean.valueOf(in.nextString());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Boolean value) throws IOException {
/* 179 */         out.value((value == null) ? "null" : value.toString());
/*     */       }
/*     */     };
/*     */   
/* 183 */   public static final TypeAdapterFactory BOOLEAN_FACTORY = newFactory(boolean.class, (Class)Boolean.class, (TypeAdapter)BOOLEAN);
/*     */ 
/*     */   
/* 186 */   public static final TypeAdapter<Number> BYTE = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 189 */         if (in.peek() == JsonToken.NULL) {
/* 190 */           in.nextNull();
/* 191 */           return null;
/*     */         } 
/*     */         try {
/* 194 */           int intValue = in.nextInt();
/* 195 */           return Byte.valueOf((byte)intValue);
/* 196 */         } catch (NumberFormatException e) {
/* 197 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 202 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 206 */   public static final TypeAdapterFactory BYTE_FACTORY = newFactory(byte.class, (Class)Byte.class, (TypeAdapter)BYTE);
/*     */ 
/*     */   
/* 209 */   public static final TypeAdapter<Number> SHORT = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 212 */         if (in.peek() == JsonToken.NULL) {
/* 213 */           in.nextNull();
/* 214 */           return null;
/*     */         } 
/*     */         try {
/* 217 */           return Short.valueOf((short)in.nextInt());
/* 218 */         } catch (NumberFormatException e) {
/* 219 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 224 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 228 */   public static final TypeAdapterFactory SHORT_FACTORY = newFactory(short.class, (Class)Short.class, (TypeAdapter)SHORT);
/*     */ 
/*     */   
/* 231 */   public static final TypeAdapter<Number> INTEGER = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 234 */         if (in.peek() == JsonToken.NULL) {
/* 235 */           in.nextNull();
/* 236 */           return null;
/*     */         } 
/*     */         try {
/* 239 */           return Integer.valueOf(in.nextInt());
/* 240 */         } catch (NumberFormatException e) {
/* 241 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 246 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 250 */   public static final TypeAdapterFactory INTEGER_FACTORY = newFactory(int.class, (Class)Integer.class, (TypeAdapter)INTEGER);
/*     */ 
/*     */   
/* 253 */   public static final TypeAdapter<Number> LONG = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 256 */         if (in.peek() == JsonToken.NULL) {
/* 257 */           in.nextNull();
/* 258 */           return null;
/*     */         } 
/*     */         try {
/* 261 */           return Long.valueOf(in.nextLong());
/* 262 */         } catch (NumberFormatException e) {
/* 263 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 268 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 272 */   public static final TypeAdapter<Number> FLOAT = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 275 */         if (in.peek() == JsonToken.NULL) {
/* 276 */           in.nextNull();
/* 277 */           return null;
/*     */         } 
/* 279 */         return Float.valueOf((float)in.nextDouble());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 283 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 287 */   public static final TypeAdapter<Number> DOUBLE = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 290 */         if (in.peek() == JsonToken.NULL) {
/* 291 */           in.nextNull();
/* 292 */           return null;
/*     */         } 
/* 294 */         return Double.valueOf(in.nextDouble());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 298 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 302 */   public static final TypeAdapter<Number> NUMBER = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 305 */         JsonToken jsonToken = in.peek();
/* 306 */         switch (jsonToken) {
/*     */           case NULL:
/* 308 */             in.nextNull();
/* 309 */             return null;
/*     */           case NUMBER:
/* 311 */             return (Number)new LazilyParsedNumber(in.nextString());
/*     */         } 
/* 313 */         throw new JsonSyntaxException("Expecting number, got: " + jsonToken);
/*     */       }
/*     */ 
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 318 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 322 */   public static final TypeAdapterFactory NUMBER_FACTORY = newFactory(Number.class, NUMBER);
/*     */   
/* 324 */   public static final TypeAdapter<Character> CHARACTER = new TypeAdapter<Character>()
/*     */     {
/*     */       public Character read(JsonReader in) throws IOException {
/* 327 */         if (in.peek() == JsonToken.NULL) {
/* 328 */           in.nextNull();
/* 329 */           return null;
/*     */         } 
/* 331 */         String str = in.nextString();
/* 332 */         if (str.length() != 1) {
/* 333 */           throw new JsonSyntaxException("Expecting character, got: " + str);
/*     */         }
/* 335 */         return Character.valueOf(str.charAt(0));
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Character value) throws IOException {
/* 339 */         out.value((value == null) ? null : String.valueOf(value));
/*     */       }
/*     */     };
/*     */   
/* 343 */   public static final TypeAdapterFactory CHARACTER_FACTORY = newFactory(char.class, (Class)Character.class, (TypeAdapter)CHARACTER);
/*     */ 
/*     */   
/* 346 */   public static final TypeAdapter<String> STRING = new TypeAdapter<String>()
/*     */     {
/*     */       public String read(JsonReader in) throws IOException {
/* 349 */         JsonToken peek = in.peek();
/* 350 */         if (peek == JsonToken.NULL) {
/* 351 */           in.nextNull();
/* 352 */           return null;
/*     */         } 
/*     */         
/* 355 */         if (peek == JsonToken.BOOLEAN) {
/* 356 */           return Boolean.toString(in.nextBoolean());
/*     */         }
/* 358 */         return in.nextString();
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, String value) throws IOException {
/* 362 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 366 */   public static final TypeAdapter<BigDecimal> BIG_DECIMAL = new TypeAdapter<BigDecimal>() {
/*     */       public BigDecimal read(JsonReader in) throws IOException {
/* 368 */         if (in.peek() == JsonToken.NULL) {
/* 369 */           in.nextNull();
/* 370 */           return null;
/*     */         } 
/*     */         try {
/* 373 */           return new BigDecimal(in.nextString());
/* 374 */         } catch (NumberFormatException e) {
/* 375 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, BigDecimal value) throws IOException {
/* 380 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 384 */   public static final TypeAdapter<BigInteger> BIG_INTEGER = new TypeAdapter<BigInteger>() {
/*     */       public BigInteger read(JsonReader in) throws IOException {
/* 386 */         if (in.peek() == JsonToken.NULL) {
/* 387 */           in.nextNull();
/* 388 */           return null;
/*     */         } 
/*     */         try {
/* 391 */           return new BigInteger(in.nextString());
/* 392 */         } catch (NumberFormatException e) {
/* 393 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, BigInteger value) throws IOException {
/* 398 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 402 */   public static final TypeAdapterFactory STRING_FACTORY = newFactory(String.class, STRING);
/*     */   
/* 404 */   public static final TypeAdapter<StringBuilder> STRING_BUILDER = new TypeAdapter<StringBuilder>()
/*     */     {
/*     */       public StringBuilder read(JsonReader in) throws IOException {
/* 407 */         if (in.peek() == JsonToken.NULL) {
/* 408 */           in.nextNull();
/* 409 */           return null;
/*     */         } 
/* 411 */         return new StringBuilder(in.nextString());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, StringBuilder value) throws IOException {
/* 415 */         out.value((value == null) ? null : value.toString());
/*     */       }
/*     */     };
/*     */   
/* 419 */   public static final TypeAdapterFactory STRING_BUILDER_FACTORY = newFactory(StringBuilder.class, STRING_BUILDER);
/*     */ 
/*     */   
/* 422 */   public static final TypeAdapter<StringBuffer> STRING_BUFFER = new TypeAdapter<StringBuffer>()
/*     */     {
/*     */       public StringBuffer read(JsonReader in) throws IOException {
/* 425 */         if (in.peek() == JsonToken.NULL) {
/* 426 */           in.nextNull();
/* 427 */           return null;
/*     */         } 
/* 429 */         return new StringBuffer(in.nextString());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, StringBuffer value) throws IOException {
/* 433 */         out.value((value == null) ? null : value.toString());
/*     */       }
/*     */     };
/*     */   
/* 437 */   public static final TypeAdapterFactory STRING_BUFFER_FACTORY = newFactory(StringBuffer.class, STRING_BUFFER);
/*     */ 
/*     */   
/* 440 */   public static final TypeAdapter<URL> URL = new TypeAdapter<URL>()
/*     */     {
/*     */       public URL read(JsonReader in) throws IOException {
/* 443 */         if (in.peek() == JsonToken.NULL) {
/* 444 */           in.nextNull();
/* 445 */           return null;
/*     */         } 
/* 447 */         String nextString = in.nextString();
/* 448 */         return "null".equals(nextString) ? null : new URL(nextString);
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, URL value) throws IOException {
/* 452 */         out.value((value == null) ? null : value.toExternalForm());
/*     */       }
/*     */     };
/*     */   
/* 456 */   public static final TypeAdapterFactory URL_FACTORY = newFactory(URL.class, URL);
/*     */   
/* 458 */   public static final TypeAdapter<URI> URI = new TypeAdapter<URI>()
/*     */     {
/*     */       public URI read(JsonReader in) throws IOException {
/* 461 */         if (in.peek() == JsonToken.NULL) {
/* 462 */           in.nextNull();
/* 463 */           return null;
/*     */         } 
/*     */         try {
/* 466 */           String nextString = in.nextString();
/* 467 */           return "null".equals(nextString) ? null : new URI(nextString);
/* 468 */         } catch (URISyntaxException e) {
/* 469 */           throw new JsonIOException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, URI value) throws IOException {
/* 474 */         out.value((value == null) ? null : value.toASCIIString());
/*     */       }
/*     */     };
/*     */   
/* 478 */   public static final TypeAdapterFactory URI_FACTORY = newFactory(URI.class, URI);
/*     */   
/* 480 */   public static final TypeAdapter<InetAddress> INET_ADDRESS = new TypeAdapter<InetAddress>()
/*     */     {
/*     */       public InetAddress read(JsonReader in) throws IOException {
/* 483 */         if (in.peek() == JsonToken.NULL) {
/* 484 */           in.nextNull();
/* 485 */           return null;
/*     */         } 
/*     */         
/* 488 */         return InetAddress.getByName(in.nextString());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, InetAddress value) throws IOException {
/* 492 */         out.value((value == null) ? null : value.getHostAddress());
/*     */       }
/*     */     };
/*     */   
/* 496 */   public static final TypeAdapterFactory INET_ADDRESS_FACTORY = newTypeHierarchyFactory(InetAddress.class, INET_ADDRESS);
/*     */ 
/*     */   
/* 499 */   public static final TypeAdapter<UUID> UUID = new TypeAdapter<UUID>()
/*     */     {
/*     */       public UUID read(JsonReader in) throws IOException {
/* 502 */         if (in.peek() == JsonToken.NULL) {
/* 503 */           in.nextNull();
/* 504 */           return null;
/*     */         } 
/* 506 */         return UUID.fromString(in.nextString());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, UUID value) throws IOException {
/* 510 */         out.value((value == null) ? null : value.toString());
/*     */       }
/*     */     };
/*     */   
/* 514 */   public static final TypeAdapterFactory UUID_FACTORY = newFactory(UUID.class, UUID);
/*     */   
/* 516 */   public static final TypeAdapterFactory TIMESTAMP_FACTORY = new TypeAdapterFactory()
/*     */     {
/*     */       public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 519 */         if (typeToken.getRawType() != Timestamp.class) {
/* 520 */           return null;
/*     */         }
/*     */         
/* 523 */         final TypeAdapter<Date> dateTypeAdapter = gson.getAdapter(Date.class);
/* 524 */         return (TypeAdapter)new TypeAdapter<Timestamp>() {
/*     */             public Timestamp read(JsonReader in) throws IOException {
/* 526 */               Date date = (Date)dateTypeAdapter.read(in);
/* 527 */               return (date != null) ? new Timestamp(date.getTime()) : null;
/*     */             }
/*     */             
/*     */             public void write(JsonWriter out, Timestamp value) throws IOException {
/* 531 */               dateTypeAdapter.write(out, value);
/*     */             }
/*     */           };
/*     */       }
/*     */     };
/*     */   
/* 537 */   public static final TypeAdapter<Calendar> CALENDAR = new TypeAdapter<Calendar>()
/*     */     {
/*     */       private static final String YEAR = "year";
/*     */       private static final String MONTH = "month";
/*     */       private static final String DAY_OF_MONTH = "dayOfMonth";
/*     */       private static final String HOUR_OF_DAY = "hourOfDay";
/*     */       private static final String MINUTE = "minute";
/*     */       private static final String SECOND = "second";
/*     */       
/*     */       public Calendar read(JsonReader in) throws IOException {
/* 547 */         if (in.peek() == JsonToken.NULL) {
/* 548 */           in.nextNull();
/* 549 */           return null;
/*     */         } 
/* 551 */         in.beginObject();
/* 552 */         int year = 0;
/* 553 */         int month = 0;
/* 554 */         int dayOfMonth = 0;
/* 555 */         int hourOfDay = 0;
/* 556 */         int minute = 0;
/* 557 */         int second = 0;
/* 558 */         while (in.peek() != JsonToken.END_OBJECT) {
/* 559 */           String name = in.nextName();
/* 560 */           int value = in.nextInt();
/* 561 */           if ("year".equals(name)) {
/* 562 */             year = value; continue;
/* 563 */           }  if ("month".equals(name)) {
/* 564 */             month = value; continue;
/* 565 */           }  if ("dayOfMonth".equals(name)) {
/* 566 */             dayOfMonth = value; continue;
/* 567 */           }  if ("hourOfDay".equals(name)) {
/* 568 */             hourOfDay = value; continue;
/* 569 */           }  if ("minute".equals(name)) {
/* 570 */             minute = value; continue;
/* 571 */           }  if ("second".equals(name)) {
/* 572 */             second = value;
/*     */           }
/*     */         } 
/* 575 */         in.endObject();
/* 576 */         return new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second);
/*     */       }
/*     */ 
/*     */       
/*     */       public void write(JsonWriter out, Calendar value) throws IOException {
/* 581 */         if (value == null) {
/* 582 */           out.nullValue();
/*     */           return;
/*     */         } 
/* 585 */         out.beginObject();
/* 586 */         out.name("year");
/* 587 */         out.value(value.get(1));
/* 588 */         out.name("month");
/* 589 */         out.value(value.get(2));
/* 590 */         out.name("dayOfMonth");
/* 591 */         out.value(value.get(5));
/* 592 */         out.name("hourOfDay");
/* 593 */         out.value(value.get(11));
/* 594 */         out.name("minute");
/* 595 */         out.value(value.get(12));
/* 596 */         out.name("second");
/* 597 */         out.value(value.get(13));
/* 598 */         out.endObject();
/*     */       }
/*     */     };
/*     */   
/* 602 */   public static final TypeAdapterFactory CALENDAR_FACTORY = newFactoryForMultipleTypes(Calendar.class, (Class)GregorianCalendar.class, CALENDAR);
/*     */ 
/*     */   
/* 605 */   public static final TypeAdapter<Locale> LOCALE = new TypeAdapter<Locale>()
/*     */     {
/*     */       public Locale read(JsonReader in) throws IOException {
/* 608 */         if (in.peek() == JsonToken.NULL) {
/* 609 */           in.nextNull();
/* 610 */           return null;
/*     */         } 
/* 612 */         String locale = in.nextString();
/* 613 */         StringTokenizer tokenizer = new StringTokenizer(locale, "_");
/* 614 */         String language = null;
/* 615 */         String country = null;
/* 616 */         String variant = null;
/* 617 */         if (tokenizer.hasMoreElements()) {
/* 618 */           language = tokenizer.nextToken();
/*     */         }
/* 620 */         if (tokenizer.hasMoreElements()) {
/* 621 */           country = tokenizer.nextToken();
/*     */         }
/* 623 */         if (tokenizer.hasMoreElements()) {
/* 624 */           variant = tokenizer.nextToken();
/*     */         }
/* 626 */         if (country == null && variant == null)
/* 627 */           return new Locale(language); 
/* 628 */         if (variant == null) {
/* 629 */           return new Locale(language, country);
/*     */         }
/* 631 */         return new Locale(language, country, variant);
/*     */       }
/*     */ 
/*     */       
/*     */       public void write(JsonWriter out, Locale value) throws IOException {
/* 636 */         out.value((value == null) ? null : value.toString());
/*     */       }
/*     */     };
/*     */   
/* 640 */   public static final TypeAdapterFactory LOCALE_FACTORY = newFactory(Locale.class, LOCALE);
/*     */   
/* 642 */   public static final TypeAdapter<JsonElement> JSON_ELEMENT = new TypeAdapter<JsonElement>() { public JsonElement read(JsonReader in) throws IOException { String number; JsonArray array;
/*     */         JsonObject object;
/* 644 */         switch (in.peek()) {
/*     */           case STRING:
/* 646 */             return (JsonElement)new JsonPrimitive(in.nextString());
/*     */           case NUMBER:
/* 648 */             number = in.nextString();
/* 649 */             return (JsonElement)new JsonPrimitive((Number)new LazilyParsedNumber(number));
/*     */           case BOOLEAN:
/* 651 */             return (JsonElement)new JsonPrimitive(Boolean.valueOf(in.nextBoolean()));
/*     */           case NULL:
/* 653 */             in.nextNull();
/* 654 */             return (JsonElement)JsonNull.INSTANCE;
/*     */           case BEGIN_ARRAY:
/* 656 */             array = new JsonArray();
/* 657 */             in.beginArray();
/* 658 */             while (in.hasNext()) {
/* 659 */               array.add(read(in));
/*     */             }
/* 661 */             in.endArray();
/* 662 */             return (JsonElement)array;
/*     */           case BEGIN_OBJECT:
/* 664 */             object = new JsonObject();
/* 665 */             in.beginObject();
/* 666 */             while (in.hasNext()) {
/* 667 */               object.add(in.nextName(), read(in));
/*     */             }
/* 669 */             in.endObject();
/* 670 */             return (JsonElement)object;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 676 */         throw new IllegalArgumentException(); }
/*     */ 
/*     */ 
/*     */       
/*     */       public void write(JsonWriter out, JsonElement value) throws IOException {
/* 681 */         if (value == null || value.isJsonNull()) {
/* 682 */           out.nullValue();
/* 683 */         } else if (value.isJsonPrimitive()) {
/* 684 */           JsonPrimitive primitive = value.getAsJsonPrimitive();
/* 685 */           if (primitive.isNumber()) {
/* 686 */             out.value(primitive.getAsNumber());
/* 687 */           } else if (primitive.isBoolean()) {
/* 688 */             out.value(primitive.getAsBoolean());
/*     */           } else {
/* 690 */             out.value(primitive.getAsString());
/*     */           }
/*     */         
/* 693 */         } else if (value.isJsonArray()) {
/* 694 */           out.beginArray();
/* 695 */           for (JsonElement e : value.getAsJsonArray()) {
/* 696 */             write(out, e);
/*     */           }
/* 698 */           out.endArray();
/*     */         }
/* 700 */         else if (value.isJsonObject()) {
/* 701 */           out.beginObject();
/* 702 */           for (Map.Entry<String, JsonElement> e : (Iterable<Map.Entry<String, JsonElement>>)value.getAsJsonObject().entrySet()) {
/* 703 */             out.name(e.getKey());
/* 704 */             write(out, e.getValue());
/*     */           } 
/* 706 */           out.endObject();
/*     */         } else {
/*     */           
/* 709 */           throw new IllegalArgumentException("Couldn't write " + value.getClass());
/*     */         } 
/*     */       } }
/*     */   ;
/*     */   
/* 714 */   public static final TypeAdapterFactory JSON_ELEMENT_FACTORY = newTypeHierarchyFactory(JsonElement.class, JSON_ELEMENT);
/*     */   
/*     */   private static final class EnumTypeAdapter<T extends Enum<T>>
/*     */     extends TypeAdapter<T> {
/* 718 */     private final Map<String, T> nameToConstant = new HashMap<String, T>();
/* 719 */     private final Map<T, String> constantToName = new HashMap<T, String>();
/*     */     
/*     */     public EnumTypeAdapter(Class<T> classOfT) {
/*     */       try {
/* 723 */         for (Enum enum_ : (Enum[])classOfT.getEnumConstants()) {
/* 724 */           String name = enum_.name();
/* 725 */           SerializedName annotation = classOfT.getField(name).<SerializedName>getAnnotation(SerializedName.class);
/* 726 */           if (annotation != null) {
/* 727 */             name = annotation.value();
/*     */           }
/* 729 */           this.nameToConstant.put(name, (T)enum_);
/* 730 */           this.constantToName.put((T)enum_, name);
/*     */         } 
/* 732 */       } catch (NoSuchFieldException e) {
/* 733 */         throw new AssertionError();
/*     */       } 
/*     */     }
/*     */     public T read(JsonReader in) throws IOException {
/* 737 */       if (in.peek() == JsonToken.NULL) {
/* 738 */         in.nextNull();
/* 739 */         return null;
/*     */       } 
/* 741 */       return this.nameToConstant.get(in.nextString());
/*     */     }
/*     */     
/*     */     public void write(JsonWriter out, T value) throws IOException {
/* 745 */       out.value((value == null) ? null : this.constantToName.get(value));
/*     */     }
/*     */   }
/*     */   
/* 749 */   public static final TypeAdapterFactory ENUM_FACTORY = newEnumTypeHierarchyFactory();
/*     */   
/*     */   public static TypeAdapterFactory newEnumTypeHierarchyFactory() {
/* 752 */     return new TypeAdapterFactory()
/*     */       {
/*     */         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 755 */           Class<? super T> rawType = typeToken.getRawType();
/* 756 */           if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
/* 757 */             return null;
/*     */           }
/* 759 */           if (!rawType.isEnum()) {
/* 760 */             rawType = rawType.getSuperclass();
/*     */           }
/* 762 */           return new TypeAdapters.EnumTypeAdapter<T>(rawType);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <TT> TypeAdapterFactory newFactory(final TypeToken<TT> type, final TypeAdapter<TT> typeAdapter) {
/* 769 */     return new TypeAdapterFactory()
/*     */       {
/*     */         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 772 */           return typeToken.equals(type) ? typeAdapter : null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <TT> TypeAdapterFactory newFactory(final Class<TT> type, final TypeAdapter<TT> typeAdapter) {
/* 779 */     return new TypeAdapterFactory()
/*     */       {
/*     */         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 782 */           return (typeToken.getRawType() == type) ? typeAdapter : null;
/*     */         }
/*     */         public String toString() {
/* 785 */           return "Factory[type=" + type.getName() + ",adapter=" + typeAdapter + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <TT> TypeAdapterFactory newFactory(final Class<TT> unboxed, final Class<TT> boxed, final TypeAdapter<? super TT> typeAdapter) {
/* 792 */     return new TypeAdapterFactory()
/*     */       {
/*     */         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 795 */           Class<? super T> rawType = typeToken.getRawType();
/* 796 */           return (rawType == unboxed || rawType == boxed) ? typeAdapter : null;
/*     */         }
/*     */         public String toString() {
/* 799 */           return "Factory[type=" + boxed.getName() + "+" + unboxed.getName() + ",adapter=" + typeAdapter + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <TT> TypeAdapterFactory newFactoryForMultipleTypes(final Class<TT> base, final Class<? extends TT> sub, final TypeAdapter<? super TT> typeAdapter) {
/* 807 */     return new TypeAdapterFactory()
/*     */       {
/*     */         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 810 */           Class<? super T> rawType = typeToken.getRawType();
/* 811 */           return (rawType == base || rawType == sub) ? typeAdapter : null;
/*     */         }
/*     */         public String toString() {
/* 814 */           return "Factory[type=" + base.getName() + "+" + sub.getName() + ",adapter=" + typeAdapter + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <TT> TypeAdapterFactory newTypeHierarchyFactory(final Class<TT> clazz, final TypeAdapter<TT> typeAdapter) {
/* 822 */     return new TypeAdapterFactory()
/*     */       {
/*     */         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 825 */           return clazz.isAssignableFrom(typeToken.getRawType()) ? typeAdapter : null;
/*     */         }
/*     */         public String toString() {
/* 828 */           return "Factory[typeHierarchy=" + clazz.getName() + ",adapter=" + typeAdapter + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\gson\internal\bind\TypeAdapters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */