package algorithms.search;

public abstract class AState {

    private String state;
    private int cost;
    private AState cameFrom;

    /**
     * Constructs a new state.
     *
     * @param state a unique string representing this state
     */
    public AState(String state){
        this.state = state;
        this.cost = 0;
    }

    /**
     * Checks if this state is equal to another object.
     * Two states are considered equal if their state identifiers are equal.
     *
     * @param other the object to compare with
     * @return true if the states are equal, false otherwise
     */
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

    /**
     * Returns the hash code for this state, based on its identifier.
     *
     * @return the hash code of this state
     */
    @Override
    public int hashCode(){
        if (state == null){
            return 0;
        }
        return state.hashCode();
    }

    /**
     * Returns a string representation of this state.
     *
     * @return the state identifier as a string
     */
    @Override
    public String toString() {
        return state;
    }

    /**
     * Sets the cost of this state.
     *
     * @param cost the cost value to set
     */
    public void setCost(int cost){
        this.cost = cost;
    }

    /**
     * Returns the cost of this state.
     *
     * @return the cost value
     */
    public int getCost() {
        return cost;
    }

    /**
     * Sets the predecessor state from which this state was reached during the search.
     *
     * @param cameFrom the previous state in the path
     */
    public void setCameFrom(AState cameFrom){
        this.cameFrom = cameFrom;
    }

    /**
     * Returns the predecessor state from which this state was reached during the search.
     *
     * @return the previous state in the path
     */
    public AState getCameFrom() {
        return cameFrom;
    }
}
