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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author xiaoditao
 */

public class Model {
    public static void main(String[] args) throws UnsupportedEncodingException, JSONException {
        Model model = new Model();
        String print = model.doFlickrSearch();
        System.out.println(print);
    }


        
        // the doFlikrSearch method is used to get the picture url and parse it to the servlet
        public String doFlickrSearch() 
            throws UnsupportedEncodingException, JSONException  {
      
        String responseJson = "";
        String flickrURL = "http://www.google.com";
        // get the url of the website that we want to do scraping
//        flickrURL = "https://dog.ceo/api/breed/african/images/random/3";
        flickrURL = "http://makeup-api.herokuapp.com/api/v1/products.json?brand=maybelline&product_type=lipstick";
        // set a string called response to get the page source 
        responseJson = fetch(flickrURL);
        String name = "";
        String imageLink = "";
        String description = "";
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
            
            int desLeft = responseJson.indexOf("\"description\"");
            int desRight = responseJson.indexOf("\"rating\"");
            desLeft += 14;
            desRight -= 1;
            //System.out.println(desRight);
            description = responseJson.substring(desLeft, desRight);
            xmlResponse += "<description>" + description + "</description>";
            
            responseJson = responseJson.substring(desRight + 20);
            //System.out.println("reaponselen  "+responseJson.length());
        }
        
         //System.out.println(xmlResponse);
        //
         //System.out.println("count "+count);
         int randomLimit = 5;
         if(count == 0) return "<root>" + "" + "</root>";
         if(count < 5 ) randomLimit = count;
         Random rd = new Random();
         int randomNum = rd.nextInt(randomLimit);
         if (randomNum == 0) randomNum++;
         //System.out.println("randomnum" + randomNum);
         for(int i = 0; i < randomNum; i++) {
             int desLeft2 = xmlResponse.indexOf("</description>");
             //System.out.println("desLeft2  "+ desLeft2);
             xmlResponse = xmlResponse.substring(desLeft2 + "</description>".length());
         }
         int left = xmlResponse.indexOf("<name>");
         int right = xmlResponse.indexOf("</description>");
         right += "</description>".length();
         xmlResponse = xmlResponse.substring(left,right);
//            System.out.println("xml after random   "+ xmlResponse);
         xmlResponse = "<root>" + xmlResponse + "</root>";
         return xmlResponse;
//        while(responseJson.contains("\"name\"")){
//            System.out.println("zong "+responseJson.length());
//            int nameLeft = responseJson.indexOf("\"name\"");
//            int nameRight = responseJson.indexOf("\"price\"");
//            nameLeft += 8;
//            nameRight -= 2;
//            name = responseJson.substring(nameLeft, nameRight);
//            xmlResponse = "<name>" + name + "</name>";
//            
//            int imageLeft = responseJson.indexOf("\"image_link\"");
//            int imageRight = responseJson.indexOf("\"product_link\"");
//            imageLeft += 13;
//            imageRight -= 2;
//            imageLink = responseJson.substring(imageLeft, imageRight);
//            xmlResponse = "<image>" + imageLink + "</image>";
//            
//            int desLeft = responseJson.indexOf("\"description\"");
//            int desRight = responseJson.indexOf("\"rating\"");
//            desLeft += 14;
//            desRight -= 2;
//            System.out.println(desRight);
//            description = responseJson.substring(desLeft, desRight);
//            xmlResponse = "<description>" + description + "</description>";
//            
//            responseJson = responseJson.substring(desRight);
//        }
//        JSONObject json;
//        json = new JSONObject(responseJson);
//        String xmlResponse = XML.toString(json);
        
//        int cutLeftFirst;
//        int lastLeft;
//        int cutRight;
        // set random to randomly select the picture
        // get the first and last picture of the website, compute how many picures are between them
        // then base on the number of the picture, randomly scrap picture
        // if there are more than 40 picures, only random the first 40 pictures
//        Random rd = new Random();
//        if(picSize.equals("mobile")){
//        cutLeftFirst = response.indexOf("<img typeof=\"foaf:Image\" class=\"img-responsive\" src=\"");
//        if (cutLeftFirst == -1) {
//            return (String) null;
//        }
//        lastLeft = response.lastIndexOf("<img typeof=\"foaf:Image\" class=\"img-responsive\" src=\"");
//        int addUp = 0;
//        if((lastLeft - cutLeftFirst - 40000) >  0) {
//            addUp = rd.nextInt(35)+1;
//        }else {
//            int num = (lastLeft - cutLeftFirst)/1000;
//            addUp = rd.nextInt(num)+1;
//             }
//        response = response.substring(addUp * 1000 + cutLeftFirst);
//        cutLeftFirst = response.indexOf("<img typeof=\"foaf:Image\" class=\"img-responsive\" src=\"");
//        if (cutLeftFirst == -1) {
//            cutLeftFirst=lastLeft;
//        } 
//        cutLeftFirst += "<img typeof=\"foaf:Image\" class=\"img-responsive\" src=\"".length();
//        cutRight = response.indexOf("\"", cutLeftFirst);
//        }else{
//        cutLeftFirst = response.indexOf("<div class=\"field-content\"><a href=\"");
//        // if there is no picture, then retrun null
//        if (cutLeftFirst == -1) {
//            return (String) null;
//        }
//        // get the first picture's left cut
//        lastLeft = response.lastIndexOf("<div class=\"field-content\"><a href=\"");
//        int addUp = 0;
//        // get the last picture's left cut, if there are more than 40 pictures between the first and last picture, set the random smaller than 40
//        if((lastLeft - cutLeftFirst - 40000) >  0) {
//            addUp = rd.nextInt(35)+1;
//        }else {
//            int num = (lastLeft - cutLeftFirst)/1000;
//            addUp = rd.nextInt(num)+1;
//             }
//        response = response.substring(addUp * 1000 + cutLeftFirst);
//        cutLeftFirst = response.indexOf("<div class=\"field-content\"><a href=\"");
//        if (cutLeftFirst == -1) {
//            cutLeftFirst=lastLeft;
//        }
//        cutLeftFirst += "<div class=\"field-content\"><a href=\"".length();
//        cutRight = response.indexOf("\"", cutLeftFirst);
//        }
//        String pictureURL = response.substring(cutLeftFirst, cutRight);
//        return pictureURL;
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

