/*    */ package net.mc.main;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.UnsupportedLookAndFeelException;
/*    */ 
/*    */ public class DarkTheme
/*    */ {
/*    */   public DarkTheme(Boolean dark) {
/* 10 */     if (dark.booleanValue()) {
/* 11 */       setDark();
/*    */     }
/*    */   }
/*    */   
/*    */   public void setDark() {
/* 16 */     UIManager.put("control", new Color(48, 48, 48));
/* 17 */     UIManager.put("info", new Color(128, 128, 128));
/* 18 */     UIManager.put("nimbusBase", new Color(10, 10, 10));
/* 19 */     UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
/* 20 */     UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
/* 21 */     UIManager.put("nimbusFocus", new Color(115, 164, 209));
/* 22 */     UIManager.put("nimbusGreen", new Color(176, 179, 50));
/* 23 */     UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
/* 24 */     UIManager.put("nimbusLightBackground", new Color(18, 30, 49));
/* 25 */     UIManager.put("nimbusOrange", new Color(191, 98, 4));
/* 26 */     UIManager.put("nimbusRed", new Color(169, 46, 34));
/* 27 */     UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
/* 28 */     UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
/* 29 */     UIManager.put("text", new Color(230, 230, 230));
/*    */     try {
/* 31 */       for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
/* 32 */         if ("Nimbus".equals(info.getName())) {
/* 33 */           UIManager.setLookAndFeel(info.getClassName());
/*    */           break;
/*    */         } 
/*    */       } 
/* 37 */     } catch (ClassNotFoundException e) {
/* 38 */       e.printStackTrace();
/* 39 */     } catch (InstantiationException e) {
/* 40 */       e.printStackTrace();
/* 41 */     } catch (IllegalAccessException e) {
/* 42 */       e.printStackTrace();
/* 43 */     } catch (UnsupportedLookAndFeelException e) {
/* 44 */       e.printStackTrace();
/* 45 */     } catch (Exception e) {
/* 46 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\mc\main\DarkTheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */