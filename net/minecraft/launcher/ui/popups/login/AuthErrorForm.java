/*     */ package net.minecraft.launcher.ui.popups.login;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.TypeAdapterFactory;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.mojang.launcher.Http;
/*     */ import com.mojang.launcher.updater.LowerCaseEnumTypeAdapterFactory;
/*     */ import java.net.URL;
/*     */ import java.util.Map;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import org.apache.commons.lang3.exception.ExceptionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AuthErrorForm
/*     */   extends JPanel
/*     */ {
/*     */   private final LogInPopup popup;
/*  23 */   private final JLabel errorLabel = new JLabel();
/*  24 */   private final Gson gson = (new GsonBuilder()).registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory()).create();
/*     */ 
/*     */   
/*     */   public AuthErrorForm(LogInPopup popup) {
/*  28 */     this.popup = popup;
/*     */     
/*  30 */     createInterface();
/*  31 */     clear();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createInterface() {
/*  36 */     setBorder(new EmptyBorder(0, 0, 15, 0));
/*  37 */     this.errorLabel.setFont(this.errorLabel.getFont().deriveFont(1));
/*  38 */     add(this.errorLabel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  43 */     setVisible(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVisible(boolean value) {
/*  48 */     super.setVisible(value);
/*  49 */     this.popup.repack();
/*     */   }
/*     */ 
/*     */   
/*     */   public void displayError(final Throwable throwable, String... lines) {
/*  54 */     if (SwingUtilities.isEventDispatchThread()) {
/*     */       
/*  56 */       String error = "";
/*  57 */       for (String line : lines) {
/*  58 */         error = error + "<p>" + line + "</p>";
/*     */       }
/*  60 */       if (throwable != null) {
/*  61 */         error = error + "<p style='font-size: 0.9em; font-style: italic;'>(" + ExceptionUtils.getRootCauseMessage(throwable) + ")</p>";
/*     */       }
/*  63 */       this.errorLabel.setText("<html><div style='text-align: center;'>" + error + " </div></html>");
/*  64 */       if (!isVisible()) {
/*  65 */         refreshStatuses();
/*     */       }
/*  67 */       setVisible(true);
/*     */     }
/*     */     else {
/*     */       
/*  71 */       SwingUtilities.invokeLater(new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/*  75 */               AuthErrorForm.this.displayError(throwable, lines);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void refreshStatuses() {
/*  83 */     this.popup.getMinecraftLauncher().getLauncher().getVersionManager().getExecutorService().submit(new Runnable()
/*     */         {
/*     */           
/*     */           public void run()
/*     */           {
/*     */             try {
/*  89 */               TypeToken<Map<String, AuthErrorForm.ServerStatus>> token = new TypeToken() {  };
/*  90 */               Map<String, AuthErrorForm.ServerStatus> statuses = (Map<String, AuthErrorForm.ServerStatus>)AuthErrorForm.this.gson.fromJson(Http.performGet(new URL("http://status.mojang.com/check?service=authserver.mojang.com"), AuthErrorForm.this.popup.getMinecraftLauncher().getLauncher().getProxy()), token.getType());
/*  91 */               if (statuses.get("authserver.mojang.com") == AuthErrorForm.ServerStatus.RED) {
/*  92 */                 AuthErrorForm.this.displayError((Throwable)null, new String[] { "It looks like our servers are down right now. Sorry!", "We're already working on the problem and will have it fixed soon.", "Please try again later!" });
/*     */               }
/*     */             }
/*  95 */             catch (Exception exception) {}
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public enum ServerStatus
/*     */   {
/* 102 */     GREEN("Online, no problems detected."), YELLOW("May be experiencing issues."), RED("Offline, experiencing problems.");
/*     */     
/*     */     private final String title;
/*     */ 
/*     */     
/*     */     ServerStatus(String title) {
/* 108 */       this.title = title;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\popups\login\AuthErrorForm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */