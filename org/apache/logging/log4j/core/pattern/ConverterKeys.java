package org.apache.logging.log4j.core.pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ConverterKeys {
  String[] value();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\ConverterKeys.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */