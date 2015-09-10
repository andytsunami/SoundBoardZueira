package com.example.soundboardzueira;

import java.io.IOException;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
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
/*
		// RISADA DO CHAVES
		Button risada = (Button) findViewById(R.id.risada);

		risada.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				toca(MediaPlayer.create(InicioActivity.this, R.raw.risada));

			}
		});
		
		
		//Ai que delicia
		Button delicia =  (Button) findViewById(R.id.delicia);
		delicia.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toca(MediaPlayer.create(InicioActivity.this, R.raw.delicia));
			}
		});
		
		// Risada do prassa
		Button cazalbe = (Button) findViewById(R.id.cazalbe);
		cazalbe.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toca(MediaPlayer.create(InicioActivity.this, R.raw.cazalbe));
				
			}
		});
		
		//Hino dos Huehuehue br
		Button huehue = (Button) findViewById(R.id.huehue);
		huehue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toca(MediaPlayer.create(InicioActivity.this, R.raw.huehue));
				
			}
		});
		*/
		//Ratinho
		Button ratinho = (Button) findViewById(R.id.ratinho);
		ratinho.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toca(getString(R.string.ratinhoUri));
				//toca(MediaPlayer.create(InicioActivity.this, Uri.parse("https://dl.dropboxusercontent.com/u/35720465/sons/ratinho.mp3")));
				
			}
		});

	}

	protected void toca(String uri) {
		Uri myUri = Uri.parse(uri);
		
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
	

}
