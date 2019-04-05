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
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;


// This example demonstrates Java servlets and HTTP
// This web service operates on string keys mapped to string values.

@WebServlet(name = "makeupName", urlPatterns = {"/Servlet/*","/dashboard"})
public class Servlet extends HttpServlet {
    static String print = "";
    static int count = 0;
    Model model = new Model();
    ConnectDataBase cdb = new ConnectDataBase();
    // GET returns a value given a key
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, UnsupportedEncodingException {
        String url=request.getServletPath();
        String nextView;
        if(url.equals("/dashboard")){
            //get string data from mangodb
            //String a = print;
            //System.out.println("a " + a );
//            for (int i=0;i<15;i++){
//                request.setAttribute("answer"+i, i);
//            }
            
            request.setAttribute("answer", print);
            request.setAttribute("count", cdb.getCount());
            request.setAttribute("average", cdb.getAvg());
            nextView="dashboard.jsp";
            RequestDispatcher view = request.getRequestDispatcher(nextView);
            view.forward(request, response);
        } 
        else {
            String path = request.getPathInfo();
            Timestamp timeRequestFromAndroid = new Timestamp(System.currentTimeMillis()); 
            System.out.println("timestamp request from android "+timeRequestFromAndroid);
            System.out.println("Console: doGET visited");
            System.out.println(path);
            String replyToAndroid = "";
            String searchTerm = path.substring(1);
            System.out.println("searchterm " + searchTerm);
            String requestAPI = model.getRequestAPI(searchTerm);
            String ip = request.getRemoteAddr();
            ///////
            //cdb.test(searchTerm);
            String replyFromAPI = model.getReplyFromAPI(searchTerm);
            ///////
            replyToAndroid = model.doFlickrSearch(searchTerm);
            
            ////top 300 from api
            ////what we send back to the android
            ///time stamp
            PrintWriter out = response.getWriter();
            out.println(replyToAndroid);
            Timestamp timeReplyToAndroid = new Timestamp(System.currentTimeMillis()); 
            try {
                print = cdb.connectMango(timeRequestFromAndroid, searchTerm, requestAPI, replyFromAPI, replyToAndroid, timeReplyToAndroid, ip);
                //print += ip;
            } catch (JSONException ex) {
                Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

