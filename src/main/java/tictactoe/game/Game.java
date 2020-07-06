package tictactoe.game;

import org.testng.annotations.Test;

import tictactoe.page.GamePage;

/**
 * @author Fanil Suratwala
 * 
 */
public class Game extends GamePage {
	@Test
	public void game() {
		launchGame();
		setXorO('x'); // choose between 'x' or 'o'
		setDifficulty("Medium"); // Easy, Medium, Impossible
		setRounds(5);
		play();
	}
}
