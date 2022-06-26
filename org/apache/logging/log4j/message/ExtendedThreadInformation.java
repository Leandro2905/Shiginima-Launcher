/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.lang.management.LockInfo;
/*     */ import java.lang.management.MonitorInfo;
/*     */ import java.lang.management.ThreadInfo;
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
/*     */ class ExtendedThreadInformation
/*     */   implements ThreadInformation
/*     */ {
/*     */   private final ThreadInfo info;
/*     */   
/*     */   public ExtendedThreadInformation(ThreadInfo thread) {
/*  33 */     this.info = thread;
/*     */   }
/*     */ 
/*     */   
/*     */   public void printThreadInfo(StringBuilder sb) {
/*  38 */     sb.append('"').append(this.info.getThreadName()).append('"');
/*  39 */     sb.append(" Id=").append(this.info.getThreadId()).append(' ');
/*  40 */     formatState(sb, this.info);
/*  41 */     if (this.info.isSuspended()) {
/*  42 */       sb.append(" (suspended)");
/*     */     }
/*  44 */     if (this.info.isInNative()) {
/*  45 */       sb.append(" (in native)");
/*     */     }
/*  47 */     sb.append('\n');
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStack(StringBuilder sb, StackTraceElement[] stack) {
/*  52 */     int i = 0;
/*  53 */     for (StackTraceElement element : stack) {
/*  54 */       sb.append("\tat ").append(element.toString());
/*  55 */       sb.append('\n');
/*  56 */       if (i == 0 && this.info.getLockInfo() != null) {
/*  57 */         Thread.State ts = this.info.getThreadState();
/*  58 */         switch (ts) {
/*     */           case BLOCKED:
/*  60 */             sb.append("\t-  blocked on ");
/*  61 */             formatLock(sb, this.info.getLockInfo());
/*  62 */             sb.append('\n');
/*     */             break;
/*     */           case WAITING:
/*  65 */             sb.append("\t-  waiting on ");
/*  66 */             formatLock(sb, this.info.getLockInfo());
/*  67 */             sb.append('\n');
/*     */             break;
/*     */           case TIMED_WAITING:
/*  70 */             sb.append("\t-  waiting on ");
/*  71 */             formatLock(sb, this.info.getLockInfo());
/*  72 */             sb.append('\n');
/*     */             break;
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/*  78 */       for (MonitorInfo mi : this.info.getLockedMonitors()) {
/*  79 */         if (mi.getLockedStackDepth() == i) {
/*  80 */           sb.append("\t-  locked ");
/*  81 */           formatLock(sb, mi);
/*  82 */           sb.append('\n');
/*     */         } 
/*     */       } 
/*  85 */       i++;
/*     */     } 
/*     */     
/*  88 */     LockInfo[] locks = this.info.getLockedSynchronizers();
/*  89 */     if (locks.length > 0) {
/*  90 */       sb.append("\n\tNumber of locked synchronizers = ").append(locks.length).append('\n');
/*  91 */       for (LockInfo li : locks) {
/*  92 */         sb.append("\t- ");
/*  93 */         formatLock(sb, li);
/*  94 */         sb.append('\n');
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void formatLock(StringBuilder sb, LockInfo lock) {
/* 100 */     sb.append('<').append(lock.getIdentityHashCode()).append("> (a ");
/* 101 */     sb.append(lock.getClassName()).append(')');
/*     */   } private void formatState(StringBuilder sb, ThreadInfo info) {
/*     */     StackTraceElement element;
/*     */     String className, method;
/* 105 */     Thread.State state = info.getThreadState();
/* 106 */     sb.append(state);
/* 107 */     switch (state) {
/*     */       case BLOCKED:
/* 109 */         sb.append(" (on object monitor owned by \"");
/* 110 */         sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId()).append(')');
/*     */         break;
/*     */       
/*     */       case WAITING:
/* 114 */         element = info.getStackTrace()[0];
/* 115 */         className = element.getClassName();
/* 116 */         method = element.getMethodName();
/* 117 */         if (className.equals("java.lang.Object") && method.equals("wait")) {
/* 118 */           sb.append(" (on object monitor");
/* 119 */           if (info.getLockOwnerName() != null) {
/* 120 */             sb.append(" owned by \"");
/* 121 */             sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
/*     */           } 
/* 123 */           sb.append(')'); break;
/* 124 */         }  if (className.equals("java.lang.Thread") && method.equals("join")) {
/* 125 */           sb.append(" (on completion of thread ").append(info.getLockOwnerId()).append(')'); break;
/*     */         } 
/* 127 */         sb.append(" (parking for lock");
/* 128 */         if (info.getLockOwnerName() != null) {
/* 129 */           sb.append(" owned by \"");
/* 130 */           sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
/*     */         } 
/* 132 */         sb.append(')');
/*     */         break;
/*     */ 
/*     */       
/*     */       case TIMED_WAITING:
/* 137 */         element = info.getStackTrace()[0];
/* 138 */         className = element.getClassName();
/* 139 */         method = element.getMethodName();
/* 140 */         if (className.equals("java.lang.Object") && method.equals("wait")) {
/* 141 */           sb.append(" (on object monitor");
/* 142 */           if (info.getLockOwnerName() != null) {
/* 143 */             sb.append(" owned by \"");
/* 144 */             sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
/*     */           } 
/* 146 */           sb.append(')'); break;
/* 147 */         }  if (className.equals("java.lang.Thread") && method.equals("sleep")) {
/* 148 */           sb.append(" (sleeping)"); break;
/* 149 */         }  if (className.equals("java.lang.Thread") && method.equals("join")) {
/* 150 */           sb.append(" (on completion of thread ").append(info.getLockOwnerId()).append(')'); break;
/*     */         } 
/* 152 */         sb.append(" (parking for lock");
/* 153 */         if (info.getLockOwnerName() != null) {
/* 154 */           sb.append(" owned by \"");
/* 155 */           sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
/*     */         } 
/* 157 */         sb.append(')');
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\message\ExtendedThreadInformation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */