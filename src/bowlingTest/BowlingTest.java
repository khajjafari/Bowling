package bowlingTest;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import bowlingGame.Frame;

public class BowlingTest {

	Frame game;
	private final static int totalRolls = 21;
	private final static int maxValue = 10;
	private final static int minValue = 0;

	@Before
	public void setup() {

		game = new Frame();
	}

	@Test
	public void testPerfectScore() {

		simulateGame(maxValue, 0);
		assertEquals("Perfect Game calculation is not correct", 300, game.calculateTotalScore());
	}

	@Test
	public void testTurkey() {

		game.roll(maxValue, 0);
		game.roll(maxValue, 2);
		game.roll(maxValue, 4);
		simulateGame(minValue, 6);
		assertEquals("Three consecutive score calculation failed", 60, game.calculateTotalScore());
	}

	@Test
	public void testOneSpare() {

		game.roll(5, 0);
		game.roll(5, 1);
		game.roll(5, 2);
		game.roll(minValue, 3);
		simulateGame(minValue, 4);
		assertEquals("One Spare in game score calculation not correct", 20, game.calculateTotalScore());
	}

	@Test
	public void testOneStrike() {

		game.roll(maxValue, 0);
		game.roll(5, 2);
		game.roll(3, 3);
		simulateGame(minValue, 4);
		assertEquals("One Strike in game score calculation not correct", 26, game.calculateTotalScore());
	}

	//after two strikes, if the third is not a strike, only the first roll of that frame is added to the first strike
	@Test
	public void testThirdStrikeNotTurkey() {

		game.roll(10, 0);
		game.roll(10, 2);
		game.roll(8, 4);
		game.roll(1, 5);
		simulateGame(0, 6);
		assertEquals("Invalid score calculated for thirs hit not being a turkey", 56, game.calculateTotalScore());
	}

	public void simulateGame(int pins, int startingTurn) {

		boolean skipNextRoll = false;
		int value = minValue;
		for (int i=startingTurn; i<totalRolls; i++) {
			if(i==totalRolls-1 && !game.needBonusHit()) {
				break;
			}
			if(skipNextRoll) {
				skipNextRoll = false;
				continue;
			}
			value = pins;
			game.roll(value, i);
			if(isFirstRollOfFrame(i)) {
				if(value == maxValue && !isLastFrame(i)) {
					skipNextRoll = true;
				}
				else {
					skipNextRoll = false;
				}
			}
		}
	}

	private static boolean isLastFrame(int i) {

		boolean lastFrame = false;
		if(i>(totalRolls-4) && i <totalRolls) {
			lastFrame = true;
		}
		return lastFrame;
	}

	private boolean isFirstRollOfFrame(int roll) {

		return roll%2==0;
	}
}