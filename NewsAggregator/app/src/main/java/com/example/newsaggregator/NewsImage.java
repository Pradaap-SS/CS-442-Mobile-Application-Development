package com.example.newsaggregator;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.io.InputStream;

class NewsImage extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        MainActivity mainActivity;
        public NewsImage(ImageView imageView, MainActivity mainActivity) {
            this.imageView = imageView;
            this.mainActivity = mainActivity;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitMap;
            try {
                InputStream in = new java.net.URL(strings[0]).openStream();
                bitMap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                bitMap = null;
                e.printStackTrace();
            }
            return bitMap;
        }
        protected void onPostExecute(Bitmap result) {
            if (result == null) {
                imageView.setImageResource(mainActivity.getResources().getIdentifier("brokenimage", "drawable", mainActivity.getPackageName()));
            } else {
                imageView.setImageBitmap(result);
            }
        }
}
