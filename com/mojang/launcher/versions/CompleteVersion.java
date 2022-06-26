package com.mojang.launcher.versions;

import java.util.Date;

public interface CompleteVersion extends Version {
  String getId();
  
  ReleaseType getType();
  
  Date getUpdatedTime();
  
  Date getReleaseTime();
  
  int getMinimumLauncherVersion();
  
  boolean appliesToCurrentEnvironment();
  
  String getIncompatibilityReason();
  
  boolean isSynced();
  
  void setSynced(boolean paramBoolean);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\versions\CompleteVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */