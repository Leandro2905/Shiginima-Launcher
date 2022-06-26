/*    */ package net.minecraft.launcher.ui;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.GradientPaint;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Image;
/*    */ import java.awt.geom.Point2D;
/*    */ import java.io.IOException;
/*    */ import javax.imageio.ImageIO;
/*    */ import javax.swing.JPanel;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TexturedPanel
/*    */   extends JPanel
/*    */ {
/* 20 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private static final long serialVersionUID = 1L;
/*    */   private Image image;
/*    */   private Image bgImage;
/*    */   
/*    */   public TexturedPanel(String filename) {
/* 27 */     setOpaque(true);
/*    */     
/*    */     try {
/* 30 */       this.bgImage = ImageIO.read(TexturedPanel.class.getResource(filename)).getScaledInstance(32, 32, 16);
/*    */     }
/* 32 */     catch (IOException e) {
/*    */       
/* 34 */       LOGGER.error("Unexpected exception initializing textured panel", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(Graphics g) {
/* 40 */     paint(g);
/*    */   }
/*    */ 
/*    */   
/*    */   public void paintComponent(Graphics graphics) {
/* 45 */     int width = getWidth() / 2 + 1;
/* 46 */     int height = getHeight() / 2 + 1;
/* 47 */     if (this.image == null || this.image.getWidth(null) != width || this.image.getHeight(null) != height) {
/*    */       
/* 49 */       this.image = createImage(width, height);
/* 50 */       copyImage(width, height);
/*    */     } 
/* 52 */     graphics.drawImage(this.image, 0, 0, width * 2, height * 2, null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void copyImage(int width, int height) {
/* 57 */     Graphics imageGraphics = this.image.getGraphics();
/* 58 */     for (int x = 0; x <= width / 32; x++) {
/* 59 */       for (int y = 0; y <= height / 32; y++) {
/* 60 */         imageGraphics.drawImage(this.bgImage, x * 32, y * 32, null);
/*    */       }
/*    */     } 
/* 63 */     if (imageGraphics instanceof Graphics2D) {
/* 64 */       overlayGradient(width, height, (Graphics2D)imageGraphics);
/*    */     }
/* 66 */     imageGraphics.dispose();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void overlayGradient(int width, int height, Graphics2D graphics) {
/* 71 */     int gh = 1;
/* 72 */     graphics.setPaint(new GradientPaint(new Point2D.Float(0.0F, 0.0F), new Color(553648127, true), new Point2D.Float(0.0F, gh), new Color(0, true)));
/* 73 */     graphics.fillRect(0, 0, width, gh);
/*    */     
/* 75 */     gh = height;
/* 76 */     graphics.setPaint(new GradientPaint(new Point2D.Float(0.0F, 0.0F), new Color(0, true), new Point2D.Float(0.0F, gh), new Color(1610612736, true)));
/* 77 */     graphics.fillRect(0, 0, width, gh);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\TexturedPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */