/**
 * 
 */

package com.liferay.portal.kernel.portlet.bridges.mvc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

/**
 * @author Andre Fabbro
 */
@RunWith(MockitoJUnitRunner.class)
public class MVCPortletExtendedTest {

	@Mock
	private ResourceRequest resourceRequest;

	@Mock
	private ResourceResponse resourceResponse;

	@InjectMocks
	private MyCoolPortlet myCoolPortlet = new MyCoolPortlet();

	private BufferedReader reader;

	@Before
	public void setUp() {

		MockitoAnnotations
			.initMocks(this);

		when(resourceResponse
			.getNamespace())
				.thenReturn("myCoolPortlet");
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

	private void prepareWritterForResponse()
		throws IOException {

		PipedInputStream pipeInput = new PipedInputStream();
		reader = new BufferedReader(new InputStreamReader(pipeInput));
		BufferedOutputStream out =
			new BufferedOutputStream(new PipedOutputStream(pipeInput));
		when(resourceResponse
			.getWriter())
				.thenReturn(new PrintWriter(out));
	}

	private void invokeCustomResourceMethodAjax(String methodName)
		throws IOException, PortletException {

		prepareWritterForResponse();

		when(resourceRequest
			.getParameter(ActionRequest.ACTION_NAME))
				.thenReturn(methodName);

		myCoolPortlet
			.serveResource(resourceRequest, resourceResponse);

		assertEquals("executed " + methodName, reader
			.readLine());
	}

}
