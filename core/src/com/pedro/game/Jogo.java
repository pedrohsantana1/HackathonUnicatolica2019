package com.pedro.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Jogo extends ApplicationAdapter {
	//Texturas
	private SpriteBatch batch;
	private Texture[] fundo;
	private Texture[] personagem;
	private Texture pequi;


	//Atributos de configuração
	private float larguraDispositivo;
	private float alturaDispositivo;
	private float gravidade = 0;
	private float posicaoInicialVerticalPersonagem;
	private float posicaoTela = 0;

	@Override
	public void create () {
		inicializarObjetos();
		inicializarTexturas();



	}
	private void inicializarObjetos(){
		batch = new SpriteBatch();
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		posicaoInicialVerticalPersonagem = alturaDispositivo/2 - alturaDispositivo/4;
		posicaoTela = larguraDispositivo;
	}

	private void inicializarTexturas(){
		fundo = new Texture[3];
		fundo[0] = new Texture("fundo1.png");
		fundo[1] = new Texture("fundo2.png");
		fundo[2] = new Texture("fundo3.png");
		personagem = new Texture[3];
		personagem[0] = new Texture("personagem1.png");
		personagem[1] = new Texture("personagem2m1.png");
		personagem[2] = new Texture("personagem2m2.png");
		pequi = new Texture("pequi.png");

	}

	@Override
	public void render () {
		verificarEstadoJogo();
	}

	private void verificarEstadoJogo(){
		batch.begin();

		boolean toqueTela = Gdx.input.justTouched();

		if( toqueTela ){
			gravidade = 25;

		}

		if(posicaoInicialVerticalPersonagem > 0 || toqueTela ) {
			if(posicaoInicialVerticalPersonagem + gravidade <=0)
			{
				posicaoInicialVerticalPersonagem = 0;
			}
			else {
				posicaoInicialVerticalPersonagem = posicaoInicialVerticalPersonagem + gravidade;
			}
		}
		desenharTexturas();

		gravidade--;

		movimentarFundo();

		batch.end();

	}
	private void movimentarFundo(){
		//Movimentar fundo
		posicaoTela = posicaoTela - Gdx.graphics.getDeltaTime()*200;
		if(posicaoTela <= 0){
			posicaoTela = larguraDispositivo;
		}
	}

	@Override
	public void dispose () {

	}


	private void desenharTexturas(){
		batch.draw(fundo[1],posicaoTela,0, larguraDispositivo, alturaDispositivo);
		batch.draw(fundo[1],posicaoTela-larguraDispositivo,0, larguraDispositivo, alturaDispositivo);
		batch.draw(personagem[0], 0, posicaoInicialVerticalPersonagem);
		batch.draw(pequi, posicaoTela, 300);



	}



}
