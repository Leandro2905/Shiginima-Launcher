package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.apache.logging.log4j.core.impl.ExtendedStackTraceElement;
import org.apache.logging.log4j.core.impl.ThrowableProxy;

abstract class ThrowableProxyMixIn {
  @JsonProperty("cause")
  @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Cause")
  private ThrowableProxyMixIn causeProxy;
  
  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  private int commonElementCount;
  
  @JsonProperty("extendedStackTrace")
  @JacksonXmlElementWrapper(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "ExtendedStackTrace")
  @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "ExtendedStackTraceItem")
  private ExtendedStackTraceElement[] extendedStackTrace;
  
  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  private String localizedMessage;
  
  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  private String message;
  
  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  private String name;
  
  @JsonIgnore
  private transient Throwable throwable;
  
  @JsonIgnore
  public abstract String getCauseStackTraceAsString();
  
  @JsonIgnore
  public abstract String getExtendedStackTraceAsString();
  
  @JsonIgnore
  public abstract StackTraceElement[] getStackTrace();
  
  @JsonProperty("suppressed")
  @JacksonXmlElementWrapper(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Suppressed")
  @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "SuppressedItem")
  public abstract ThrowableProxy[] getSuppressedProxies();
  
  @JsonIgnore
  public abstract String getSuppressedStackTrace();
  
  @JsonIgnore
  public abstract Throwable getThrowable();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jackson\ThrowableProxyMixIn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */