package com.example.soundboardzueira;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.soundboardzueira.dao.SomDAO;
import com.example.soundboardzueira.dto.SomDTO;
import com.example.soundboardzueira.model.Som;

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
		
		MyDownloadTask downloadTask = new MyDownloadTask();
		downloadTask.execute(myUri.toString());
		
		SomDAO somDAO = new SomDAO(InicioActivity.this);
		FileInputStream fis = null;
		
		String nomeSom = recuperaNome(uri);
		if(somDAO.existe(nomeSom)){
			//System.out.println("Som achado: " + nomeSom);
			try {
				Som som = somDAO.buscarPorNome(nomeSom);
				File tempMp3 = File.createTempFile("temp", "mp3", getCacheDir());
				tempMp3.deleteOnExit();
				FileOutputStream fos = new FileOutputStream(tempMp3);
				fos.write(som.getBinario());
				fos.close();
				fis = new FileInputStream(tempMp3);
			} catch( Exception e) {
				e.printStackTrace();
			}
			
		} else {
			try {
				MyDownloadTask task = new MyDownloadTask();
				byte[] arquivo = task.execute(myUri.toString()).get();
	            
	            Som som = new Som();
	            som.setNome(nomeSom);
	            som.setBinario(arquivo);
	            
	            somDAO.insere(som);
	            
	           
	            
				File tempMp3 = File.createTempFile("temp", "mp3", getCacheDir());
				tempMp3.deleteOnExit();
				FileOutputStream fos = new FileOutputStream(tempMp3);
				fos.write(som.getBinario());
				fos.close();
				fis = new FileInputStream(tempMp3);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
		player.reset();
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		
		try {
			if(fis != null) {
				player.setDataSource(fis.getFD());
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			player.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		player.start();
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
			
			int id = 0;  
			if(result != null){
				
				LayoutInflater layoutInflater = InicioActivity.this.getLayoutInflater();
				for (final SomDTO somDTO : result) {
						
					
						Button botao = new Button(InicioActivity.this);
						//Button botao = (Button) layoutInflater.inflate(R.layout.modelo_botao, null);
						
						
					
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
						
						
						botao.setWidth(128);
						botao.setPadding(14, 14, 14, 14);
						botao.setHeight(128);
						
						botao.setBackground(drawable);
						
						botao.setId(id);
						
						
						TextView texto = new TextView(InicioActivity.this);
						texto.setText(somDTO.getNome());
						texto.setTextSize(20);
						texto.setId(id);
						
						

						botao.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							toca(somDTO.getUrl());
						}
					});
					
					if(id % 2 == 0){
						LinearLayout direita = (LinearLayout) findViewById(R.id.layoutdireita);
						direita.addView(botao);
						direita.addView(texto);
						
					} else {
						LinearLayout esquerda = (LinearLayout) findViewById(R.id.layoutesquerda);
						esquerda.addView(botao);
						esquerda.addView(texto);
					}
					
					id = id + 1;
					
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
	
	class MyDownloadTask extends AsyncTask<String,String,byte[]>
	{
	    protected byte[] doInBackground(String... params) {
	    	try {
	    		URL url = new URL(params[0]);
	    		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
	    		//System.out.println(httpConn.getResponseCode());
	    		InputStream inputStream = httpConn.getInputStream();
	    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    		
	    		int bytesRead = -1;
	            byte[] buffer = new byte[1024];
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	            	bos.write(buffer, 0, bytesRead);
	            }
	            
	            httpConn.disconnect();
	            
	            return bos.toByteArray();
	    	} catch(Exception e) {
	    		e.printStackTrace();
	    	}
	    	
	    	return null;
	    	
	 }
	}

	
}