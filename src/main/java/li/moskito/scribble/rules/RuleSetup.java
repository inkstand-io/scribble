package li.moskito.scribble.rules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This source-only annotation marks a method to be used for setting up a rule and is not intended to be used as public
 * API. The annotation provides a guide for users of the scribble rule API.
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface RuleSetup {

    /**
     * Indicates a requirement level of the setup method.
     * 
     * @return
     */
    RequirementLevel value() default RequirementLevel.OPTIONAL;

    /**
     * Indicates the
     *
     * @author Gerald Muecke, gerald@moskito.li
     */
    public static enum RequirementLevel {
        /**
         * The setup method is optional
         */
        OPTIONAL,
        /**
         * The setup method is required
         */
        REQUIRED, ;
    }
}
