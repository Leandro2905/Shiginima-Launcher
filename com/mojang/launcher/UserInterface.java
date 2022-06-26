package com.mojang.launcher;

import com.mojang.launcher.updater.DownloadProgress;
import com.mojang.launcher.versions.CompleteVersion;
import java.io.File;

public interface UserInterface {
  void showLoginPrompt();
  
  void setVisible(boolean paramBoolean);
  
  void shutdownLauncher();
  
  void hideDownloadProgress();
  
  void setDownloadProgress(DownloadProgress paramDownloadProgress);
  
  void showCrashReport(CompleteVersion paramCompleteVersion, File paramFile, String paramString);
  
  void gameLaunchFailure(String paramString);
  
  void updatePlayState();
  
  void updateSideState();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\UserInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */