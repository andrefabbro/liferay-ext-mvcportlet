# Liferay MVCPortlet Extension

This is a extension for default Liferay MVCPortlet which will include new features like routing to multiple resources methods and automatic bind of the request to Java Beans.

For now, there's only the feature for multiple ajax calls, like in MVCPortlet on Liferay 7.

## Installation

1. Checkout the code and install in your local repo with gradle:
```bash
gradle install
```
2. Put as a dependency in your portlet project, as the following:
```xml
<dependency>
	<groupId>com.liferay.portal.ext</groupId>
	<artifactId>mvcportlet-extension</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Use

Instead extend MVCPortlet, you should use *MVCPortletExtended*, then add as many resource methods you wish:

```java
public class MySuperCoolPortlet extends MVCPortletExtended {

	public void resourceMethodOne(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {
			
		// do something
	}

	public void resourceMethodTwo(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {

		// something else
	}
}
```

In your JSP, just add a parameter named *javax.portlet.action* with the value of the method name that you want to run, like follows: 

```jsp
<portlet:resourceURL var="methodOneURL">
	<portlet:param name="javax.portlet.action" value="resourceMethodOne" />
</portlet:resourceURL>

<portlet:resourceURL var="methodTwoURL">
	<portlet:param name="javax.portlet.action" value="resourceMethodTwo" />
</portlet:resourceURL>
```

## Portlet Example

You can find an example of that in the follow link: https://github.com/andrefabbro/liferay-ext-mvcportlet-example
