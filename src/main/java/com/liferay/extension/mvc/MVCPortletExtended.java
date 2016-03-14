/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.extension.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.liferay.extension.mvc.json.PortletJSONResource;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

import javax.portlet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Andre Fabbro
 */
public class MVCPortletExtended extends MVCPortlet {

	protected static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	@Override
	public void serveResource(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IOException, PortletException {

		invokeHideDefaultSuccessMessage(resourceRequest);

		String path = getPath(resourceRequest, resourceResponse);

		if (path != null) {
			include(
				path, resourceRequest, resourceResponse,
				PortletRequest.RESOURCE_PHASE);
		}

		invokeResourceExtStack(resourceRequest, resourceResponse);
	}

	protected void invokeResourceExtStack(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IOException, PortletException {

		if (!isProcessResourceRequest(resourceRequest)) {
			return;
		}

		if (!callResourceMethod(resourceRequest, resourceResponse)) {
			return;
		}

		if (!SessionErrors
			.isEmpty(resourceRequest)) {
			return;
		}

		if (!SessionMessages
			.isEmpty(resourceRequest)) {
			return;
		}

		// code from default GenericPortlet Impl
		if (resourceRequest
			.getResourceID() != null) {
			PortletRequestDispatcher rd = getPortletConfig()
				.getPortletContext().getRequestDispatcher(resourceRequest
					.getResourceID());
			if (rd != null)
				rd
					.forward(resourceRequest, resourceResponse);
		}
	}

	protected void invokeHideDefaultSuccessMessage(
		PortletRequest portletRequest) {

		boolean hideDefaultSuccessMessage = ParamUtil
			.getBoolean(portletRequest, "hideDefaultSuccessMessage");

		if (hideDefaultSuccessMessage) {
			hideDefaultSuccessMessage(portletRequest);
		}
	}

	protected void hideDefaultSuccessMessage(PortletRequest portletRequest) {

		SessionMessages
			.add(portletRequest, PortalUtil
				.getPortletId(portletRequest) +
				SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
	}

	protected String getPath(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		String mvcPath = portletRequest
			.getParameter("mvcPath");

		if (mvcPath == null) {
			mvcPath = (String) portletRequest
				.getAttribute(getMVCPathAttributeName(portletResponse
					.getNamespace()));
		}

		// Check deprecated parameter

		if (mvcPath == null) {
			mvcPath = portletRequest
				.getParameter("jspPage");
		}

		return mvcPath;
	}

	protected String getMVCPathAttributeName(String namespace) {

		return namespace
			.concat(StringPool.PERIOD).concat(
				MVCRenderConstantsExt.MVC_PATH_REQUEST_ATTRIBUTE_NAME);
	}

	protected boolean callResourceMethod(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws PortletException {

		String actionName = ParamUtil
			.getString(resourceRequest, ActionRequest.ACTION_NAME);

		if (Validator
			.isNull(actionName) || actionName
				.equals("callResourceMethod") ||
			actionName
				.equals("serveResource")) {

			return false;
		}

		try {
			Method method = getResourceMethod(actionName);

			invokeMethod(resourceRequest, resourceResponse, method);

			return true;
		}
		catch (NoSuchMethodException nsme) {
			try {
				super.serveResource(resourceRequest, resourceResponse);

				return true;
			}
			catch (Exception e) {
				throw new PortletException(e);
			}
		}
		catch (InvocationTargetException ite) {
			Throwable cause = ite
				.getCause();

			if (cause != null) {
				throw new PortletException(cause);
			}
			else {
				throw new PortletException(ite);
			}
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

	private void invokeMethod(ResourceRequest resourceRequest, ResourceResponse resourceResponse, Method method)
			throws IllegalAccessException, InvocationTargetException, IOException {

		if (!method.isAnnotationPresent(PortletJSONResource.class)) {
			method.invoke(this, resourceRequest, resourceResponse);
			return;
		}

		PortletJSONResource annotation = method.getAnnotation(PortletJSONResource.class);
		Class attributeClass = annotation.attributeClass();

		Object invoke = method.invoke(this, resourceRequest, resourceResponse,
				readObjectFromBody(resourceRequest, attributeClass));

		if (annotation.jsonResponse()) {
			jsonResponse(resourceResponse, invoke);
		}
	}

	protected Method getResourceMethod(String actionName)
			throws NoSuchMethodException {

		Method result = _resourceMethods.get(actionName);

		if (result != null) {
			return result;
		}

		Class<?> clazz = getClass();

		result = getMethodByActionName(actionName, clazz);

		_resourceMethods.put(actionName, result);

		return result;
	}

	private Method getMethodByActionName(String actionName, Class<?> clazz)
			throws NoSuchMethodException {
		try {
			return clazz.getMethod(actionName, ResourceRequest.class, ResourceResponse.class);
		} catch (NoSuchMethodException e) {
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if (!method.getName().equals(actionName) || !method.isAnnotationPresent(PortletJSONResource.class)) {
					continue;
				}
				PortletJSONResource annotation = method.getAnnotation(PortletJSONResource.class);
				return clazz.getMethod(actionName, ResourceRequest.class, ResourceResponse.class, annotation.attributeClass());
			}
			throw e;
		}
	}

	private <T> T readObjectFromBody(ResourceRequest request, Class<T> clazz)  {
		StringBuilder buffer = new StringBuilder();
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			return null;
		}
		return new Gson().fromJson(buffer.toString(), clazz);
	}

	private void jsonResponse(ResourceResponse response, Object object) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		JSON_MAPPER.writeValue(response.getPortletOutputStream(), object);
	}

	private final Map<String, Method> _resourceMethods =
		new ConcurrentHashMap<>();

}