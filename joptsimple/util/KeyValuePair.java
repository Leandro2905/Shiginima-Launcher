/*    */ package joptsimple.util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class KeyValuePair
/*    */ {
/*    */   public final String key;
/*    */   public final String value;
/*    */   
/*    */   private KeyValuePair(String key, String value) {
/* 43 */     this.key = key;
/* 44 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static KeyValuePair valueOf(String asString) {
/* 55 */     int equalsIndex = asString.indexOf('=');
/* 56 */     if (equalsIndex == -1) {
/* 57 */       return new KeyValuePair(asString, "");
/*    */     }
/* 59 */     String aKey = asString.substring(0, equalsIndex);
/* 60 */     String aValue = (equalsIndex == asString.length() - 1) ? "" : asString.substring(equalsIndex + 1);
/*    */     
/* 62 */     return new KeyValuePair(aKey, aValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object that) {
/* 67 */     if (!(that instanceof KeyValuePair)) {
/* 68 */       return false;
/*    */     }
/* 70 */     KeyValuePair other = (KeyValuePair)that;
/* 71 */     return (this.key.equals(other.key) && this.value.equals(other.value));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 76 */     return this.key.hashCode() ^ this.value.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 81 */     return this.key + '=' + this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimpl\\util\KeyValuePair.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */