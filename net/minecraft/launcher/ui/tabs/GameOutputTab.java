/*     */ package net.minecraft.launcher.ui.tabs;
/*     */ 
/*     */ import com.mojang.launcher.events.GameOutputLogProcessor;
/*     */ import com.mojang.launcher.game.process.GameProcess;
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
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GameOutputTab
/*     */   extends JScrollPane
/*     */   implements GameOutputLogProcessor
/*     */ {
/*  30 */   private static final Font MONOSPACED = new Font("Monospaced", 0, 12);
/*     */   private static final int MAX_LINE_COUNT = 1000;
/*  32 */   private final JTextArea console = new JTextArea();
/*  33 */   private final JPopupMenu popupMenu = new JPopupMenu();
/*  34 */   private final JMenuItem copyTextButton = new JMenuItem("Copy All Text");
/*     */   
/*     */   private final Launcher minecraftLauncher;
/*     */   private boolean alreadyCensored = false;
/*     */   
/*     */   public GameOutputTab(Launcher minecraftLauncher) {
/*  40 */     this.minecraftLauncher = minecraftLauncher;
/*     */     
/*  42 */     this.popupMenu.add(this.copyTextButton);
/*  43 */     this.console.setComponentPopupMenu(this.popupMenu);
/*     */     
/*  45 */     this.copyTextButton.addActionListener(new ActionListener()
/*     */         {
/*     */           
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/*     */             try {
/*  51 */               StringSelection ss = new StringSelection(GameOutputTab.this.console.getText());
/*  52 */               Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
/*     */             }
/*  54 */             catch (Exception exception) {}
/*     */           }
/*     */         });
/*  57 */     this.console.setFont(MONOSPACED);
/*  58 */     this.console.setEditable(false);
/*  59 */     this.console.setMargin((Insets)null);
/*     */     
/*  61 */     setViewportView(this.console);
/*     */     
/*  63 */     this.console.getDocument().addDocumentListener(new DocumentListener()
/*     */         {
/*     */           public void insertUpdate(DocumentEvent e)
/*     */           {
/*  67 */             SwingUtilities.invokeLater(new Runnable()
/*     */                 {
/*     */                   public void run()
/*     */                   {
/*  71 */                     Document document = GameOutputTab.this.console.getDocument();
/*  72 */                     Element root = document.getDefaultRootElement();
/*  73 */                     while (root.getElementCount() > 1001) {
/*     */                       
/*     */                       try {
/*  76 */                         document.remove(0, root.getElement(0).getEndOffset());
/*     */                       }
/*  78 */                       catch (BadLocationException badLocationException) {}
/*     */                     } 
/*     */                   }
/*     */                 });
/*     */           }
/*     */ 
/*     */           
/*     */           public void removeUpdate(DocumentEvent e) {}
/*     */           
/*     */           public void changedUpdate(DocumentEvent e) {}
/*     */         });
/*     */   }
/*     */   
/*     */   public Launcher getMinecraftLauncher() {
/*  92 */     return this.minecraftLauncher;
/*     */   }
/*     */ 
/*     */   
/*     */   public void print(final String line) {
/*  97 */     if (!SwingUtilities.isEventDispatchThread()) {
/*     */       
/*  99 */       SwingUtilities.invokeLater(new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/* 103 */               GameOutputTab.this.print(line);
/*     */             }
/*     */           });
/*     */       return;
/*     */     } 
/* 108 */     Document document = this.console.getDocument();
/* 109 */     JScrollBar scrollBar = getVerticalScrollBar();
/* 110 */     boolean shouldScroll = false;
/* 111 */     if (getViewport().getView() == this.console) {
/* 112 */       shouldScroll = (scrollBar.getValue() + scrollBar.getSize().getHeight() + (MONOSPACED.getSize() * 4) > scrollBar.getMaximum());
/*     */     }
/*     */     
/*     */     try {
/* 116 */       document.insertString(document.getLength(), line, null);
/*     */     }
/* 118 */     catch (BadLocationException badLocationException) {}
/* 119 */     if (shouldScroll) {
/* 120 */       scrollBar.setValue(2147483647);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGameOutput(GameProcess process, String logLine) {
/* 126 */     if (!this.alreadyCensored) {
/*     */       
/* 128 */       int index = logLine.indexOf("(Session ID is");
/* 129 */       if (index > 0) {
/*     */         
/* 131 */         this.alreadyCensored = true;
/* 132 */         logLine = logLine.substring(0, index) + "(Session ID is <censored>)";
/*     */       } 
/*     */     } 
/* 135 */     print(logLine + "\n");
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\tabs\GameOutputTab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */