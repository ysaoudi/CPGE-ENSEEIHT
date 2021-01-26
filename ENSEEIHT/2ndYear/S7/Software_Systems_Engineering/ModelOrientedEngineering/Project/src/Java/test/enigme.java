package test;
import Game_Controller.*;
import Display.Display;
import Display.DisplayImpl;
import My_game.*;
import My_game.Object;

public class enigme {

    public static void main (String[] args){
            Game enigme = new Game("Enigme");
            Place succes = new Place("succes");
            Place debut = new Place("debut");
            Place echec = new Place("echec");
            Path toSuccess = new Path("toSucces");
            Path toEchec = new Path("toEchec");
            Person sphynx = new Person("Sphynx");
            Knowledge reussite = new Knowledge("Réussite");
            Object tentative = new Object("Tentative");
            Explorer explorateur = new Explorer("Explorateur");
            Condition conditionToSuccess = new Condition("conditionToSuccess");
            Condition conditionToEchec = new Condition("conditionToEchec");
            Condition conditionSphynx = new Condition("conditionSphynx");
            //Sphynx
            SubConditions hasmt1tentative = new SubConditions();
            SubConditions hasNoReussite = new SubConditions();
            //ToReussite
            SubConditions hasReussite = new SubConditions();
            //ToEchec
            SubConditions hasNoTentative = new SubConditions();
            Action actionBonneReponse = new Action("bonneReponse");
            Action actionMauvaiseReponse = new Action("mauvaiseReponse");
            Choice choixSphynx = new Choice("choixSphynx");
            Interaction interactionSphynx = new Interaction("La Question du Sphynx");


    //fill Objects
    tentative.setCount(3);
    tentative.setSize(1);
    //fill Knowledge
    reussite.setCount(1);

    //fill Explorateur
    explorateur.setCurrentPlace(debut);
    explorateur.setCurrentWeight(0);
    explorateur.addObject(tentative);

    //fill Persons
    sphynx.setDescription("Question du sphynx");
    sphynx.setActivityCondition(conditionSphynx);
    sphynx.setInteraction(interactionSphynx);
    sphynx.setIsActive(true);

    //fill Paths
    toSuccess.setPlace1(debut);
    toSuccess.setPlace2(succes);
    toSuccess.setOpenCondition(conditionToSuccess);
    toSuccess.setOpen(false);
    toEchec.setPlace1(debut);
    toEchec.setPlace2(echec);
    toEchec.setOpenCondition(conditionToEchec);
    toEchec.setOpen(false);
    

    //fill Places
    debut.addPersons(sphynx);
    debut.addPaths(toSuccess);
    debut.addPaths(toEchec);


    //fill Subconditions 
    hasNoReussite.setSc(reussite,Comparator.EXACTLY,0);
    hasReussite.setSc(reussite, Comparator.EXACTLY, 1);
    hasmt1tentative.setSc(tentative, Comparator.MORETHAN,1);
    hasNoTentative.setSc(tentative, Comparator.EXACTLY, 0);

    //fill Conditions
    conditionSphynx.addSubCond(hasNoReussite);
    conditionSphynx.addSubCond(hasmt1tentative);
    conditionSphynx.addLogicalOp(LogicalOperator.OR);
    conditionToEchec.addSubCond(hasNoTentative);
    conditionToSuccess.addSubCond(hasReussite);
    conditionSphynx.setExplorer(explorateur);
    conditionToEchec.setExplorer(explorateur);
    conditionToSuccess.setExplorer(explorateur);



    //fill Actions 
    actionBonneReponse.addKnowledge(reussite);
    actionMauvaiseReponse.addObjectsToConsume(tentative);

    // fill choice
    choixSphynx.setDescription("La question du sphynx");
    choixSphynx.addActions(actionMauvaiseReponse);
    choixSphynx.addActions(actionBonneReponse);
    

    //fill Interaction
    interactionSphynx.addChoices(choixSphynx);


    //fill Game
    enigme.setExplorer(explorateur);
    enigme.setStartingPlace(debut);
    enigme.addFinishingPlace(succes);
    enigme.addFinishingPlace(echec);
    enigme.setMaxWeight(3);
    // Construction controleur
    Display display = new DisplayImpl();
    Controller controller = new ControllerImpl(display, enigme);

    //display.displayChoices(interactionSphynx.getChoices());
    //display.displayActions(choixSphynx);
    //System.out.println();
    controller.play();

    }


}
