package com.mojang.launcher.updater;

import com.mojang.launcher.events.RefreshedVersionsListener;
import com.mojang.launcher.updater.download.DownloadJob;
import com.mojang.launcher.versions.CompleteVersion;
import com.mojang.launcher.versions.ReleaseType;
import com.mojang.launcher.versions.Version;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public interface VersionManager {
  void refreshVersions() throws IOException;
  
  List<VersionSyncInfo> getVersions();
  
  List<VersionSyncInfo> getVersions(VersionFilter<? extends ReleaseType> paramVersionFilter);
  
  VersionSyncInfo getVersionSyncInfo(Version paramVersion);
  
  VersionSyncInfo getVersionSyncInfo(String paramString);
  
  VersionSyncInfo getVersionSyncInfo(Version paramVersion1, Version paramVersion2);
  
  List<VersionSyncInfo> getInstalledVersions();
  
  CompleteVersion getLatestCompleteVersion(VersionSyncInfo paramVersionSyncInfo) throws IOException;
  
  DownloadJob downloadVersion(VersionSyncInfo paramVersionSyncInfo, DownloadJob paramDownloadJob) throws IOException;
  
  DownloadJob downloadResources(DownloadJob paramDownloadJob, CompleteVersion paramCompleteVersion) throws IOException;
  
  ThreadPoolExecutor getExecutorService();
  
  void addRefreshedVersionsListener(RefreshedVersionsListener paramRefreshedVersionsListener);
  
  void removeRefreshedVersionsListener(RefreshedVersionsListener paramRefreshedVersionsListener);
  
  VersionSyncInfo syncVersion(VersionSyncInfo paramVersionSyncInfo) throws IOException;
  
  void installVersion(CompleteVersion paramCompleteVersion) throws IOException;
  
  void uninstallVersion(CompleteVersion paramCompleteVersion) throws IOException;
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launche\\updater\VersionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */