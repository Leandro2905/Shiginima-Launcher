/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
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
/*    */ @Beta
/*    */ @GwtCompatible
/*    */ public final class RemovalNotification<K, V>
/*    */   implements Map.Entry<K, V>
/*    */ {
/*    */   @Nullable
/*    */   private final K key;
/*    */   @Nullable
/*    */   private final V value;
/*    */   private final RemovalCause cause;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   RemovalNotification(@Nullable K key, @Nullable V value, RemovalCause cause) {
/* 48 */     this.key = key;
/* 49 */     this.value = value;
/* 50 */     this.cause = (RemovalCause)Preconditions.checkNotNull(cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RemovalCause getCause() {
/* 57 */     return this.cause;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean wasEvicted() {
/* 65 */     return this.cause.wasEvicted();
/*    */   }
/*    */   @Nullable
/*    */   public K getKey() {
/* 69 */     return this.key;
/*    */   }
/*    */   @Nullable
/*    */   public V getValue() {
/* 73 */     return this.value;
/*    */   }
/*    */   
/*    */   public final V setValue(V value) {
/* 77 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 81 */     if (object instanceof Map.Entry) {
/* 82 */       Map.Entry<?, ?> that = (Map.Entry<?, ?>)object;
/* 83 */       return (Objects.equal(getKey(), that.getKey()) && Objects.equal(getValue(), that.getValue()));
/*    */     } 
/*    */     
/* 86 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 90 */     K k = getKey();
/* 91 */     V v = getValue();
/* 92 */     return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 99 */     String str1 = String.valueOf(String.valueOf(getKey())), str2 = String.valueOf(String.valueOf(getValue())); return (new StringBuilder(1 + str1.length() + str2.length())).append(str1).append("=").append(str2).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\cache\RemovalNotification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */