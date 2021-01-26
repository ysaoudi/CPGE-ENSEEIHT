package My_game;

public class SubConditions {

    private String name;
    private Comparator comparator;
    private Integer compared;
    private GameElement gameElement;

    public SubConditions() {
    }

    public SubConditions(GameElement gameElement,  Integer compared, Comparator comparator) {
        this.comparator = comparator;
        this.compared = compared;
        this.gameElement = gameElement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Comparator getComparator() {
            return comparator;
        }

        public void setComparator(Comparator comparator) {
            this.comparator = comparator;
        }

        public Integer getCompared() {
            return compared;
        }

        public void setCompared(Integer compared) {
            this.compared = compared;
        }

        public GameElement getGameElement() {
            return gameElement;
        }

        public void setGameElement(GameElement gameElement) {
            this.gameElement = gameElement;
        }

        public void setSc(GameElement ge, Comparator com, Integer compared){
            setGameElement(ge);
            setComparator(com);
            setCompared(compared);
        }

}
