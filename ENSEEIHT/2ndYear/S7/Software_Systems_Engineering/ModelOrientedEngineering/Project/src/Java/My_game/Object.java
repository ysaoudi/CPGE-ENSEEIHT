package My_game;

public class Object extends GameElement {
    private int count;
    private boolean isVisible;
    private Condition visibilityCondition;
    private int size;
    private Condition dropCondition;

    public Object(String n){
        super(n);
        this.count = 0;
        this.size = 0;
        this.isVisible = true;
        this.visibilityCondition = null;
        this.dropCondition = null;
    }

    public Object(boolean isVisible, Condition visibilityCondition, int size, Condition dropCondition, int count, String name) {
        super(name);
        this.isVisible = isVisible;
        this.visibilityCondition = visibilityCondition;
        this.size = size;
        this.dropCondition = dropCondition;
        this.count = count;
    }

    public Object(boolean isVisible, Condition visibilityCondition, int size, int count, String name) {
        super(name);
        this.isVisible = isVisible;
        this.visibilityCondition = visibilityCondition;
        this.size = size;
        this.count = count;
    }

    public Object(boolean isVisible, int size, Condition dropCondition, String name) {
        super(name);
        this.isVisible = isVisible;
        this.size = size;
        this.dropCondition = dropCondition;
    }

    public Object(boolean isVisible, int size, int count, String name) {
        super(name);
        this.isVisible = isVisible;
        this.size = size;
        this.count = count;
    }

    
    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Condition getVisibilityCondition() {
        return visibilityCondition;
    }

    public void setVisibilityCondition(Condition visibilityCondition) {
        this.visibilityCondition = visibilityCondition;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void setName(String s){
        name = s;

    }
    public Condition getDropCondition() {
        return dropCondition;
    }

    public void setDropCondition(Condition dropCondition) {
        this.dropCondition = dropCondition;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean sameAs(Object obj) {
        return this.equals(obj);
    }

    public void updateVisibility() {
        if (this.visibilityCondition.ConditionVerifiee()) {
            this.setVisible(true);
        }
    }

}
