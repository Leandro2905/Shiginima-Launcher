/*    */ package net.minecraft.launcher.ui.bottombar;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.GridBagLayout;
/*    */ import javax.swing.JPanel;
/*    */ 
/*    */ 
/*    */ public abstract class SidebarGridForm
/*    */   extends JPanel
/*    */ {
/*    */   protected void createInterface() {
/* 13 */     GridBagLayout layout = new GridBagLayout();
/* 14 */     GridBagConstraints constraints = new GridBagConstraints();
/* 15 */     setLayout(layout);
/*    */     
/* 17 */     populateGrid(constraints);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract void populateGrid(GridBagConstraints paramGridBagConstraints);
/*    */   
/*    */   protected <T extends Component> T add(T component, GridBagConstraints constraints, int x, int y, int weight, int width) {
/* 24 */     return add(component, constraints, x, y, weight, width, 10);
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T extends Component> T add(T component, GridBagConstraints constraints, int x, int y, int weight, int width, int anchor) {
/* 29 */     constraints.gridx = x;
/* 30 */     constraints.gridy = y;
/* 31 */     constraints.weightx = weight;
/* 32 */     constraints.weighty = 1.0D;
/* 33 */     constraints.gridwidth = width;
/* 34 */     constraints.anchor = anchor;
/*    */     
/* 36 */     add((Component)component, constraints);
/* 37 */     return component;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\bottombar\SidebarGridForm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */