package com.ecommerce.admin.item;

import com.ecommerce.general.item.ItemDaoImpl;
import com.ecommerce.general.helper.Helper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DeleteItemController", urlPatterns = {"/admin/delete-item"})
public class DeleteItemController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // get itemId param from the request
        String itemId = request.getParameter("itemid");

        // return the itemId if number or return 0
        long id = itemId != null && Helper.isNumber(itemId) ? Long.parseLong(itemId) : 0;

        // delete item depending on the itemId
        boolean itemDeleted = new ItemDaoImpl(getServletContext()).deleteItem(id);

        if (itemDeleted) {
            // redirect to the previous page with deleted message
            Helper.redriectToPrevPage(request, response, "item deleted", false);
        } else {
            // redirect to the previous page with error message
            Helper.redriectToPrevPage(request, response, "Theres No Such ID", true);
        }
    }

}
