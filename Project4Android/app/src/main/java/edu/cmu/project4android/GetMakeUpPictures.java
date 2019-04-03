package edu.cmu.project4android;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class GetMakeUpPictures {
    MakeUpApi ip = null;
    public static String nameString = "";

    /*
     * search is the public GetPicture method.  Its arguments are the search term, and the InterestingPicture object that called it.  This provides a callback
     * path such that the pictureReady method in that object is called when the picture is available from the search.
     */
    public void search(String searchTerm, MakeUpApi ip) {
        System.out.println("GetPicture");
        this.ip = ip;
        new AsyncFlickrSearch().execute(searchTerm);
    }

    /*
     * AsyncTask provides a simple way to use a thread separate from the UI thread in which to do network operations.
     * doInBackground is run in the helper thread.
     * onPostExecute is run in the UI thread, allowing for safe UI updates.
     */
    private class AsyncFlickrSearch extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urls) {
            System.out.println("doInBackground");
            return search(urls[0]);
        }

        protected void onPostExecute (Bitmap picture) {
            System.out.append("onPostExecute");
            ip.pictureReady(picture);
        }

        /*
         * Search Flickr.com for the searchTerm argument, and return a Bitmap that can be put in an ImageView
         */
        private Bitmap search(String searchTerm) {
            System.out.println("AsyncFlickrSearch.search");
            String pictureURL = "";
            System.out.println("searchTerm "+searchTerm);
            Document doc = getRemoteXML("http://10.0.2.2:8080/Project4Task1/Servlet/" + searchTerm);
            if(doc == null) System.out.println("error for null");
            /////////if doc == null every thing null dior eyeline
            String xmlString = "";
            try
            {
                DOMSource domSource = new DOMSource(doc);
                StringWriter writer = new StringWriter();
                StreamResult result = new StreamResult(writer);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.transform(domSource, result);
                xmlString = writer.toString();
            }
            catch(TransformerException ex)
            {
                ex.printStackTrace();

            }
            System.out.println(xmlString);
            if(!xmlString.contains("<image>")) {
                return null;
            }
            int leftName = xmlString.indexOf("<name>");
            int rightName = xmlString.indexOf("</name>");
            int tagNameLen = "<name>".length();
            leftName = leftName +tagNameLen;

            nameString = xmlString.substring(leftName, rightName);
            System.out.println("name "+ nameString);
            int left = xmlString.indexOf("<image>");
            int right = xmlString.indexOf("</image>");
            int tagLen = "<image>".length();
            left = left + tagLen +1 ;
            right--;
            pictureURL = xmlString.substring(left, right);
            System.out.println("xml "+xmlString);
            System.out.println("picurl "+pictureURL);
            ////////////////////////////////////////////////
//            NodeList nl = doc.getElementsByTagName("message");
//            if(nl == null) System.out.println("nl is null");
//            if (nl.getLength() == 0) {
//                return null; // no pictures found
//            } else {
//                Element element = (Element) nl.item(0);
//                pictureURL = element.getTextContent();
//                System.out.println(pictureURL);
                //////////////////////////////////////////
//                int pic = new Random().nextInt(nl.getLength()); //choose a random picture
//                Element e = (Element) nl.item(pic);
//                String farm = e.getAttribute("farm");
//                String server = e.getAttribute("server");
//                String id = e.getAttribute("id");
//                String secret = e.getAttribute("secret");
//                pictureURL = "http://farm"+farm+".static.flickr.com/"+server+"/"+id+"_"+secret+"_z.jpg";
         //   }
            // At this point, we have the URL of the picture that resulted from the search.  Now load the image itself.
            try {
                URL u = new URL(pictureURL);
                return getRemoteImage(u);
            } catch (Exception e) {
                e.printStackTrace();
                return null; // so compiler does not complain
            }

        }

        /*
         * Given a url that will request XML, return a Document with that XML, else null
         */
        private Document getRemoteXML(String url) {
            System.out.println("getRemoteXML");
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource(url);
                return db.parse(is);
            } catch (Exception e) {
                System.out.print("Yikes, hit the error: "+e);
                return null;
            }
        }

        /*
         * Given a URL referring to an image, return a bitmap of that image
         */
        private Bitmap getRemoteImage(final URL url) {
            System.out.println("getRemoteImage");
            try {
                final URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Bitmap bm = BitmapFactory.decodeStream(bis);
                bis.close();
                return bm;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
