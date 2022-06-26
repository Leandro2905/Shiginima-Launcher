package org.apache.logging.log4j.core.config.plugins;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.logging.log4j.core.config.plugins.visitors.PluginElementVisitor;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@PluginVisitorStrategy(PluginElementVisitor.class)
public @interface PluginElement {
  String value();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugins\PluginElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */