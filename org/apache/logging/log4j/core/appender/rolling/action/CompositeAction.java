/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ public class CompositeAction
/*     */   extends AbstractAction
/*     */ {
/*     */   private final Action[] actions;
/*     */   private final boolean stopOnError;
/*     */   
/*     */   public CompositeAction(List<Action> actions, boolean stopOnError) {
/*  46 */     this.actions = new Action[actions.size()];
/*  47 */     actions.toArray(this.actions);
/*  48 */     this.stopOnError = stopOnError;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/*  57 */       execute();
/*  58 */     } catch (IOException ex) {
/*  59 */       LOGGER.warn("Exception during file rollover.", ex);
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
/*     */   public boolean execute() throws IOException {
/*  71 */     if (this.stopOnError) {
/*  72 */       for (Action action : this.actions) {
/*  73 */         if (!action.execute()) {
/*  74 */           return false;
/*     */         }
/*     */       } 
/*     */       
/*  78 */       return true;
/*     */     } 
/*  80 */     boolean status = true;
/*  81 */     IOException exception = null;
/*     */     
/*  83 */     for (Action action : this.actions) {
/*     */       try {
/*  85 */         status &= action.execute();
/*  86 */       } catch (IOException ex) {
/*  87 */         status = false;
/*     */         
/*  89 */         if (exception == null) {
/*  90 */           exception = ex;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     if (exception != null) {
/*  96 */       throw exception;
/*     */     }
/*     */     
/*  99 */     return status;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     return CompositeAction.class.getSimpleName() + Arrays.toString((Object[])this.actions);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\rolling\action\CompositeAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */