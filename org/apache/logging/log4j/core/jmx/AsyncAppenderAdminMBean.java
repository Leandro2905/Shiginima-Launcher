package org.apache.logging.log4j.core.jmx;

public interface AsyncAppenderAdminMBean {
  public static final String PATTERN = "org.apache.logging.log4j2:type=%s,component=AsyncAppenders,name=%s";
  
  String getName();
  
  String getLayout();
  
  boolean isIgnoreExceptions();
  
  String getErrorHandler();
  
  String getFilter();
  
  String[] getAppenderRefs();
  
  boolean isIncludeLocation();
  
  boolean isBlocking();
  
  String getErrorRef();
  
  int getQueueCapacity();
  
  int getQueueRemainingCapacity();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jmx\AsyncAppenderAdminMBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */