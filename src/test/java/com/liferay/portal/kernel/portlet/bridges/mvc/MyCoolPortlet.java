/**
 * 
 */

package com.liferay.portal.kernel.portlet.bridges.mvc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

/**
 * @author andrefabbro
 */
public class MyCoolPortlet extends MVCPortletExtended {

	public void ajaxMethodOne(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IOException, PortletException {

		PrintWriter writter = resourceResponse
			.getWriter();

		writter
			.write("executed ajaxMethodOne");
		writter
			.close();

	}

	public void ajaxMethodTwo(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IOException, PortletException {

		PrintWriter writter = resourceResponse
			.getWriter();

		writter
			.write("executed ajaxMethodTwo");
		writter
			.close();
	}

}
