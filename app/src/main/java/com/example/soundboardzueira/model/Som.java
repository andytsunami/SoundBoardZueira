package com.example.soundboardzueira.model;

import java.io.Serializable;

public class Som implements Serializable {
	
	private static final long serialVersionUID = 9189553136818753100L;
	private Long id;
	private String nome;
	private byte[] binario;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public byte[] getBinario() {
		return binario;
	}
	public void setBinario(byte[] binario) {
		this.binario = binario;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}