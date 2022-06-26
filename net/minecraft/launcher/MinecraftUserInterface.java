package net.minecraft.launcher;

import com.mojang.launcher.UserInterface;
import com.mojang.launcher.events.GameOutputLogProcessor;
import net.minecraft.launcher.game.MinecraftGameRunner;

public interface MinecraftUserInterface extends UserInterface {
  void showOutdatedNotice();
  
  String getTitle();
  
  GameOutputLogProcessor showGameOutputTab(MinecraftGameRunner paramMinecraftGameRunner);
  
  boolean shouldDowngradeProfiles();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\MinecraftUserInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */