package My_game;

import java.util.ArrayList;

public class Path extends GameElement {
    private boolean isOpen;
    private Condition openCondition;
    private Place Place1, Place2;
    private ArrayList<GameElement> transmittedElements;
    private ArrayList<GameElement> consumedElements;
    private Condition transmissCondition;
    private Condition consumptionCondition;


    public Path(String n){
        super(n);
        this.transmittedElements = new ArrayList<GameElement>();
        this.consumedElements = new ArrayList<GameElement>();
        this.isOpen = false;
    }

    public Path(boolean isOpen, Condition openCondition, Place place1, Place place2,
            ArrayList<GameElement> transmittedElements, ArrayList<GameElement> consumedElements,
            Condition transmissCondition, Condition consumptionCondition, String name) {
        super(name);
        this.isOpen = isOpen;
        this.openCondition = openCondition;
        Place1 = place1;
        Place2 = place2;

        if (checkArrayContentType(transmittedElements, Object.class)
                || checkArrayContentType(transmittedElements, Knowledge.class) ) {
            this.transmittedElements = transmittedElements;
        }

        if (checkArrayContentType(consumedElements, Object.class)
                || checkArrayContentType(consumedElements, Knowledge.class)) {
            this.consumedElements = consumedElements;
        }

        this.transmissCondition = transmissCondition;
        this.consumptionCondition = consumptionCondition;
    }

    public boolean checkArrayContentType(ArrayList<?> List, Class<?> contentClass) {
        return !List.isEmpty() && List.getClass().getComponentType().isAssignableFrom(contentClass);
    }


    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Condition getOpenCondition() {
        return openCondition;
    }

    public void setOpenCondition(Condition openCondition) {
        this.openCondition = openCondition;
    }

    public Place getPlace1() {
        return Place1;
    }

    public void setPlace1(Place place1) {
        Place1 = place1;
    }

    public Place getPlace2() {
        return Place2;
    }

    public void setPlace2(Place place2) {
        Place2 = place2;
    }

    public ArrayList<GameElement> getTransmittedElements() {
        return transmittedElements;
    }

    public void setTransmittedElements(ArrayList<GameElement> transmittedElements) {
        this.transmittedElements = transmittedElements;
    }

    public ArrayList<GameElement> getConsumedElements() {
        return consumedElements;
    }

    public void setConsumedElements(ArrayList<GameElement> consumedElements) {
        this.consumedElements = consumedElements;
    }

    public Condition getTransmissCondition() {
        return transmissCondition;
    }

    public void setTransmissCondition(Condition transmissCondition) {
        this.transmissCondition = transmissCondition;
    }

    public Condition getConsumptionCondition() {
        return consumptionCondition;
    }

    public void setConsumptionCondition(Condition consumptionCondition) {
        this.consumptionCondition = consumptionCondition;
    }

    public void updateVisibility() {
        if (this.openCondition.ConditionVerifiee()) {
            this.setOpen(true);
        }
        else{this.setOpen(false);}
    }
}
