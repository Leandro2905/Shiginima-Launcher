package com.mojang.launcher.game.runner;

import com.mojang.launcher.game.GameInstanceStatus;
import com.mojang.launcher.updater.VersionSyncInfo;
import com.mojang.launcher.updater.download.DownloadJob;

public interface GameRunner {
  GameInstanceStatus getStatus();
  
  void playGame(VersionSyncInfo paramVersionSyncInfo);
  
  boolean hasRemainingJobs();
  
  void addJob(DownloadJob paramDownloadJob);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\game\runner\GameRunner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */