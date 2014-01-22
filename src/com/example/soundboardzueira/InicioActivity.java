package com.example.soundboardzueira;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class InicioActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listagem_sons);

		// RISADA DO CHAVES
		Button risada = (Button) findViewById(R.id.risada);

		risada.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MediaPlayer player = MediaPlayer.create(InicioActivity.this, R.raw.risada);
				player.start();

			}
		});
		
		Button delicia =  (Button) findViewById(R.id.delicia);
		delicia.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MediaPlayer player = MediaPlayer.create(InicioActivity.this, R.raw.delicia);
				player.start();
				
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inicio, menu);
		return true;
	}

}
