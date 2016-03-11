/**
 * 
 */

package com.liferay.extension.mvc;

import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author andrefabbro
 */
public class MVCRenderConstantsExt {

	public static final String MVC_PATH_REQUEST_ATTRIBUTE_NAME =
		MVCPortlet.class
			.getName() + "#MVC_PATH";

	public static final String MVC_PATH_VALUE_SKIP_DISPATCH =
		"com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderConstants#MVC_PATH_SKIP_DISPATCH";

	public static final String PORTLET_CONTEXT_OVERRIDE_REQUEST_ATTIBUTE_NAME_PREFIX =
		"portlet.context.override.";

}
