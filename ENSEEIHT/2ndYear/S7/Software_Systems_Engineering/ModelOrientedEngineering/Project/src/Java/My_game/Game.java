package My_game;
import java.util.ArrayList;

public class Game {
    private String name;
    private Explorer explorer;
    private int maxWeight;
    private ArrayList<GameElement> gameElements;
    private Place startingPlace;
    private ArrayList<Place> finishingPlaces;

    public Game (String Name){
        this.name = Name;
        this.gameElements = new ArrayList<GameElement>();
        this.finishingPlaces = new ArrayList<Place>();
        this.maxWeight = 0;
    }

    public Game(String name, Explorer explorer, int maxWeight, ArrayList<GameElement> gameElements, Place startingPlace,
            ArrayList<Place> finishingPlaces) {
        this.name = name;
        this.explorer = explorer;
        this.maxWeight = maxWeight;
        this.gameElements = gameElements;
        this.startingPlace = startingPlace;
        this.finishingPlaces = finishingPlaces;
    }

    public Game(String name, Explorer explorer, int maxWeight, Place startingPlace, ArrayList<Place> finishingPlaces) {
        this.name = name;
        this.explorer = explorer;
        this.maxWeight = maxWeight;
        this.startingPlace = startingPlace;
        this.finishingPlaces = finishingPlaces;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Explorer getExplorer() {
        return explorer;
    }

    public void setExplorer(Explorer explorer) {
        this.explorer = explorer;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public ArrayList<GameElement> getGameElements() {
        return gameElements;
    }

    public void addGameElement(GameElement gameElement){
        gameElements.add(gameElement);
    }

    public void removeGameElement(GameElement gameElement){
        gameElements.remove(gameElement);
    }

    public void setGameElements(ArrayList<GameElement> gameElements) {
        this.gameElements = gameElements;
    }

    public Place getStartingPlace() {
        return startingPlace;
    }

    public void setStartingPlace(Place startingPlace) {
        this.startingPlace = startingPlace;
    }

    public ArrayList<Place> getFinishingPlaces() {
        return finishingPlaces;
    }

    public ArrayList<String> getFinishingPlacesNames(){
        ArrayList<String> names = new ArrayList<String>();
        for(Place p : finishingPlaces){
            names.add(p.getName());
        }
        return names;
    }

    public void addFinishingPlace(Place newFinishingPlace){
        finishingPlaces.add(newFinishingPlace);
    }

    public void removeFinishingPlace(Place poppedFinishingPlace){
        finishingPlaces.remove(poppedFinishingPlace);
    }

    public void setFinishingPlaces(ArrayList<Place> finishingPlaces) {
        this.finishingPlaces = finishingPlaces;
    }


}