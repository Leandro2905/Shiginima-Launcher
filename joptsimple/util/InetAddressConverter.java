/*    */ package joptsimple.util;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
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
/*    */ public class InetAddressConverter
/*    */   implements ValueConverter<InetAddress>
/*    */ {
/*    */   public InetAddress convert(String value) {
/*    */     try {
/* 42 */       return InetAddress.getByName(value);
/* 43 */     } catch (UnknownHostException e) {
/* 44 */       throw new ValueConversionException("Cannot convert value [" + value + " into an InetAddress", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public Class<InetAddress> valueType() {
/* 49 */     return InetAddress.class;
/*    */   }
/*    */   
/*    */   public String valuePattern() {
/* 53 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimpl\\util\InetAddressConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */