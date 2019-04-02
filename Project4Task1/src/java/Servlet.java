/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author xiaoditao
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;

// This example demonstrates Java servlets and HTTP
// This web service operates on string keys mapped to string values.

@WebServlet(urlPatterns = {"/Servlet/*"})
public class Servlet extends HttpServlet {
    
    Model model = new Model();
    
    // GET returns a value given a key
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, UnsupportedEncodingException {
        String path = request.getPathInfo();
        System.out.println("Console: doGET visited");
        
        String result = "";
        try {
            result = model.doFlickrSearch();
            System.out.println("result " + result);
            PrintWriter out = response.getWriter();
            out.println(result);
        } catch (JSONException ex) {
            Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

