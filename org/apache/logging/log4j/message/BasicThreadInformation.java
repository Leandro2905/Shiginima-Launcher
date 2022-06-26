/*     */ package org.apache.logging.log4j.message;
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
/*     */ class BasicThreadInformation
/*     */   implements ThreadInformation
/*     */ {
/*     */   private static final int HASH_SHIFT = 32;
/*     */   private static final int HASH_MULTIPLIER = 31;
/*     */   private final long id;
/*     */   private final String name;
/*     */   private final String longName;
/*     */   private final Thread.State state;
/*     */   private final int priority;
/*     */   private final boolean isAlive;
/*     */   private final boolean isDaemon;
/*     */   private final String threadGroupName;
/*     */   
/*     */   public BasicThreadInformation(Thread thread) {
/*  39 */     this.id = thread.getId();
/*  40 */     this.name = thread.getName();
/*  41 */     this.longName = thread.toString();
/*  42 */     this.state = thread.getState();
/*  43 */     this.priority = thread.getPriority();
/*  44 */     this.isAlive = thread.isAlive();
/*  45 */     this.isDaemon = thread.isDaemon();
/*  46 */     ThreadGroup group = thread.getThreadGroup();
/*  47 */     this.threadGroupName = (group == null) ? null : group.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  52 */     if (this == o) {
/*  53 */       return true;
/*     */     }
/*  55 */     if (o == null || getClass() != o.getClass()) {
/*  56 */       return false;
/*     */     }
/*     */     
/*  59 */     BasicThreadInformation that = (BasicThreadInformation)o;
/*     */     
/*  61 */     if (this.id != that.id) {
/*  62 */       return false;
/*     */     }
/*  64 */     if ((this.name != null) ? !this.name.equals(that.name) : (that.name != null)) {
/*  65 */       return false;
/*     */     }
/*     */     
/*  68 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  73 */     int result = (int)(this.id ^ this.id >>> 32L);
/*  74 */     result = 31 * result + ((this.name != null) ? this.name.hashCode() : 0);
/*  75 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printThreadInfo(StringBuilder sb) {
/*  84 */     sb.append('"').append(this.name).append("\" ");
/*  85 */     if (this.isDaemon) {
/*  86 */       sb.append("daemon ");
/*     */     }
/*  88 */     sb.append("prio=").append(this.priority).append(" tid=").append(this.id).append(' ');
/*  89 */     if (this.threadGroupName != null) {
/*  90 */       sb.append("group=\"").append(this.threadGroupName).append('"');
/*     */     }
/*  92 */     sb.append('\n');
/*  93 */     sb.append("\tThread state: ").append(this.state.name()).append('\n');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printStack(StringBuilder sb, StackTraceElement[] trace) {
/* 103 */     for (StackTraceElement element : trace)
/* 104 */       sb.append("\tat ").append(element).append('\n'); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\message\BasicThreadInformation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */