package bowlingGame;

import java.util.InputMismatchException;
import java.util.Scanner;

public class RunGame {

	private final static int maxValue = 10;
	private final static int minValue = 0;
	private final static int totalRolls = 21;

	public static void main(String[] args) {

		Frame game = new Frame();

		int lastValue = minValue;
		boolean isValidNumber = false;
		boolean skipNextRoll = false;
		int value = minValue;
		System.out.println("**Let's BOWL**");
		Scanner scan = null;
		for (int i=0; i<totalRolls; i++) {
			isValidNumber = false;
			int newMax = maxValue - lastValue;
			if(i==(totalRolls-2) && lastValue==maxValue) {
				newMax = maxValue;
			}
			if(i==(totalRolls-1) && !game.needBonusHit()) {
				break;
			}
			if(skipNextRoll) {
				skipNextRoll = false;
				lastValue = minValue;
				continue;
			}
			while(!isValidNumber) {
				try {
					scan = new Scanner(System.in);
					System.out.print("Roll Number " + (i+1) + ": Please enter a value between " + minValue + " and " + newMax + ":");
					value = scan.nextInt();
					if(value<=newMax && value>=minValue) {
						isValidNumber = true;
						game.roll(value, i);
						if(i%2==0) {
							lastValue = value;
							if(value == maxValue && !isLastFrame(i)) {
								skipNextRoll = true;
							}
							else {
								skipNextRoll = false;
							}
						}
						else {
							lastValue = minValue;
						}
					}
					else {
						System.out.println("Your input is not in range. Please try again");
					}
				}
				catch(InputMismatchException e) {

					System.out.println("Error: Please enter a valid number");
				}
			}
		}
		scan.close();
		System.out.println("Game Over. Thanks for playing :)");
		System.out.println("Final Score:");
		game.printScoreBoard();
	}

	private static boolean isLastFrame(int i) {

		boolean lastFrame = false;
		if(i>(totalRolls-4) && i <totalRolls) {
			lastFrame = true;
		}
		return lastFrame;
	}
}