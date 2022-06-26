/*    */ package org.apache.logging.log4j.core.net.ssl;
/*    */ 
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.security.KeyStore;
/*    */ import java.security.KeyStoreException;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.cert.CertificateException;
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
/*    */ public class AbstractKeyStoreConfiguration
/*    */   extends StoreConfiguration<KeyStore>
/*    */ {
/*    */   private final KeyStore keyStore;
/*    */   private final String keyStoreType;
/*    */   
/*    */   public AbstractKeyStoreConfiguration(String location, String password, String keyStoreType) throws StoreConfigurationException {
/* 36 */     super(location, password);
/* 37 */     this.keyStoreType = (keyStoreType == null) ? "JKS" : keyStoreType;
/* 38 */     this.keyStore = load();
/*    */   }
/*    */ 
/*    */   
/*    */   protected KeyStore load() throws StoreConfigurationException {
/* 43 */     FileInputStream fin = null;
/*    */     
/* 45 */     LOGGER.debug("Loading keystore from file with params(location={})", new Object[] { getLocation() });
/*    */     try {
/* 47 */       if (getLocation() == null) {
/* 48 */         throw new IOException("The location is null");
/*    */       }
/* 50 */       fin = new FileInputStream(getLocation());
/* 51 */       KeyStore ks = KeyStore.getInstance(this.keyStoreType);
/* 52 */       ks.load(fin, getPasswordAsCharArray());
/* 53 */       LOGGER.debug("Keystore successfully loaded with params(location={})", new Object[] { getLocation() });
/* 54 */       return ks;
/* 55 */     } catch (CertificateException e) {
/* 56 */       LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type {}", new Object[] { this.keyStoreType });
/* 57 */       throw new StoreConfigurationException(e);
/* 58 */     } catch (NoSuchAlgorithmException e) {
/* 59 */       LOGGER.error("The algorithm used to check the integrity of the keystore cannot be found");
/* 60 */       throw new StoreConfigurationException(e);
/* 61 */     } catch (KeyStoreException e) {
/* 62 */       LOGGER.error(e);
/* 63 */       throw new StoreConfigurationException(e);
/* 64 */     } catch (FileNotFoundException e) {
/* 65 */       LOGGER.error("The keystore file({}) is not found", new Object[] { getLocation() });
/* 66 */       throw new StoreConfigurationException(e);
/* 67 */     } catch (IOException e) {
/* 68 */       LOGGER.error("Something is wrong with the format of the keystore or the given password");
/* 69 */       throw new StoreConfigurationException(e);
/*    */     } finally {
/*    */       try {
/* 72 */         if (fin != null) {
/* 73 */           fin.close();
/*    */         }
/* 75 */       } catch (IOException e) {
/* 76 */         LOGGER.debug(e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public KeyStore getKeyStore() {
/* 82 */     return this.keyStore;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\ssl\AbstractKeyStoreConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */