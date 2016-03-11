# Liferay MVCPortlet Extension

This is a extension for default Liferay `MVCPortlet` which will include new features like routing to multiple resources methods and automatic bind of the request to Java Beans.

For now, there's only the feature for multiple ajax calls, like in `MVCPortlet` on Liferay 7.

## Installation

1. Checkout the code and install in your local repo with gradle:
```bash
gradle install
```
2. Put as a dependency in your portlet project, as the following for Maven:
```xml
<dependency>
	<groupId>com.liferay.portal.ext</groupId>
	<artifactId>mvcportlet-extension</artifactId>
	<version>0.0.3-SNAPSHOT</version>
</dependency>
```
or as the following for Gradle:
```
compile 'com.liferay.portal.ext:mvcportlet-extension:0.0.3-SNAPSHOT'
```

## Use

### Java

Instead extend MVCPortlet, you should use `MVCPortletExtended`, then add as many resource methods you wish:

#### Classic

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

#### JSON prepared way

You can annotate your methods with `@PortletJSONResource` specifying the class of the parameter that you want to receive.

This parameter should be send in the body request in JSON format and this extension will convert it the specified class.

This annotation also has an option for parametrize the response. You can use the attribute `jsonResponse` for specify if the response will be in JSON format to. Its default is true.

```java
public class MySuperCoolPortlet extends MVCPortletExtended {

    @PortletJSONResource(attributeClass = MyDTO.class)
    public MyResponseDTO resourceMethodOne(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse,
			MyDTO myDTO) throws IOException,
			PortletException {

		// do something

		return new MyResponseDTO(); // This will be written in the response as JSON
	}

    @PortletJSONResource(attributeClass = MyOtherDTO.class, jsonResponse = false)
    public void resourceMethodTwo(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse,
			MyOtherDTO myOtherDTO) throws IOException,
			PortletException {

		// something else

		return new MyResponseDTO(); // This will not be written in any place
	}
}
```

### JSP

In your JSP, just add a parameter named `javax.portlet.action` with the value of the method name that you want to run, like follows:

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
