package My_game;

import java.util.ArrayList;

public class Explorer extends GameElement {
    private ArrayList<Object> objects;
    private ArrayList<Knowledge> knowledge;
    private int currentWeight;
    private Place currentPlace;



    public Explorer(String n){
        super(n);
        this.objects = new ArrayList<Object>();
        this.knowledge = new ArrayList<Knowledge>();
        this.currentWeight = 0;
    }


    public Explorer(String name, ArrayList<Object> objects, ArrayList<Knowledge> knowledge, int currentWeight,
            Place currentPlace) {
        super(name);
        this.objects = objects;
        this.knowledge = knowledge;
        this.currentWeight = currentWeight;
        this.currentPlace = currentPlace;
    }

    public Explorer(String name, int currentWeight, Place currentPlace) {
        super(name);
        this.currentWeight = currentWeight;
        this.currentPlace = currentPlace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Object> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<Object> objects) {
        this.objects = objects;
    }

    public ArrayList<Knowledge> getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(ArrayList<Knowledge> knowledge) {
        this.knowledge = knowledge;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public int getCumulatedWeight() {
        int c = 0;
        for (Object o : objects) {
            c += o.getCount() * o.getSize();
        }
        return c;
    }

    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }

    public Place getCurrentPlace() {
        return currentPlace;
    }

    public void setCurrentPlace(Place currentPlace) {
        this.currentPlace = currentPlace;
    }
    
    public int existKnowledge( Knowledge k){
        int e = -1;
        for (int i = 0; i< knowledge.size();i++){
            if (knowledge.get(i).sameAs(k)){
               e = i;
            }
        }
        return e;
    }
    public void addKnowledges(ArrayList<Knowledge> knowledges) {
        for (Knowledge k : knowledges){
             addKnowledge(k);
        }
    }

    public void addKnowledge(Knowledge k) {
        if(existKnowledge(k) == -1){
        this.knowledge.add(k);
        }
        else {
            Knowledge kn = knowledge.get(existKnowledge(k));
            knowledge.get(existKnowledge(k)).setCount(kn.getCount() + 1);
        }
    }

    public int existObject( Object o){
        int e = -1;
        for (int i = 0; i< objects.size();i++){
            if (objects.get(i).sameAs(o)){
               e = i;
            }
        }
        return e;
    }
    public void addObjects(ArrayList<Object> obj) {
        for (Object o : obj){
             addObject(o);
        }
    }

    public void addObject(Object o) {
        if(existObject(o) == -1){
        this.objects.add(o);
        }
        else {
            Object obj = objects.get(existObject(o));
            objects.get(existObject(o)).setCount(obj.getCount() + 1);
        }
        setCurrentWeight(getCumulatedWeight());
    }

    public void removeObjects(ArrayList<Object> obj) {
        for (Object o : obj){
             this.removeObject(o);
        }
    }

    public void removeObject(Object o) {
        
        if (getquantite(o) >= 1) {
            Object obj = objects.get(existObject(o));
            objects.get(existObject(o)).setCount(obj.getCount() - 1);
        }
        setCurrentWeight(getCumulatedWeight());
    }

    public int getquantite(Object o) {
        int n = 0;
        for (Object obj : this.objects) {
            if (o.sameAs(obj)) {
                n += obj.getCount();
            }
        }
        return n;
    }

    public int getquantite(Knowledge k) {
        int n = 0;
        for (Knowledge kn : this.knowledge) {
            if (kn.sameAs(k)) {
                n += k.getCount();
            }
        }
        return n;
    }

}