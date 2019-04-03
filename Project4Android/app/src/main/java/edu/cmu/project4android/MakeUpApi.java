package edu.cmu.project4android;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MakeUpApi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("//////////////////////////onClick");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * The click listener will need a reference to this object, so that upon successfully finding a picture from Flickr, it
         * can callback to this object with the resulting picture Bitmap.  The "this" of the OnClick will be the OnClickListener, not
         * this InterestingPicture.
         */
        final MakeUpApi ma = this;

        /*
         * Find the "submit" button, and add a listener to it
         */
        Button submitButton = (Button)findViewById(R.id.submit);


        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View viewParam) {
                String searchTerm = ((EditText)findViewById(R.id.searchTerm)).getText().toString();
                GetMakeUpPictures gp = new GetMakeUpPictures();
                gp.search(searchTerm, ma); // Done asynchronously in another thread.  It calls ip.pictureReady() in this thread when complete.
            }
        });
    }

    /*
     * This is called by the GetPicture object when the picture is ready.  This allows for passing back the Bitmap picture for updating the ImageView
     */
    public void pictureReady(Bitmap picture) {
        System.out.println("pictureReady");
        TextView resultTextViewName = (TextView) findViewById(R.id.resultName);
        TextView resultTextView = (TextView) findViewById(R.id.resultTV);
        ImageView pictureView = (ImageView)findViewById(R.id.dogPicture);
        TextView searchView = (EditText)findViewById(R.id.searchTerm);
        if (picture != null) {
            resultTextView.setText("This is a picture for " + ((EditText)findViewById(R.id.searchTerm)).getText().toString());
            resultTextViewName.setText("Product Name: " + GetMakeUpPictures.nameString);
            resultTextViewName.setVisibility(View.VISIBLE);
            pictureView.setImageBitmap(picture);
            pictureView.setVisibility(View.VISIBLE);
        } else {
            //resultTextView.setText("Sorry, I could not find a picture of a" + ((EditText)findViewById(R.id.searchTerm)).getText().toString());
            resultTextView.setText("Sorry, I could not find a picture based on what you input, try to change the input, or click submit again");
            pictureView.setImageResource(R.mipmap.ic_launcher);
            pictureView.setVisibility(View.INVISIBLE);
            resultTextViewName.setVisibility(View.INVISIBLE);
        }

        searchView.setText("");
        pictureView.invalidate();
    }
}
