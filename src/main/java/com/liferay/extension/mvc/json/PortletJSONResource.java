package com.liferay.extension.mvc.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for process JSON parameters received in the request body
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PortletJSONResource {

    Class attributeClass() default void.class;

    boolean jsonResponse() default true;

}
