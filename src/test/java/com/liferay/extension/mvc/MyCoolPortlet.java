/**
 * 
 */

package com.liferay.extension.mvc;

import com.liferay.extension.mvc.json.PortletJSONResource;
import com.liferay.extension.mvc.util.DTOConverterUtil;
import com.liferay.extension.mvc.util.PersonDTO;
import com.liferay.extension.mvc.util.CompanyDTO;

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

	@PortletJSONResource(attributeClass = PersonDTO.class)
	public CompanyDTO ajaxAnnotatedMethodOne(ResourceRequest request, ResourceResponse response, PersonDTO dto) {
		return DTOConverterUtil.buildCompanyDTO();
	}

}
