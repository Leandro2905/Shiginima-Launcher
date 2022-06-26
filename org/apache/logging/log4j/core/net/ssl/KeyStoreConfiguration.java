/*    */ package org.apache.logging.log4j.core.net.ssl;
/*    */ 
/*    */ import java.security.KeyStoreException;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.UnrecoverableKeyException;
/*    */ import javax.net.ssl.KeyManagerFactory;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*    */ @Plugin(name = "KeyStore", category = "Core", printObject = true)
/*    */ public class KeyStoreConfiguration
/*    */   extends AbstractKeyStoreConfiguration
/*    */ {
/*    */   private final String keyManagerFactoryAlgorithm;
/*    */   
/*    */   public KeyStoreConfiguration(String location, String password, String keyStoreType, String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
/* 39 */     super(location, password, keyStoreType);
/* 40 */     this.keyManagerFactoryAlgorithm = (keyManagerFactoryAlgorithm == null) ? KeyManagerFactory.getDefaultAlgorithm() : keyManagerFactoryAlgorithm;
/*    */   }
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
/*    */   @PluginFactory
/*    */   public static KeyStoreConfiguration createKeyStoreConfiguration(@PluginAttribute("location") String location, @PluginAttribute("password") String password, @PluginAttribute("type") String keyStoreType, @PluginAttribute("keyManagerFactoryAlgorithm") String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
/* 66 */     return new KeyStoreConfiguration(location, password, keyStoreType, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public KeyManagerFactory initKeyManagerFactory() throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
/* 71 */     KeyManagerFactory kmFactory = KeyManagerFactory.getInstance(this.keyManagerFactoryAlgorithm);
/* 72 */     kmFactory.init(getKeyStore(), getPasswordAsCharArray());
/* 73 */     return kmFactory;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\ssl\KeyStoreConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */