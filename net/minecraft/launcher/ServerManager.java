/*    */ package net.minecraft.launcher;
/*    */ 
/*    */ import com.evilco.mc.nbt.stream.NbtInputStream;
/*    */ import com.evilco.mc.nbt.stream.NbtOutputStream;
/*    */ import com.evilco.mc.nbt.tag.ITag;
/*    */ import com.evilco.mc.nbt.tag.TagByte;
/*    */ import com.evilco.mc.nbt.tag.TagCompound;
/*    */ import com.evilco.mc.nbt.tag.TagList;
/*    */ import com.evilco.mc.nbt.tag.TagString;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.mc.main.Util;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerManager
/*    */ {
/* 33 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */ 
/*    */   
/*    */   public static void addServer(Byte hideServer, String serverName, String serverIP) {
/*    */     try {
/* 38 */       File serverData = new File(Util.getWorkingDirectory(), "servers.dat");
/* 39 */       if (serverData.exists()) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 55 */         NbtInputStream nis = new NbtInputStream(new FileInputStream(serverData));
/*    */         
/* 57 */         TagCompound tag = (TagCompound)nis.readTag();
/*    */ 
/*    */         
/* 60 */         List<TagCompound> tca = tag.getList("servers", TagCompound.class);
/*    */         
/* 62 */         List<String> currentIPS = new ArrayList<>();
/*    */         
/* 64 */         for (TagCompound t : tca) {
/* 65 */           currentIPS.add(t.getString("ip").toLowerCase());
/*    */         }
/*    */ 
/*    */         
/* 69 */         if (!currentIPS.contains(serverIP.toLowerCase())) {
/*    */ 
/*    */           
/* 72 */           TagCompound toAdd = new TagCompound("");
/*    */           
/* 74 */           toAdd.setTag((ITag)new TagByte("hideAddress", hideServer.byteValue()));
/* 75 */           toAdd.setTag((ITag)new TagString("name", serverName));
/* 76 */           toAdd.setTag((ITag)new TagString("ip", serverIP));
/*    */           
/* 78 */           TagList tl = new TagList("servers");
/* 79 */           tl.addTag((ITag)toAdd);
/* 80 */           for (TagCompound tc : tca) {
/* 81 */             tl.addTag((ITag)tc);
/*    */           }
/*    */           
/* 84 */           TagCompound finalTag = new TagCompound("");
/*    */           
/* 86 */           finalTag.setTag((ITag)tl);
/*    */           
/* 88 */           ByteArrayOutputStream outputStreamNBT = new ByteArrayOutputStream();
/* 89 */           NbtOutputStream nbtOutputStream = new NbtOutputStream(outputStreamNBT);
/*    */ 
/*    */           
/* 92 */           nbtOutputStream.write((ITag)finalTag);
/*    */           
/* 94 */           OutputStream outputStream = new FileOutputStream(serverData);
/* 95 */           outputStreamNBT.writeTo(outputStream);
/* 96 */           LOGGER.info("[Shiginima Launcher Log] Added the server " + serverName + " with IP: " + serverIP + " to the server list!");
/*    */         } else {
/* 98 */           LOGGER.info("[Shiginima Launcher Log] Could not add the server " + serverName + " with IP: " + serverIP + " to the list as the IP already exists!");
/*    */         }
/*    */       
/*    */       } 
/* :2 */     } catch (FileNotFoundException fileNotFoundException) {
/*    */     
/* :4 */     } catch (IOException iOException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\ServerManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */