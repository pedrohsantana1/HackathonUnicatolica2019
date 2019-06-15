package com.pedro.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

public class Jogo extends ApplicationAdapter {
	private Random random;
	//Texturas
	private SpriteBatch batch;
	private Texture[] fundo;
	private Texture[] personagem;
	private Texture[] alimento;
	private BitmapFont textoScore;
	private Texture textoProducao;
	private Texture textoEntrega;
	private Texture textoChegou;
	private ArrayList<Item> itens;

	//Formas para colisao
	private ShapeRenderer shapeRenderer;
	private Circle circuloPersonagem;
	private Circle circuloAlimento;

	//Atributos de configuração
	private float larguraDispositivo;
	private float alturaDispositivo;
	private float gravidade = 0;
	private int estadoBg = 0;
	private int estadoJogo = 0;
	private int pontos = 0;
	private int velocidade = 3;

	private float posPersonagemY;
	private float posTelaX = 0;
	private float posAlimentoX;
	private int posAlimentoY;

	private double tempoInicial;
	private float tempoTotal;
	private double tempoInicialItem;

	private boolean teste = false;
	

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
		posTelaX = larguraDispositivo;
		posAlimentoX = larguraDispositivo;
		random = new Random();
		textoScore = new BitmapFont();
		textoScore.setColor(com.badlogic.gdx.graphics.Color.WHITE);
		textoScore.getData().setScale(5);

		shapeRenderer = new ShapeRenderer();
		circuloPersonagem = new Circle();
		circuloAlimento = new Circle();
		itens = new ArrayList<Item>();


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

		verificarestadoJogoJogo();
		detectarColisoes();
	}

	private void verificarestadoJogoJogo(){

		batch.begin();
		switch(estadoJogo){
			case 0: // init
				tempoTotal = 20000;
				tempoInicial = System.currentTimeMillis();
				tempoInicialItem = System.currentTimeMillis();
				
				estadoJogo = 1;
				break;
			case 1: // Em produção
				if (System.currentTimeMillis() - tempoInicialItem >= 1000){
					tempoInicialItem = System.currentTimeMillis();
					novoAlimento(random.nextInt( (int)(larguraDispositivo) ), (int)(alturaDispositivo), random.nextInt(3));
				}
				for (Item item : itens) {

					if(item.y - velocidade< 0 - alimento[0].getHeight())
					{
						itens.remove(item);
					}
					item.y -= velocidade;

				}
				if(System.currentTimeMillis() - tempoInicial >= tempoTotal){
					tempoTotal = 7000;
					tempoInicial = System.currentTimeMillis();
					estadoJogo = 2;
					estadoBg = 1;
				}
				posAlimentoX -= velocidade;
				break;
			case 2: // Em entrega
				if(System.currentTimeMillis() - tempoInicial >= tempoTotal){
					tempoTotal = 7000;
					tempoInicial = System.currentTimeMillis();
					estadoJogo = 3;
					estadoBg = 2;
				}
				posAlimentoX -= velocidade;
				break;
			case 3: // Chegou
				
				break;
		}

		boolean toqueTela = Gdx.input.justTouched();

		if( toqueTela ){
			gravidade = 25;
			//estadoBg = (estadoBg==2)?0:++estadoBg;
		}

		if(posPersonagemY > 0 || toqueTela ) {
			if(posPersonagemY + gravidade <=0){
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

	private void detectarColisoes() {
		circuloPersonagem.set(
				larguraDispositivo / 2 - personagem[estadoBg].getWidth() / 2, posPersonagemY + personagem[estadoBg].getHeight() / 2, personagem[estadoBg].getHeight() / 2
		);
		circuloAlimento.set(
				posAlimentoX + alimento[estadoBg].getWidth() / 2, posAlimentoY + alimento[estadoBg].getHeight() / 2, alimento[estadoBg].getWidth() / 2
		);
		boolean colidiuObjetos = Intersector.overlaps(circuloPersonagem, circuloAlimento);

		if (colidiuObjetos) {
			pontos++;
//			novoAlimento();

		/*
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.BLACK);

		shapeRenderer.circle( larguraDispositivo/2 - personagem[estadoBg].getWidth()/2, posPersonagemY + personagem[estadoBg].getHeight()/2, personagem[estadoBg].getHeight()/2);
		shapeRenderer.circle( posTelaX + alimento[estadoBg].getWidth()/2 , posAlimentoY + alimento[estadoBg].getHeight()/2, alimento[estadoBg].getWidth()/2);


		shapeRenderer.end();
		*/
		}
	}
	private void novoAlimento(int x, int y, int type){
		itens.add(new Item(x, y, type));
		
		
		posAlimentoX = larguraDispositivo;
		posAlimentoY = random.nextInt((int) alturaDispositivo-alimento[0].getHeight()-textoProducao.getHeight());
	}
	private void movimentarFundo(){
		//Movimentar fundo
		posTelaX -= velocidade;
		if(posTelaX <= 0){
			posTelaX = larguraDispositivo;
		}
	}

	@Override
	public void dispose () {

	}


	private void desenharTexturas(){
		switch (estadoBg){
			case 0:
				//textoestadoJogo0.draw(batch, "Seu pedido está em produção", 0, (int)(alturaDispositivo*0.9));
				batch.draw(fundo[0],posTelaX,0, larguraDispositivo, alturaDispositivo);
				batch.draw(fundo[0],posTelaX-larguraDispositivo,0,larguraDispositivo, alturaDispositivo);
				//batch.draw(fundo[0],posTelaX-larguraDispositivo,0,larguraDispositivo/2 - textoProducao.getWidth()/2 +larguraDispositivo, alturaDispositivo);
				batch.draw(textoProducao,larguraDispositivo/2 - textoProducao.getWidth()/2, alturaDispositivo - textoProducao.getHeight()-50);
				batch.draw(personagem[0], larguraDispositivo/2-personagem[0].getWidth(), posPersonagemY);
				batch.draw(alimento[0], posAlimentoX, posAlimentoY);
				for(Item item: itens){
					batch.draw(alimento[item.type],item.x, item.y);
				}
				textoScore.draw(batch, "Score: "+pontos,larguraDispositivo/2 - textoProducao.getWidth()/2 + 50 , alturaDispositivo - textoProducao.getHeight()-100);

				break;
			case 1:
				batch.draw(fundo[1],posTelaX,0, larguraDispositivo, alturaDispositivo);
				batch.draw(fundo[1],posTelaX-larguraDispositivo,0, larguraDispositivo, alturaDispositivo);
				batch.draw(textoEntrega,larguraDispositivo/2 - textoProducao.getWidth()/2, alturaDispositivo - textoProducao.getHeight()-50);
				batch.draw(personagem[1], larguraDispositivo/2-personagem[0].getWidth(), posPersonagemY);
				batch.draw(alimento[1], posAlimentoX, posAlimentoY);
				textoScore.draw(batch, "Score: "+pontos,larguraDispositivo/2 - textoProducao.getWidth()/2 + 50 , alturaDispositivo - textoProducao.getHeight()-100);

				break;
			case 2:
				batch.draw(fundo[2],0,0, larguraDispositivo, alturaDispositivo);
				batch.draw(textoChegou,larguraDispositivo/2 - textoProducao.getWidth()/2, alturaDispositivo - textoProducao.getHeight()-50);
				batch.draw(personagem[2], larguraDispositivo/2-personagem[0].getWidth(), posPersonagemY);
				textoScore.draw(batch, "Score: "+pontos,larguraDispositivo/2 - textoProducao.getWidth()/2 + 50 , alturaDispositivo - textoProducao.getHeight()-100);
				break;
		}
	}
}
