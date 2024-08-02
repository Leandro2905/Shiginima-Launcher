/*     */ package net.minecraft.launcher;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.TypeAdapterFactory;
/*     */ import com.mojang.launcher.updater.LowerCaseEnumTypeAdapterFactory;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import net.mc.main.Main;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ public class LauncherConstants
/*     */ {
/*     */   public static final int VERSION_FORMAT = 21;
/*     */   public static final int PROFILES_FORMAT = 1;
/*  20 */   public static final String VERSION_NAME = "Shiginima Launcher v" + Main.currentv;
/*     */   public static final String BACK_IMAGE = "/sponge.png";
/*  22 */   public static final URI URL_REGISTER = constantURI("https://account.mojang.com/register");
/*     */   public static final String DEFAULT_PROFILE_NAME = "(Default)";
/*     */   public static final String URL_DOWNLOAD_BASE = "https://s3.amazonaws.com/Minecraft.Download/";
/*     */   public static final String URL_RESOURCE_BASE = "https://resources.download.minecraft.net/";
/*     */   public static final String URL_LIBRARY_BASE = "https://libraries.minecraft.net/";
/*     */   public static final String URL_BLOG = "http://servers.teamshiginima.com/";
/*     */   public static final String URL_SUPPORT = "http://help.mojang.com/?ref=launcher";
/*     */   public static final String URL_STATUS_CHECKER = "http://status.mojang.com/check";
/*     */   public static final int UNVERSIONED_BOOTSTRAP_VERSION = 0;
/*     */   public static final int MINIMUM_BOOTSTRAP_SUPPORTED = 4;
/*     */   public static final int SUPER_COOL_BOOTSTRAP_VERSION = 100;
/*     */   public static final String URL_BOOTSTRAP_DOWNLOAD = "https://mojang.com/2013/06/minecraft-1-6-pre-release/";
/*  34 */   public static final String[] LAUNCHER_OUT_OF_DATE_BUTTONS = new String[] { "Nevermind, close this launcher", "I'm sure. Reset my settings." };
/*  35 */   public static final String[] BOOTSTRAP_OUT_OF_DATE_BUTTONS = new String[] { "Go to URL", "Close" };
/*  36 */   public static final String[] CONFIRM_PROFILE_DELETION_OPTIONS = new String[] { "Delete profile", "Cancel" };
/*  37 */   public static final URI URL_FORGOT_USERNAME = constantURI("http://help.mojang.com/customer/portal/articles/1233873?ref=launcher");
/*  38 */   public static final URI URL_FORGOT_PASSWORD_MINECRAFT = constantURI("http://help.mojang.com/customer/portal/articles/329524-change-or-forgot-password?ref=launcher");
/*  39 */   public static final URI URL_FORGOT_MIGRATED_EMAIL = constantURI("http://help.mojang.com/customer/portal/articles/1205055-minecraft-launcher-error---migrated-account?ref=launcher");
/*  40 */   public static final URI URL_DEMO_HELP = constantURI("https://help.mojang.com/customer/portal/articles/1218766-can-only-play-minecraft-demo?ref=launcher");
/*  41 */   public static final URI URL_UPGRADE = constantURI("https://launcher.mojang.com/download/MinecraftInstaller.msi");
/*     */   public static final int MAX_NATIVES_LIFE_IN_SECONDS = 3600;
/*     */   public static final int MAX_SKIN_LIFE_IN_SECONDS = 604800;
/*     */   public static final String URL_JAR_FALLBACK = "https://s3.amazonaws.com/Minecraft.Download/";
/*  45 */   public static final URI URL_UPGRADE_WINDOWS = constantURI("https://launcher.mojang.com/download/MinecraftInstaller.msi");
/*  46 */   public static final URI URL_UPGRADE_OSX = constantURI("https://launcher.mojang.com/download/Minecraft.dmg");
/*     */   
/*     */   public static URI constantURI(String input) {
/*     */     try {
/*  50 */       return new URI(input);
/*  51 */     } catch (URISyntaxException e) {
/*  52 */       throw new Error(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static URL constantURL(String input) {
/*     */     try {
/*  58 */       return new URL(input);
/*  59 */     } catch (MalformedURLException e) {
/*  60 */       throw new Error(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getVersionName() {
/*  65 */     return VERSION_NAME;
/*     */   }
/*     */   
/*  68 */   public static final LauncherProperties PROPERTIES = getProperties();
/*     */   
/*     */   private static LauncherProperties getProperties() {
/*  71 */     Gson gson = (new GsonBuilder()).registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory()).create();
/*  72 */     InputStream stream = LauncherConstants.class.getResourceAsStream("/launcher_properties.json");
/*  73 */     if (stream != null) {
/*     */       try {
/*  75 */         return (LauncherProperties)gson.fromJson(IOUtils.toString(stream), LauncherProperties.class);
/*  76 */       } catch (IOException e) {
/*  77 */         e.printStackTrace();
/*     */       } finally {
/*  79 */         IOUtils.closeQuietly(stream);
/*     */       } 
/*     */     }
/*  82 */     return new LauncherProperties();
/*     */   }
/*     */   
/*     */   public enum LauncherEnvironment {
/*  86 */     PRODUCTION(""), STAGING(" (STAGING VERSION, NOT FINAL)"), DEV(" (DEV VERSION, NOT FINAL)");
/*     */     
/*     */     private final String title;
/*     */     
/*     */     LauncherEnvironment(String title) {
/*  91 */       this.title = title;
/*     */     }
/*     */     
/*     */     public String getTitle() {
/*  95 */       return this.title;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LauncherProperties
/*     */   {
/* 101 */     private LauncherConstants.LauncherEnvironment environment = LauncherConstants.LauncherEnvironment.PRODUCTION;
/* 102 */     private URL versionManifest = LauncherConstants.constantURL("https://launchermeta.mojang.com/mc/game/version_manifest.json");
/*     */     
/*     */     public LauncherConstants.LauncherEnvironment getEnvironment() {
/* 105 */       return this.environment;
/*     */     }
/*     */     
/*     */     public URL getVersionManifest() {
/* 109 */       return this.versionManifest;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\LauncherConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
