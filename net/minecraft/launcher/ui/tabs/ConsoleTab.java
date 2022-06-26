/*     */ package net.minecraft.launcher.ui.tabs;
/*     */ 
/*     */ import com.mojang.util.QueueLogAppender;
/*     */ import java.awt.Font;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConsoleTab
/*     */   extends JScrollPane
/*     */ {
/*  25 */   private static final Font MONOSPACED = new Font("Monospaced", 0, 12);
/*  26 */   private final JTextArea console = new JTextArea();
/*  27 */   private final JPopupMenu popupMenu = new JPopupMenu();
/*  28 */   private final JMenuItem copyTextButton = new JMenuItem("Copy All Text");
/*     */   
/*     */   private final Launcher minecraftLauncher;
/*     */   
/*     */   public ConsoleTab(Launcher minecraftLauncher) {
/*  33 */     this.minecraftLauncher = minecraftLauncher;
/*     */     
/*  35 */     this.popupMenu.add(this.copyTextButton);
/*  36 */     this.console.setComponentPopupMenu(this.popupMenu);
/*     */     
/*  38 */     this.copyTextButton.addActionListener(new ActionListener()
/*     */         {
/*     */           
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/*     */             try {
/*  44 */               StringSelection ss = new StringSelection(ConsoleTab.this.console.getText());
/*  45 */               Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
/*     */             }
/*  47 */             catch (Exception exception) {}
/*     */           }
/*     */         });
/*  50 */     this.console.setFont(MONOSPACED);
/*  51 */     this.console.setEditable(false);
/*  52 */     this.console.setMargin((Insets)null);
/*     */     
/*  54 */     setViewportView(this.console);
/*     */     
/*  56 */     Thread thread = new Thread(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/*     */             String line;
/*  61 */             while ((line = QueueLogAppender.getNextLogEvent("DevelopmentConsole")) != null) {
/*  62 */               ConsoleTab.this.print(line);
/*     */             }
/*     */           }
/*     */         });
/*  66 */     thread.setDaemon(true);
/*  67 */     thread.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public Launcher getMinecraftLauncher() {
/*  72 */     return this.minecraftLauncher;
/*     */   }
/*     */ 
/*     */   
/*     */   public void print(final String line) {
/*  77 */     if (!SwingUtilities.isEventDispatchThread()) {
/*     */       
/*  79 */       SwingUtilities.invokeLater(new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/*  83 */               ConsoleTab.this.print(line);
/*     */             }
/*     */           });
/*     */       return;
/*     */     } 
/*  88 */     Document document = this.console.getDocument();
/*  89 */     JScrollBar scrollBar = getVerticalScrollBar();
/*  90 */     boolean shouldScroll = false;
/*  91 */     if (getViewport().getView() == this.console) {
/*  92 */       shouldScroll = (scrollBar.getValue() + scrollBar.getSize().getHeight() + (MONOSPACED.getSize() * 4) > scrollBar.getMaximum());
/*     */     }
/*     */     
/*     */     try {
/*  96 */       document.insertString(document.getLength(), line, null);
/*     */     }
/*  98 */     catch (BadLocationException badLocationException) {}
/*  99 */     if (shouldScroll)
/* 100 */       scrollBar.setValue(2147483647); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\tabs\ConsoleTab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */