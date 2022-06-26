/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.IllegalFormatException;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ public class MessageFormatMessage
/*     */   implements Message
/*     */ {
/*  34 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final long serialVersionUID = -665975803997290697L;
/*     */   
/*     */   private static final int HASHVAL = 31;
/*     */   
/*     */   private String messagePattern;
/*     */   private transient Object[] argArray;
/*     */   private String[] stringArgs;
/*     */   private transient String formattedMessage;
/*     */   private transient Throwable throwable;
/*     */   
/*     */   public MessageFormatMessage(String messagePattern, Object... arguments) {
/*  47 */     this.messagePattern = messagePattern;
/*  48 */     this.argArray = arguments;
/*  49 */     if (arguments != null && arguments.length > 0 && arguments[arguments.length - 1] instanceof Throwable) {
/*  50 */       this.throwable = (Throwable)arguments[arguments.length - 1];
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  60 */     if (this.formattedMessage == null) {
/*  61 */       this.formattedMessage = formatMessage(this.messagePattern, this.argArray);
/*     */     }
/*  63 */     return this.formattedMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  72 */     return this.messagePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/*  81 */     if (this.argArray != null) {
/*  82 */       return this.argArray;
/*     */     }
/*  84 */     return (Object[])this.stringArgs;
/*     */   }
/*     */   
/*     */   protected String formatMessage(String msgPattern, Object... args) {
/*     */     try {
/*  89 */       return MessageFormat.format(msgPattern, args);
/*  90 */     } catch (IllegalFormatException ife) {
/*  91 */       LOGGER.error("Unable to format msg: " + msgPattern, ife);
/*  92 */       return msgPattern;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  98 */     if (this == o) {
/*  99 */       return true;
/*     */     }
/* 101 */     if (o == null || getClass() != o.getClass()) {
/* 102 */       return false;
/*     */     }
/*     */     
/* 105 */     MessageFormatMessage that = (MessageFormatMessage)o;
/*     */     
/* 107 */     if ((this.messagePattern != null) ? !this.messagePattern.equals(that.messagePattern) : (that.messagePattern != null)) {
/* 108 */       return false;
/*     */     }
/* 110 */     if (!Arrays.equals((Object[])this.stringArgs, (Object[])that.stringArgs)) {
/* 111 */       return false;
/*     */     }
/*     */     
/* 114 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 119 */     int result = (this.messagePattern != null) ? this.messagePattern.hashCode() : 0;
/* 120 */     result = 31 * result + ((this.stringArgs != null) ? Arrays.hashCode((Object[])this.stringArgs) : 0);
/* 121 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 127 */     return "StringFormatMessage[messagePattern=" + this.messagePattern + ", args=" + Arrays.toString(this.argArray) + ']';
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 132 */     out.defaultWriteObject();
/* 133 */     getFormattedMessage();
/* 134 */     out.writeUTF(this.formattedMessage);
/* 135 */     out.writeUTF(this.messagePattern);
/* 136 */     out.writeInt(this.argArray.length);
/* 137 */     this.stringArgs = new String[this.argArray.length];
/* 138 */     int i = 0;
/* 139 */     for (Object obj : this.argArray) {
/* 140 */       this.stringArgs[i] = obj.toString();
/* 141 */       i++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 146 */     in.defaultReadObject();
/* 147 */     this.formattedMessage = in.readUTF();
/* 148 */     this.messagePattern = in.readUTF();
/* 149 */     int length = in.readInt();
/* 150 */     this.stringArgs = new String[length];
/* 151 */     for (int i = 0; i < length; i++) {
/* 152 */       this.stringArgs[i] = in.readUTF();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 163 */     return this.throwable;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\message\MessageFormatMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */