package com.mojang.launcher.events;

import com.mojang.launcher.game.process.GameProcess;

public interface GameOutputLogProcessor {
  void onGameOutput(GameProcess paramGameProcess, String paramString);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\events\GameOutputLogProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */