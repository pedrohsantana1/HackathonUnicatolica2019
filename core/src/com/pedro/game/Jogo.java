package com.pedro.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;
import java.util.Timer;

public class Jogo extends ApplicationAdapter {
	//Texturas
	private SpriteBatch batch;
	private Texture[] fundo;
	private Texture[] personagem;
	private Texture[] alimento;
	private BitmapFont textoEstado0;
	private BitmapFont textoEstado1;
	private BitmapFont textoEstado2;
	private Texture textoProducao;
	private Texture textoEntrega;
	private Texture textoChegou;

	//Formas para colisao
	private ShapeRenderer shapeRenderer;
	private Circle circuloPersonagem;
	private Circle circuloAlimento;

	//Atributos de configuração
	private float larguraDispositivo;
	private float alturaDispositivo;
	private float gravidade = 0;
	private float posPersonagemY;
	private float posicaoTela = 0;
	private Random random;
	private int posicaoFruta;
	private int bg = 0;
	private int estado = 0;
	private float contador;
	private double tempoInicial;

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
		shapeRenderer = new ShapeRenderer();
		circuloPersonagem = new Circle();
		circuloAlimento = new Circle();

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
		alimento = new Texture[3];
		alimento[0] = new Texture("pequi.png");
		alimento[1] = new Texture("pizza.png");
		alimento[2] = new Texture("pizza.png");
		textoProducao = new Texture("textoProducao.png");
		textoEntrega = new Texture("textoEntrega.png");
		textoChegou = new Texture("textoChegou.png");
	}

	@Override
	public void render () {

		verificarEstadoJogo();
		detectarColisoes();
	}

	private void verificarEstadoJogo(){
		batch.begin();
		switch(estado){
			case 0: // init
				contador = 7*1000;
				tempoInicial = System.currentTimeMillis();
				estado = 1;
				break;
			case 1: // Em produção
				if(System.currentTimeMillis() - tempoInicial >= contador){
					contador = 7*1000;
					tempoInicial = System.currentTimeMillis();
					estado = 2;
					bg = 1;
				}
				break;
			case 2: // Em entrega
				if(System.currentTimeMillis() - tempoInicial >= contador){
					contador = 7*1000;
					tempoInicial = System.currentTimeMillis();
					estado = 3;
					bg = 2;
				}
				break;
			case 3: // Chegou
				break;
		}

		boolean toqueTela = Gdx.input.justTouched();

		if( toqueTela ){
			gravidade = 25;
			//bg = (bg==2)?0:++bg;
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
			posicaoFruta = random.nextInt((int) alturaDispositivo-alimento[0].getHeight()-textoProducao.getHeight());
		}
	}

	@Override
	public void dispose () {

	}


	private void desenharTexturas(){




		switch (bg){
			case 0:
				//textoEstado0.draw(batch, "Seu pedido está em produção", 0, (int)(alturaDispositivo*0.9));
				batch.draw(fundo[0],posicaoTela,0, larguraDispositivo, alturaDispositivo);
				batch.draw(fundo[0],posicaoTela-larguraDispositivo,0, larguraDispositivo, alturaDispositivo);
				batch.draw(textoProducao,larguraDispositivo/2 - textoProducao.getWidth()/2, alturaDispositivo - textoProducao.getHeight()-50);
				batch.draw(personagem[0], larguraDispositivo/2-personagem[0].getWidth(), posPersonagemY);
				batch.draw(alimento[0], posicaoTela, posicaoFruta);

				break;
			case 1:
				batch.draw(fundo[1],posicaoTela,0, larguraDispositivo, alturaDispositivo);
				batch.draw(fundo[1],posicaoTela-larguraDispositivo,0, larguraDispositivo, alturaDispositivo);
				batch.draw(textoEntrega,larguraDispositivo/2 - textoProducao.getWidth()/2, alturaDispositivo - textoProducao.getHeight()-50);
				batch.draw(personagem[1], larguraDispositivo/2-personagem[0].getWidth(), posPersonagemY);
				batch.draw(alimento[1], posicaoTela, posicaoFruta);

				break;
			case 2:
				batch.draw(fundo[2],0,0, larguraDispositivo, alturaDispositivo);
				batch.draw(textoChegou,larguraDispositivo/2 - textoProducao.getWidth()/2, alturaDispositivo - textoProducao.getHeight()-50);
				batch.draw(personagem[2], larguraDispositivo/2-personagem[0].getWidth(), posPersonagemY);
				break;
		}




	}

	private void detectarColisoes() {
		circuloPersonagem.set(
				larguraDispositivo / 2 - personagem[bg].getWidth() / 2, posPersonagemY + personagem[bg].getHeight() / 2, personagem[bg].getHeight() / 2
		);
		circuloAlimento.set(
				posicaoTela + alimento[bg].getWidth() / 2, posicaoFruta + alimento[bg].getHeight() / 2, alimento[bg].getWidth() / 2
		);
		boolean colidiuObjetos = Intersector.overlaps(circuloPersonagem, circuloAlimento);

		if(colidiuObjetos){

		}
		/*
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.BLACK);

		shapeRenderer.circle( larguraDispositivo/2 - personagem[bg].getWidth()/2, posPersonagemY + personagem[bg].getHeight()/2, personagem[bg].getHeight()/2);
		shapeRenderer.circle( posicaoTela + alimento[bg].getWidth()/2 , posicaoFruta + alimento[bg].getHeight()/2, alimento[bg].getWidth()/2);
		*/

		shapeRenderer.end();
	}

}
