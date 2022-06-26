package org.apache.logging.log4j.core.config.plugins.util;

public interface TypeConverter<T> {
  T convert(String paramString) throws Exception;
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugin\\util\TypeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */