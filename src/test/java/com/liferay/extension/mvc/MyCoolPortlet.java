package com.liferay.extension.mvc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.liferay.extension.mvc.json.PortletJSONResource;
import com.liferay.extension.mvc.security.PortletSecured;
import com.liferay.extension.mvc.util.CompanyDTO;
import com.liferay.extension.mvc.util.DTOConverterUtil;
import com.liferay.extension.mvc.util.PersonDTO;

/**
 * @author andrefabbro
 */
public class MyCoolPortlet extends MVCPortletExtended {

    public void ajaxMethodOne(
        ResourceRequest resourceRequest, ResourceResponse resourceResponse)
        throws IOException, PortletException {

        PrintWriter writter = resourceResponse.getWriter();

        writter.write("executed ajaxMethodOne");
        writter.close();

    }

    public void ajaxMethodTwo(
        ResourceRequest resourceRequest, ResourceResponse resourceResponse)
        throws IOException, PortletException {

        PrintWriter writter = resourceResponse.getWriter();

        writter.write("executed ajaxMethodTwo");
        writter.close();
    }

    @PortletJSONResource(attributeClass = PersonDTO.class)
    public CompanyDTO ajaxAnnotatedMethodOne(
        ResourceRequest request, ResourceResponse response, PersonDTO dto) {

        return DTOConverterUtil.buildCompanyDTO();
    }

    @PortletSecured({
        "ADD_RESOURCE", "EDIT_RESOURCE"
    })
    @PortletJSONResource(attributeClass = PersonDTO.class)
    public CompanyDTO ajaxSecuredAnnotatedMethod(
        ResourceRequest request, ResourceResponse response, PersonDTO dto) {

        return DTOConverterUtil.buildCompanyDTO();
    }
    
    @PortletJSONResource(attributeClass = PersonDTO.class)
    public void ajaxAnnotatedMethodOneThatReturnsVoid(
            ResourceRequest request, ResourceResponse response, PersonDTO dto)
            throws IOException, PortletException{
        
        if(dto == null) 
            System.out.println("DTO is NULL...!!!!!!!!!!!!!!");
        
        PrintWriter writter = response.getWriter();

        writter.write("executed ajaxAnnotatedMethodOneThatReturnsVoid");
        writter.close();
    }

}
