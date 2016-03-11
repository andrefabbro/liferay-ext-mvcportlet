package com.liferay.extension.mvc;

import com.liferay.extension.mvc.util.DTOConverterUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.portlet.ActionRequest;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    private PipedInputStream pipeInput;

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

    private void prepareWriterForResponse()
            throws IOException {

        PipedInputStream pipeInput = new PipedInputStream();
        reader = new BufferedReader(new InputStreamReader(pipeInput));
        BufferedOutputStream out = new BufferedOutputStream(new PipedOutputStream(pipeInput));

        when(resourceResponse.getWriter()).thenReturn(new PrintWriter(out));
    }

    private void prepareReaderForRequest()
            throws IOException {

        BufferedReader mockReader = mock(BufferedReader.class);
        when(mockReader.readLine()).thenReturn(DTOConverterUtil.buildCompanyJSON()).thenReturn(null);
        when(resourceRequest.getReader()).thenReturn(mockReader);
    }

    private void prepareOutputStreamForResponse()
            throws IOException {

        pipeInput = new PipedInputStream();
        OutputStream portletOutputStream = new PipedOutputStream(pipeInput);
        when(resourceResponse.getPortletOutputStream()).thenReturn(portletOutputStream);
    }

    private void commonLogic(String methodName)
            throws IOException, PortletException {

        prepareWriterForResponse();
        when(resourceRequest.getParameter(ActionRequest.ACTION_NAME)).thenReturn(methodName);
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
