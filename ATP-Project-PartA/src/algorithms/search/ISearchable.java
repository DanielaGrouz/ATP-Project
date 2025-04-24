package algorithms.search;

import java.util.ArrayList;

public interface ISearchable {
    /**
     * Returns the starting state of the problem.
     *
     * @return the start state
     */
    AState getStartState();

    /**
     * Returns the goal state of the problem.
     *
     * @return the goal state
     */
    AState getGoalState();

    /**
     * Returns all possible and legal states that can be reached from the given state.
     *
     * @param aState the current state
     * @return a list of all the possible successor states
     */
    ArrayList<AState> getAllPossibleStates(AState aState);

}
