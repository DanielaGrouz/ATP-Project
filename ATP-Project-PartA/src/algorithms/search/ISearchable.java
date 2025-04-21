package algorithms.search;

import java.util.ArrayList;

public interface ISearchable {

    AState getStartState();

    AState getGoleState();

    ArrayList<AState> getSuccessors(AState s);
}
