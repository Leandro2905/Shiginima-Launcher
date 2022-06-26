package net.minecraft.launcher.updater;

import java.net.URL;

public abstract class AbstractDownloadInfo {
  abstract URL getUrl();
  
  abstract String getSha1();
  
  abstract int getSize();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\updater\AbstractDownloadInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */