package li.moskito.scribble.rules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This source-only annotation marks a method to be used for setting up a rule and is not intended to be used as public
 * API.
 * 
 * @author Gerald Muecke, gerald@moskito.li
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface RuleSetup {

}
