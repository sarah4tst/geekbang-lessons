package org.geektimes.projects.user.web.controller;

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

/**
 * 输出 “Hello,World” Controller
 */
@Path("/register")
public class RegisterController implements PageController {
    private UserService userService = ComponentContext.getInstance().getComponent("bean/UserService");

    @GET
    @POST
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        if (request.getMethod().equalsIgnoreCase(HttpMethod.POST)) {
            return doPost(request,response);
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
            request.setAttribute("Error_Message","无效输入，请输入用户名与密码");
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
                request.setAttribute("Error_Message","Register Failed, username has been taken");
            }
        } catch (ValidationException e) {
            request.setAttribute("Error_Message",e.getMessage());
        }


        return "index.jsp";
    }
}
