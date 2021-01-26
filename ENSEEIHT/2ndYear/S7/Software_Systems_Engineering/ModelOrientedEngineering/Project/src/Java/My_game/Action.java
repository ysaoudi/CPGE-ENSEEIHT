package My_game;

import java.util.ArrayList;

public class Action extends GameElement {
    private ArrayList<Object> objectsToGive;
    private ArrayList<Knowledge> knowledge;
    private ArrayList<Object> objectsToConsume;
    private Condition objectTransmissionCondition;
    private Condition knowledgeTransmissionCondition;
    private Condition objectConsumptionCondition;


    public Action(String n){
        super(n);
        this.objectsToConsume = new ArrayList<Object>();
        this.knowledge = new ArrayList<Knowledge>();
        this.objectsToGive = new ArrayList<Object>();
    }


    public Action(ArrayList<Object> objectsToGive, ArrayList<Knowledge> knowledge, ArrayList<Object> objectsToConsume, String name) {
        super(name);
        this.objectsToGive = objectsToGive;
        this.knowledge = knowledge;
        this.objectsToConsume = objectsToConsume;
    }

    public Action(ArrayList<Object> objectsToGive, ArrayList<Knowledge> knowledge, ArrayList<Object> objectsToConsume,
            Condition objectTransmissionCondition, Condition knowledgeTransmissionCondition,
            Condition objectConsumptionCondition, String name) {
        super(name);
        this.objectsToGive = objectsToGive;
        this.knowledge = knowledge;
        this.objectsToConsume = objectsToConsume;
        this.objectTransmissionCondition = objectTransmissionCondition;
        this.knowledgeTransmissionCondition = knowledgeTransmissionCondition;
        this.objectConsumptionCondition = objectConsumptionCondition;
    }

    public ArrayList<Object> getObjectsToGive() {
        return objectsToGive;
    }

    public void setObjectsToGive(ArrayList<Object> objectsToGive) {
        this.objectsToGive = objectsToGive;
    }

    public ArrayList<Knowledge> getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(ArrayList<Knowledge> knowledge) {
        this.knowledge = knowledge;
    }

    public ArrayList<Object> getObjectsToConsume() {
        return objectsToConsume;
    }

    public void setObjectsToConsume(ArrayList<Object> objectsToConsume) {
        this.objectsToConsume = objectsToConsume;
    }

    public Condition getObjectTransmissionCondition() {
        return objectTransmissionCondition;
    }

    public void setObjectTransmissionCondition(Condition objectTransmissionCondition) {
        this.objectTransmissionCondition = objectTransmissionCondition;
    }

    public Condition getKnowledgeTransmissionCondition() {
        return knowledgeTransmissionCondition;
    }

    public void setKnowledgeTransmissionCondition(Condition knowledgeTransmissionCondition) {
        this.knowledgeTransmissionCondition = knowledgeTransmissionCondition;
    }

    public Condition getObjectConsumptionCondition() {
        return objectConsumptionCondition;
    }

    public void setObjectConsumptionCondition(Condition objectConsumptionCondition) {
        this.objectConsumptionCondition = objectConsumptionCondition;
    }
    public void addObjectsTogive(Object o){
        objectsToGive.add(o);
    }
    public void addObjectsToConsume(Object o){
        objectsToConsume.add(o);
    }
    public void addKnowledge(Knowledge k){
        knowledge.add(k);
    }
      
}
