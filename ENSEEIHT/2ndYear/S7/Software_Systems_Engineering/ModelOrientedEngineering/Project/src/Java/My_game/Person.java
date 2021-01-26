package My_game;

public class Person extends GameElement {
    private String description;
    private Boolean isActive;
    private Condition activityCondition;
    private Interaction interaction;


    public Person(String n){
        super(n);
    }


    public Person(String desc, boolean Active, Condition visibleWhen, Interaction interact, String name) {
        super(name);
        description = desc;
        isActive = Active;
        activityCondition = visibleWhen;
        interaction = interact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Condition getActivityCondition() {
        return activityCondition;
    }

    public void setActivityCondition(Condition activityCondition) {
        this.activityCondition = activityCondition;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }

    public void updateVisibility() {
        if (this.activityCondition.ConditionVerifiee()) {
            this.setIsActive(true);
        }
        else {this.setIsActive(false);}
    }

}
/*
 * Person: 'person' name=ID 'is' description=STRING '{' 'active' isActive =
 * Boolean ('if' activityCondition = Condition)? 'interactable' 'with'
 * interaction = Interaction '}' ;
 */