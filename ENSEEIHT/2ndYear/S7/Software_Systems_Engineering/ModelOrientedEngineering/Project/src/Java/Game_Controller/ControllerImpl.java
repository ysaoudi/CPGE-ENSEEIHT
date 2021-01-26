package Game_Controller;

import java.util.ArrayList;
import java.util.Scanner;
import Display.Display;
import My_game.*;
import My_game.Object;

public class ControllerImpl implements Controller {
    private Display dis;
    private Place currentplace;
    private Explorer explorer;
    private Game game;
    private Scanner sc;

    public ControllerImpl(Display dis, Game game) {
        this.dis = dis;
        this.game = game;
        this.explorer = game.getExplorer();
        this.currentplace = game.getStartingPlace();
        this.sc = new Scanner(System.in);
    }

    
    @Override
    public void play() {
        //int choice = 0;
        //int action = 0;
        Path destination;
        ArrayList<Person> prs;
       // boolean updateneeded = false;
        while (!Isinside(game.getFinishingPlacesNames())) {
            // inform the player of the current place
            dis.displaylocation(currentplace);
            dis.locationdetails(currentplace);
            dis.displayPossessedObjects(explorer);
            // interactions with all active people
            prs = currentplace.getActivePersons();
            if (prs.size() != 0) {
                for (Person p : prs) {
                    interact(p);
                }
            }
            explorer.addKnowledges(currentplace.getActiveKnowledge());
            currentplace.removeActiveKnowledge();

            
            // Un seul chemin obligatoire
            destination = getThePath(currentplace);
            if (destination != null) {
                takeNextPath(destination);
            }
        }
        dis.endGame();

    }

    // Vérifie que l'explorateur est dans une certaine place
    public boolean Isinside(ArrayList<String> names) {
        boolean is = false;
        for (String e : names) {
            if (this.currentplace.getName() == e) {
                is = true;
            }
        }
        return is;
    }

    public void interact(Person p) {
        int entry;
        Choice choice;
        Action action;
        // Afficher les choix possibles
        dis.interaction(p);
        // lire l'entrée du clavier
        entry = readEntry();
        choice = p.getInteraction().getChoices().get(entry);
        // Afficher les actions possibles
        dis.displayPossessedObjects(explorer);
        dis.possessedKnowledge(explorer);
        dis.displayActions(choice);

        // Lire l'entrée du clavier
        entry = readEntry();
        action = choice.getActions().get(entry);
        // Faire l'action (ie Consommer les objets à consommer et ajouter ceux à
        // ajouter)
        doAction(action);
        dis.displayPossessedObjects(explorer);
        dis.possessedKnowledge(explorer);

    }

    public Path getThePath(Place place) {
        Path path = null;
        if(place.getOpenPaths().size()>0){
           path = place.getOpenPaths().get(0);
        }
        return path;

    }

    public void takeNextPath(Path p) {
        dis.pathTaken(p);
        for(GameElement g : p.getTransmittedElements()){
            if(g instanceof Knowledge){
                Knowledge k = (Knowledge) g;
                explorer.addKnowledge(k);
            }
            if(g instanceof Object){
                Object o = (Object) g;
                if(o.getSize() <= (game.getMaxWeight() - explorer.getCumulatedWeight()) ){
                    explorer.addObject(o);
                }
            }
        }
        currentplace = p.getPlace2();

    }

    public int readEntry() {
        String str = sc.nextLine();
        int res = Integer.parseInt(str);
        return res-1;
    }

    public void UpdateCondition() {
        for (GameElement ge : game.getGameElements()) {
            if (ge instanceof Object) {
                Object o = (Object) ge;
                o.updateVisibility();
            }
        }
    }

    public void doAction(Action action) {
        ArrayList<Object> objectsToConsume = action.getObjectsToConsume();
        ArrayList<Object> objectsTogiveList = action.getObjectsToGive();
        ArrayList<Knowledge> knowledge = action.getKnowledge();
        for (Object o : objectsToConsume) {
            explorer.removeObject(o);
        }
        for (Object o : objectsTogiveList) {
            explorer.addObject(o);
        }
        for (Knowledge k : knowledge) {
            explorer.addKnowledge(k);
        }

    }
}
/**
 * if (ge instanceof Knowledge) { Knowledge k = (Knowledge) ge;
 * k.updateVisibility(); } }if (ge instanceof Path) { Path p = (Path) ge; if
 * (p.getOpenCondition().ConditionVerifiee()) { p.setOpen(true); }if(ge
 * instanceof Person){ Person p = (Person) ge; if
 * (p.getActivityCondition().ConditionVerifiee()) { p.setIsActive(true); } } }
 * 
 * }
 */
