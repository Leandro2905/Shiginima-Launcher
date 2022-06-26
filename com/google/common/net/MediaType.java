/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableListMultimap;
/*     */ import com.google.common.collect.ImmutableMultiset;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.ListMultimap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.collect.Multimaps;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.concurrent.Immutable;
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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ @Immutable
/*     */ public final class MediaType
/*     */ {
/*     */   private static final String CHARSET_ATTRIBUTE = "charset";
/*  85 */   private static final ImmutableListMultimap<String, String> UTF_8_CONSTANT_PARAMETERS = ImmutableListMultimap.of("charset", Ascii.toLowerCase(Charsets.UTF_8.name()));
/*     */ 
/*     */ 
/*     */   
/*  89 */   private static final CharMatcher TOKEN_MATCHER = CharMatcher.ASCII.and(CharMatcher.JAVA_ISO_CONTROL.negate()).and(CharMatcher.isNot(' ')).and(CharMatcher.noneOf("()<>@,;:\\\"/[]?="));
/*     */ 
/*     */   
/*  92 */   private static final CharMatcher QUOTED_TEXT_MATCHER = CharMatcher.ASCII.and(CharMatcher.noneOf("\"\\\r"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   private static final CharMatcher LINEAR_WHITE_SPACE = CharMatcher.anyOf(" \t\r\n");
/*     */   
/*     */   private static final String APPLICATION_TYPE = "application";
/*     */   
/*     */   private static final String AUDIO_TYPE = "audio";
/*     */   
/*     */   private static final String IMAGE_TYPE = "image";
/*     */   
/*     */   private static final String TEXT_TYPE = "text";
/*     */   private static final String VIDEO_TYPE = "video";
/*     */   private static final String WILDCARD = "*";
/* 109 */   private static final Map<MediaType, MediaType> KNOWN_TYPES = Maps.newHashMap();
/*     */   
/*     */   private static MediaType createConstant(String type, String subtype) {
/* 112 */     return addKnownType(new MediaType(type, subtype, ImmutableListMultimap.of()));
/*     */   }
/*     */   
/*     */   private static MediaType createConstantUtf8(String type, String subtype) {
/* 116 */     return addKnownType(new MediaType(type, subtype, UTF_8_CONSTANT_PARAMETERS));
/*     */   }
/*     */   
/*     */   private static MediaType addKnownType(MediaType mediaType) {
/* 120 */     KNOWN_TYPES.put(mediaType, mediaType);
/* 121 */     return mediaType;
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
/* 134 */   public static final MediaType ANY_TYPE = createConstant("*", "*");
/* 135 */   public static final MediaType ANY_TEXT_TYPE = createConstant("text", "*");
/* 136 */   public static final MediaType ANY_IMAGE_TYPE = createConstant("image", "*");
/* 137 */   public static final MediaType ANY_AUDIO_TYPE = createConstant("audio", "*");
/* 138 */   public static final MediaType ANY_VIDEO_TYPE = createConstant("video", "*");
/* 139 */   public static final MediaType ANY_APPLICATION_TYPE = createConstant("application", "*");
/*     */ 
/*     */   
/* 142 */   public static final MediaType CACHE_MANIFEST_UTF_8 = createConstantUtf8("text", "cache-manifest");
/*     */   
/* 144 */   public static final MediaType CSS_UTF_8 = createConstantUtf8("text", "css");
/* 145 */   public static final MediaType CSV_UTF_8 = createConstantUtf8("text", "csv");
/* 146 */   public static final MediaType HTML_UTF_8 = createConstantUtf8("text", "html");
/* 147 */   public static final MediaType I_CALENDAR_UTF_8 = createConstantUtf8("text", "calendar");
/* 148 */   public static final MediaType PLAIN_TEXT_UTF_8 = createConstantUtf8("text", "plain");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   public static final MediaType TEXT_JAVASCRIPT_UTF_8 = createConstantUtf8("text", "javascript");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   public static final MediaType TSV_UTF_8 = createConstantUtf8("text", "tab-separated-values");
/* 162 */   public static final MediaType VCARD_UTF_8 = createConstantUtf8("text", "vcard");
/* 163 */   public static final MediaType WML_UTF_8 = createConstantUtf8("text", "vnd.wap.wml");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   public static final MediaType XML_UTF_8 = createConstantUtf8("text", "xml");
/*     */ 
/*     */   
/* 172 */   public static final MediaType BMP = createConstant("image", "bmp");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   public static final MediaType CRW = createConstant("image", "x-canon-crw");
/* 183 */   public static final MediaType GIF = createConstant("image", "gif");
/* 184 */   public static final MediaType ICO = createConstant("image", "vnd.microsoft.icon");
/* 185 */   public static final MediaType JPEG = createConstant("image", "jpeg");
/* 186 */   public static final MediaType PNG = createConstant("image", "png");
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
/* 203 */   public static final MediaType PSD = createConstant("image", "vnd.adobe.photoshop");
/* 204 */   public static final MediaType SVG_UTF_8 = createConstantUtf8("image", "svg+xml");
/* 205 */   public static final MediaType TIFF = createConstant("image", "tiff");
/* 206 */   public static final MediaType WEBP = createConstant("image", "webp");
/*     */ 
/*     */   
/* 209 */   public static final MediaType MP4_AUDIO = createConstant("audio", "mp4");
/* 210 */   public static final MediaType MPEG_AUDIO = createConstant("audio", "mpeg");
/* 211 */   public static final MediaType OGG_AUDIO = createConstant("audio", "ogg");
/* 212 */   public static final MediaType WEBM_AUDIO = createConstant("audio", "webm");
/*     */ 
/*     */   
/* 215 */   public static final MediaType MP4_VIDEO = createConstant("video", "mp4");
/* 216 */   public static final MediaType MPEG_VIDEO = createConstant("video", "mpeg");
/* 217 */   public static final MediaType OGG_VIDEO = createConstant("video", "ogg");
/* 218 */   public static final MediaType QUICKTIME = createConstant("video", "quicktime");
/* 219 */   public static final MediaType WEBM_VIDEO = createConstant("video", "webm");
/* 220 */   public static final MediaType WMV = createConstant("video", "x-ms-wmv");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 228 */   public static final MediaType APPLICATION_XML_UTF_8 = createConstantUtf8("application", "xml");
/* 229 */   public static final MediaType ATOM_UTF_8 = createConstantUtf8("application", "atom+xml");
/* 230 */   public static final MediaType BZIP2 = createConstant("application", "x-bzip2");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 240 */   public static final MediaType EOT = createConstant("application", "vnd.ms-fontobject");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 250 */   public static final MediaType EPUB = createConstant("application", "epub+zip");
/* 251 */   public static final MediaType FORM_DATA = createConstant("application", "x-www-form-urlencoded");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 260 */   public static final MediaType KEY_ARCHIVE = createConstant("application", "pkcs12");
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
/* 272 */   public static final MediaType APPLICATION_BINARY = createConstant("application", "binary");
/* 273 */   public static final MediaType GZIP = createConstant("application", "x-gzip");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 279 */   public static final MediaType JAVASCRIPT_UTF_8 = createConstantUtf8("application", "javascript");
/*     */   
/* 281 */   public static final MediaType JSON_UTF_8 = createConstantUtf8("application", "json");
/* 282 */   public static final MediaType KML = createConstant("application", "vnd.google-earth.kml+xml");
/* 283 */   public static final MediaType KMZ = createConstant("application", "vnd.google-earth.kmz");
/* 284 */   public static final MediaType MBOX = createConstant("application", "mbox");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 292 */   public static final MediaType APPLE_MOBILE_CONFIG = createConstant("application", "x-apple-aspen-config");
/*     */   
/* 294 */   public static final MediaType MICROSOFT_EXCEL = createConstant("application", "vnd.ms-excel");
/* 295 */   public static final MediaType MICROSOFT_POWERPOINT = createConstant("application", "vnd.ms-powerpoint");
/*     */   
/* 297 */   public static final MediaType MICROSOFT_WORD = createConstant("application", "msword");
/* 298 */   public static final MediaType OCTET_STREAM = createConstant("application", "octet-stream");
/* 299 */   public static final MediaType OGG_CONTAINER = createConstant("application", "ogg");
/* 300 */   public static final MediaType OOXML_DOCUMENT = createConstant("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
/*     */   
/* 302 */   public static final MediaType OOXML_PRESENTATION = createConstant("application", "vnd.openxmlformats-officedocument.presentationml.presentation");
/*     */   
/* 304 */   public static final MediaType OOXML_SHEET = createConstant("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
/*     */   
/* 306 */   public static final MediaType OPENDOCUMENT_GRAPHICS = createConstant("application", "vnd.oasis.opendocument.graphics");
/*     */   
/* 308 */   public static final MediaType OPENDOCUMENT_PRESENTATION = createConstant("application", "vnd.oasis.opendocument.presentation");
/*     */   
/* 310 */   public static final MediaType OPENDOCUMENT_SPREADSHEET = createConstant("application", "vnd.oasis.opendocument.spreadsheet");
/*     */   
/* 312 */   public static final MediaType OPENDOCUMENT_TEXT = createConstant("application", "vnd.oasis.opendocument.text");
/*     */   
/* 314 */   public static final MediaType PDF = createConstant("application", "pdf");
/* 315 */   public static final MediaType POSTSCRIPT = createConstant("application", "postscript");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 321 */   public static final MediaType PROTOBUF = createConstant("application", "protobuf");
/* 322 */   public static final MediaType RDF_XML_UTF_8 = createConstantUtf8("application", "rdf+xml");
/* 323 */   public static final MediaType RTF_UTF_8 = createConstantUtf8("application", "rtf");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 333 */   public static final MediaType SFNT = createConstant("application", "font-sfnt");
/* 334 */   public static final MediaType SHOCKWAVE_FLASH = createConstant("application", "x-shockwave-flash");
/*     */   
/* 336 */   public static final MediaType SKETCHUP = createConstant("application", "vnd.sketchup.skp");
/* 337 */   public static final MediaType TAR = createConstant("application", "x-tar");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 347 */   public static final MediaType WOFF = createConstant("application", "font-woff");
/* 348 */   public static final MediaType XHTML_UTF_8 = createConstantUtf8("application", "xhtml+xml");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 356 */   public static final MediaType XRD_UTF_8 = createConstantUtf8("application", "xrd+xml");
/* 357 */   public static final MediaType ZIP = createConstant("application", "zip");
/*     */   
/*     */   private final String type;
/*     */   
/*     */   private final String subtype;
/*     */   private final ImmutableListMultimap<String, String> parameters;
/*     */   
/*     */   private MediaType(String type, String subtype, ImmutableListMultimap<String, String> parameters) {
/* 365 */     this.type = type;
/* 366 */     this.subtype = subtype;
/* 367 */     this.parameters = parameters;
/*     */   }
/*     */ 
/*     */   
/*     */   public String type() {
/* 372 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String subtype() {
/* 377 */     return this.subtype;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableListMultimap<String, String> parameters() {
/* 382 */     return this.parameters;
/*     */   }
/*     */   
/*     */   private Map<String, ImmutableMultiset<String>> parametersAsMap() {
/* 386 */     return Maps.transformValues((Map)this.parameters.asMap(), new Function<Collection<String>, ImmutableMultiset<String>>()
/*     */         {
/*     */           public ImmutableMultiset<String> apply(Collection<String> input) {
/* 389 */             return ImmutableMultiset.copyOf(input);
/*     */           }
/*     */         });
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
/*     */   public Optional<Charset> charset() {
/* 403 */     ImmutableSet<String> charsetValues = ImmutableSet.copyOf((Collection)this.parameters.get("charset"));
/* 404 */     switch (charsetValues.size()) {
/*     */       case 0:
/* 406 */         return Optional.absent();
/*     */       case 1:
/* 408 */         return Optional.of(Charset.forName((String)Iterables.getOnlyElement((Iterable)charsetValues)));
/*     */     } 
/* 410 */     String str = String.valueOf(String.valueOf(charsetValues)); throw new IllegalStateException((new StringBuilder(33 + str.length())).append("Multiple charset values defined: ").append(str).toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType withoutParameters() {
/* 419 */     return this.parameters.isEmpty() ? this : create(this.type, this.subtype);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType withParameters(Multimap<String, String> parameters) {
/* 428 */     return create(this.type, this.subtype, parameters);
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
/*     */   public MediaType withParameter(String attribute, String value) {
/* 440 */     Preconditions.checkNotNull(attribute);
/* 441 */     Preconditions.checkNotNull(value);
/* 442 */     String normalizedAttribute = normalizeToken(attribute);
/* 443 */     ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
/* 444 */     for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)this.parameters.entries()) {
/* 445 */       String key = entry.getKey();
/* 446 */       if (!normalizedAttribute.equals(key)) {
/* 447 */         builder.put(key, entry.getValue());
/*     */       }
/*     */     } 
/* 450 */     builder.put(normalizedAttribute, normalizeParameterValue(normalizedAttribute, value));
/* 451 */     MediaType mediaType = new MediaType(this.type, this.subtype, builder.build());
/*     */     
/* 453 */     return (MediaType)MoreObjects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
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
/*     */   public MediaType withCharset(Charset charset) {
/* 466 */     Preconditions.checkNotNull(charset);
/* 467 */     return withParameter("charset", charset.name());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasWildcard() {
/* 472 */     return ("*".equals(this.type) || "*".equals(this.subtype));
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
/*     */   public boolean is(MediaType mediaTypeRange) {
/* 502 */     return ((mediaTypeRange.type.equals("*") || mediaTypeRange.type.equals(this.type)) && (mediaTypeRange.subtype.equals("*") || mediaTypeRange.subtype.equals(this.subtype)) && this.parameters.entries().containsAll((Collection)mediaTypeRange.parameters.entries()));
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
/*     */   public static MediaType create(String type, String subtype) {
/* 514 */     return create(type, subtype, (Multimap<String, String>)ImmutableListMultimap.of());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MediaType createApplicationType(String subtype) {
/* 523 */     return create("application", subtype);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MediaType createAudioType(String subtype) {
/* 532 */     return create("audio", subtype);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MediaType createImageType(String subtype) {
/* 541 */     return create("image", subtype);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MediaType createTextType(String subtype) {
/* 550 */     return create("text", subtype);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MediaType createVideoType(String subtype) {
/* 559 */     return create("video", subtype);
/*     */   }
/*     */ 
/*     */   
/*     */   private static MediaType create(String type, String subtype, Multimap<String, String> parameters) {
/* 564 */     Preconditions.checkNotNull(type);
/* 565 */     Preconditions.checkNotNull(subtype);
/* 566 */     Preconditions.checkNotNull(parameters);
/* 567 */     String normalizedType = normalizeToken(type);
/* 568 */     String normalizedSubtype = normalizeToken(subtype);
/* 569 */     Preconditions.checkArgument((!"*".equals(normalizedType) || "*".equals(normalizedSubtype)), "A wildcard type cannot be used with a non-wildcard subtype");
/*     */     
/* 571 */     ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
/* 572 */     for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)parameters.entries()) {
/* 573 */       String attribute = normalizeToken(entry.getKey());
/* 574 */       builder.put(attribute, normalizeParameterValue(attribute, entry.getValue()));
/*     */     } 
/* 576 */     MediaType mediaType = new MediaType(normalizedType, normalizedSubtype, builder.build());
/*     */     
/* 578 */     return (MediaType)MoreObjects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
/*     */   }
/*     */   
/*     */   private static String normalizeToken(String token) {
/* 582 */     Preconditions.checkArgument(TOKEN_MATCHER.matchesAllOf(token));
/* 583 */     return Ascii.toLowerCase(token);
/*     */   }
/*     */   
/*     */   private static String normalizeParameterValue(String attribute, String value) {
/* 587 */     return "charset".equals(attribute) ? Ascii.toLowerCase(value) : value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MediaType parse(String input) {
/* 596 */     Preconditions.checkNotNull(input);
/* 597 */     Tokenizer tokenizer = new Tokenizer(input);
/*     */     try {
/* 599 */       String type = tokenizer.consumeToken(TOKEN_MATCHER);
/* 600 */       tokenizer.consumeCharacter('/');
/* 601 */       String subtype = tokenizer.consumeToken(TOKEN_MATCHER);
/* 602 */       ImmutableListMultimap.Builder<String, String> parameters = ImmutableListMultimap.builder();
/* 603 */       while (tokenizer.hasMore()) {
/* 604 */         String value; tokenizer.consumeCharacter(';');
/* 605 */         tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
/* 606 */         String attribute = tokenizer.consumeToken(TOKEN_MATCHER);
/* 607 */         tokenizer.consumeCharacter('=');
/*     */         
/* 609 */         if ('"' == tokenizer.previewChar()) {
/* 610 */           tokenizer.consumeCharacter('"');
/* 611 */           StringBuilder valueBuilder = new StringBuilder();
/* 612 */           while ('"' != tokenizer.previewChar()) {
/* 613 */             if ('\\' == tokenizer.previewChar()) {
/* 614 */               tokenizer.consumeCharacter('\\');
/* 615 */               valueBuilder.append(tokenizer.consumeCharacter(CharMatcher.ASCII)); continue;
/*     */             } 
/* 617 */             valueBuilder.append(tokenizer.consumeToken(QUOTED_TEXT_MATCHER));
/*     */           } 
/*     */           
/* 620 */           value = valueBuilder.toString();
/* 621 */           tokenizer.consumeCharacter('"');
/*     */         } else {
/* 623 */           value = tokenizer.consumeToken(TOKEN_MATCHER);
/*     */         } 
/* 625 */         parameters.put(attribute, value);
/*     */       } 
/* 627 */       return create(type, subtype, (Multimap<String, String>)parameters.build());
/* 628 */     } catch (IllegalStateException e) {
/* 629 */       String str = String.valueOf(String.valueOf(input)); throw new IllegalArgumentException((new StringBuilder(18 + str.length())).append("Could not parse '").append(str).append("'").toString(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class Tokenizer {
/*     */     final String input;
/* 635 */     int position = 0;
/*     */     
/*     */     Tokenizer(String input) {
/* 638 */       this.input = input;
/*     */     }
/*     */     
/*     */     String consumeTokenIfPresent(CharMatcher matcher) {
/* 642 */       Preconditions.checkState(hasMore());
/* 643 */       int startPosition = this.position;
/* 644 */       this.position = matcher.negate().indexIn(this.input, startPosition);
/* 645 */       return hasMore() ? this.input.substring(startPosition, this.position) : this.input.substring(startPosition);
/*     */     }
/*     */     
/*     */     String consumeToken(CharMatcher matcher) {
/* 649 */       int startPosition = this.position;
/* 650 */       String token = consumeTokenIfPresent(matcher);
/* 651 */       Preconditions.checkState((this.position != startPosition));
/* 652 */       return token;
/*     */     }
/*     */     
/*     */     char consumeCharacter(CharMatcher matcher) {
/* 656 */       Preconditions.checkState(hasMore());
/* 657 */       char c = previewChar();
/* 658 */       Preconditions.checkState(matcher.matches(c));
/* 659 */       this.position++;
/* 660 */       return c;
/*     */     }
/*     */     
/*     */     char consumeCharacter(char c) {
/* 664 */       Preconditions.checkState(hasMore());
/* 665 */       Preconditions.checkState((previewChar() == c));
/* 666 */       this.position++;
/* 667 */       return c;
/*     */     }
/*     */     
/*     */     char previewChar() {
/* 671 */       Preconditions.checkState(hasMore());
/* 672 */       return this.input.charAt(this.position);
/*     */     }
/*     */     
/*     */     boolean hasMore() {
/* 676 */       return (this.position >= 0 && this.position < this.input.length());
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 681 */     if (obj == this)
/* 682 */       return true; 
/* 683 */     if (obj instanceof MediaType) {
/* 684 */       MediaType that = (MediaType)obj;
/* 685 */       return (this.type.equals(that.type) && this.subtype.equals(that.subtype) && parametersAsMap().equals(that.parametersAsMap()));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 690 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 695 */     return Objects.hashCode(new Object[] { this.type, this.subtype, parametersAsMap() });
/*     */   }
/*     */   
/* 698 */   private static final Joiner.MapJoiner PARAMETER_JOINER = Joiner.on("; ").withKeyValueSeparator("=");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 705 */     StringBuilder builder = (new StringBuilder()).append(this.type).append('/').append(this.subtype);
/* 706 */     if (!this.parameters.isEmpty()) {
/* 707 */       builder.append("; ");
/* 708 */       ListMultimap listMultimap = Multimaps.transformValues((ListMultimap)this.parameters, new Function<String, String>()
/*     */           {
/*     */             public String apply(String value) {
/* 711 */               return MediaType.TOKEN_MATCHER.matchesAllOf(value) ? value : MediaType.escapeAndQuote(value);
/*     */             }
/*     */           });
/* 714 */       PARAMETER_JOINER.appendTo(builder, listMultimap.entries());
/*     */     } 
/* 716 */     return builder.toString();
/*     */   }
/*     */   
/*     */   private static String escapeAndQuote(String value) {
/* 720 */     StringBuilder escaped = (new StringBuilder(value.length() + 16)).append('"');
/* 721 */     for (char ch : value.toCharArray()) {
/* 722 */       if (ch == '\r' || ch == '\\' || ch == '"') {
/* 723 */         escaped.append('\\');
/*     */       }
/* 725 */       escaped.append(ch);
/*     */     } 
/* 727 */     return escaped.append('"').toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\net\MediaType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */