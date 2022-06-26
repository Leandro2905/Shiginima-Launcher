/*    */ package org.yaml.snakeyaml.util;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.net.URLDecoder;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.CharBuffer;
/*    */ import java.nio.charset.CharacterCodingException;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.CharsetDecoder;
/*    */ import java.nio.charset.CodingErrorAction;
/*    */ import org.yaml.snakeyaml.error.YAMLException;
/*    */ import org.yaml.snakeyaml.external.com.google.gdata.util.common.base.Escaper;
/*    */ import org.yaml.snakeyaml.external.com.google.gdata.util.common.base.PercentEscaper;
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
/*    */ public abstract class UriEncoder
/*    */ {
/* 32 */   private static final CharsetDecoder UTF8Decoder = Charset.forName("UTF-8").newDecoder().onMalformedInput(CodingErrorAction.REPORT);
/*    */ 
/*    */   
/*    */   private static final String SAFE_CHARS = "-_.!~*'()@:$&,;=[]/";
/*    */ 
/*    */   
/* 38 */   private static final Escaper escaper = (Escaper)new PercentEscaper("-_.!~*'()@:$&,;=[]/", false);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String encode(String uri) {
/* 44 */     return escaper.escape(uri);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String decode(ByteBuffer buff) throws CharacterCodingException {
/* 51 */     CharBuffer chars = UTF8Decoder.decode(buff);
/* 52 */     return chars.toString();
/*    */   }
/*    */   
/*    */   public static String decode(String buff) {
/*    */     try {
/* 57 */       return URLDecoder.decode(buff, "UTF-8");
/* 58 */     } catch (UnsupportedEncodingException e) {
/* 59 */       throw new YAMLException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyam\\util\UriEncoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */