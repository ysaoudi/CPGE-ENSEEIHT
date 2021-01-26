package Display;

import java.util.ArrayList;
import My_game.*;
import My_game.Object;
public class DisplayImpl implements Display {
        @Override
        public void displaylocation(Place place){
            System.out.println();
            System.out.println("you are in :" + place.getName() );
            System.out.println();            
        }
         @Override
        public void locationdetails(Place place){
            ArrayList<Object> visibleObjects = place.getVisibleObjects();
            ArrayList<Person> activePersons = place.getActivePersons();
            ArrayList<Knowledge> activeKnowledge = place.getActiveKnowledge();

        System.out.println("******************* Visible objects *******************");
		displayObjects(visibleObjects);
		System.out.println("***************** Active people *****************");
		displayPeople(activePersons);
        System.out.println("***************** Active knowledge *****************");
        displayKnowledge(activeKnowledge);
		System.out.println("*******************************************************");
        }
        
        @Override
        public void availablepaths(Place place){
            System.out.println("******************* available paths *******************");
            displayPaths(place.getOpenPaths());
            System.out.println("*******************************************************");
        }

        @Override
        public void pathTaken(Path path){
            System.out.println();
            System.out.println("the path you are taking is:" + path.getName());
            System.out.println();
            
        }
        @Override
        public void possessedObjects(Explorer explorer, Game game){
           System.out.println();
           System.out.print("you possess the following objects :");
           displayObjects(explorer.getObjects());
           System.out.println("the cumulated weight is:" + explorer.getCumulatedWeight());
           System.out.println("the available weight is :" +( game.getMaxWeight() - explorer.getCumulatedWeight()));
        }
         
        @Override
        public void possessedKnowledge(Explorer explorer){
            System.out.println();
            System.out.println("you possess the following set of knowledge:");
            displayKnowledge(explorer.getKnowledge());
        }

        @Override
        public void newKnowledge(Knowledge knowledge){
            System.out.println();
            System.out.println("you have acquired the knowledge:" + knowledge.getName());
    
        }

        @Override
        public void chosenObject(Object object){
            System.out.println();
            System.out.println("you have chosen the object:" + object.getName());
    
        }
        
        @Override
        public void newObject(Object object){
            System.out.println();
            System.out.println("you just acquire the object:" + object.getName());    
    
        }
       @Override
       public void notEnoughPlace(Object object){
           System.out.println("you do not have enough space to acquire" + object);
       }

       @Override
       public void impossibleDrop(){
           System.out.println("you can not drop anything here");
       }

       @Override
       public void droppedObject(Object object){
           System.out.println("you have just dropped" + object);
       }
       
       @Override
       public void consommumedObject(Object object){
        System.out.println("you have just consumed" + object.getName());
    }

    @Override
    public void interaction(Person person){
        System.out.println(person.getName() + " gives you the following choices:");
        displayChoices(person.getInteraction().getChoices());
    }

    @Override
    public int displayActions(Choice choice){
        int i = 0;
        ArrayList<Action> actions = choice.getActions();
        if (actions.size() == 0) {
            System.out.println("there are no actions !");
        }
        else {
            System.out.println("What do you want to do ?");
            for(Action a : actions){
                i++;
			System.out.println("   " + i + ") " + a.getName());
            }
        }
        return i;

    }


// TODO  corriger les affichages
@Override 
public int displayObjects(ArrayList<Object> objects){
    int i = 0;
    if (objects.size() == 0){
        System.out.println("there are no objects here !");
     }
     else{
         for (Object o : objects){
             i++;
             System.out.println("Id, name, size");
             System.out.println("  " + i + "  "+ o.getName() + "  " +  o.getSize());
         }
     }
     return i;
}

@Override
public int displayChoices(ArrayList<Choice> choices){
    int i = 0;
    if (choices.size() == 0) {
        System.out.println("there are no choices for you !");
    }
    else {
        System.out.println("What do you want to choose from ?");
        for(Choice c : choices){
            i++;
        System.out.println("   " + i + ") " + c.getDescription());
        }
    }
    return i;

}

@Override
public int displayPossessedObjects(Explorer explorer){
    int i = 0;
    ArrayList<Object> possessedObjects = explorer.getObjects();
    if(possessedObjects.size() == 0){
        System.out.println("You do not possess any objects :(");
    }
    else{
        System.out.println(" Quantity name size");
        for (Object o : possessedObjects){
			i++;
			System.out.println("  "+ o.getCount() +"  " + o.getName() +"  "+ o.getSize()); 
    }  
}

    return i;
}

//@Override 
public int displayKnowledge(ArrayList<Knowledge> knowledge){
    int i = 0;
    if (knowledge.size() == 0) {
        System.out.println("there is no knowledge !");
    }
    else {
        for(Knowledge n : knowledge){
            i++;
        System.out.println("   " + i + ") " + n.getName());
        }
    }
    return i;
}

//@Override
public int displayPeople(ArrayList<Person> people){
    int i = 0;
    if (people.size() == 0) {
        System.out.println("there are no people !");
    }
    else {
        for(Person p : people){
            i++;
        System.out.println("   " + i + ") " + p.getName());
        }
    }
    return i;
}

//@Override
public int displayPaths(ArrayList<Path> paths){
    int i = 0;
    if (paths.size() == 0) {
        System.out.println("there are no paths !");
    }
    else {
        for(Path p : paths){
            i++;
        System.out.println("   " + i + ") " + p.getName());
        }
    }
    return i;
}

@Override
public void endGame() {
    System.out.println("you have finished the game !");
}

}


