package org.geektimes.projects.user.web.controller;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.apache.commons.lang.StringUtils;
import org.geektimes.context.ComponentContext;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.management.ContextMXBean;
import org.geektimes.projects.user.service.UserService;
import javax.validation.ValidationException;
import org.geektimes.web.mvc.controller.PageController;

/** 输出 “Hello,World” Controller */
@Path("/register")
public class RegisterController extends ContextMXBean implements PageController {
  private UserService userService = ComponentContext.getInstance().getComponent("bean/UserService");

  private static final String DEFAULT_RESP_HEADER_NAME = "X-CUSTOM-TAG";
  private static final String DEFAULT_RESP_HEADER_VAL = "1.0";

  // Set responses header for all requests of this controller
  private static String customResponseHeaderName = DEFAULT_RESP_HEADER_NAME;
  private static String customResponseHeaderVal = DEFAULT_RESP_HEADER_VAL;

  // MBean related
  private static final MBeanAttributeInfo ATTR_INFO_HEADER = new MBeanAttributeInfo("customResponseHeaderName", "java.lang.String",
      "Customized response header name",
      true, true, false);
  private static final MBeanAttributeInfo ATTRIBUTE_INFO_VAL =  new MBeanAttributeInfo("customResponseHeaderVal","java.lang.String",
      "Customize the response header value",
      true, true, false);

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

  @Override
  protected void createMBeanInfo() {
    this.dMBeanInfo = new MBeanInfo(
        RegisterController.class.getName(),
        "Register MBean Allow set response header name nad value",
        mBeanAttributeInfos,
        null, null,null);
  }

  @Override
  protected void createMBeanAttributeInfos() {
    this.mBeanAttributeInfos = new MBeanAttributeInfo[]{ATTR_INFO_HEADER,ATTRIBUTE_INFO_VAL};
  }
}
