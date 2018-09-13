/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecommerce.admin.controller;

import com.ecommerce.dao.CommentDaoImpl;
import com.ecommerce.dao.ItemDaoImpl;
import com.ecommerce.dao.UserDaoImpl;
import com.ecommerce.helper.Helper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DashboardController extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // get registered session
        HttpSession session = request.getSession();

        // check if the username exist in session 
        if (session.getAttribute("username") != null) {
            // set page title
            Helper.setTitle(request, "Dashboard");

            // get user dao to do operation on user
            UserDaoImpl userDao = new UserDaoImpl(getServletContext());

            // get item dao to do operation on item
            ItemDaoImpl itemDao = new ItemDaoImpl(getServletContext());

            // get comment dao to do operation on comment
            CommentDaoImpl commentDao = new CommentDaoImpl(getServletContext());

            // set the number of users to the request
            request.setAttribute("numUsers", userDao.getNumUsers());

            // set the number of pending users to the request
            request.setAttribute("numPendingUsers", userDao.getNumPendingUsers());

            // set the latest five users to the request
            request.setAttribute("latestUsers", userDao.getLatestUsers(5));

            // set the number of items to the request
            request.setAttribute("numItems", itemDao.getNumItems());

            // set the latest five items to the request
            request.setAttribute("latestItems", itemDao.getLatestItems(5));

            // set the number of comments to the request
            request.setAttribute("numComments", commentDao.getNumComments());

            // set the latest five comments to the request
            request.setAttribute("latestComments", commentDao.getLatestComments(5));

            // forword the requset to the dashboard page
            Helper.forwardRequest(request, response, "/WEB-INF/views/admin/dashboard.jsp");
        } else {
            // redirect to login page if session not exits
            response.sendRedirect("login");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}