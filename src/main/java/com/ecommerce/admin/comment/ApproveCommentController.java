package com.ecommerce.admin.comment;

import com.ecommerce.general.comment.CommentDaoImpl;
import com.ecommerce.general.helper.Helper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ApproveCommentController", urlPatterns = {"/admin/approve-comment"})
public class ApproveCommentController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // get commentId param from the request
        String commentId = request.getParameter("commentid");

        // return the commentId if number or return 0
        long id = commentId != null && Helper.isNumber(commentId) ? Long.parseLong(commentId) : 0;

        // approve comment depending on the commentId
        boolean commentApproved = new CommentDaoImpl(getServletContext()).approveComment(id);
        if (commentApproved) {
            // redirect to the previous page with deleted message
            Helper.redriectToPrevPage(request, response, "comment approved", false);
        } else {
            // redirect to the previous page with error message
            Helper.redriectToPrevPage(request, response, "Theres No Such ID", true);
        }
    }
}
