/*     */ package net.mc.main;
/*     */ 
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import java.awt.Desktop;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.Authenticator;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.PasswordAuthentication;
/*     */ import java.net.Proxy;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import joptsimple.ArgumentAcceptingOptionSpec;
/*     */ import joptsimple.NonOptionArgumentSpec;
/*     */ import joptsimple.OptionException;
/*     */ import joptsimple.OptionParser;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
/*     */ import net.minecraft.launcher.Language;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ import net.minecraft.launcher.StorageManager;
/*     */ 
/*     */ public class Main
/*     */ {
/*     */   public static Language lh;
/*  39 */   public static String currentv = "4.400";
/*  40 */   private static String newestv = "";
/*  41 */   private static String wherenewv = "";
/*  42 */   private static String updateurl = "";
/*  43 */   private static Boolean isLatest = Boolean.valueOf(true);
/*  44 */   public static String cbuttontext = "Launcher Website";
/*  45 */   public static String cbuttonlink = "http://teamshiginima.com/";
/*  46 */   public static String sButton = "Support Ticket";
/*  47 */   public static String sLink = "http://teamshiginima.com/";
/*  48 */   public static String npg = "http://news.teamshiginima.com/";
/*  49 */   public static List<String> dnames = new ArrayList<>();
/*     */   
/*  51 */   public static String[] urls = new String[] { "teamshiginima.com", "teamshiginima.net", "teamshiginima.info", "shigmeahyea.us.to" };
/*     */   
/*  53 */   public static String bUrl = "http://teamshiginima.com/";
/*  54 */   public static String bUrl2 = "teamshiginima.com";
/*     */   
/*  56 */   public static String backupBaseURL = "http://shigmeahyea.us.to/";
/*  57 */   public static String backupBaseURL2 = "shigmeahyea.us.to";
/*     */   
/*     */   private static JFrame frame;
/*     */   
/*     */   private static File workingDirectory;
/*     */   private static Proxy proxy;
/*     */   private static PasswordAuthentication passwordAuthentication;
/*     */   private static String[] argss;
/*     */   
/*     */   public static void restartApplication() throws IOException, URISyntaxException {
/*  67 */     System.out.println("Restarting launcher'");
/*     */ 
/*     */     
/*  70 */     Launcher l = new Launcher(frame, workingDirectory, proxy, passwordAuthentication, argss, Integer.valueOf(100));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkUpdates() {
/*     */     try {
/*     */       URL url;
/*  79 */       if (isReachable(bUrl2).booleanValue()) {
/*     */         
/*  81 */         System.out.println("Using default URL.");
/*     */         
/*  83 */         url = new URL(bUrl + "version.php");
/*     */       } else {
/*     */         
/*  86 */         System.out.println("Could not reach " + bUrl + ". Trying to reach old URL.");
/*     */         
/*  88 */         if (isReachable(backupBaseURL2).booleanValue()) {
/*     */           
/*  90 */           System.out.println("Using backup URL.");
/*     */           
/*  92 */           url = new URL(backupBaseURL + "version.php");
/*     */         } else {
/*     */           
/*  95 */           System.out.println("Could not reach backup URL. Exiting update check sequence.");
/*     */           
/*  97 */           isLatest = Boolean.valueOf(true);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */       
/* 103 */       URLConnection yc = url.openConnection();
/* 104 */       yc.addRequestProperty("User-Agent", "Mozilla/4.76");
/*     */       
/* 106 */       BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
/*     */       
/* 108 */       int i = 0;
/*     */       String inputLine;
/* 110 */       while ((inputLine = in.readLine()) != null) {
/* 111 */         if (i == 0) {
/* 112 */           newestv = inputLine;
/* 113 */         } else if (i == 1) {
/* 114 */           wherenewv = inputLine;
/* 115 */         } else if (i == 2) {
/* 116 */           updateurl = inputLine;
/* 117 */         } else if (i == 3) {
/* 118 */           cbuttontext = inputLine;
/* 119 */         } else if (i == 4) {
/* 120 */           cbuttonlink = inputLine;
/* 121 */         } else if (i == 5) {
/* 122 */           sButton = inputLine;
/* 123 */         } else if (i == 6) {
/* 124 */           sLink = inputLine;
/*     */         } 
/* 126 */         i++;
/*     */       } 
/* 128 */       in.close();
/* 129 */       if (!newestv.equalsIgnoreCase(currentv)) {
/* 130 */         System.out.println(newestv);
/* 131 */         System.out.println(currentv);
/* 132 */         isLatest = Boolean.valueOf(false);
/*     */       } else {
/* 134 */         isLatest = Boolean.valueOf(true);
/*     */       } 
/* 136 */     } catch (IOException ex) {
/* 137 */       URL url; isLatest = Boolean.valueOf(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void startMC(String[] args) throws IOException {
/*     */     OptionSet optionSet;
/* 144 */     for (String s : args) {
/* 145 */       System.out.println(s);
/*     */     }
/*     */     
/* 148 */     System.setProperty("java.net.preferIPv4Stack", "true");
/*     */     
/* 150 */     OptionParser optionParser = new OptionParser();
/* 151 */     optionParser.allowsUnrecognizedOptions();
/*     */     
/* 153 */     optionParser.accepts("help", "Show help").forHelp();
/* 154 */     optionParser.accepts("force", "Force updating");
/*     */     
/* 156 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec1 = optionParser.accepts("proxyHost", "Optional").withRequiredArg();
/* 157 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec2 = optionParser.accepts("proxyPort", "Optional").withRequiredArg().defaultsTo("8080", (Object[])new String[0]).ofType(Integer.class);
/* 158 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec3 = optionParser.accepts("proxyUser", "Optional").withRequiredArg();
/* 159 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec4 = optionParser.accepts("proxyPass", "Optional").withRequiredArg();
/* 160 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec5 = optionParser.accepts("workdir", "Optional").withRequiredArg().ofType(File.class).defaultsTo(Util.getWorkingDirectory(), (Object[])new File[0]);
/* 161 */     NonOptionArgumentSpec nonOptionArgumentSpec = optionParser.nonOptions();
/*     */     
/*     */     try {
/* 164 */       optionSet = optionParser.parse(args);
/* 165 */     } catch (OptionException e) {
/* 166 */       optionParser.printHelpOn(System.out);
/* 167 */       System.out.println("(to pass in arguments to minecraft directly use: '--' followed by your arguments");
/*     */       return;
/*     */     } 
/* 170 */     if (optionSet.has("help")) {
/* 171 */       optionParser.printHelpOn(System.out);
/*     */       return;
/*     */     } 
/* 174 */     String hostName = (String)optionSet.valueOf((OptionSpec)argumentAcceptingOptionSpec1);
/* 175 */     proxy = Proxy.NO_PROXY;
/* 176 */     if (hostName != null) {
/*     */       try {
/* 178 */         proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(hostName, ((Integer)optionSet.valueOf((OptionSpec)argumentAcceptingOptionSpec2)).intValue()));
/* 179 */       } catch (Exception exception) {}
/*     */     }
/*     */     
/* 182 */     String proxyUser = (String)optionSet.valueOf((OptionSpec)argumentAcceptingOptionSpec3);
/* 183 */     String proxyPass = (String)optionSet.valueOf((OptionSpec)argumentAcceptingOptionSpec4);
/* 184 */     passwordAuthentication = null;
/* 185 */     if (!proxy.equals(Proxy.NO_PROXY) && stringHasValue(proxyUser) && stringHasValue(proxyPass)) {
/* 186 */       passwordAuthentication = new PasswordAuthentication(proxyUser, proxyPass.toCharArray());
/*     */       
/* 188 */       final PasswordAuthentication auth = passwordAuthentication;
/* 189 */       Authenticator.setDefault(new Authenticator()
/*     */           {
/*     */             protected PasswordAuthentication getPasswordAuthentication() {
/* 192 */               return auth;
/*     */             }
/*     */           });
/*     */     } 
/* 196 */     workingDirectory = (File)optionSet.valueOf((OptionSpec)argumentAcceptingOptionSpec5);
/* 197 */     if (workingDirectory.exists() && !workingDirectory.isDirectory()) {
/* 198 */       throw new RuntimeException("Invalid working directory: " + workingDirectory);
/*     */     }
/* 200 */     if (!workingDirectory.exists() && !workingDirectory.mkdirs()) {
/* 201 */       throw new RuntimeException("Unable to create directory: " + workingDirectory);
/*     */     }
/* 203 */     frame = new JFrame();
/* 204 */     argss = args;
/*     */     
/* 206 */     Launcher l = new Launcher(frame, workingDirectory, proxy, passwordAuthentication, args, Integer.valueOf(100));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Boolean isReachable(String url) {
/*     */     try {
/* 212 */       HttpURLConnection.setFollowRedirects(false);
/*     */ 
/*     */       
/* 215 */       HttpURLConnection con = (HttpURLConnection)(new URL("http://" + url + "/version.php")).openConnection();
/* 216 */       con.setRequestMethod("HEAD");
/* 217 */       return Boolean.valueOf((con.getResponseCode() == 200));
/* 218 */     } catch (Exception e) {
/* 219 */       e.printStackTrace();
/* 220 */       return Boolean.valueOf(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws IOException {
/* 245 */     StorageManager sm = new StorageManager();
/* 246 */     sm.load();
/*     */     
/* 248 */     new DarkTheme(sm.getDark());
/*     */     
/* 250 */     String lang = sm.getLanguage();
/* 251 */     lh = new Language();
/*     */     try {
/* 253 */       lh.loadFileNames();
/* 254 */     } catch (URISyntaxException ex) {
/* 255 */       Logger.getLogger(Main.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     } 
/* 257 */     lh.load(lang);
/*     */     
/* 259 */     checkUpdates();
/*     */     
/* 261 */     if (!isLatest.booleanValue()) {
/* 262 */       Object[] options1 = { Language.get("button.ok"), Language.get("button.update"), Language.get("button.cancel") };
/*     */       
/* 264 */       JPanel panel = new JPanel();
/*     */       
/* 266 */       panel.add(new JLabel("<html><center>" + wherenewv + "<br>You have version: v" + currentv + "<br>The latest version is: v" + newestv + "</center></html>"));
/*     */       
/* 268 */       int result = JOptionPane.showOptionDialog(null, panel, Language.get("update.title.updateavail"), 1, -1, null, options1, null);
/*     */       
/* 270 */       if (result == 0) {
/* 271 */         startMC(args);
/* 272 */       } else if (result == 1) {
/*     */         
/*     */         try {
/* 275 */           OperatingSystem.openLink(new URI(updateurl));
/* 276 */         } catch (Exception e) {
/*     */           try {
/* 278 */             Desktop.getDesktop().browse(new URI(updateurl));
/* 279 */           } catch (Exception exception) {}
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 287 */       startMC(args);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean stringHasValue(String string) {
/* 292 */     return (string != null && !string.isEmpty());
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\mc\main\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */