package My_game;

import java.util.ArrayList;

public class Place extends GameElement {

    private ArrayList<Knowledge> knowledge;
    private ArrayList<Object> objects;
    private ArrayList<Person> persons;
    private ArrayList<Path> paths;



    public Place(String n){
        super(n);
        persons = new ArrayList<Person>();
        paths = new ArrayList<Path>();
        knowledge = new ArrayList<Knowledge>();
        objects = new ArrayList<Object>();
    }

    public Place(ArrayList<Knowledge> knowledge, ArrayList<Object> objects, ArrayList<Person> persons,
            ArrayList<Path> paths, String name) {
        super(name);
        this.knowledge = knowledge;
        this.objects = objects;
        this.persons = persons;
        this.paths = paths;
    }

    public ArrayList<Knowledge> getKnowledge() {
        return knowledge;
    }

    public ArrayList<Knowledge> getActiveKnowledge() {
        updateVisibilityKnowledge();
        ArrayList<Knowledge> activeKnowledge = new ArrayList<Knowledge>();
        for (Knowledge n : knowledge) {
            if (n.getIsActive()) {
                activeKnowledge.add(n);
            }
        }
        return activeKnowledge;
    }

    public void updateVisibilityObjects() {
        for (Object o : objects) {
            o.updateVisibility();
        }
    }

    public void updateVisibilityKnowledge() {
        for (Knowledge k : knowledge) {
            k.updateVisibility();
        }
    }

    public void updateVisibilityPaths() {
        for (Path p : paths) {
            p.updateVisibility();
        }
    }

    public void updateVisibilityPersons(){
        for(Person p : persons) {
            p.updateVisibility();
        }
    }

    public void setKnowledge(ArrayList<Knowledge> knowledge) {
        this.knowledge = knowledge;
    }

    public void removeActiveKnowledge(){
        for(Knowledge k : knowledge){
            if(k.getIsActive()){
                knowledge.remove(k);
            }
        }
    }
    public ArrayList<Object> getObjects() {
        return objects;
    }

    public ArrayList<Object> getVisibleObjects() {
        updateVisibilityObjects();
        ArrayList<Object> visibleobjects = new ArrayList<Object>();
        for (Object o : objects) {
            if (o.isVisible()) {
                visibleobjects.add(o);
            }
        }
        return visibleobjects;
    }

    public void setObjects(ArrayList<Object> objects) {
        this.objects = objects;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public ArrayList<Person> getActivePersons() {
        updateVisibilityPersons();
        ArrayList<Person> activePersons = new ArrayList<Person>();
        for (Person p : persons) {
            if (p.getIsActive()) {
                activePersons.add(p);
            }
        }
        return activePersons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public ArrayList<Path> getPaths() {
        return paths;
    }

    public ArrayList<Path> getOpenPaths() {
        updateVisibilityPaths();
        ArrayList<Path> openPaths = new ArrayList<Path>();
        for (Path p : paths) {
            if (p.isOpen()) {
                openPaths.add(p);
            }
        }
        return openPaths;
    }

    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }

    public void addPersons(Person person){
        persons.add(person);
    }
    public void addKnowledge(Knowledge k){
        knowledge.add(k);
    }
    public void addPaths(Path p){
        paths.add(p);
    }

    public void addObject(Object o){
        objects.add(o);
    }
}
