package com.example.soundboardzueira;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

public class UrlTask extends AsyncTask<String, Void, Drawable>{

	@Override
	protected Drawable doInBackground(String... urls) {
		
		Bitmap bmp;
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(urls[0]).openConnection();
		} catch (MalformedURLException e) {
			Log.i("IMAGEM", "Caminho da imagem incorreto");
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("IMAGEM", "Não foi possivel se conecatar com a URL da imagem.");
			e.printStackTrace();
		}
		try {
			conn.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("IMAGEM", "Não foi possivel se conecatar com a URL da imagem.");
			e.printStackTrace();
		}
		InputStream inputStream = null;
		try {
			inputStream = conn.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("IMAGEM", "Não foi possivel ler o conteudo da imagem.");
			e.printStackTrace();
		}
		
		bmp = BitmapFactory.decodeStream(inputStream);
		
		return new BitmapDrawable(bmp);
		
	}
	
	@Override
	protected void onPostExecute(Drawable result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
	
	
	

}
