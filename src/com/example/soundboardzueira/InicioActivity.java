package com.example.soundboardzueira;

import java.io.IOException;

import com.example.soundboardzueira.dao.SomDAO;
import com.example.soundboardzueira.model.Som;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
	

}
