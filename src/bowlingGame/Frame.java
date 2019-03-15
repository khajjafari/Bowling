package bowlingGame;

public class Frame {

	private int[] rolls;
	private int[] scoreBoard;
	private int currentRollNumber;
	private int numPins=0;
	private final static int maxValue = 10;
	private final static int minValue = 0;
	private final static int totalRolls = 21;
	private boolean isLastHitOfFrame = false;

	public Frame(){

		rolls = new int[totalRolls];
		scoreBoard = new int[maxValue];
		currentRollNumber=minValue;
	}

	public void roll(int roll, int turn){

		currentRollNumber = turn;
		numPins = roll;
		//If this is the first roll of the last frame
		if(currentRollNumber == (totalRolls-3)) {
			scoreBoard[currentRollNumber/2]=roll;
			//if frame 9 was a strike
			if(wasPreviousHitStrike(currentRollNumber-1) || wasPreviousHitSplit(currentRollNumber-1)) {
				scoreBoard[(currentRollNumber/2)-1]+=roll;
				//if frame 8 was also a strike
				if(wasPreviousHitStrike(currentRollNumber-3) && wasPreviousHitStrike(currentRollNumber-1)) {
					scoreBoard[(currentRollNumber/2)-2]+=roll;
				}
			}
		}
		//If this is the second roll of the last frame
		else if(currentRollNumber == (totalRolls-2)) {
			scoreBoard[(currentRollNumber-1)/2]+=roll;
			//if frame 9 was a strike
			if(wasPreviousHitStrike(currentRollNumber-2)) {
				scoreBoard[((currentRollNumber-1)/2)-1]+=roll;
			}
		}
		//If this is the bonus roll of the last frame
		else if(currentRollNumber==(totalRolls-1)) {
			scoreBoard[(currentRollNumber-2)/2]+=roll;
		}
		else {
			calculateScore();
		}
		if(isLastHitOfFrame) {
			printScoreBoard();
			isLastHitOfFrame = false;
		}
	}

	private void calculateScore(){

		rolls[currentRollNumber] = numPins;
		//first hit of the frame is a strike
		if(numPins==maxValue && isFirstRollOfFrame(currentRollNumber)){
			rolls[currentRollNumber+1]=minValue;
			scoreBoard[(currentRollNumber)/2] = maxValue;
			calulateBonus();
			isLastHitOfFrame = true;
		}
		//first hit of the frame is not a strike
		else if(numPins!=maxValue && isFirstRollOfFrame(currentRollNumber)){
			calulateBonus();
		}
		//on second hit
		else if(!isFirstRollOfFrame(currentRollNumber)) {
			scoreBoard[(currentRollNumber-1)/2] = rolls[currentRollNumber-1]+rolls[currentRollNumber];
			calulateBonus();
			isLastHitOfFrame = true;
		}
	}

	private void calulateBonus() {

		int previousScore = minValue;
		int previousFrameSecondHit = minValue;
		boolean firstRoll = false;
		//If this is the first hit of the frame
		if(isFirstRollOfFrame(currentRollNumber)) {
			previousScore = (currentRollNumber/2)-1;
			previousFrameSecondHit = currentRollNumber-1;
			firstRoll = true;
		}
		//if it's the second hit of the frame
		else {
			previousScore = ((currentRollNumber-1)/2)-1;
			previousFrameSecondHit = currentRollNumber-2;
		}
		if(currentRollNumber>1) {
			if(wasPreviousHitStrike(previousFrameSecondHit)) {
				scoreBoard[previousScore]+=scoreBoard[previousScore+1];
				if(currentRollNumber>3) {
					//if the previous two hit was a strike
					if(scoreBoard[previousScore-1]==20 && rolls[previousFrameSecondHit-2] == 0) {
						//if this is the third strike in a row
						if(firstRoll && numPins==maxValue) {
							scoreBoard[previousScore-1]+=scoreBoard[previousScore+1];
						}
						if(firstRoll && numPins<maxValue) {
							scoreBoard[previousScore-1]+=numPins;
						}
					}
				}
			}
			//if the previous hit was a split
			else if(wasPreviousHitSplit(previousFrameSecondHit) && isFirstRollOfFrame(currentRollNumber)) {
				scoreBoard[previousScore]+=rolls[currentRollNumber];
			}
		}
	}

	private boolean wasPreviousHitStrike(int previousFrameSecondHit) {

		boolean wasStrike = false;
		int previousScore = rolls[previousFrameSecondHit] + rolls[previousFrameSecondHit-1];
		if(previousScore == maxValue && rolls[previousFrameSecondHit]==minValue) {
			wasStrike = true;
		}
		return wasStrike;
	}

	private boolean wasPreviousHitSplit(int previousFrameSecondHit) {

		boolean wasSplit = false;
		int previousScore = rolls[previousFrameSecondHit] + rolls[previousFrameSecondHit-1];
		if(previousScore == maxValue && rolls[previousFrameSecondHit]!=minValue) {
			wasSplit = true;
		}
		return wasSplit;
	}

	public boolean needBonusHit() {

		boolean needBonus = false;
		if(scoreBoard[(currentRollNumber-1)/2]>=maxValue) {
			needBonus = true;
		}
		return needBonus;
	}

	public int calculateTotalScore() {

		int maxScore = minValue;
		for(int score:scoreBoard) {
			maxScore+=score;
		}
		return maxScore;
	}

	private boolean isFirstRollOfFrame(int roll) {

		return roll%2==0;
	}

	public void printScoreBoard() {

		String frameScore = "";
		System.out.println("-------------------------------------------------------------------------");
		System.out.println("|    ||    ||    ||    ||    ||    ||    ||    ||    ||    || Total Score");
		for(int i = 0; i<maxValue; i++) {
			if(scoreBoard[i] == 0) {
				frameScore = "--";
			}
			else {
				frameScore = Integer.toString(scoreBoard[i]);
				if(scoreBoard[i]<10) {
					frameScore+=" ";
				}
			}
			System.out.print("| " + frameScore + " |");
		}
		System.out.println("|------------");
		System.out.println("|    ||    ||    ||    ||    ||    ||    ||    ||    ||    ||     " + calculateTotalScore());
		System.out.println("-------------------------------------------------------------------------");
	}
}