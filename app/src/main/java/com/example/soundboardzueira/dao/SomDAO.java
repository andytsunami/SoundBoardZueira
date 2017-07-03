package com.example.soundboardzueira.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.soundboardzueira.model.Som;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SomDAO {
	
	private static final int VERSAO = 1;
	private static final String BANCO = "SonsZuera";
	private static final String TABELA = "SOM";
	private static final String COLUNAS[] = {"id", "nome", "binario"};
	
	private final SQLiteOpenHelper sqlHelper;
	private final Context context;
	
	public SomDAO(Context context) {
		this.context = context;
		this.sqlHelper = new SQLiteOpenHelper(this.context, SomDAO.BANCO, null, SomDAO.VERSAO) {
			
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				aoAtualizar(db, oldVersion, newVersion);
			}
			
			@Override
			public void onCreate(SQLiteDatabase db) {
				aoCriar(db);
			}
			
			@Override
			public synchronized void close() {
				super.close();
			}
		};
	}

	public void aoCriar(SQLiteDatabase sqlDdb) {
		String create = "CREATE TABLE " + TABELA + "(id INTEGER PRIMARY KEY, nome TEXT UNIQUE NOT NULL, binario BLOB);";
		sqlDdb.execSQL(create);
	}
	
	
	public void aoAtualizar(SQLiteDatabase db, int oldVersion, int newVersion) {
		//TODO Mudar na proxima versão do banco. Importante manter os sons já baixados
		String sql = "DROP TABLE IF EXISTS " + TABELA;
		db.execSQL(sql);
	}
	
	public void insere(Som som){
		ContentValues valores = toValues(som);
		sqlHelper.getWritableDatabase().insert(TABELA, null, valores);
	}
	
	public void fecha(){
		this.sqlHelper.close();
	}
	
	public List<Som> getLista(){
		ArrayList<Som> sons = new ArrayList<Som>();
		Cursor cursor = this.sqlHelper.getWritableDatabase().query(TABELA, COLUNAS, null, null, null, null, null);
		while(cursor.moveToNext()){
			Som som = new Som();
			som.setId(cursor.getLong(0));
			som.setNome(cursor.getString(1));
			som.setBinario(cursor.getBlob(2));
			
			sons.add(som);
		}
		
		cursor.close();
		return sons;
	}
	
	public void alterar(Som som){
		String[] args = { som.getId().toString() };
		ContentValues values = toValues(som);
		this.sqlHelper.getWritableDatabase().update(TABELA, values, "id=?", args);
	}
	
	public void deletar(Som som){
		String[] args = {som.getId().toString()};
		this.sqlHelper.getWritableDatabase().delete(TABELA, "id=?", args);
	}
	
	public boolean existe(String nome){
		Cursor cursor = this.sqlHelper.getReadableDatabase().rawQuery("SELECT nome FROM " + TABELA + " WHERE nome = ?", new String[]{nome});
		int count = cursor.getCount();
		
		cursor.close();
		
		return count > 0;
		
	}
	
	public Som buscarPorNome(String nome){
		Cursor cursor = this.sqlHelper.getReadableDatabase().rawQuery("SELECT * FROM " + TABELA + " WHERE nome = ?", new String[]{nome});
		Som som = new Som();
		while(cursor.moveToNext()){
			som.setId(cursor.getLong(0));
			som.setNome(cursor.getString(1));
			som.setBinario(cursor.getBlob(2));
		}
		
		return som;
	}
	
	private ContentValues toValues(Som som){
		ContentValues valores = new ContentValues();
		
		valores.put("nome", som.getNome());
		valores.put("binario", som.getBinario());
		
		return valores;
	}

}