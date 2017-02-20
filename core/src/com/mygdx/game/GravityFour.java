package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

public class GravityFour extends ApplicationAdapter implements GestureDetector.GestureListener{
	SpriteBatch batch;
	Texture texture;
	Texture X;
	Texture O;
	Texture focus;
	Sprite XO[][];
	Tile[][] board;// matrix of Tile objects
	Coord[][] spots;//matrix of coordinates corresponding to possible places a tile will be
	int ti;//touched column
	int tj;//touched row
	boolean firstMove;
	boolean xTurn;

	private final static int GRID_WIDTH = 9;
	private final static int GRID_HEIGHT = 14;

	@Override
	public void create () {
		batch = new SpriteBatch();
		board = new Tile[GRID_WIDTH][GRID_HEIGHT];
		spots = new Coord[GRID_WIDTH][GRID_HEIGHT];
		XO = new Sprite[GRID_WIDTH][GRID_HEIGHT];
		texture = new Texture("square.png");
		focus = new Texture("focus.png");
		X = new Texture("X.png");

		firstMove = true;
		xTurn = true;

		for(int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				//for all tiles and spots
				board[i][j] = new Tile(texture); //Create tile
				board[i][j].setValue(0); //set value to empty
				spots[i][j] = new Coord(150 + (i * board[i][j].getSprite().getWidth()), 500 + (j * board[i][j].getSprite().getHeight())); //set the spots
				board[i][j].setPosition(spots[i][j]);
			}
		}

		GestureDetector gd = new GestureDetector(this);
		Gdx.input.setInputProcessor(gd);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(247/255f, 247/255f, 247/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		for(int i = 0; i < board.length; i++){
			for(int j = 0; j< board[i].length; j++){
				board[i][j].getSprite().draw(batch);
				if(board[i][j].getValue() > 0){
					board[i][j].getMark().draw(batch);
				}
			}
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		texture.dispose();
		focus.dispose();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		y = Gdx.graphics.getHeight() - y;

		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if(x > board[i][j].getSprite().getX() && (x < (board[i][j].getSprite().getX()+board[i][j].getSprite().getWidth())) && (y > board[i][j].getSprite().getY()) && (y < (board[i][j].getSprite().getY()+board[i][j].getSprite().getHeight()))){
					if(legalMove(i,j)){
						tapActions(i,j);
					}
				}
			}
		}


		return false;
	}

	public void tapActions(int i, int j){
		if(i == ti && j == tj){
			board[ti][tj].setSprite(texture, board[ti][tj].getPosition());
			playTile(i,j);
		}else{
			board[ti][tj].setSprite(texture, board[ti][tj].getPosition());

			ti = i;
			tj = j;

			board[i][j].setSprite(focus, board[ti][tj].getPosition());
		}
	}

	public void playTile(int i, int j){
		if(firstMove){
			firstMove = false;
		}
		if(xTurn){
			xTurn = false;
			board[i][j].setValue(1);

			if(checkForFour(i,j,0,1)){
				for(int k = 0; k < board.length; k++) {
					for (int l = 0; l < board[k].length; l++) {
						board[k][l].setValue(0);
					}
				}
			}


		}else{
			xTurn = true;
			board[i][j].setValue(2);


			if(checkForFour(i,j,0,2)){
				for(int k = 0; k < board.length; k++) {
					for (int l = 0; l < board[k].length; l++) {
						board[k][l].getMark().getTexture().dispose();
						board[k][l].setValue(0);
					}
				}
			}


		}

	}

	public boolean checkForFour(int i, int j, int inARow, int type){
		Gdx.app.log("inARow", ""+inARow);

		if(checkLeft(i,j, inARow,type) + checkRight(i,j,inARow,type) + 1  >= 4 || checkUp(i,j,inARow,type) + checkDown(i,j,inARow,type) + 1  >= 4 || checkUpRight(i,j,inARow,type) + checkDownLeft(i,j,inARow,type)   >= 4 || checkDownRight(i,j,inARow,type) + checkUpLeft(i,j,inARow,type)   >= 4){
			return true;
		}

		return false;
	}

	public int checkLeft(int i, int j, int inARow, int type){
		Gdx.app.log("inARowleft", ""+inARow);
		if(inARow==4){
			return inARow;
		}
		if(i > 0){
			if(board[i-1][j].getValue() == type){
				inARow++;
				return checkLeft(i-1,j,inARow,type);
			}else{
				return inARow;
			}
		}else {
			return inARow;
		}
	}

	public int checkRight(int i, int j, int inARow, int type){
		Gdx.app.log("inARowright", ""+inARow);
		if(inARow==4){
			return inARow;
		}
		if(i < board.length-1){
			if(board[i+1][j].getValue() == type){
				inARow++;
				return checkRight(i+1,j,inARow,type);
			}else{
				return inARow;
			}
		}else{
			return inARow;
		}
	}

	public int checkUp(int i, int j, int inARow, int type) {
		Gdx.app.log("inARowup", "" + inARow);
		if (inARow == 4) {
			return inARow;
		}
		if (j < board.length - 1) {
			if (board[i][j+1].getValue() == type) {
				inARow++;
				return checkUp(i, j+1, inARow, type);
			}else{
				return inARow;
			}
		}else{
			return inARow;
		}
	}

	public int checkDown(int i, int j, int inARow, int type) {
		Gdx.app.log("inARowdown", "" + inARow);
		if (inARow == 4) {
			return inARow;
		}
		if (j > 0) {
			if (board[i][j-1].getValue() == type) {
				inARow++;
				return checkDown(i, j-1, inARow, type);
			}else{
				return inARow;
			}
		}else {
			return inARow;
		}
	}

	public int checkDownLeft(int i, int j, int inARow, int type) {
		Gdx.app.log("inARow", "" + inARow);
		if (inARow == 4) {
			return inARow;
		}
		if (j > 0 && i > 0) {
			if (board[i-1][j-1].getValue() == type) {
				inARow++;
				return checkDownLeft(i-1, j-1, inARow, type);
			}else{
				return inARow;
			}
		}else{
			return inARow;
		}
	}

	public int checkDownRight(int i, int j, int inARow, int type) {
		Gdx.app.log("inARow", "" + inARow);
		if (inARow == 4) {
			return inARow;
		}
		if (j > 0 && i < board.length-1) {
			if (board[i+1][j-1].getValue() == type) {
				inARow++;
				return checkDownRight(i+1, j-1, inARow, type);
			}else{
				return inARow;
			}
		}else{
			return inARow;
		}
	}

	public int checkUpLeft(int i, int j, int inARow, int type) {
		Gdx.app.log("inARow", "" + inARow);
		if (inARow == 4) {
			return inARow;
		}
		if (j < board.length && i > 0) {
			if (board[i-1][j+1].getValue() == type) {
				inARow++;
				return checkUpLeft(i-1, j+1, inARow, type);
			}else{
				return inARow;
			}
		}else{
			return inARow;
		}
	}

	public int checkUpRight(int i, int j, int inARow, int type) {
		inARow++;

		Gdx.app.log("inARow", "" + inARow);
		if (inARow == 4) {
			return inARow;
		}
		if (j < board.length && i < board.length-1) {
			if (board[i+1][j+1].getValue() == type) {
				inARow++;
				return checkUpRight(i+1, j+1, inARow, type);
			}else{
				return inARow;
			}
		}else{
			return inARow;
		}
	}

	public boolean legalMove(int i ,int j){
		if(firstMove){
			return true;
		}else{
			if(board[i][j].getValue() > 0 ){
				return false;
			}
			if(i > 0){
				if(board[i-1][j].getValue() > 0){
					return true;
				}
			}
			if(i < board.length-1){
				if(board[i+1][j].getValue() > 0){
					return true;
				}
			}
			if(j > 0){
				if(board[i][j-1].getValue() > 0){
					return true;
				}
			}
			if(j < board.length){
				if(board[i][j+1].getValue() > 0){
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	@Override
	public void pinchStop() {

	}


}
