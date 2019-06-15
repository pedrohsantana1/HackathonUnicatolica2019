package com.pedro.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class Jogo extends ApplicationAdapter {
	//Texturas
	private SpriteBatch batch;
	private Texture[] fundo;
	private Texture[] personagem;
	private Texture pequi;
	private BitmapFont textoEstado0;
	private BitmapFont textoEstado1;
	private BitmapFont textoEstado2;



	//Atributos de configuração
	private float larguraDispositivo;
	private float alturaDispositivo;
	private float gravidade = 0;
	private float posPersonagemY;
	private float posicaoTela = 0;
	private Random random;
	private int posicaoFruta;
	private int bg = 0;

	@Override
	public void create () {
		inicializarObjetos();
		inicializarTexturas();



	}
	private void inicializarObjetos(){
		batch = new SpriteBatch();
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		posPersonagemY = alturaDispositivo/2 - alturaDispositivo/4;
		posicaoTela = larguraDispositivo;
		random = new Random();
		textoEstado0 = new BitmapFont();
		textoEstado0.setColor(com.badlogic.gdx.graphics.Color.WHITE);
		textoEstado0.getData().setScale(3);
		textoEstado1 = new BitmapFont();
		textoEstado1.setColor(com.badlogic.gdx.graphics.Color.WHITE);
		textoEstado1.getData().setScale(3);
		textoEstado2 = new BitmapFont();
		textoEstado2.setColor(com.badlogic.gdx.graphics.Color.WHITE);
		textoEstado2.getData().setScale(3);
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
			bg = (bg==2)?0:++bg;
		}

		if(posPersonagemY > 0 || toqueTela ) {
			if(posPersonagemY + gravidade <=0)
			{
				posPersonagemY = 0;
			}
			else {
				posPersonagemY = posPersonagemY + gravidade;
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
			posicaoFruta = random.nextInt((int) alturaDispositivo-pequi.getHeight());
		}
	}

	@Override
	public void dispose () {

	}


	private void desenharTexturas(){
		batch.draw(fundo[bg],posicaoTela,0, larguraDispositivo, alturaDispositivo);
		batch.draw(fundo[bg],posicaoTela-larguraDispositivo,0, larguraDispositivo, alturaDispositivo);
		batch.draw(personagem[0], larguraDispositivo/2-personagem[0].getWidth(), posPersonagemY);
		batch.draw(pequi, posicaoTela, posicaoFruta);

		switch (bg){
			case 0:
				textoEstado0.draw(batch, "Seu pedido está em produção", 0, (int)(alturaDispositivo*0.9));
				break;
			case 1:
				textoEstado1.draw(batch, "Seu pedido saiu para entrega", 0, (int)(alturaDispositivo*0.9));
				break;
			case 2:
				textoEstado2.draw(batch, "Seu pedido chegou!", 0, (int)(alturaDispositivo*0.9));
				break;
		}



	}



}
