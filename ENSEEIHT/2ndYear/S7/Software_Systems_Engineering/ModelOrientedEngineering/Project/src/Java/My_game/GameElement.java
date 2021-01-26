package My_game;

public abstract class GameElement {
    protected String name;

    public GameElement(String n){
        name = n;
    }
    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }
}