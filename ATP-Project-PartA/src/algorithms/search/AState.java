package algorithms.search;

public abstract class AState {

    private String state;
    private int cost;
    private AState cameFrom;

    public AState(String state){
        this.state = state;
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
        if (state == null) {
            return otherState.state == null;
        }
        //else
        return state.equals(otherState.state);
    }


    @Override
    public int hashCode(){
        if (state == null){
            return 0;
        }
        return state.hashCode();
    }

    public void setCost(int cost){
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public void setCameFrom(AState cameFrom){
        this.cameFrom = cameFrom;
    }

    public AState getCameFrom() {
        return cameFrom;
    }
}
