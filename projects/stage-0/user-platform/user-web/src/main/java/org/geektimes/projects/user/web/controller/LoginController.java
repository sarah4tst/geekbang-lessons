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
import org.geektimes.projects.user.management.ContextMXBean;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.web.mvc.controller.PageController;

/**
 * 输出 “Hello,World” Controller
 */
@Path("/login")
public class LoginController extends ContextMXBean implements PageController {

    private UserService userService = ComponentContext.getInstance().getComponent("bean/UserService");

    private String version = "1.0";

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @GET
    @POST
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        if (request.getMethod().equalsIgnoreCase(HttpMethod.POST)) {
            return doPost(request,response);
        }
        return "login-form.jsp";
    }

    private String doPost(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        // 后端校验
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            request.setAttribute("Error_Message","无效输入，请输入用户名与密码");
        } else if (null == userService.queryUserByNameAndPassword(name,password)) {
            request.setAttribute("Error_Message","Login Failed");
        } else {
            request.setAttribute("Login_Name", name);
        }

        return "index.jsp";
    }

    @Override
    protected void createMBeanInfo() {
        this.dMBeanInfo = new MBeanInfo(
            this.getClass().getName(),
            "Login MBean Allow set response header name nad value",
            mBeanAttributeInfos,
            null, null,null);
    }

    @Override
    protected void createMBeanAttributeInfos() {
        MBeanAttributeInfo ATTR_INFO_HEADER = new MBeanAttributeInfo("version", "java.lang.String",
            "version",
            true, true, false);
        mBeanAttributeInfos = new MBeanAttributeInfo[] {
            ATTR_INFO_HEADER
        };
    }
}
