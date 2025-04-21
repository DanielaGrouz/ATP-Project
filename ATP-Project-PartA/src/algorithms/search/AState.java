package algorithms.search;

public abstract class AState {

    private String state;
    private double cost;
    public AState cameFrom;

    public AState(String state){
        this.state = state;
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
        if (state==null){
            return 0;
        }
        return state.hashCode(); //??????
    }

}
