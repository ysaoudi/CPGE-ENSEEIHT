package My_game;

public class Knowledge extends GameElement {
    private Integer count;
    private Boolean isActive;
    private Condition activityCondition;



    public Knowledge(String n){
        super(n);
    }
    
    public Knowledge(Boolean isActive, Condition activityCondition, String name) {
        super(name);
        this.isActive = isActive;
        this.activityCondition = activityCondition;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Knowledge(Boolean isActive, String name) {
        super(name);
        this.isActive = isActive;
        this.activityCondition = null;
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

    public void updateVisibility() {
        if (this.activityCondition.ConditionVerifiee()) {
            this.setIsActive(true);
        }
    }

    public boolean sameAs(Knowledge k) {
        return this.equals(k);
    }
}
