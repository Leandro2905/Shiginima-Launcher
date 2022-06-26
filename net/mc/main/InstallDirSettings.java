/*    */ package net.mc.main;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.Properties;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import javax.swing.JFileChooser;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JOptionPane;
/*    */ import net.minecraft.launcher.Launcher;
/*    */ 
/*    */ public class InstallDirSettings
/*    */ {
/*    */   public static File loadAtStartup(JFrame frame, File defaultWorkingDir) {
/*    */     File workingDirectory;
/* 19 */     Properties prop = new Properties();
/* 20 */     File file = new File("./MOL_Properties.properties");
/*    */     
/* 22 */     if (!file.exists()) {
/*    */       
/* 24 */       workingDirectory = changeDirInternal(frame, defaultWorkingDir, prop, file);
/*    */     } else {
/*    */       
/*    */       try {
/* 28 */         prop.load(new FileInputStream(file));
/* 29 */       } catch (IOException ex) {
/* 30 */         Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, (String)null, ex);
/*    */       } 
/* 32 */       workingDirectory = new File(prop.getProperty("installation_dir"));
/*    */     } 
/* 34 */     return workingDirectory;
/*    */   }
/*    */ 
/*    */   
/*    */   public static File changeDir(JFrame frame, File currentWorkingDir) {
/* 39 */     Properties prop = new Properties();
/* 40 */     File file = new File("./MOL_Properties.properties");
/* 41 */     return changeDirInternal(frame, currentWorkingDir, prop, file);
/*    */   }
/*    */ 
/*    */   
/*    */   private static File changeDirInternal(JFrame frame, File currentWorkingDir, Properties prop, File settingFile) {
/* 46 */     File workingDirectory = new File(currentWorkingDir.getAbsolutePath());
/*    */     
/* 48 */     JFileChooser fileChooser = new JFileChooser();
/* 49 */     fileChooser.setFileSelectionMode(1);
/* 50 */     fileChooser.setDialogTitle("Choose your Minecraft installation directory : (Cancelling will choose the default one)");
/* 51 */     int ret = fileChooser.showOpenDialog(frame);
/*    */     
/* 53 */     if (ret == 0) {
/* 54 */       File dir = fileChooser.getSelectedFile();
/* 55 */       if (!dir.exists()) {
/* 56 */         dir.mkdirs();
/*    */       }
/* 58 */       prop.setProperty("installation_dir", dir.getAbsolutePath());
/* 59 */       workingDirectory = dir;
/* 60 */     } else if (ret == 1) {
/*    */       
/* 62 */       prop.setProperty("installation_dir", workingDirectory.getAbsolutePath());
/*    */     } 
/*    */     
/*    */     try {
/* 66 */       prop.store(new FileOutputStream(settingFile), "");
/* 67 */     } catch (IOException ex) {
/* 68 */       Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, (String)null, ex);
/*    */     } 
/* 70 */     JOptionPane.showMessageDialog(frame, "Minecraft will be installed in " + workingDirectory.getAbsolutePath() + ".\n You can change it by pressing the \"Install folder\" button next to the \"play\" button.\n Note that when you change the installation folder, the content of the previous folder won't be deleted.");
/* 71 */     return workingDirectory;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\mc\main\InstallDirSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */