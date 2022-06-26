/*     */ package net.minecraft.launcher.ui.tabs.website;
/*     */ 
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.net.URI;
/*     */ import javafx.application.Platform;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.concurrent.Worker;
/*     */ import javafx.embed.swing.JFXPanel;
/*     */ import javafx.scene.Group;
/*     */ import javafx.scene.Scene;
/*     */ import javafx.scene.web.WebEngine;
/*     */ import javafx.scene.web.WebView;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.events.Event;
/*     */ import org.w3c.dom.events.EventListener;
/*     */ import org.w3c.dom.events.EventTarget;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JFXBrowser
/*     */   implements Browser
/*     */ {
/*  37 */   private static final Logger LOGGER = LogManager.getLogger();
/*  38 */   private final Object lock = new Object();
/*  39 */   private final JFXPanel fxPanel = new JFXPanel();
/*     */   private String urlToBrowseTo;
/*     */   private Dimension size;
/*     */   private WebView browser;
/*     */   private WebEngine webEngine;
/*     */   
/*     */   public JFXBrowser() {
/*  46 */     Platform.runLater(new Runnable() {
/*     */           public void run() {
/*  48 */             Group root = new Group();
/*  49 */             Scene scene = new Scene(root);
/*     */             
/*  51 */             JFXBrowser.this.fxPanel.setScene(scene);
/*  52 */             synchronized (JFXBrowser.this.lock) {
/*  53 */               JFXBrowser.this.browser = new WebView();
/*  54 */               JFXBrowser.this.browser.setContextMenuEnabled(false);
/*  55 */               if (JFXBrowser.this.size != null) {
/*  56 */                 JFXBrowser.this.resize(JFXBrowser.this.size);
/*     */               }
/*  58 */               JFXBrowser.this.webEngine = JFXBrowser.this.browser.getEngine();
/*  59 */               JFXBrowser.this.webEngine.setJavaScriptEnabled(false);
/*  60 */               JFXBrowser.this.webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
/*     */                     public void changed(ObservableValue observable, Object oldValue, Object newState) {
/*  62 */                       if (newState == Worker.State.SUCCEEDED) {
/*  63 */                         EventListener listener = new EventListener() {
/*     */                             public void handleEvent(Event event) {
/*  65 */                               if (event.getTarget() instanceof Element) {
/*  66 */                                 Element element = (Element)event.getTarget();
/*  67 */                                 String href = element.getAttribute("href");
/*  68 */                                 while (StringUtils.isEmpty(href) && element.getParentNode() instanceof Element) {
/*  69 */                                   element = (Element)element.getParentNode();
/*  70 */                                   href = element.getAttribute("href");
/*     */                                 } 
/*  72 */                                 if (href != null && href.length() > 0) {
/*     */                                   try {
/*  74 */                                     OperatingSystem.openLink(new URI(href));
/*  75 */                                   } catch (Exception e) {
/*  76 */                                     JFXBrowser.LOGGER.error("Unexpected exception opening link " + href, e);
/*     */                                   } 
/*  78 */                                   event.preventDefault();
/*  79 */                                   event.stopPropagation();
/*     */                                 } 
/*     */                               } 
/*     */                             }
/*     */                           };
/*  84 */                         Document doc = JFXBrowser.this.webEngine.getDocument();
/*  85 */                         if (doc != null) {
/*  86 */                           NodeList elements = doc.getElementsByTagName("a");
/*  87 */                           for (int i = 0; i < elements.getLength(); i++) {
/*  88 */                             Node item = elements.item(i);
/*  89 */                             if (item instanceof EventTarget) {
/*  90 */                               ((EventTarget)item).addEventListener("click", listener, false);
/*     */                             }
/*     */                           } 
/*     */                         } 
/*     */                       } 
/*     */                     }
/*     */                   });
/*  97 */               if (JFXBrowser.this.urlToBrowseTo != null) {
/*  98 */                 JFXBrowser.this.loadUrl(JFXBrowser.this.urlToBrowseTo);
/*     */               }
/*     */             } 
/* 101 */             root.getChildren().add(JFXBrowser.this.browser);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void loadUrl(final String url) {
/* 107 */     synchronized (this.lock) {
/* 108 */       this.urlToBrowseTo = url;
/* 109 */       if (this.webEngine != null) {
/* 110 */         Platform.runLater(new Runnable() {
/*     */               public void run() {
/* 112 */                 JFXBrowser.this.webEngine.load(url);
/*     */               }
/*     */             });
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public Component getComponent() {
/* 120 */     return this.fxPanel;
/*     */   }
/*     */   
/*     */   public void resize(Dimension size) {
/* 124 */     synchronized (this.lock) {
/* 125 */       this.size = size;
/* 126 */       if (this.browser != null) {
/* 127 */         this.browser.setMinSize(size.getWidth(), size.getHeight());
/* 128 */         this.browser.setMaxSize(size.getWidth(), size.getHeight());
/* 129 */         this.browser.setPrefSize(size.getWidth(), size.getHeight());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\tabs\website\JFXBrowser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */