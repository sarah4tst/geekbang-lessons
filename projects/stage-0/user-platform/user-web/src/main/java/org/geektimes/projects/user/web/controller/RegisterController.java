package org.geektimes.projects.user.web.controller;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.apache.commons.lang.StringUtils;
import org.geektimes.context.ComponentContext;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserService;
import javax.validation.ValidationException;
import org.geektimes.web.mvc.controller.PageController;

/** 输出 “Hello,World” Controller */
@Path("/register")
public class RegisterController implements PageController, DynamicMBean {
  private UserService userService = ComponentContext.getInstance().getComponent("bean/UserService");

  private static final String DEFAULT_RESP_HEADER_NAME = "X-CUSTOM-TAG";
  private static final String DEFAULT_RESP_HEADER_VAL = "1.0";

  // Set responses header for all requests of this controller
  private static String customResponseHeaderName = DEFAULT_RESP_HEADER_NAME;
  private static String customResponseHeaderVal = DEFAULT_RESP_HEADER_VAL;

  // MBean related
  private static final String ATT_CUSTOM_RESPONSE_HEADER_VAL = "customResponseHeaderVal";
  private static final String ATT_CUSTOM_RESPONSE_HEADER_NAME = "customResponseHeaderName";
  private static final MBeanAttributeInfo ATTR_INFO_HEADER = new MBeanAttributeInfo(ATT_CUSTOM_RESPONSE_HEADER_NAME, "java.lang.String",
      "Customized response header name",
      true, true, false);
  private static final MBeanAttributeInfo ATTRIBUTE_INFO_VAL =  new MBeanAttributeInfo(ATT_CUSTOM_RESPONSE_HEADER_VAL,"java.lang.String",
      "Customize the response header value",
      true, true, false);
  private static final Map<String, MBeanAttributeInfo> attributeInfoMap = new HashMap<String, MBeanAttributeInfo>() {{
    put(ATT_CUSTOM_RESPONSE_HEADER_NAME, ATTR_INFO_HEADER);
    put(ATT_CUSTOM_RESPONSE_HEADER_VAL, ATTRIBUTE_INFO_VAL);
  }};
  private static MBeanInfo dMBeanInfo = new MBeanInfo(
      RegisterController.class.getName(),
      "Register MBean Allow set response header name nad value",
      attributeInfoMap.values().toArray(new MBeanAttributeInfo[0]),
      null, null,null);


  @GET
  @POST
  public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
    response.setHeader(customResponseHeaderName, customResponseHeaderVal);
    if (request.getMethod().equalsIgnoreCase(HttpMethod.POST)) {
      return doPost(request, response);
    }
    return "register-form.jsp";
  }

  private String doPost(HttpServletRequest request, HttpServletResponse response) {
    String name = request.getParameter("name");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String phoneNumber = request.getParameter("phoneNumber");

    // 后端校验
    if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
      request.setAttribute("Error_Message", "无效输入，请输入用户名与密码");
      return "index.jsp";
    }

    User user = new User();
    user.setName(name);
    user.setPassword(password);
    user.setEmail(email);
    user.setPhoneNumber(phoneNumber);

    try {
      if (userService.register(user)) {
        request.setAttribute("Login_Name", name);
      } else {
        request.setAttribute("Error_Message", "Register Failed, username has been taken");
      }
    } catch (ValidationException e) {
      request.setAttribute("Error_Message", e.getMessage());
    }

    return "index.jsp";
  }

  // ---- MBean related -----
  @Override
  public Object getAttribute(String attribute)
      throws AttributeNotFoundException, MBeanException, ReflectionException {
    try {
      if (attributeInfoMap.containsKey(attribute)) {
        BeanInfo beanInfo = Introspector.getBeanInfo(RegisterController.class, Object.class);
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
          if (attribute.equals(descriptor.getName())) {
            Method readMethod = descriptor.getReadMethod();
            return readMethod.invoke(this, null);
          }
        }
      }
    } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    throw new AttributeNotFoundException(attribute);
  }

  @Override
  public void setAttribute(Attribute attribute)
      throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException,
          ReflectionException {
    try {
      if (!attributeInfoMap.containsKey(attribute.getName())) {
        throw new AttributeNotFoundException(attribute.getName());
      }
      BeanInfo beanInfo = Introspector.getBeanInfo(RegisterController.class, Object.class);
      for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
        if (attribute.getName().equals(descriptor.getName())) {
          Method writeMethod = descriptor.getWriteMethod();
          writeMethod.invoke(this, attribute.getValue());
        }
      }
      System.out.println("*** setAttribute success, this.toString() = " + this.toString());
    } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  @Override
  public AttributeList getAttributes(String[] attributes) {
    AttributeList result = new AttributeList();
    Arrays.stream(attributes).filter(attr -> attributeInfoMap.containsKey(attr)).forEach(attributeName-> {
      try {
        Object o = getAttribute(attributeName);
        result.add(new Attribute(attributeName, o));
      } catch (AttributeNotFoundException | MBeanException | ReflectionException e) {
        e.printStackTrace();
      }
    });
    return result;
  }

  @Override
  public AttributeList setAttributes(AttributeList attributes) {
    if (attributes == null || attributes.size() == 0) {
      return null;
    }
    AttributeList result = new AttributeList();
    attributes.stream().filter(o -> o instanceof Attribute).map(o -> (Attribute) o).forEach(attribute -> {
      try {
        setAttribute(attribute);
        result.add(attribute);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    return result;
  }

  @Override
  public MBeanInfo getMBeanInfo() {
    return dMBeanInfo;
  }

  @Override
  public Object invoke(String actionName, Object[] params, String[] signature)
      throws MBeanException, ReflectionException {
    throw new ReflectionException(new UnsupportedOperationException(actionName));
  }

  @Override
  public String toString() {
    return "RegisterController{" +
        "customResponseHeaderName='" + customResponseHeaderName + '\'' +
        ", customResponseHeaderVal='" + customResponseHeaderVal + '\'' +
        '}';
  }

  public String getCustomResponseHeaderName() {
    return customResponseHeaderName;
  }

  public void setCustomResponseHeaderName(String customResponseHeaderName) {
    this.customResponseHeaderName = customResponseHeaderName;
  }

  public String getCustomResponseHeaderVal() {
    return customResponseHeaderVal;
  }

  public void setCustomResponseHeaderVal(String customResponseHeaderVal) {
    this.customResponseHeaderVal = customResponseHeaderVal;
  }
}
