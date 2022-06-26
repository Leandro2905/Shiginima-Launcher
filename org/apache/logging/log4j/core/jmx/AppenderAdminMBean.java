package org.apache.logging.log4j.core.jmx;

public interface AppenderAdminMBean {
  public static final String PATTERN = "org.apache.logging.log4j2:type=%s,component=Appenders,name=%s";
  
  String getName();
  
  String getLayout();
  
  boolean isIgnoreExceptions();
  
  String getErrorHandler();
  
  String getFilter();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jmx\AppenderAdminMBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */