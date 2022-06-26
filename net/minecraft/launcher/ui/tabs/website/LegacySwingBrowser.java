/*     */ package net.minecraft.launcher.ui.tabs.website;
/*     */ 
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextPane;
/*     */ import javax.swing.event.HyperlinkEvent;
/*     */ import javax.swing.event.HyperlinkListener;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ import net.minecraft.launcher.ServerManager;
/*     */ import net.minecraft.launcher.game.GameLaunchDispatcher;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class LegacySwingBrowser implements Browser {
/*  32 */   private static final Logger LOGGER = LogManager.getLogger();
/*  33 */   private final JScrollPane scrollPane = new JScrollPane();
/*  34 */   private final JTextPane browser = new JTextPane();
/*     */   
/*     */   public LegacySwingBrowser() {
/*  37 */     this.browser.setEditable(false);
/*  38 */     this.browser.setMargin((Insets)null);
/*  39 */     this.browser.setBackground(Color.DARK_GRAY);
/*  40 */     this.browser.setContentType("text/html");
/*  41 */     this.browser.setText("<html><body><font color=\"#808080\"><br><br><br><br><br><br><br><center><h1>Loading page..</h1></center></font></body></html>");
/*  42 */     this.browser.addHyperlinkListener(new HyperlinkListener()
/*     */         {
/*     */           public void hyperlinkUpdate(HyperlinkEvent he) {
/*  45 */             if (he.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
/*     */               
/*  47 */               String tofind = "addserver.shiginima";
/*     */               
/*  49 */               Boolean wasFound = Boolean.valueOf(false);
/*     */               try {
/*  51 */                 wasFound = Boolean.valueOf(URLDecoder.decode(he.getURL().toString(), "UTF-8").toLowerCase().contains(tofind.toLowerCase()));
/*  52 */               } catch (UnsupportedEncodingException ex) {
/*  53 */                 Logger.getLogger(LegacySwingBrowser.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */               } 
/*     */               
/*  56 */               if (wasFound.booleanValue()) {
/*     */                 
/*     */                 try {
/*  59 */                   String decodedURL = URLDecoder.decode(he.getURL().toString(), "UTF-8");
/*     */                   
/*  61 */                   System.out.println(decodedURL);
/*     */                   
/*  63 */                   Pattern p = Pattern.compile("\\{(.*?)\\}");
/*  64 */                   Matcher m = p.matcher(decodedURL);
/*     */                   
/*  66 */                   String rawGET = "";
/*  67 */                   String serverIP = "";
/*  68 */                   String serverNAME = "Minecraft Server";
/*     */                   
/*  70 */                   while (m.find()) {
/*  71 */                     rawGET = m.group(1);
/*     */                     
/*  73 */                     if (rawGET != "") {
/*  74 */                       String[] parts = rawGET.split(",");
/*     */                       
/*  76 */                       String ipPart = parts[0];
/*  77 */                       String namePart = parts[1];
/*     */                       
/*  79 */                       String[] finalIpPart = ipPart.split("\\|");
/*  80 */                       String[] finalNamePart = namePart.split("\\|");
/*     */                       
/*  82 */                       String finalIp = finalIpPart[1];
/*  83 */                       String finalName = finalNamePart[1];
/*     */                       
/*  85 */                       Object[] options1 = { "Add To Your Servers List", "Join Server", "Cancel" };
/*  86 */                       JPanel panel = new JPanel();
/*     */                       
/*  88 */                       panel.add(new JLabel("<html><center>What would you like to do with the server:<br>Name: " + finalName + "<br>IP: " + finalIp + "</center></html>"));
/*     */                       
/*  90 */                       int result = JOptionPane.showOptionDialog(null, panel, "Server Manager", 1, -1, null, options1, null);
/*     */                       
/*  92 */                       if (result == 0) {
/*  93 */                         ServerManager.addServer(Byte.valueOf((byte)0), finalName, finalIp); continue;
/*  94 */                       }  if (result == 1)
/*     */                       {
/*  96 */                         List<String> newList = new ArrayList<>();
/*     */                         
/*  98 */                         Collections.addAll(newList, GameLaunchDispatcher.additionalLaunchArgs);
/*     */                         
/* 100 */                         newList.add("--server");
/* 101 */                         newList.add(finalIp);
/*     */                         
/* 103 */                         String[] toCopy = newList.<String>toArray(new String[newList.size()]);
/*     */                         
/* 105 */                         GameLaunchDispatcher.additionalLaunchArgs = toCopy;
/*     */                         
/* 107 */                         System.out.println("ARGS: " + toCopy);
/*     */                         
/* 109 */                         Launcher.getInstance().getLaunchDispatcher().play();
/*     */                       
/*     */                       }
/*     */                     
/*     */                     }
/*     */                   
/*     */                   }
/*     */                 
/*     */                 }
/* 118 */                 catch (UnsupportedEncodingException unsupportedEncodingException) {}
/*     */               } else {
/*     */ 
/*     */                 
/*     */                 try {
/* 123 */                   LegacySwingBrowser.LOGGER.info("Opening Link: " + he.getURL().toString());
/* 124 */                   OperatingSystem.openLink(he.getURL().toURI());
/* 125 */                 } catch (Exception e) {
/* 126 */                   LegacySwingBrowser.LOGGER.error("Unexpected exception opening link " + he.getURL(), e);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 133 */     this.scrollPane.setViewportView(this.browser);
/*     */   }
/*     */   
/*     */   public void loadUrl(final String url) {
/* 137 */     Thread thread = new Thread("Update website tab") {
/*     */         public void run() {
/*     */           try {
/* 140 */             LegacySwingBrowser.this.browser.setPage(new URL(url));
/*     */           }
/* 142 */           catch (Exception e) {
/* 143 */             LegacySwingBrowser.LOGGER.error("Unexpected exception loading " + url, e);
/* 144 */             LegacySwingBrowser.this.browser.setText("<html><body><font color=\"#808080\"><br><br><br><br><br><br><br><center><h1>Failed to get page</h1><br>" + e.toString() + "</center></font></body></html>");
/*     */           } 
/*     */         }
/*     */       };
/* 148 */     thread.setDaemon(true);
/* 149 */     thread.start();
/*     */   }
/*     */   
/*     */   public Component getComponent() {
/* 153 */     return this.scrollPane;
/*     */   }
/*     */   
/*     */   public void resize(Dimension size) {}
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\tabs\website\LegacySwingBrowser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */