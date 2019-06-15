package com.pedro.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
	private Texture[] obstaculo;
	private BitmapFont textoScore;
	private BitmapFont textoProducaoAviso;
    private BitmapFont textoEntregaAviso;
    private BitmapFont textoChegouAviso;
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
	private float gravidadeX = 0;
	private int estadoBg = 0;
	private int estadoJogo = 0;
	private int pontos = 0;
	private int velocidade = 3;

	private float posPersonagemY;
	private float posPersonagemX;
	private float posTelaX = 0;
	private float posicaoObstaculoHorizontal = larguraDispositivo;
	private int posAlimentoY;
	private float gravidadePersonagem = 0;
	private int objeto = 0;

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
		posPersonagemY = (int)(alturaDispositivo*0.2);
		posPersonagemX  = larguraDispositivo/2;
		posTelaX = larguraDispositivo;
		random = new Random();
		textoScore = new BitmapFont();
        textoScore.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        textoScore.getData().setScale(5);

        textoProducaoAviso = new BitmapFont();
        textoProducaoAviso.setColor(Color.GREEN);
        textoProducaoAviso.getData().setScale((larguraDispositivo / 768)*2);


        textoEntregaAviso = new BitmapFont();
        textoEntregaAviso.setColor(Color.GREEN);
        textoEntregaAviso.getData().setScale(2);

        textoChegouAviso = new BitmapFont();
        textoChegouAviso.setColor(Color.GREEN);
        textoChegouAviso.getData().setScale(2);

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
		alimento = new Texture[4];
		alimento[0] = new Texture("pequi.png");
		alimento[1] = new Texture("pizza.png");
		alimento[2] = new Texture("tomate.png");
        alimento[3] = new Texture("bomba.png");

        //Instanciando obstaculos
        obstaculo = new Texture[3];
        obstaculo[0] = new Texture("obstaculo1.png");
        obstaculo[1] = new Texture("obstaculo2.png");
        obstaculo[2] = new Texture("obstaculo3.png");

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
				tempoTotal = 100;
				tempoInicial = System.currentTimeMillis();
				tempoInicialItem = System.currentTimeMillis();
				
				estadoJogo = 1;
				break;
			case 1: // Em produção
				if (System.currentTimeMillis() - tempoInicialItem >= 1000){
					tempoInicialItem = System.currentTimeMillis();
					int type = random.nextInt(4);
					novoAlimento(random.nextInt( (int)(larguraDispositivo - alimento[type].getWidth()) ), (int)(alturaDispositivo), type);
				}
				for (Item item : itens) {
					item.y -= velocidade;
				}
				if(System.currentTimeMillis() - tempoInicial >= tempoTotal){
					tempoTotal = 100000;
					tempoInicial = System.currentTimeMillis();
					estadoJogo = 2;
					estadoBg = 1;
				}
				break;
			case 2: // Em entrega
				if(System.currentTimeMillis() - tempoInicial >= tempoTotal){
					tempoTotal = 7000;
					tempoInicial = System.currentTimeMillis();
					estadoJogo = 3;
					estadoBg = 2;

				}
                boolean toqueTela2 = Gdx.input.isTouched();
                if( toqueTela2 ) {
                    gravidadePersonagem = 10;
                }

                break;
			case 3: // Chegou
				
				break;
		}

		//if(gravidadePersonagem <= posPersonagemY )
          //  gravidadePersonagem = 0;
		boolean toqueTela = Gdx.input.isTouched();

		if( toqueTela ) {
		    if(Gdx.input.getX() < larguraDispositivo/2){
		        if(posPersonagemX - velocidade < 0) posPersonagemX = 0;
		        else posPersonagemX -= velocidade*2;
            }
            else{
		        if(posPersonagemX + velocidade > larguraDispositivo - personagem[estadoBg].getWidth()) posPersonagemX = larguraDispositivo - personagem[estadoBg].getWidth();
		        else posPersonagemX += velocidade*2;
            }
        }

		desenharTexturas();
		movimentarFundo();
		movimentaObstaculo();

		batch.end();

	}

	private void detectarColisoes() {
		circuloPersonagem.set(
				posPersonagemX + personagem[estadoBg].getWidth() / 2, posPersonagemY + personagem[estadoBg].getHeight() / 2, personagem[estadoBg].getHeight() / 2
		);
        int tempColisao = -1;
        int posicao = -1;
        for (Item item : itens) {
            circuloAlimento.set( item.x + alimento[item.type].getWidth()/2 , item.y+ alimento[item.type].getHeight()/2, alimento[item.type].getWidth()/2);


            boolean colidiuObjetos = Intersector.overlaps(circuloPersonagem, circuloAlimento);

            if (colidiuObjetos) {

                tempColisao = itens.indexOf(item);
                posicao = item.type;

                break;
            }
        }

        if(tempColisao >0)
        {
            switch (posicao){
                case 0:
                    pontos = pontos + 1;
                    break;
                case 1:
                    pontos = pontos + 2;
                    break;
                case 2:
                    pontos = pontos + 3;
                    break;
                case 3:
                    pontos = pontos - 2;
                    break;
            }
            //pontos;
            itens.remove(tempColisao);
        }





//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.BLACK);
//        for (Item item : itens) {
//                shapeRenderer.circle( item.x + alimento[item.type].getWidth()/2 , item.y+ alimento[item.type].getHeight()/2, alimento[item.type].getWidth()/2);
//        }
//		shapeRenderer.circle( posPersonagemX + personagem[estadoBg].getWidth() / 2, posPersonagemY + personagem[estadoBg].getHeight()/2, personagem[estadoBg].getHeight()/2);
//		shapeRenderer.end();
	}

	private void novoAlimento(int x, int y, int type){
		itens.add(new Item(x, y, type));
	}

	private void movimentarFundo(){
		//Movimentar fundo
		posTelaX -= velocidade;
		if(posTelaX <= 0){
			posTelaX = larguraDispositivo;
		}
	}

	private void movimentaObstaculo(){
        posicaoObstaculoHorizontal -= velocidade;
        if(posicaoObstaculoHorizontal <= -obstaculo[objeto].getWidth()){
            posicaoObstaculoHorizontal = larguraDispositivo;
            objeto++;
        }
        if(objeto > 2){
            objeto = 0;
        }
    }

	@Override
	public void dispose () {
	}

	private void desenharTexturas(){
		switch (estadoBg){
			case 0:

				batch.draw(fundo[0],posTelaX,0, larguraDispositivo, alturaDispositivo);
				batch.draw(fundo[0],posTelaX-larguraDispositivo,0,larguraDispositivo, alturaDispositivo);

				batch.draw(personagem[0], posPersonagemX, posPersonagemY,larguraDispositivo*0.15f,larguraDispositivo*0.15f);
				for(Item item: itens){
					batch.draw(alimento[item.type],item.x, item.y,larguraDispositivo*0.10f,larguraDispositivo*0.10f);
				}
                textoProducaoAviso.draw(batch, "Colete os itens você pode ganhar desconto",
                        larguraDispositivo/2 + 100 - textoProducao.getWidth() , alturaDispositivo -
                        textoProducao.getHeight() - textoProducao.getHeight()/2 - 15);

				textoScore.draw(batch, "Score: "+pontos,larguraDispositivo/2 - textoProducao.getWidth()/2 + 50 , alturaDispositivo - textoProducao.getHeight()-100);

                batch.draw(textoProducao,larguraDispositivo/2 - textoProducao.getWidth()/2, alturaDispositivo -
                        textoProducao.getHeight()-50,larguraDispositivo*0.50f,larguraDispositivo*0.15f);
				break;
			case 1:
				batch.draw(fundo[1],posTelaX,0, larguraDispositivo, alturaDispositivo);
				batch.draw(fundo[1],posTelaX-larguraDispositivo,0, larguraDispositivo, alturaDispositivo);

				batch.draw(personagem[1], larguraDispositivo/2-personagem[0].getWidth(), posPersonagemY+gravidadePersonagem);
				batch.draw(obstaculo[objeto],posicaoObstaculoHorizontal, posPersonagemY - personagem[1].getHeight()/4);

                textoEntregaAviso.draw(batch, "Desvie dos objetos você pode ganhar frete grátis ",
                        larguraDispositivo/2+50 - textoProducao.getWidth() , alturaDispositivo -
                                textoProducao.getHeight() - textoProducao.getHeight()/2 - 15);
				textoScore.draw(batch, "Score: "+pontos,larguraDispositivo/2 - textoProducao.getWidth()/2 + 50 , alturaDispositivo - textoProducao.getHeight()-100);
                batch.draw(textoEntrega,larguraDispositivo/2 - textoProducao.getWidth()/2, alturaDispositivo - textoProducao.getHeight()-50);
				break;
			case 2:
				batch.draw(fundo[2],0,0, larguraDispositivo, alturaDispositivo);

				batch.draw(personagem[2], larguraDispositivo/2-personagem[0].getWidth(), posPersonagemY);
				textoScore.draw(batch, "Score: "+pontos,larguraDispositivo/2 - textoProducao.getWidth()/2 + 50 , alturaDispositivo - textoProducao.getHeight()-100);
                batch.draw(textoChegou,larguraDispositivo/2 - textoProducao.getWidth()/2, alturaDispositivo - textoProducao.getHeight()-50);
				break;
		}
	}
}
