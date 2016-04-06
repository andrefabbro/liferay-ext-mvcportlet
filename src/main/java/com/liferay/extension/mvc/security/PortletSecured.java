package com.liferay.extension.mvc.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation define a list of security permissions
 * <p>
 * Example:
 * 
 * <pre>
 * &#064;Secured({ &quot;ADD_RESOURCE&quot; })
 * public void create(Contact contact);
 * 
 * &#064;Secured({ &quot;ADD_RESOURCE&quot;, &quot;EDIT_RESOURCE&quot; })
 * public void update(Contact contact);
 * 
 * </pre>
 * 
 * @author André Fabbro
 */
@Target({
    ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PortletSecured {

    /**
     * Returns the list of security permissions (e.g. ADD_RESOURCE,
     * EDIT_RESOURCE).
     *
     * @return String[] The secure permissions
     */
    public String[] value();

}
