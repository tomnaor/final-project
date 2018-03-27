package org.eliconcepts.geekking;

import android.os.AsyncTask;

import com.felipecsl.gifimageview.library.GifImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class Gif extends AsyncTask<String, Void, byte[]> {

    private GifImageView gif;

    Gif(GifImageView gif){
        this.gif = gif;
    }

    @Override
    protected byte[] doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == 200) {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[1024];
                while ((nRead = in.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                return buffer.toByteArray();
            }
        } catch(MalformedURLException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } return null;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            gif.setBytes(bytes);
        }
}
