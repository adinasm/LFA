import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.min;

/**
 * Class that implements a DFA.
 */
public class DFA {
    private String startState;
    private Set<String> finalStates;
    private Map<String, HashSet<String>> transitions;
    private Map<String, HashSet<String>> reversedTransitions;

    private Set<String> accesibleStates;
    private Set<String> productiveStates;
    private Set<String> usefulStates;

    private Boolean acceptsE;
    private Boolean acceptsFinite;
    private Boolean accesibleFinalState;

    /**
     * Creates the dfa.
     */
    public DFA() {
        finalStates         = new HashSet<String>();
        transitions         = new HashMap<String, HashSet<String>>();
        reversedTransitions = new HashMap<String, HashSet<String>>();

        accesibleStates     = new HashSet<String>();
        productiveStates    = new HashSet<String>();
        usefulStates        = new HashSet<String>();

        acceptsE            = false;
        acceptsFinite       = true;
        accesibleFinalState = false;        
    }

    /**
     * Sets the start state.
     *
     * @param startState The new start state.
     */
    public void setStartState(final String startState) {
        this.startState = startState;
    }

    /**
     * Adds the given state to the set of final states. If it is equal to the
     * start state, then the dfa accepts e.
     *
     * @param finalState  The final state that has to be added.
     */
    public void addFinalState(final String finalState) {
        finalStates.add(finalState);

        if (startState.equals(finalState)) {
            acceptsE = true;
        }
    }

    /**
     * Saves the last read transition in the graph, as well as its reverse in
     * the transposed graph.
     *
     * @param source         The source of the transition.
     * @param destination    The destination of the transition.
     */
    public void addTransition(final String source, final String destination) {
        HashSet<String> newEntry = new HashSet<String>();

        if (transitions.containsKey(source)) {
            newEntry = transitions.get(source);
        }

        newEntry.add(destination);
        transitions.put(source, newEntry);

        newEntry = new HashSet<String>();

        if (reversedTransitions.containsKey(destination)) {
            newEntry = reversedTransitions.get(destination);
        }

        newEntry.add(source);
        reversedTransitions.put(destination, newEntry);
    }

    /**
     * Does a DFS traversal of the graph starting from the start state. All
     * the states that are found in the traversal are accesible states.
     *
     * @param state The current state.
     */
    public void computeAccesibleStates(String state) {
        HashSet<String> neigh = transitions.get(state);
        accesibleStates.add(state);

        if (neigh == null) {
            return;
        }

        for (String nextState : neigh) {
            if (!accesibleStates.contains(nextState)) {
                accesibleStates.add(nextState);
                computeAccesibleStates(nextState);
            }
        }
    }

    /**
     * Does DFS traversals on the transposed graph starting from all the final
     * states in order to discover the productive states. If they are also
     * accesible states, then they are saved in the useful states.
     *
     * @param state The current state.
     */
    public void computeUsefulStatesHelper(String state) {
        HashSet<String> neigh = reversedTransitions.get(state);
        productiveStates.add(state);

        if (accesibleStates.contains(state)) {
            usefulStates.add(state);

            if (finalStates.contains(state)) {
                accesibleFinalState = true;
            }
        }

        if (neigh == null) {
            return;
        }

        for (String nextState : neigh) {
            if (!productiveStates.contains(nextState)) {
                computeUsefulStatesHelper(nextState);
            }
        }
    }

    /**
     * Does DFS traversals on the transposed graph starting from all the final
     * states in order to discover the useful states.
     */
    public void computeUsefulStates() {
        computeAccesibleStates(startState);

        for (String state : finalStates) {
            if (!productiveStates.contains(state)) {
                computeUsefulStatesHelper(state);
            }
        }
    }

    /**
     * Determines if the current dfa accepts e.
     *
     * @return If the dfa accepts e.
     */
    public boolean acceptsE() {
        return acceptsE;
    }

    /**
     * Determines if the current dfa accepts an empty language by
     * computing the accesible, productive and useful states.
     *
     * @return If the dfa accepts an empty language.
     */
    public boolean acceptsEmptyLanguage() {
        computeUsefulStates();

        return ((usefulStates.size() == 0) || !accesibleFinalState
            || !productiveStates.contains(startState));
    }

    /**
     * Performs a DFS traversal in order to detect cycles. If a cycle that
     * contains a useful state is found, then the traversal ends and the dfa
     * accepts an infinite language.
     *
     * @param state     The current state.
     * @param visited   The set of visited states.
     * @param inStack   The set of states that are being processed.
     */
    public void findUsefulCycle(String state, Set<String> visited,
        Set<String> inStack) {
        HashSet<String> neigh = transitions.get(state);

        inStack.add(state);
        visited.add(state);

        if (neigh != null) {
            for (String nextState : neigh) {
                if (!visited.contains(nextState) && acceptsFinite) {
                    findUsefulCycle(nextState, visited, inStack);
                } else if (inStack.contains(nextState)
                        && usefulStates.contains(nextState)) {
                        acceptsFinite = false;
                }
            }
        }

        inStack.remove(state);
    }

    /**
     * Determines if the current dfa accepts a finite language by performing
     * a DFS traversal from the start state and 
     *
     * @return If the dfa accepts a finite language.
     */
    public boolean acceptsFiniteLanguage() {
        Set<String> inStack = new HashSet<String>();
        Set<String> visited = new HashSet<String>();

        computeUsefulStates();
        findUsefulCycle(startState, visited, inStack);

        return acceptsFinite;
    }

    /**
     * Prints the accesible states.
     */
    public void printAccesibleStates() {
        computeAccesibleStates(startState);

        for (String state : accesibleStates) {
            System.out.println(state);
        }
    }

    /**
     * Prints the useful states.
     */
    public void printUsefulStates() {
        computeUsefulStates();

        for (String state : usefulStates) {
            System.out.println(state);
        }
    }
}
