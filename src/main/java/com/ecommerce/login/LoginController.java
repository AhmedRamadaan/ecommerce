//<editor-fold >
package com.ecommerce.login;

import com.ecommerce.bean.User;
import com.ecommerce.dao.UserDaoImpl;
import com.ecommerce.helper.CookieHelper;
import com.ecommerce.helper.HashHelper;
import com.ecommerce.helper.Helper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    String customerJspPath = null;
    String loginJspPath = null;
    private ServletContext servletContext = null;

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        servletContext = getServletContext();
        customerJspPath = servletContext.getInitParameter("customerJspPath");
        loginJspPath = servletContext.getInitParameter("loginJspPath");

    }//</editor-fold >

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        if ((session.getAttribute("user") != null) || (CookieHelper.isCookie("user", request, response))) {
            if ((session.getAttribute("groupId") != null) || (CookieHelper.isCookie("groupId", request, response))) {
                // go to dashboard
                response.sendRedirect("admin/dashboard");
            } else {
                // go to home 
                response.sendRedirect("");
            }
        } else {
            String previous = request.getParameter("previous");

            if (previous != null) {
                previous = "?previous=" + previous;
            } else {
                previous = "";
            }

            // forword the requset to the login page
            Helper.forwardRequest(request, response, loginJspPath + "login_register.jsp" + previous, "Login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // get FORM params from the request
        String username = request.getParameter("user");
        String password = request.getParameter("pass");
        String remember = request.getParameter("remember");
        // hash password
        String passwordHashed = HashHelper.stringHash(password);
        //get param previous page from url
        String previous = request.getParameter("previous");

        // get USER logging from DB
        User user = new UserDaoImpl(servletContext).getLoginUser(username, passwordHashed);
        ////////////////////////////////////////////////////////////////////////
        //////////////////////Start if user existed in DB///////////////////////
        if (user != null) {

            int groupId = user.getGroupId();

            if (remember != null && remember.equalsIgnoreCase("y")) {
                //<set Cookies>if user doing Remember Me 
                CookieHelper.addCookie("user", username, response);
                CookieHelper.addCookie("userId", "" + user.getId(), response);
                CookieHelper.addCookie("fullName", (user.getFullName().split(" ")[0]), response);

                // set group id to cookie if admin
                if (groupId == 1) {
                    CookieHelper.addCookie("groupId", groupId + "", response);
                }
            } else {
                //get session
                HttpSession session = request.getSession();

                //<set Session>if user ignore remember Me 
                SetUserSession(user, session);

                // set group id to session if admin
                if (groupId == 1) {
                    session.setAttribute("groupId", groupId);
                }
            }
            //check on parameter previous from url
            if (previous != null && !previous.equals("")) {//previousPage == null <improve>
                response.sendRedirect(previous);
            } else {

                if (groupId == 1) {
                    response.sendRedirect("admin/dashboard");
                } else {
                    response.sendRedirect("home");
                }
            }
            //////////////////////End user existed in DB//////////////////////////
            //////////////////////Start if user Not existed in DB//////////////////////////
        } else {
            List<String> formErrors = new ArrayList();
            formErrors.add("user not Existed! try again ");

            request.setAttribute("errors", formErrors);
            // redirect to login page 
            Helper.setTitle(request, "Login");
            if (previous != null) {
                Helper.forwardRequest(request, response, loginJspPath + "login_register.jsp?previous=" + previous);
            } else {
                Helper.forwardRequest(request, response, loginJspPath + "login_register.jsp");
            }
        }
        //////////////////////End if user Not existed in DB//////////////////////////
    }

    public void SetUserSession(User user, HttpSession session) {

        //set session for user if remember me not checked 
        session.setAttribute("user", user.getName());
        session.setAttribute("userId", user.getId());
        session.setAttribute("fullName", (user.getFullName().split(" ")[0]));
    }

}