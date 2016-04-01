package com.liferay.extension.mvc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.liferay.extension.mvc.util.DTOConverterUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * @author Andre Fabbro
 */
@RunWith(MockitoJUnitRunner.class)
public class MVCPortletExtendedTest {

    private static final String PORTLET_PK = "00001";

    private static final String PORTLET_NAME = "myCoolPortlet";

    @Mock
    private ResourceRequest resourceRequest;

    @Mock
    private ResourceResponse resourceResponse;

    @Mock
    private ThemeDisplay themeDisplay;

    @Mock
    private PermissionChecker permissionChecker;

    @Mock
    private PortletDisplay portletDisplay;

    @InjectMocks
    private MyCoolPortlet myCoolPortlet = new MyCoolPortlet();

    private BufferedReader reader;

    private PipedInputStream pipeInput;

    @Before
    public void setUp() {

	MockitoAnnotations.initMocks(this);

	when(resourceResponse.getNamespace()).thenReturn(PORTLET_NAME);
    }

    @Test
    public void invokeSecuredAnnotatedMethodSuccess() throws Exception {

	when(permissionChecker.isOmniadmin()).thenReturn(false);
	when(permissionChecker.hasPermission(0, PORTLET_NAME, PORTLET_PK,
		"ADD_RESOURCE")).thenReturn(true);
	when(permissionChecker.hasPermission(0, PORTLET_NAME, PORTLET_PK,
		"EDIT_RESOURCE")).thenReturn(true);

	invokeCustomResourceAnnotatedMethodAjax("ajaxSecuredAnnotatedMethod");
    }

    @Test(expected = PortletException.class)
    public void invokeSecuredAnnotatedMethodFail() throws Exception {

	when(permissionChecker.isOmniadmin()).thenReturn(false);
	when(permissionChecker.hasPermission(0, PORTLET_NAME, PORTLET_PK,
		"ADD_RESOURCE")).thenReturn(true);
	when(permissionChecker.hasPermission(0, PORTLET_NAME, PORTLET_PK,
		"EDIT_RESOURCE")).thenReturn(false);

	invokeCustomResourceAnnotatedMethodAjax("ajaxSecuredAnnotatedMethod");
    }

    @Test
    public void dontConsiderOmniUserInPermissionChecker() throws Exception {

	when(permissionChecker.isOmniadmin()).thenReturn(true);
	when(permissionChecker.hasPermission(0, PORTLET_NAME, PORTLET_PK,
		"ADD_RESOURCE")).thenReturn(false);
	when(permissionChecker.hasPermission(0, PORTLET_NAME, PORTLET_PK,
		"EDIT_RESOURCE")).thenReturn(false);

	invokeCustomResourceAnnotatedMethodAjax("ajaxSecuredAnnotatedMethod");
    }

    @Test
    public void invokeCustomResourceMethodAjaxOne()
	    throws IOException, PortletException {

	invokeCustomResourceMethodAjax("ajaxMethodOne");
    }

    @Test
    public void invokeCustomResourceMethodAjaxTwo()
	    throws IOException, PortletException {

	invokeCustomResourceMethodAjax("ajaxMethodTwo");
    }

    @Test
    public void invokeCustomResourceAnnotatedMethodAjaxOne()
	    throws IOException, PortletException {

	invokeCustomResourceAnnotatedMethodAjax("ajaxAnnotatedMethodOne");
    }

    private void invokeCustomResourceMethodAjax(String methodName)
	    throws IOException, PortletException {

	commonLogic(methodName);

	assertEquals("executed " + methodName, reader.readLine());
    }

    private void invokeCustomResourceAnnotatedMethodAjax(String methodName)
	    throws IOException, PortletException {

	prepareReaderForRequest();
	prepareOutputStreamForResponse();
	commonLogic(methodName);

	assertEquals(DTOConverterUtil.buildCompanyJSON(), readResponse());
    }

    private void prepareWriterForResponse() throws IOException {

	PipedInputStream pipeInput = new PipedInputStream();
	reader = new BufferedReader(new InputStreamReader(pipeInput));
	BufferedOutputStream out = new BufferedOutputStream(
		new PipedOutputStream(pipeInput));

	when(resourceResponse.getWriter()).thenReturn(new PrintWriter(out));
    }

    private void prepareReaderForRequest() throws IOException {

	BufferedReader mockReader = mock(BufferedReader.class);
	when(mockReader.readLine())
		.thenReturn(DTOConverterUtil.buildCompanyJSON())
		.thenReturn(null);
	when(resourceRequest.getReader()).thenReturn(mockReader);

	when(portletDisplay.getPortletName()).thenReturn(PORTLET_NAME);
	when(portletDisplay.getResourcePK()).thenReturn(PORTLET_PK);

	when(themeDisplay.getPermissionChecker()).thenReturn(permissionChecker);
	when(themeDisplay.getScopeGroupId()).thenReturn(0l);
	when(themeDisplay.getPortletDisplay()).thenReturn(portletDisplay);

	when(resourceRequest.getAttribute(WebKeys.THEME_DISPLAY))
		.thenReturn(themeDisplay);
    }

    private void prepareOutputStreamForResponse() throws IOException {

	pipeInput = new PipedInputStream();
	OutputStream portletOutputStream = new PipedOutputStream(pipeInput);
	when(resourceResponse.getPortletOutputStream())
		.thenReturn(portletOutputStream);
    }

    private void commonLogic(String methodName)
	    throws IOException, PortletException {

	prepareWriterForResponse();
	when(resourceRequest.getParameter(ActionRequest.ACTION_NAME))
		.thenReturn(methodName);
	myCoolPortlet.serveResource(resourceRequest, resourceResponse);
    }

    private String readResponse() throws IOException {

	String result = "";
	byte[] buffer = new byte[1024];
	int read;
	while ((read = pipeInput.read(buffer)) != -1) {
	    result = result.concat(new String(buffer, 0, read));
	}
	return result;
    }

}
