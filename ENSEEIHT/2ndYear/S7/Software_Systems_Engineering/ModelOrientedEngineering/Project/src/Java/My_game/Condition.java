package My_game;

import java.util.ArrayList;

public class Condition extends GameElement {
    private Explorer explorer;
    private ArrayList<SubConditions> subConditions;
    private ArrayList<LogicalOperator> logicalOperators;

    public Condition(String name) {
        super(name);
        this.subConditions = new ArrayList<>();
        this.logicalOperators = new ArrayList<>();
        }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Condition(Explorer explorer, ArrayList<SubConditions> subConditions,
            ArrayList<LogicalOperator> logicalOperators, String name) {
        super(name);
        this.explorer = explorer;
        this.subConditions = subConditions;
        this.logicalOperators = logicalOperators;
    }

    public Explorer getExplorer() {
        return explorer;
    }

    public void setExplorer(Explorer explorer) {
        this.explorer = explorer;
    }

    public ArrayList<SubConditions> getSubConditions() {
        return subConditions;
    }

    public void setSubConditions(ArrayList<SubConditions> subConditions) {
        this.subConditions = subConditions;
    }

    public ArrayList<LogicalOperator> getLogicalOperators() {
        return logicalOperators;
    }

    public void setLogicalOperators(ArrayList<LogicalOperator> logicalOperators) {
        this.logicalOperators = logicalOperators;
    }

    private boolean subconditionRespectee(SubConditions sub, Explorer e) {
        boolean res = true;
        Comparator com = sub.getComparator();
        GameElement gm = sub.getGameElement();
        if(gm instanceof Object){
            Object o = (Object) gm;
            int compared = sub.getCompared();

            switch (com) {
                case EXACTLY:
                    if (!(e.getquantite(o) == compared)) {
                        res = false;
                    }
                    break;
                case STRICTLYMORETHAN:
                    if (!(e.getquantite(o) > compared)) {
                        res = false;
                    }
                    break;
                case MORETHAN:
                    if (!(e.getquantite(o) >= compared)) {
                        res = false;
                    }
                    break;
                case STRICTLYLESSTHAN:
                    if (!(e.getquantite(o) < compared)) {
                        res = false;
                    }
                    break;
                case LESSTHAN:
                    if (!(e.getquantite(o) <= compared)) {
                        res = false;
                    }
                    break;
                default:
                    res = true;
                    break;
               }
        }


           if(gm instanceof Knowledge){
              Knowledge n = (Knowledge) gm;
              int compared = sub.getCompared();

              switch (com) {
                  case EXACTLY:
                      if (!(e.getquantite(n) == compared)) {
                          res = false;
                      }
                      break;
                  case STRICTLYMORETHAN:
                      if (!(e.getquantite(n) > compared)) {
                          res = false;
                      }
                      break;
                  case MORETHAN:
                      if (!(e.getquantite(n) >= compared)) {
                          res = false;
                      }
                      break;
                  case STRICTLYLESSTHAN:
                      if (!(e.getquantite(n) < compared)) {
                          res = false;
                      }
                      break;
                  case LESSTHAN:
                      if (!(e.getquantite(n) <= compared)) {
                          res = false;
                      }
                      break;
                  default:
                      res = true;
                      break;
                }

            }

        return res;
    }

    public boolean ConditionVerifiee() {

        int nbSubCond = subConditions.size();
        boolean res = true;
        if (nbSubCond > 0){
            res = subconditionRespectee(subConditions.get(0), explorer);
            int compt = 1;
            while (compt < nbSubCond) {
                if (logicalOperators.get(compt -1) == LogicalOperator.OR) {
                    res = subconditionRespectee(subConditions.get(compt), explorer) || res;
                } else {
                    res =  subconditionRespectee(subConditions.get(compt), explorer) && res;
                }
                compt ++ ;
            }
      }
        return res;
     
     }
        
     public void addSubCond(SubConditions sc){
        subConditions.add(sc);
    }
    public void addLogicalOp(LogicalOperator lo){
        logicalOperators.add(lo);
    }
        

    
}
