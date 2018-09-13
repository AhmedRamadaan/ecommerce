/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecommerce.helper;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Helper {

    /**
     * check if the string is number or not
     * 
     * @param value
     * @return boolean
     */
    public static boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * redirect to the previous page 
     * 
     * @param request
     * @param response
     * @param message
     * @param error
     * @throws ServletException
     * @throws IOException 
     */
    public static void redriectToPrevPage(HttpServletRequest request, HttpServletResponse response, String message, boolean error) throws ServletException, IOException {
        if (!error) {
            request.getSession().setAttribute("success", message);
        } else {
            request.getSession().setAttribute("error", message);
        }
        if (request.getHeader("referer") == null) {
            setTitle(request, "Login");
            request.getRequestDispatcher("login").forward(request, response);
        } else {
            setTitle(request, getTitleFromRefererLink(request.getHeader("referer")));
            response.sendRedirect(request.getHeader("referer"));
        }
    }

    /**
     * forward request to specific page and set title of page
     * 
     * @param request
     * @param response
     * @param page
     * @param title
     * @throws ServletException
     * @throws IOException 
     */
    public static void forwardRequest(HttpServletRequest request, HttpServletResponse response, String page, String title) throws ServletException, IOException {
        setTitle(request, title);
        forwardRequest(request, response, page);
    }
    
    
    /**
     * forward request to specific page
     * 
     * @param request
     * @param response
     * @param page
     * @throws ServletException
     * @throws IOException 
     */
    public static void forwardRequest(HttpServletRequest request, HttpServletResponse response, String page) throws ServletException, IOException {
        request.getRequestDispatcher(page).forward(request, response);
    }

    /**
     * set title of page
     * 
     * @param request
     * @param title 
     */
    public static void setTitle(HttpServletRequest request, String title) {
        request.getServletContext().setAttribute("title", title);
    }

    
    /**
     * 
     * get title page from referer link 
     * 
     * @param link
     * @return 
     */
    public static String getTitleFromRefererLink(String link) {
        /**
         * link like: http://localhost:8084/ecommerce/items?action=Add get
         * items?action=Add
         */
        // String query = link.split("/")[4];

        /**
         * items?action=Add get items
         */
        // String pageName = query.split("\\?")[0];
        
        /**
         * get i
         */
        // String firstCharToUC = String.valueOf(pageName.charAt(0)).toUpperCase();
        
        /**
         * merge all to be Items
         */
        // String title = firstCharToUC + pageName.substring(1);
        
        String title = String.valueOf("http://localhost:8084/ecommerce/items?action=Add".split("/")[4].split("\\?")[0].charAt(0)).toUpperCase() + "http://localhost:8084/ecommerce/items?action=Add".split("/")[4].split("\\?")[0].substring(1);
        
        return title;
    }

    /**
     * remove last characters from string 
     * 
     * @param str
     * @param chars
     * @return 
     */
    public static String rTrim(String str, String chars) {
        Pattern pattern = Pattern.compile(chars + "$");
        return pattern.matcher(str).replaceAll("");
    }
    
}