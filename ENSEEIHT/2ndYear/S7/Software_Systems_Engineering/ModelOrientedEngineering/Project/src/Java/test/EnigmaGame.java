
package test;
import Game_Controller.*;
import Display.Display;
import Display.DisplayImpl;
import My_game.*;
import My_game.Object;

public class EnigmaGame {
public static void main(String[] args) {
		/**
		 * Init all elements
		 */
		Game enigmaGame = new Game("enigmaGame");
		Place enigma = new Place("Enigma");
		Place success = new Place("Success");
		Place failure = new Place("Failure");
		Path enigmaToSuccess = new Path("EnigmaToSuccess");
		Path enigmaToFailure = new Path("EnigmaToFailure");
		Object attempt = new Object("Attempt");
		Knowledge victory = new Knowledge("Victory");
		Condition hasFailedEnigma = new Condition("hasFailedEnigma");
		Condition hasAnsweredCorrectly = new Condition("hasAnsweredCorrectly");
		Condition canAnswer = new Condition("canAnswer");
		Person phoenix = new Person("Phoenix");
		Interaction answerPhoenix = new Interaction("AnswerPhoenix");
		
        Action wrongAnswer = new Action("wrongAnswer");
        Action rightAnswer = new Action("rightAnswer");
        
        Choice answer = new Choice("answer");
        
        Explorer hamid = new Explorer("Hamid");
        
		SubConditions subconditionEXACTLY0Attempt = new SubConditions(attempt, 0, Comparator.EXACTLY);
		hasFailedEnigma.addSubCond(subconditionEXACTLY0Attempt);
		
		hasFailedEnigma.setExplorer(hamid);
		SubConditions subconditionEXACTLY1Victory = new SubConditions(victory, 1, Comparator.EXACTLY);
		hasAnsweredCorrectly.addSubCond(subconditionEXACTLY1Victory);
		
		hasAnsweredCorrectly.setExplorer(hamid);
		SubConditions subconditionEXACTLY0Victory = new SubConditions(victory, 0, Comparator.EXACTLY);
		canAnswer.addSubCond(subconditionEXACTLY0Victory);
		
		SubConditions subconditionMORETHAN1Attempt = new SubConditions(attempt, 1,  Comparator.MORETHAN);
		canAnswer.addSubCond(subconditionMORETHAN1Attempt);
		canAnswer.addLogicalOp(LogicalOperator.AND);
		canAnswer.setExplorer(hamid);
		
		enigma.addPersons(phoenix);
		
		enigma.addPaths(enigmaToSuccess);
		enigma.addPaths(enigmaToFailure);
		
		
		
		success.addPaths(enigmaToSuccess);
		
		
		
		failure.addPaths(enigmaToFailure);
		
		
		;
		enigmaToSuccess.setPlace1(enigma);
		enigmaToSuccess.setPlace2(success);
		enigmaToSuccess.setOpenCondition(hasAnsweredCorrectly);
		enigmaToSuccess.setOpen(true);
		enigmaToFailure.setPlace1(enigma);
		enigmaToFailure.setPlace2(failure);
		enigmaToFailure.setOpenCondition(hasFailedEnigma);
		enigmaToFailure.setOpen(true);
		;
		attempt.setCount(3);
		attempt.setSize(1);
		;
		victory.setCount(1);
		;
		phoenix.setDescription("Phoenix's Famous Question");
		phoenix.setActivityCondition(canAnswer);
		phoenix.setInteraction(answerPhoenix);
		phoenix.setIsActive(true);
		;
		wrongAnswer.addObjectsToConsume(attempt);
		
		
		
		
		rightAnswer.addKnowledge(victory);
		;
		answer.setDescription("Answer Enigma");
		answer.addActions(wrongAnswer);
		answer.addActions(rightAnswer);
		;
		answerPhoenix.addChoices(answer);
		;
		enigmaGame.setExplorer(hamid);
		enigmaGame.setStartingPlace(enigma);
		enigmaGame.setMaxWeight(3);
		enigmaGame.addFinishingPlace(success);
		enigmaGame.addFinishingPlace(failure);
		;
		hamid.setCurrentPlace(enigma);
		hamid.setCurrentWeight(3);
		hamid.addObject(attempt);
		;
	
		/**
		 * Launch the game
		 */
        Display display = new DisplayImpl();
		Controller controleur = new ControllerImpl(display, enigmaGame);
		controleur.play();
	}
}
