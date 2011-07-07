package br.com.cooperalfa.model;

import java.beans.PropertyChangeListener;

import javax.swing.annotation.Bindable;

@Bindable
public class Cliente {
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
    }

    private String nome;

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }

}
