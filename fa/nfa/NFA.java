package fa.nfa;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import fa.State;

public class NFA implements NFAInterface {

    // Represents a linkedlist of states in NFA
    private LinkedHashSet<NFAState> states;

    // Represents a linkedlist of symbols in NFA
    private LinkedHashSet<Character> sigma;

    // Represents a linkedlist of the start state in NFA
    private LinkedHashSet<NFAState> startState;

    // Represents a linkedlist of the final state(s) in NFA
    private LinkedHashSet<NFAState> finalState;

    // Represents a linkedlist of the transitions in a given state of NFA
    private Map<NFAState, Map<Character, Set<NFAState>>> transitions;

    /**
     * @constructor
     * constructor for NFA object
     */
    public NFA() {
        states = new LinkedHashSet<NFAState>();
        sigma = new LinkedHashSet<Character>();
        startState = new LinkedHashSet<>();
        finalState = new LinkedHashSet<>();
        transitions = new HashMap<>();
    }

    @Override
    public boolean addState(String name) {
        NFAState newState = new NFAState(name);

        if (states.contains(newState)) {
            return false;
        }

        return states.add(newState);
    }

    @Override
    public boolean setFinal(String name) {
        for (NFAState NFAState : states) {
            if (NFAState.getName().equals(name)) {
                finalState.add(NFAState);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean setStart(String name) {
        if (startState.size() == 0) {
            for (NFAState NFAState : states) {
                if (NFAState.getName().equals(name)) {
                    startState.add(NFAState);
                    return true;
                }
            }

        } else {
            for (NFAState NFAState : states) {
                if (NFAState.getName().equals(name)) {
                    startState.clear();
                    startState.add(NFAState);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void addSigma(char symbol) {
        sigma.add(symbol);
    }

    @Override
    public boolean accepts(String s) {
        // TODO Implement to accept for epsilon transitions
            return false;
    }

    @Override
    public Set<Character> getSigma() {
        return sigma;
    }

    @Override
    public NFAState getState(String name) {
        for (State state : states) {
            if (state.getName().equals(name)) {
                return (NFAState) state;
            }

        }
        return null;
    }

    @Override
    public boolean isFinal(String name) {
        for (NFAState state : finalState) {
            if (state.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isStart(String name) {
        for (NFAState nfaState : startState) {
            if (nfaState.getName().equals(name)) {
                return true;
            }

        }
        return false;
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getToState'");
    }

    @Override
    public Set<NFAState> eClosure(NFAState s) {
        // TODO Implement Depth First Search  using stack in loop
        //      Eclosure loop should push children of current node 
        //      Onto stack 
        throw new UnsupportedOperationException("Unimplemented method 'eClosure'");
    }

    @Override
    public int maxCopies(String s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'maxCopies'");
    }

    @Override
    public boolean addTransition(String fromState, Set<String> toStates, char onSymb) {

        // Checks if symbol is epsilon symbol
        if (onSymb == 'e' || !sigma.contains(onSymb))
            return false;

        NFAState source = null;
        NFAState destination = null;

        // Getting the states we will add transitions too
        for (NFAState state : states) {
            if (state.getName().equals(fromState)) {
                source = state;
            }
            if (state.getName().equals(toStates)) {
                destination = state;
            }
        }

        // If either state is not found, return false
        if (source == null || destination == null) {
            return false;
        }

        // Add the transition
        transitions.putIfAbsent(source, new HashMap<>());
        Map<Character, NFAState> stateTransitions = transitions.get(source);

        if (stateTransitions.containsKey(onSymb))return true;
        
        stateTransitions.put(onSymb, destination);
        return true;
    }

    @Override
    public boolean isDFA() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isDFA'");
    }
    
}
