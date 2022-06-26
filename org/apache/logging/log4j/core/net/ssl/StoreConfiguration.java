/*    */ package org.apache.logging.log4j.core.net.ssl;
/*    */ 
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ public class StoreConfiguration<T>
/*    */ {
/* 25 */   protected static final StatusLogger LOGGER = StatusLogger.getLogger();
/*    */   
/*    */   private String location;
/*    */   private String password;
/*    */   
/*    */   public StoreConfiguration(String location, String password) {
/* 31 */     this.location = location;
/* 32 */     this.password = password;
/*    */   }
/*    */   
/*    */   public String getLocation() {
/* 36 */     return this.location;
/*    */   }
/*    */   
/*    */   public void setLocation(String location) {
/* 40 */     this.location = location;
/*    */   }
/*    */   
/*    */   public String getPassword() {
/* 44 */     return this.password;
/*    */   }
/*    */   
/*    */   public char[] getPasswordAsCharArray() {
/* 48 */     return (this.password == null) ? null : this.password.toCharArray();
/*    */   }
/*    */   
/*    */   public void setPassword(String password) {
/* 52 */     this.password = password;
/*    */   }
/*    */   
/*    */   protected T load() throws StoreConfigurationException {
/* 56 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 61 */     int prime = 31;
/* 62 */     int result = 1;
/* 63 */     result = 31 * result + ((this.location == null) ? 0 : this.location.hashCode());
/* 64 */     result = 31 * result + ((this.password == null) ? 0 : this.password.hashCode());
/* 65 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 70 */     if (this == obj) {
/* 71 */       return true;
/*    */     }
/* 73 */     if (obj == null) {
/* 74 */       return false;
/*    */     }
/* 76 */     if (!(obj instanceof StoreConfiguration)) {
/* 77 */       return false;
/*    */     }
/* 79 */     StoreConfiguration<?> other = (StoreConfiguration)obj;
/* 80 */     if (this.location == null) {
/* 81 */       if (other.location != null) {
/* 82 */         return false;
/*    */       }
/* 84 */     } else if (!this.location.equals(other.location)) {
/* 85 */       return false;
/*    */     } 
/* 87 */     if (this.password == null) {
/* 88 */       if (other.password != null) {
/* 89 */         return false;
/*    */       }
/* 91 */     } else if (!this.password.equals(other.password)) {
/* 92 */       return false;
/*    */     } 
/* 94 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\ssl\StoreConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */