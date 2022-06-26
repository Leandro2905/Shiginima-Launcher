package com.mojang.authlib.minecraft;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import java.util.Map;

public interface MinecraftSessionService {
  void joinServer(GameProfile paramGameProfile, String paramString1, String paramString2) throws AuthenticationException;
  
  GameProfile hasJoinedServer(GameProfile paramGameProfile, String paramString) throws AuthenticationUnavailableException;
  
  Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(GameProfile paramGameProfile, boolean paramBoolean);
  
  GameProfile fillProfileProperties(GameProfile paramGameProfile, boolean paramBoolean);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\minecraft\MinecraftSessionService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */