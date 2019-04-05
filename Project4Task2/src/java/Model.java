/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author xiaoditao
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Random;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 *
 * @author xiaoditao
 */

public class Model {
//    public static void main(String[] args) throws UnsupportedEncodingException {
//        Model model = new Model();
//        String print = model.doFlickrSearch("dior");
//        System.out.println(print);
//    }
        
        public String getReplyFromAPI(String input) throws UnsupportedEncodingException{
            String responseJson = "";
            String flickrURL = "http://www.google.com";
        // get the url of the website that we want to do scraping
//        flickrURL = "https://dog.ceo/api/breed/african/images/random/3";
            flickrURL = "http://makeup-api.herokuapp.com/api/v1/products.json?brand=" + input + "&product_type=lipstick";
            
        // set a string called response to get the page source 
            responseJson = fetch(flickrURL);
            if(responseJson.length()>200) {
                responseJson = responseJson.substring(0,200);
            }
            String res = responseJson;
            return responseJson;
        }

        public String getRequestAPI(String input) {
            String res = "http://makeup-api.herokuapp.com/api/v1/products.json?brand=" + input + "&product_type=lipstick";
            return res;
        }
        // the doFlikrSearch method is used to get the picture url and parse it to the servlet
        public String doFlickrSearch(String input) 
            throws UnsupportedEncodingException {
      
        String responseJson = "";
        String flickrURL = "http://www.google.com";
        // get the url of the website that we want to do scraping
//        flickrURL = "https://dog.ceo/api/breed/african/images/random/3";
        flickrURL = "http://makeup-api.herokuapp.com/api/v1/products.json?brand=" + input + "&product_type=lipstick";
        System.out.println("flickerurl" + flickrURL);
        // set a string called response to get the page source 
        responseJson = fetch(flickrURL);
        String name = "";
        String imageLink = "";
        //String description = "";
        String xmlResponse = "";
        int count = 0;
        while(responseJson.contains("\"name\"")){
           // System.out.println("reaponselen  "+responseJson.length());
            count++;
            int nameLeft = responseJson.indexOf("\"name\"");
            int nameRight = responseJson.indexOf("\"price\"");
            nameLeft += 8;
            nameRight -= 2;
            //System.out.println("nameleft "+nameLeft);
            //System.out.println("nameright "+nameRight);
            name = responseJson.substring(nameLeft, nameRight);
            xmlResponse += "<name>" + name + "</name>";
            
            int imageLeft = responseJson.indexOf("\"image_link\"");
            int imageRight = responseJson.indexOf("\"product_link\"");
            imageLeft += 13;
            imageRight -= 1;
            imageLink = responseJson.substring(imageLeft, imageRight);
            xmlResponse += "<image>" + imageLink + "</image>";            
            responseJson = responseJson.substring(imageRight + 15);
            //System.out.println("reaponselen  "+responseJson.length());
        }
        
        // System.out.println(xmlResponse);
         //System.out.println("count "+count);
         int randomLimit = 20;
         if(count == 0) return "<root>" + "" + "</root>";
         if(count < 20 ) randomLimit = count;
         Random rd = new Random();
         int randomNum = rd.nextInt(randomLimit);
         if (randomNum == 0) randomNum++;
         for(int i = 0; i < randomNum; i++) {
             int desLeft2 = xmlResponse.indexOf("</image>");
             xmlResponse = xmlResponse.substring(desLeft2 + "</image>".length());
         }
         int left = xmlResponse.indexOf("<name>");
         int right = xmlResponse.indexOf("</image>");
         right += "</image>".length();
         xmlResponse = xmlResponse.substring(left,right);
         xmlResponse = "<root>" + xmlResponse + "</root>";
         return xmlResponse;
    }
       
               
        
        
private String fetch(String searchURL) {
        try {
            createTrustManager();
        } catch (KeyManagementException ex) {
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }   
        // use a string called response to get the page source
        String response = "";
        try {
            URL url = new URL(searchURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                response += str;
            }
            in.close();
          // if we cannot get the searchURL, e.g. turn off the wifi, or when the source page crashed
          // we will return a non-null value to response
        } catch (IOException e) {
            return "hi, there is an error";
        }
         return response;
    }
    
    private void createTrustManager() throws KeyManagementException, NoSuchAlgorithmException{
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }


}

