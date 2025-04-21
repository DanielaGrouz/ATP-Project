package algorithms.search;

public abstract class AState {

    private String stateName;
    private double cost;
    private AState cameFrom;

    public AState(String stateName){
        this.stateName = stateName;
        this.cost = 0;
    }

    @Override
    public boolean equals(Object other){
        if (this == other){
            return true;
        }
        if (other == null || getClass() != other.getClass()){
            return false;
        }

        AState otherState = (AState) other;
        if (stateName == null) {
            return otherState.stateName == null;
        }
        //else
        return stateName.equals(otherState.stateName);
    }


    @Override
    public int hashCode(){
        if (stateName == null){
            return 0;
        }
        return stateName.hashCode(); //??????
    }

    public void setCost(double cost){
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public AState getCameFrom(){
        return cameFrom;
    }
}
