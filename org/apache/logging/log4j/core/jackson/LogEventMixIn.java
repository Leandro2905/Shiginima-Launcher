package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.Message;

@JsonRootName("Event")
@JacksonXmlRootElement(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Event")
@JsonFilter("org.apache.logging.log4j.core.impl.Log4jLogEvent")
@JsonPropertyOrder({"timeMillis", "threadName", "level", "loggerName", "marker", "message", "thrown", "ContextMap", "contextStack", "loggerFQCN", "Source", "endOfBatch"})
abstract class LogEventMixIn implements LogEvent {
  private static final long serialVersionUID = 1L;
  
  @JsonProperty("contextMap")
  @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "ContextMap")
  @JsonSerialize(using = ListOfMapEntrySerializer.class)
  @JsonDeserialize(using = ListOfMapEntryDeserializer.class)
  public abstract Map<String, String> getContextMap();
  
  @JsonProperty("contextStack")
  @JacksonXmlElementWrapper(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "ContextStack")
  @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "ContextStackItem")
  public abstract ThreadContext.ContextStack getContextStack();
  
  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  public abstract Level getLevel();
  
  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  public abstract String getLoggerFqcn();
  
  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  public abstract String getLoggerName();
  
  @JsonProperty("marker")
  @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Marker")
  public abstract Marker getMarker();
  
  @JsonProperty("message")
  @JsonSerialize(using = MessageSerializer.class)
  @JsonDeserialize(using = SimpleMessageDeserializer.class)
  @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Message")
  public abstract Message getMessage();
  
  @JsonProperty("source")
  @JsonDeserialize(using = Log4jStackTraceElementDeserializer.class)
  @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Source")
  public abstract StackTraceElement getSource();
  
  @JsonProperty("thread")
  @JacksonXmlProperty(isAttribute = true, localName = "thread")
  public abstract String getThreadName();
  
  @JsonIgnore
  public abstract Throwable getThrown();
  
  @JsonProperty("thrown")
  @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Thrown")
  public abstract ThrowableProxy getThrownProxy();
  
  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  public abstract long getTimeMillis();
  
  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  public abstract boolean isEndOfBatch();
  
  @JsonIgnore
  public abstract boolean isIncludeLocation();
  
  public abstract void setEndOfBatch(boolean paramBoolean);
  
  public abstract void setIncludeLocation(boolean paramBoolean);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jackson\LogEventMixIn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */