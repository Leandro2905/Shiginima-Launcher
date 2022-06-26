package joptsimple;

import java.util.Collection;
import java.util.List;

public interface OptionDescriptor {
  Collection<String> options();
  
  String description();
  
  List<?> defaultValues();
  
  boolean isRequired();
  
  boolean acceptsArguments();
  
  boolean requiresArgument();
  
  String argumentDescription();
  
  String argumentTypeIndicator();
  
  boolean representsNonOptions();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\OptionDescriptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */