/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecommerce.admin.controller;

import com.ecommerce.bean.Category;
import com.ecommerce.dao.CategoryDaoImpl;
import com.ecommerce.helper.Helper;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CategoryController extends HttpServlet {

    

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

        // check if the username exists in session 
        if (session.getAttribute("username") != null) {
            // set page title
            Helper.setTitle(request, "Categories");

            // get the action param value if exists or return manage
            String action = request.getParameter("action") != null ? request.getParameter("action") : "Manage";
            if (action.equals("Manage")) {
                // get all categories with assending order
                List<Category> categories = new CategoryDaoImpl(getServletContext()).getAllCategories("ASC");

                // get sort param from the request
                String sort = request.getParameter("sort");

                if (sort != null) {
                    // get all categories with order depending on param sort 
                    categories = new CategoryDaoImpl(getServletContext()).getAllCategories(sort);
                }

                // set categories to request
                request.setAttribute("categories", categories);

                // set the sort to request to ordering categories depending on it
                request.setAttribute("sort", sort);

                // forword request to manage page
                Helper.forwardRequest(request, response, "/WEB-INF/views/admin/manage_categories.jsp");
            } else if (action.equals("Add")) {
                // forword request to add page
                Helper.forwardRequest(request, response, "/WEB-INF/views/admin/add_category.jsp");
            } else if (action.equals("Edit")) {
                // get categoryId param from the request
                String categoryId = request.getParameter("categoryid");

                // return the categoryId if number or return 0
                long Id = categoryId != null && Helper.isNumber(categoryId) ? Long.parseLong(categoryId) : 0;

                // get category depending on categoryId
                Category categoryFounded = new CategoryDaoImpl(getServletContext()).getCategoryById(Id);
                if (categoryFounded != null) {
                    // set the found category to the request
                    request.setAttribute("category", categoryFounded);

                    // forword request to edit page
                    Helper.forwardRequest(request, response, "/WEB-INF/views/admin/edit_category.jsp");
                } else {
                    // redirect to the previous page with error message
                    Helper.redriectToPrevPage(request, response, "There`s No Such ID", true);
                }
            } else if (action.equals("Delete")) {
                // get categoryId param from the request
                String categoryId = request.getParameter("categoryid");

                // return the categoryId if number or return 0
                long Id = categoryId != null && Helper.isNumber(categoryId) ? Long.parseLong(categoryId) : 0;

                // delete category depending on the categoryId
                boolean categoryDeleted = new CategoryDaoImpl(getServletContext()).deleteCategory(Id);
                if (categoryDeleted) {
                    // redirect to the previous page with deleted message
                    Helper.redriectToPrevPage(request, response, "category deleted", false);
                } else {
                    // redirect to the previous page with error message
                    Helper.redriectToPrevPage(request, response, "There`s No Such ID", true);
                }
            }
        } else {
            // redirect to the login page if the username does not exists in session 
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
        // get registered session
        HttpSession session = request.getSession();

        // check if the username exists in session
        if (session.getAttribute("username") != null) {
            // set page title
            Helper.setTitle(request, "Categories");

            // get the action param value
            String action = request.getParameter("action");

            if (action.equals("Add")) {
                // get form params from the request
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                int ordering = Integer.parseInt(request.getParameter("ordering"));
                int visibility = Integer.parseInt(request.getParameter("visibility"));
                int commenting = Integer.parseInt(request.getParameter("commenting"));
                int ads = Integer.parseInt(request.getParameter("ads"));

                // make new category and set info to it 
                Category category = new Category();
                category.setName(name);
                category.setDescription(description);
                category.setOrdering(ordering);
                category.setVisibility(visibility);
                category.setAllowComments(commenting);
                category.setAllowAds(ads);

                // add new category 
                boolean categoryAdded = new CategoryDaoImpl(getServletContext()).addCategory(category);
                if (!categoryAdded) {
                    String error = "Sorry This Category Is Exist";
                    // set error message to request if category does not add successfully
                    request.setAttribute("error", error);
                } else {
                    // set success message to request if category added successfully
                    request.setAttribute("success", "category added");
                }

                // forword request to the add page
                Helper.forwardRequest(request, response, "/WEB-INF/views/admin/add_category.jsp");
            } else if (action.equals("Edit")) {
                // get form params from the request
                long id = Long.parseLong(request.getParameter("categoryid"));
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                int ordering = Integer.parseInt(request.getParameter("ordering"));
                int visibility = Integer.parseInt(request.getParameter("visibility"));
                int commenting = Integer.parseInt(request.getParameter("commenting"));
                int ads = Integer.parseInt(request.getParameter("ads"));

                // make new category and set info to it 
                Category category = new Category();
                category.setId(id);
                category.setName(name);
                category.setDescription(description);
                category.setOrdering(ordering);
                category.setVisibility(visibility);
                category.setAllowComments(commenting);
                category.setAllowAds(ads);

                // update category
                boolean categoryUpdated = new CategoryDaoImpl(getServletContext()).updateCategory(category);

                if (!categoryUpdated) {
                    String error = "error in update";
                    // set error message to request if category does not update successfully
                    request.setAttribute("error", error);
                } else {
                    // set success message to request if category updated successfully
                    request.setAttribute("success", "category updated");
                }

                // set category to request
                request.setAttribute("category", category);

                // forword request to the edit page
                Helper.forwardRequest(request, response, "/WEB-INF/views/admin/edit_category.jsp");
            }
        } else {
            // redirect to the login page if the username does not exists in session
            response.sendRedirect("login");
        }
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