
package test;
import Game_Controller.*;
import Display.Display;
import Display.DisplayImpl;
import My_game.*;
import My_game.Object;

public class Exams {
public static void main(String[] args) {
		/**
		 * Init all elements
		 */
		Game exams = new Game("exams");
		Person firstSessionExam = new Person("FirstSessionExam");
		Interaction takeFirstSession = new Interaction("TakeFirstSession");
		Person secondSessionExam = new Person("SecondSessionExam");
		Interaction takeSecondSession = new Interaction("TakeSecondSession");
		Person director = new Person("Director");
		Interaction begToRetakeYear = new Interaction("BegToRetakeYear");
		Place firstSession = new Place("FirstSession");
		Place secondSession = new Place("SecondSession");
		Place success = new Place("Success");
		Place failure = new Place("Failure");
		Place retakeYear = new Place("RetakeYear");
		Path firstSessionToSuccess = new Path("FirstSessionToSuccess");
		Path firstSessionToSecondSession = new Path("FirstSessionToSecondSession");
		Path secondSessionToSuccess = new Path("SecondSessionToSuccess");
		Path secondSessionToRetakeYear = new Path("SecondSessionToRetakeYear");
		Path secondSessionToFailure = new Path("SecondSessionToFailure");
		Path retakeYearToFirstSession = new Path("RetakeYearToFirstSession");
		Object firstSessionAttempt = new Object("FirstSessionAttempt");
		Object secondSessionAttempt = new Object("SecondSessionAttempt");
		Knowledge passedExams = new Knowledge("PassedExams");
		Object yearAttempt = new Object("YearAttempt");
		Condition hasPassedExam = new Condition("hasPassedExam");
		Condition canAttendFirstSession = new Condition("canAttendFirstSession");
		Condition canAttendSecondSession = new Condition("canAttendSecondSession");
		Condition hasFailedExams = new Condition("hasFailedExams");
		Condition canRetakeYear = new Condition("canRetakeYear");
		
        Action failFirstSessionExam = new Action("failFirstSessionExam");
        Action passFirstSessionExam = new Action("passFirstSessionExam");
        Action failSecondSessionExam = new Action("failSecondSessionExam");
        Action passSecondSessionExam = new Action("passSecondSessionExam");
        Action beg = new Action("beg");
        
        Choice firstSessionAnswer = new Choice("firstSessionAnswer");
        Choice secondSessionAnswer = new Choice("secondSessionAnswer");
        Choice begDirector = new Choice("begDirector");
        
        Explorer khalid = new Explorer("Khalid");
        
		SubConditions subconditionMORETHAN1hasPassedExamPassedExams = new SubConditions(passedExams, 1, Comparator.MORETHAN);
		hasPassedExam.addSubCond(subconditionMORETHAN1hasPassedExamPassedExams);
		
		hasPassedExam.setExplorer(khalid);
		SubConditions subconditionMORETHAN1canAttendFirstSessionFirstSessionAttempt = new SubConditions(firstSessionAttempt, 1, Comparator.MORETHAN);
		canAttendFirstSession.addSubCond(subconditionMORETHAN1canAttendFirstSessionFirstSessionAttempt);
		
		canAttendFirstSession.setExplorer(khalid);
		SubConditions subconditionMORETHAN1canAttendSecondSessionSecondSessionAttempt = new SubConditions(secondSessionAttempt, 1, Comparator.MORETHAN);
		canAttendSecondSession.addSubCond(subconditionMORETHAN1canAttendSecondSessionSecondSessionAttempt);
		
		canAttendSecondSession.setExplorer(khalid);
		SubConditions subconditionEXACTLY0hasFailedExamsSecondSessionAttempt = new SubConditions(secondSessionAttempt, 0, Comparator.EXACTLY);
		hasFailedExams.addSubCond(subconditionEXACTLY0hasFailedExamsSecondSessionAttempt);
		
		SubConditions subconditionEXACTLY0hasFailedExamsFirstSessionAttempt = new SubConditions(firstSessionAttempt, 0,  Comparator.EXACTLY);
		hasFailedExams.addSubCond(subconditionEXACTLY0hasFailedExamsFirstSessionAttempt);
		hasFailedExams.addLogicalOp(LogicalOperator.AND);
		SubConditions subconditionEXACTLY0hasFailedExamsYearAttempt = new SubConditions(yearAttempt, 0,  Comparator.EXACTLY);
		hasFailedExams.addSubCond(subconditionEXACTLY0hasFailedExamsYearAttempt);
		hasFailedExams.addLogicalOp(LogicalOperator.AND);
		hasFailedExams.setExplorer(khalid);
		SubConditions subconditionEXACTLY0canRetakeYearSecondSessionAttempt = new SubConditions(secondSessionAttempt, 0, Comparator.EXACTLY);
		canRetakeYear.addSubCond(subconditionEXACTLY0canRetakeYearSecondSessionAttempt);
		
		SubConditions subconditionEXACTLY0canRetakeYearFirstSessionAttempt = new SubConditions(firstSessionAttempt, 0,  Comparator.EXACTLY);
		canRetakeYear.addSubCond(subconditionEXACTLY0canRetakeYearFirstSessionAttempt);
		canRetakeYear.addLogicalOp(LogicalOperator.AND);
		SubConditions subconditionEXACTLY1canRetakeYearYearAttempt = new SubConditions(yearAttempt, 1,  Comparator.EXACTLY);
		canRetakeYear.addSubCond(subconditionEXACTLY1canRetakeYearYearAttempt);
		canRetakeYear.addLogicalOp(LogicalOperator.AND);
		canRetakeYear.setExplorer(khalid);
		
		firstSession.addPersons(firstSessionExam);
		
		firstSession.addPaths(firstSessionToSuccess);
		firstSession.addPaths(firstSessionToSecondSession);
		firstSession.addPaths(retakeYearToFirstSession);
		
		
		secondSession.addPersons(secondSessionExam);
		
		secondSession.addPaths(secondSessionToSuccess);
		secondSession.addPaths(secondSessionToFailure);
		secondSession.addPaths(firstSessionToSecondSession);
		secondSession.addPaths(secondSessionToRetakeYear);
		
		
		
		success.addPaths(firstSessionToSuccess);
		success.addPaths(secondSessionToSuccess);
		
		
		
		failure.addPaths(secondSessionToFailure);
		
		
		retakeYear.addPersons(director);
		
		retakeYear.addPaths(secondSessionToRetakeYear);
		retakeYear.addPaths(retakeYearToFirstSession);
		
		
		;
		firstSessionToSuccess.setPlace1(firstSession);
		firstSessionToSuccess.setPlace2(success);
		firstSessionToSuccess.setOpenCondition(hasPassedExam);
		firstSessionToSuccess.setOpen(true);
		firstSessionToSecondSession.setPlace1(firstSession);
		firstSessionToSecondSession.setPlace2(secondSession);
		firstSessionToSecondSession.setOpenCondition(canAttendSecondSession);
		firstSessionToSecondSession.setOpen(true);
		secondSessionToSuccess.setPlace1(secondSession);
		secondSessionToSuccess.setPlace2(success);
		secondSessionToSuccess.setOpenCondition(hasPassedExam);
		secondSessionToSuccess.setOpen(true);
		secondSessionToRetakeYear.setPlace1(secondSession);
		secondSessionToRetakeYear.setPlace2(retakeYear);
		secondSessionToRetakeYear.setOpenCondition(canRetakeYear);
		secondSessionToRetakeYear.setOpen(true);
		secondSessionToFailure.setPlace1(secondSession);
		secondSessionToFailure.setPlace2(failure);
		secondSessionToFailure.setOpenCondition(hasFailedExams);
		secondSessionToFailure.setOpen(true);
		retakeYearToFirstSession.setPlace1(retakeYear);
		retakeYearToFirstSession.setPlace2(firstSession);
		retakeYearToFirstSession.setOpenCondition(canAttendFirstSession);
		retakeYearToFirstSession.setOpen(true);
		;
		firstSessionAttempt.setCount(1);
		firstSessionAttempt.setSize(1);
		secondSessionAttempt.setCount(1);
		secondSessionAttempt.setSize(1);
		yearAttempt.setCount(1);
		yearAttempt.setSize(0);
		;
		passedExams.setCount(1);
		;
		firstSessionExam.setDescription("Answer Exam!");
		firstSessionExam.setActivityCondition(canAttendFirstSession);
		firstSessionExam.setInteraction(takeFirstSession);
		firstSessionExam.setIsActive(true);
		secondSessionExam.setDescription("Answer Exam!");
		secondSessionExam.setActivityCondition(canAttendSecondSession);
		secondSessionExam.setInteraction(takeSecondSession);
		secondSessionExam.setIsActive(true);
		director.setDescription("Beg to retake year");
		director.setActivityCondition(canRetakeYear);
		director.setInteraction(begToRetakeYear);
		director.setIsActive(true);
		;
		failFirstSessionExam.addObjectsToConsume(firstSessionAttempt);
		
		failFirstSessionExam.addObjectsTogive(secondSessionAttempt);
		
		
		
		passFirstSessionExam.addKnowledge(passedExams);
		failSecondSessionExam.addObjectsToConsume(secondSessionAttempt);
		
		
		
		
		passSecondSessionExam.addKnowledge(passedExams);
		beg.addObjectsToConsume(yearAttempt);
		
		beg.addObjectsTogive(firstSessionAttempt);
		
		;
		firstSessionAnswer.setDescription("Answer Exam");
		firstSessionAnswer.addActions(failFirstSessionExam);
		firstSessionAnswer.addActions(passFirstSessionExam);
		secondSessionAnswer.setDescription("Answer Exam");
		secondSessionAnswer.addActions(failSecondSessionExam);
		secondSessionAnswer.addActions(passSecondSessionExam);
		begDirector.setDescription("BEEEG");
		begDirector.addActions(beg);
		;
		takeFirstSession.addChoices(firstSessionAnswer);
		takeSecondSession.addChoices(secondSessionAnswer);
		begToRetakeYear.addChoices(begDirector);
		;
		exams.setExplorer(khalid);
		exams.setStartingPlace(firstSession);
		exams.setMaxWeight(3);
		exams.addFinishingPlace(success);
		exams.addFinishingPlace(failure);
		;
		khalid.setCurrentPlace(firstSession);
		khalid.setCurrentWeight(2);
		khalid.addObject(firstSessionAttempt);
		khalid.addObject(yearAttempt);
		;
	
		/**
		 * Launch the game
		 */
        Display display = new DisplayImpl();
		Controller controleur = new ControllerImpl(display, exams);
		controleur.play();
	}
}
