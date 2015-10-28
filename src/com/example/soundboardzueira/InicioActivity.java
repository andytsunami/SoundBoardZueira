package com.example.soundboardzueira;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.soundboardzueira.dto.SomDTO;

public class InicioActivity extends Activity {
	
	private MediaPlayer player = new MediaPlayer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listagem_sons);
		
		new DownloadJsonAsyncTask().execute("https://dl.dropboxusercontent.com/u/35720465/sons/sons.json");
		
	}

	protected void toca(String uri) {
		Uri myUri = Uri.parse(uri);
		
		/*
		SomDAO somDAO = new SomDAO(InicioActivity.this);
		
		String nomeSom = recuperaNome(uri);
		if(somDAO.existe(nomeSom)){
			Som som = somDAO.buscarPorNome(nomeSom);
			
		} 
		*/
		
		player.reset();
		//midia = new MediaPlayer();
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		
		try {
			player.setDataSource(getApplicationContext(),myUri);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			player.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		player.start();

		/*if(player.isPlaying()){
			player.stop();
			player = midia;
		} else {
			player = midia;
		}
		player.start();*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inicio, menu);
		return true;
	}
	
	private String recuperaNome(String uri){
		String[] partes = uri.split("/");
		String nomeArquivo = partes[partes.length -1];
		
		return nomeArquivo.replace(".mp3", "");
	}
	
	class DownloadJsonAsyncTask extends AsyncTask<String, Void, List<SomDTO>>{

		ProgressDialog dialogo;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialogo = ProgressDialog.show(InicioActivity.this,"Segure o reggae!","Atualizando a lista de sons...");
			
		}
		
		@Override
		protected List<SomDTO> doInBackground(String... params) {
			String url = params[0];
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			
			try {
				HttpResponse response = httpClient.execute(httpGet);
				HttpEntity entity = response.getEntity();
				
				if(entity != null){
					InputStream instream = entity.getContent();
					String json = toString(instream);
					instream.close();
					
					List<SomDTO> sonsList = getSomDto(json);
					
					return sonsList;
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(List<SomDTO> result) {
			super.onPostExecute(result);
			dialogo.dismiss();
			
			if(result != null){
				
				LayoutInflater layoutInflater = InicioActivity.this.getLayoutInflater();
				
				for (final SomDTO somDTO : result) {
						
					
						//Button botao = new Button(InicioActivity.this);
						Button botao = (Button) layoutInflater.inflate(R.layout.modelo_botao, null);
						
					
						AsyncTask<String,Void,Drawable> execute = new UrlTask().execute(somDTO.getImage());
						
						Drawable drawable = null;
						try {
							drawable = execute.get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						
						//botao.setCompoundDrawables(null, null, drawable, null);
						botao.setText(somDTO.getNome());
						botao.setBackground(drawable);
						
					
						botao.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							toca(somDTO.getUrl());
						}
					});
					
					
					LinearLayout direita = (LinearLayout) findViewById(R.id.layoutdireita);
					direita.addView(botao);
					
				}
			}
			
		}
		
		private String toString(InputStream is) throws IOException{
			byte[] bytes = new byte[1024];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int lidos;
			while((lidos = is.read(bytes)) > 0){
				baos.write(bytes,0,lidos);
			}
			
			return new String(baos.toByteArray());
			
		}
		
		private List<SomDTO> getSomDto(String jsonString){
			ArrayList<SomDTO> sons = new ArrayList<SomDTO>();
			
			try{
				JSONArray somLists = new JSONArray(jsonString);
				JSONObject somList = somLists.getJSONObject(0);
				JSONArray sonsArray = somList.getJSONArray("sons");
				
				JSONObject somDTO;
				
				for ( int i = 0 ;i < sonsArray.length(); i++) {
					somDTO = new JSONObject(sonsArray.getString(i));
					
					Log.i("ZACA", "nome = " + somDTO.getString("nome"));
					
					SomDTO som = new SomDTO();
					som.setNome(somDTO.getString("nome"));
					som.setUrl(somDTO.getString("url"));
					som.setImage(somDTO.getString("image"));
					
					sons.add(som);
				}
				
			} catch (JSONException e){
				Log.e("ZACA", "Erro no parsing do JSON", e);
			}
			
			return sons;
		}
		
	}

	
}