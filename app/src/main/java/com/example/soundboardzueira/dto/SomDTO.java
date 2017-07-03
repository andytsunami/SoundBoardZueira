package com.example.soundboardzueira.dto;

import java.io.Serializable;

public class SomDTO implements Serializable{

	private static final long serialVersionUID = 142543101130164424L;
	private String nome;
	private String url;
	private String image;
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	
}
