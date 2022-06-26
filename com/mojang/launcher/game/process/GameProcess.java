package com.mojang.launcher.game.process;

import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.List;

public interface GameProcess {
  List<String> getStartupArguments();
  
  Collection<String> getSysOutLines();
  
  Predicate<String> getSysOutFilter();
  
  boolean isRunning();
  
  void setExitRunnable(GameProcessRunnable paramGameProcessRunnable);
  
  GameProcessRunnable getExitRunnable();
  
  int getExitCode();
  
  void stop();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\game\process\GameProcess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */