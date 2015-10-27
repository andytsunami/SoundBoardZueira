package com.example.soundboardzueira;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.soundboardzueira.dao.SomDAO;
import com.example.soundboardzueira.model.Som;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class InicioActivity extends Activity {
	
	private MediaPlayer player = new MediaPlayer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listagem_sons);
		// RISADA DO CHAVES
		Button risada = (Button) findViewById(R.id.risada);

		risada.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				toca(getString(R.string.risadaUri));

			}
		});
		
		// Risada do prassa
		Button cazalbe = (Button) findViewById(R.id.cazalbe);
		cazalbe.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toca(getString(R.string.cazalbeUri));
				
			}
		});
		
		//Ai que delicia
		Button delicia =  (Button) findViewById(R.id.delicia);
		delicia.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toca(getString(R.string.deliciaUri));
			}
		});
		
		//Que viadão bonito
		Button viadao =  (Button) findViewById(R.id.viadao);
		viadao.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toca(getString(R.string.viadaoUri));
			}
		});
		
		//Hino dos Huehuehue br
		Button hinoHue = (Button) findViewById(R.id.hinoHue);
		hinoHue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toca(getString(R.string.hinoHueUri));
				
			}
		});
		
		//Huehuehue
		Button huehue = (Button) findViewById(R.id.huehue);
		huehue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toca(getString(R.string.huehueUri));
					
			}
		});
				
		
		//Ratinho
		Button ratinho = (Button) findViewById(R.id.ratinho);
		ratinho.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toca(getString(R.string.ratinhoUri));
				
			}
		});
		
		//Moises
		Button moises = (Button) findViewById(R.id.moises);
		moises.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				toca(getString(R.string.moisesUri));
						
					}
				});

		//Ratinho
		Button morrediabo = (Button) findViewById(R.id.morrediabo);
		morrediabo.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				toca(getString(R.string.morrediaboUri));
						
					}
				});
		//UEPA
		Button uepa = (Button) findViewById(R.id.uepa);
		uepa.setOnClickListener(new View.OnClickListener() {
							
			@Override
			public void onClick(View v) {
				toca(getString(R.string.uepaUri));
								
					}
				});
		//ERROU
		Button errou = (Button) findViewById(R.id.errou);
		errou.setOnClickListener(new View.OnClickListener() {
									
			@Override
			public void onClick(View v) {
				toca(getString(R.string.errouUri));
									
					}
				});
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
