/*    */ package org.apache.logging.log4j.core.net.ssl;
/*    */ 
/*    */ import java.security.KeyStoreException;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import javax.net.ssl.TrustManagerFactory;
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
/*    */ @Plugin(name = "TrustStore", category = "Core", printObject = true)
/*    */ public class TrustStoreConfiguration
/*    */   extends AbstractKeyStoreConfiguration
/*    */ {
/*    */   private final String trustManagerFactoryAlgorithm;
/*    */   
/*    */   public TrustStoreConfiguration(String location, String password, String keyStoreType, String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
/* 38 */     super(location, password, keyStoreType);
/* 39 */     this.trustManagerFactoryAlgorithm = (trustManagerFactoryAlgorithm == null) ? TrustManagerFactory.getDefaultAlgorithm() : trustManagerFactoryAlgorithm;
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
/*    */   public static TrustStoreConfiguration createKeyStoreConfiguration(@PluginAttribute("location") String location, @PluginAttribute("password") String password, @PluginAttribute("type") String keyStoreType, @PluginAttribute("trustManagerFactoryAlgorithm") String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
/* 65 */     return new TrustStoreConfiguration(location, password, keyStoreType, trustManagerFactoryAlgorithm);
/*    */   }
/*    */   
/*    */   public TrustManagerFactory initTrustManagerFactory() throws NoSuchAlgorithmException, KeyStoreException {
/* 69 */     TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(this.trustManagerFactoryAlgorithm);
/* 70 */     tmFactory.init(getKeyStore());
/* 71 */     return tmFactory;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\ssl\TrustStoreConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */