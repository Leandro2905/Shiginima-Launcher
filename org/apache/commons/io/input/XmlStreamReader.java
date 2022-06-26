/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.io.ByteOrderMark;
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
/*     */ public class XmlStreamReader
/*     */   extends Reader
/*     */ {
/*     */   private static final int BUFFER_SIZE = 4096;
/*     */   private static final String UTF_8 = "UTF-8";
/*     */   private static final String US_ASCII = "US-ASCII";
/*     */   private static final String UTF_16BE = "UTF-16BE";
/*     */   private static final String UTF_16LE = "UTF-16LE";
/*     */   private static final String UTF_32BE = "UTF-32BE";
/*     */   private static final String UTF_32LE = "UTF-32LE";
/*     */   private static final String UTF_16 = "UTF-16";
/*     */   private static final String UTF_32 = "UTF-32";
/*     */   private static final String EBCDIC = "CP1047";
/*  87 */   private static final ByteOrderMark[] BOMS = new ByteOrderMark[] { ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   private static final ByteOrderMark[] XML_GUESS_BYTES = new ByteOrderMark[] { new ByteOrderMark("UTF-8", new int[] { 60, 63, 120, 109 }), new ByteOrderMark("UTF-16BE", new int[] { 0, 60, 0, 63 }), new ByteOrderMark("UTF-16LE", new int[] { 60, 0, 63, 0 }), new ByteOrderMark("UTF-32BE", new int[] { 0, 0, 0, 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109 }), new ByteOrderMark("UTF-32LE", new int[] { 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109, 0, 0, 0 }), new ByteOrderMark("CP1047", new int[] { 76, 111, 167, 148 }) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Reader reader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String encoding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String defaultEncoding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultEncoding() {
/* 122 */     return this.defaultEncoding;
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
/*     */   public XmlStreamReader(File file) throws IOException {
/* 138 */     this(new FileInputStream(file));
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
/*     */   public XmlStreamReader(InputStream is) throws IOException {
/* 153 */     this(is, true);
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
/*     */   
/*     */   public XmlStreamReader(InputStream is, boolean lenient) throws IOException {
/* 184 */     this(is, lenient, (String)null);
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
/*     */ 
/*     */   
/*     */   public XmlStreamReader(InputStream is, boolean lenient, String defaultEncoding) throws IOException {
/* 216 */     this.defaultEncoding = defaultEncoding;
/* 217 */     BOMInputStream bom = new BOMInputStream(new BufferedInputStream(is, 4096), false, BOMS);
/* 218 */     BOMInputStream pis = new BOMInputStream(bom, true, XML_GUESS_BYTES);
/* 219 */     this.encoding = doRawStream(bom, pis, lenient);
/* 220 */     this.reader = new InputStreamReader(pis, this.encoding);
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
/*     */   public XmlStreamReader(URL url) throws IOException {
/* 241 */     this(url.openConnection(), (String)null);
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
/*     */   public XmlStreamReader(URLConnection conn, String defaultEncoding) throws IOException {
/* 264 */     this.defaultEncoding = defaultEncoding;
/* 265 */     boolean lenient = true;
/* 266 */     String contentType = conn.getContentType();
/* 267 */     InputStream is = conn.getInputStream();
/* 268 */     BOMInputStream bom = new BOMInputStream(new BufferedInputStream(is, 4096), false, BOMS);
/* 269 */     BOMInputStream pis = new BOMInputStream(bom, true, XML_GUESS_BYTES);
/* 270 */     if (conn instanceof java.net.HttpURLConnection || contentType != null) {
/* 271 */       this.encoding = doHttpStream(bom, pis, contentType, lenient);
/*     */     } else {
/* 273 */       this.encoding = doRawStream(bom, pis, lenient);
/*     */     } 
/* 275 */     this.reader = new InputStreamReader(pis, this.encoding);
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
/*     */   public XmlStreamReader(InputStream is, String httpContentType) throws IOException {
/* 297 */     this(is, httpContentType, true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStreamReader(InputStream is, String httpContentType, boolean lenient, String defaultEncoding) throws IOException {
/* 336 */     this.defaultEncoding = defaultEncoding;
/* 337 */     BOMInputStream bom = new BOMInputStream(new BufferedInputStream(is, 4096), false, BOMS);
/* 338 */     BOMInputStream pis = new BOMInputStream(bom, true, XML_GUESS_BYTES);
/* 339 */     this.encoding = doHttpStream(bom, pis, httpContentType, lenient);
/* 340 */     this.reader = new InputStreamReader(pis, this.encoding);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStreamReader(InputStream is, String httpContentType, boolean lenient) throws IOException {
/* 378 */     this(is, httpContentType, lenient, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 387 */     return this.encoding;
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
/*     */   public int read(char[] buf, int offset, int len) throws IOException {
/* 400 */     return this.reader.read(buf, offset, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 410 */     this.reader.close();
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
/*     */   private String doRawStream(BOMInputStream bom, BOMInputStream pis, boolean lenient) throws IOException {
/* 425 */     String bomEnc = bom.getBOMCharsetName();
/* 426 */     String xmlGuessEnc = pis.getBOMCharsetName();
/* 427 */     String xmlEnc = getXmlProlog(pis, xmlGuessEnc);
/*     */     try {
/* 429 */       return calculateRawEncoding(bomEnc, xmlGuessEnc, xmlEnc);
/* 430 */     } catch (XmlStreamReaderException ex) {
/* 431 */       if (lenient) {
/* 432 */         return doLenientDetection(null, ex);
/*     */       }
/* 434 */       throw ex;
/*     */     } 
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
/*     */   private String doHttpStream(BOMInputStream bom, BOMInputStream pis, String httpContentType, boolean lenient) throws IOException {
/* 452 */     String bomEnc = bom.getBOMCharsetName();
/* 453 */     String xmlGuessEnc = pis.getBOMCharsetName();
/* 454 */     String xmlEnc = getXmlProlog(pis, xmlGuessEnc);
/*     */     try {
/* 456 */       return calculateHttpEncoding(httpContentType, bomEnc, xmlGuessEnc, xmlEnc, lenient);
/*     */     }
/* 458 */     catch (XmlStreamReaderException ex) {
/* 459 */       if (lenient) {
/* 460 */         return doLenientDetection(httpContentType, ex);
/*     */       }
/* 462 */       throw ex;
/*     */     } 
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
/*     */   private String doLenientDetection(String httpContentType, XmlStreamReaderException ex) throws IOException {
/* 478 */     if (httpContentType != null && httpContentType.startsWith("text/html")) {
/* 479 */       httpContentType = httpContentType.substring("text/html".length());
/* 480 */       httpContentType = "text/xml" + httpContentType;
/*     */       try {
/* 482 */         return calculateHttpEncoding(httpContentType, ex.getBomEncoding(), ex.getXmlGuessEncoding(), ex.getXmlEncoding(), true);
/*     */       }
/* 484 */       catch (XmlStreamReaderException ex2) {
/* 485 */         ex = ex2;
/*     */       } 
/*     */     } 
/* 488 */     String encoding = ex.getXmlEncoding();
/* 489 */     if (encoding == null) {
/* 490 */       encoding = ex.getContentTypeEncoding();
/*     */     }
/* 492 */     if (encoding == null) {
/* 493 */       encoding = (this.defaultEncoding == null) ? "UTF-8" : this.defaultEncoding;
/*     */     }
/* 495 */     return encoding;
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
/*     */   String calculateRawEncoding(String bomEnc, String xmlGuessEnc, String xmlEnc) throws IOException {
/* 511 */     if (bomEnc == null) {
/* 512 */       if (xmlGuessEnc == null || xmlEnc == null) {
/* 513 */         return (this.defaultEncoding == null) ? "UTF-8" : this.defaultEncoding;
/*     */       }
/* 515 */       if (xmlEnc.equals("UTF-16") && (xmlGuessEnc.equals("UTF-16BE") || xmlGuessEnc.equals("UTF-16LE")))
/*     */       {
/* 517 */         return xmlGuessEnc;
/*     */       }
/* 519 */       return xmlEnc;
/*     */     } 
/*     */ 
/*     */     
/* 523 */     if (bomEnc.equals("UTF-8")) {
/* 524 */       if (xmlGuessEnc != null && !xmlGuessEnc.equals("UTF-8")) {
/* 525 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 526 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 528 */       if (xmlEnc != null && !xmlEnc.equals("UTF-8")) {
/* 529 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 530 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 532 */       return bomEnc;
/*     */     } 
/*     */ 
/*     */     
/* 536 */     if (bomEnc.equals("UTF-16BE") || bomEnc.equals("UTF-16LE")) {
/* 537 */       if (xmlGuessEnc != null && !xmlGuessEnc.equals(bomEnc)) {
/* 538 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 539 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 541 */       if (xmlEnc != null && !xmlEnc.equals("UTF-16") && !xmlEnc.equals(bomEnc)) {
/* 542 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 543 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 545 */       return bomEnc;
/*     */     } 
/*     */ 
/*     */     
/* 549 */     if (bomEnc.equals("UTF-32BE") || bomEnc.equals("UTF-32LE")) {
/* 550 */       if (xmlGuessEnc != null && !xmlGuessEnc.equals(bomEnc)) {
/* 551 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 552 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 554 */       if (xmlEnc != null && !xmlEnc.equals("UTF-32") && !xmlEnc.equals(bomEnc)) {
/* 555 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 556 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 558 */       return bomEnc;
/*     */     } 
/*     */ 
/*     */     
/* 562 */     String msg = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 563 */     throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
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
/*     */   String calculateHttpEncoding(String httpContentType, String bomEnc, String xmlGuessEnc, String xmlEnc, boolean lenient) throws IOException {
/* 584 */     if (lenient && xmlEnc != null) {
/* 585 */       return xmlEnc;
/*     */     }
/*     */ 
/*     */     
/* 589 */     String cTMime = getContentTypeMime(httpContentType);
/* 590 */     String cTEnc = getContentTypeEncoding(httpContentType);
/* 591 */     boolean appXml = isAppXml(cTMime);
/* 592 */     boolean textXml = isTextXml(cTMime);
/*     */ 
/*     */     
/* 595 */     if (!appXml && !textXml) {
/* 596 */       String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
/* 597 */       throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
/*     */     } 
/*     */ 
/*     */     
/* 601 */     if (cTEnc == null) {
/* 602 */       if (appXml) {
/* 603 */         return calculateRawEncoding(bomEnc, xmlGuessEnc, xmlEnc);
/*     */       }
/* 605 */       return (this.defaultEncoding == null) ? "US-ASCII" : this.defaultEncoding;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 610 */     if (cTEnc.equals("UTF-16BE") || cTEnc.equals("UTF-16LE")) {
/* 611 */       if (bomEnc != null) {
/* 612 */         String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
/* 613 */         throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 615 */       return cTEnc;
/*     */     } 
/*     */ 
/*     */     
/* 619 */     if (cTEnc.equals("UTF-16")) {
/* 620 */       if (bomEnc != null && bomEnc.startsWith("UTF-16")) {
/* 621 */         return bomEnc;
/*     */       }
/* 623 */       String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
/* 624 */       throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
/*     */     } 
/*     */ 
/*     */     
/* 628 */     if (cTEnc.equals("UTF-32BE") || cTEnc.equals("UTF-32LE")) {
/* 629 */       if (bomEnc != null) {
/* 630 */         String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
/* 631 */         throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 633 */       return cTEnc;
/*     */     } 
/*     */ 
/*     */     
/* 637 */     if (cTEnc.equals("UTF-32")) {
/* 638 */       if (bomEnc != null && bomEnc.startsWith("UTF-32")) {
/* 639 */         return bomEnc;
/*     */       }
/* 641 */       String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
/* 642 */       throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
/*     */     } 
/*     */     
/* 645 */     return cTEnc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String getContentTypeMime(String httpContentType) {
/* 655 */     String mime = null;
/* 656 */     if (httpContentType != null) {
/* 657 */       int i = httpContentType.indexOf(";");
/* 658 */       if (i >= 0) {
/* 659 */         mime = httpContentType.substring(0, i);
/*     */       } else {
/* 661 */         mime = httpContentType;
/*     */       } 
/* 663 */       mime = mime.trim();
/*     */     } 
/* 665 */     return mime;
/*     */   }
/*     */   
/* 668 */   private static final Pattern CHARSET_PATTERN = Pattern.compile("charset=[\"']?([.[^; \"']]*)[\"']?");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String getContentTypeEncoding(String httpContentType) {
/* 679 */     String encoding = null;
/* 680 */     if (httpContentType != null) {
/* 681 */       int i = httpContentType.indexOf(";");
/* 682 */       if (i > -1) {
/* 683 */         String postMime = httpContentType.substring(i + 1);
/* 684 */         Matcher m = CHARSET_PATTERN.matcher(postMime);
/* 685 */         encoding = m.find() ? m.group(1) : null;
/* 686 */         encoding = (encoding != null) ? encoding.toUpperCase(Locale.US) : null;
/*     */       } 
/*     */     } 
/* 689 */     return encoding;
/*     */   }
/*     */   
/* 692 */   public static final Pattern ENCODING_PATTERN = Pattern.compile("<\\?xml.*encoding[\\s]*=[\\s]*((?:\".[^\"]*\")|(?:'.[^']*'))", 8);
/*     */   
/*     */   private static final String RAW_EX_1 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch";
/*     */   
/*     */   private static final String RAW_EX_2 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM";
/*     */   
/*     */   private static final String HTTP_EX_1 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL";
/*     */   
/*     */   private static final String HTTP_EX_2 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch";
/*     */   
/*     */   private static final String HTTP_EX_3 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME";
/*     */ 
/*     */   
/*     */   private static String getXmlProlog(InputStream is, String guessedEnc) throws IOException {
/* 706 */     String encoding = null;
/* 707 */     if (guessedEnc != null) {
/* 708 */       byte[] bytes = new byte[4096];
/* 709 */       is.mark(4096);
/* 710 */       int offset = 0;
/* 711 */       int max = 4096;
/* 712 */       int c = is.read(bytes, offset, max);
/* 713 */       int firstGT = -1;
/* 714 */       String xmlProlog = null;
/* 715 */       while (c != -1 && firstGT == -1 && offset < 4096) {
/* 716 */         offset += c;
/* 717 */         max -= c;
/* 718 */         c = is.read(bytes, offset, max);
/* 719 */         xmlProlog = new String(bytes, 0, offset, guessedEnc);
/* 720 */         firstGT = xmlProlog.indexOf('>');
/*     */       } 
/* 722 */       if (firstGT == -1) {
/* 723 */         if (c == -1) {
/* 724 */           throw new IOException("Unexpected end of XML stream");
/*     */         }
/* 726 */         throw new IOException("XML prolog or ROOT element not found on first " + offset + " bytes");
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 731 */       int bytesRead = offset;
/* 732 */       if (bytesRead > 0) {
/* 733 */         is.reset();
/* 734 */         BufferedReader bReader = new BufferedReader(new StringReader(xmlProlog.substring(0, firstGT + 1)));
/*     */         
/* 736 */         StringBuffer prolog = new StringBuffer();
/* 737 */         String line = bReader.readLine();
/* 738 */         while (line != null) {
/* 739 */           prolog.append(line);
/* 740 */           line = bReader.readLine();
/*     */         } 
/* 742 */         Matcher m = ENCODING_PATTERN.matcher(prolog);
/* 743 */         if (m.find()) {
/* 744 */           encoding = m.group(1).toUpperCase();
/* 745 */           encoding = encoding.substring(1, encoding.length() - 1);
/*     */         } 
/*     */       } 
/*     */     } 
/* 749 */     return encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isAppXml(String mime) {
/* 760 */     return (mime != null && (mime.equals("application/xml") || mime.equals("application/xml-dtd") || mime.equals("application/xml-external-parsed-entity") || (mime.startsWith("application/") && mime.endsWith("+xml"))));
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
/*     */   static boolean isTextXml(String mime) {
/* 775 */     return (mime != null && (mime.equals("text/xml") || mime.equals("text/xml-external-parsed-entity") || (mime.startsWith("text/") && mime.endsWith("+xml"))));
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\input\XmlStreamReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */