package org.jivesoftware.smack.provider;

import java.util.Collection;

public interface ProviderLoader {
  Collection<IQProviderInfo> getIQProviderInfo();
  
  Collection<ExtensionProviderInfo> getExtensionProviderInfo();
  
  Collection<StreamFeatureProviderInfo> getStreamFeatureProviderInfo();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\provider\ProviderLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */