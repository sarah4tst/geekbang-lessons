package org.geektimes.projects.user.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.apache.commons.lang.StringUtils;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.projects.user.service.UserServiceImpl;
import org.geektimes.web.mvc.controller.PageController;

/**
 * 输出 “Hello,World” Controller
 */
@Path("/login")
public class LoginController implements PageController {

    UserService userService = new UserServiceImpl();

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
}
