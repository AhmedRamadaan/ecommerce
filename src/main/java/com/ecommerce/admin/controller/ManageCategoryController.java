package com.ecommerce.admin.controller;

import com.ecommerce.bean.Category;
import com.ecommerce.dao.CategoryDaoImpl;
import com.ecommerce.helper.Helper;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "ManageCategoryController", urlPatterns = {"/admin/manage-categories"})

public class ManageCategoryController extends HttpServlet {
    String adminJspPath = null;
    ServletContext servletContext = null;

    @Override
    public void init() throws ServletException {
        servletContext = getServletContext();
        adminJspPath = servletContext.getInitParameter("adminJspPath");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // set page title
        Helper.setTitle(request, "Categories");

        // get all categories with assending order
        List<Category> supCategories = new CategoryDaoImpl(servletContext).getAllSupCategories("ASC");
        List<Category> subCategories = new CategoryDaoImpl(servletContext).getAllSubCategories("ASC");

        // get sort param from the request
        String sort = request.getParameter("sort");

        if (sort != null) {
            // get all categories with order depending on param sort 
            supCategories = new CategoryDaoImpl(servletContext).getAllSupCategories(sort);
        }

        // set all super categories to request
        request.setAttribute("supCategories", supCategories);
        // set all sub categories to request
        request.setAttribute("subCategories", subCategories);

        // set the sort to request to ordering categories depending on it
        request.setAttribute("sort", sort);

        // forword request to manage page
        Helper.forwardRequest(request, response, adminJspPath + "manage_categories.jsp");
    }

}
