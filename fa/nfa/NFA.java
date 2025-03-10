package fa.nfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import fa.State;



/**
 * @author Bryce Kratzer
 * @author Tristan Jones
 * @version CS 361 Spring 2025
 * @description This class has methods for creating an NFA such as adding characters, transitions, and initial and final states.
 */
public class NFA implements NFAInterface {

    // Represents a linkedlist of states in NFA
    private LinkedHashSet<NFAState> states;

    // Represents a linkedlist of symbols in NFA
    private LinkedHashSet<Character> sigma;

    // Represents a linkedlist of the start state in NFA
    private LinkedHashSet<NFAState> startState;

    // Represents a linkedlist of the final state(s) in NFA
    private LinkedHashSet<NFAState> finalState;

    // Represents each states character's transitions
    //
    // In NFA, characters can have multiple transitions per state.
    // The key of our map is our current state, the value of our map is
    // the characters and the characters transitions.
    private Map<NFAState, Map<Character, Set<NFAState>>> transitions;

    /**
     * @constructor
     *              constructor for NFA object
     */
    public NFA() {
        states = new LinkedHashSet<NFAState>();
        sigma = new LinkedHashSet<Character>();
        startState = new LinkedHashSet<>();
        finalState = new LinkedHashSet<>();
        transitions = new HashMap<NFAState, Map<Character, Set<NFAState>>>();
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

        if (startState.isEmpty())
            return false;

        // Get all possible start states based on the eclosure of our start state
        Set<NFAState> currentStates = eClosure(startState.iterator().next());

        for (char currentSymb : s.toCharArray()) {

            Set<NFAState> nextStates = new HashSet<>();

            // Loop through each state and get all transitions for current symbol
            for (NFAState state : currentStates) {

                // Check if this state has ANY transitions defined
                if (transitions.containsKey(state)) {

                    // Then check if it has transitions for this specific symbol
                    if (transitions.get(state).containsKey(currentSymb)) {
                        nextStates.addAll(transitions.get(state).get(currentSymb));
                    }
                }
            }

            // Represents the eclosures of all next states
            Set<NFAState> allNextStates = new HashSet<>();

            // Loop through all possible next states obtained and
            // add them to the eclosure set
            for (NFAState state : nextStates) {
                allNextStates.addAll(eClosure(state));
            }

            currentStates = allNextStates;
        }
        for (NFAState finalState : finalState) {
            if (currentStates.contains(finalState)) {
                return true;
            }
        }
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
        
        if (!transitions.containsKey(from)) {
            return new HashSet<>();
        }

      
        Map<Character, Set<NFAState>> stateTransitions = transitions.get(from);

       
        if (!stateTransitions.containsKey(onSymb)) {
            return new HashSet<>();
        }

      
        return new HashSet<>(stateTransitions.get(onSymb));
    }
    @Override
    public Set<NFAState> eClosure(NFAState s) {

        HashSet<NFAState> eClosure = new HashSet<>();
        Stack<NFAState> stack = new Stack<>();

        eClosure.add(s);
        stack.push(s);

        // To track if we have went through the loop more than once.
        // Will be used to check if we traversed the entire NFA
        boolean wentThroughOnce = false;

        while (!stack.empty()) {

            // Look at most recently viewed state from stack
            NFAState currentState = stack.peek();

            // Get all transitions for current state, if no transitions continue
            Map<Character, Set<NFAState>> currentStateTransitions = transitions.get(currentState);

            // If no states in general, pop current state out of our stack
            if (currentStateTransitions == null) {
                stack.pop();
                continue;
            }

            // Get all epsilon transitions from our current state
            Set<NFAState> currentEpsilonTransitions = transitions.get(currentState).get('e');

            // If no epsilon transitions or we traversed the entire NFA pop current state
            // out off stack
            if (currentEpsilonTransitions == null || (currentState.equals(s) && wentThroughOnce == true)) {
                stack.pop();
                continue;
            }

            // For each state reachable through an ε-transition
            for (NFAState nextState : currentEpsilonTransitions) {

                // If this state hasn't been visited yet
                if (!eClosure.contains(nextState)) {

                    // Add it to the result set
                    eClosure.add(nextState);

                    // Push it to the stack to explore its ε-transitions later
                    stack.push(nextState);
                } else {
                    // If already visited, take out of stack
                    stack.pop();
                }
            }
            wentThroughOnce = true;
        }
        return eClosure;
    }

    @Override
    public int maxCopies(String s) {
        if (startState.isEmpty()) return 0;

        Set<NFAState> currentStates = eClosure(startState.iterator().next()); 
        int maxCopies = currentStates.size(); 

        for (char symbol : s.toCharArray()) {
            Set<NFAState> nextStates = new HashSet<>();

           
            for (NFAState state : currentStates) {
                nextStates.addAll(getToState(state, symbol));
            }

           
            Set<NFAState> allNextStates = new HashSet<>();
            for (NFAState state : nextStates) {
                allNextStates.addAll(eClosure(state));
            }

            currentStates = allNextStates; 

           
            maxCopies = Math.max(maxCopies, currentStates.size());
        }

        return maxCopies;
    }

    @Override
    public boolean addTransition(String fromState, Set<String> toStates, char onSymb) {

        // Checks if symbol is epsilon symbol
        if (onSymb != 'e' && !sigma.contains(onSymb))
            return false;

        NFAState source = null;

        // Represents a set of all states our symbol will go too
        // as one symbol can have multiple transitions from one state
        HashSet<NFAState> destinations = new HashSet<>();

        // Getting the states we will add transitions too
        for (NFAState state : states) {
            if (state.getName().equals(fromState)) {
                source = state;
            }

            // Checks if state is in our state
            if (toStates.contains(state.getName())) {
                // Add transitions too our set of transitions
                destinations.add(state);
            }
        }

        // If start state is not found or we don't
        // find all states in the set of given states
        // return false
        if (source == null || destinations.size() != toStates.size()) {
            return false;
        }

        // If source has no transitions, add to transitions
        transitions.putIfAbsent(source, new HashMap<>());

        // Get the transitions for the current state
        Map<Character, Set<NFAState>> stateTransitions = transitions.get(source);

        // Initialize the set for this symbol if it doesn't exist
        stateTransitions.putIfAbsent(onSymb, new HashSet<>());

        // Get transitions for the symbol
        Set<NFAState> symbolTransitions = stateTransitions.get(onSymb);

        // Add all destinations to the symbol transitions
        symbolTransitions.addAll(destinations);

        return true;
    }

    @Override
public boolean isDFA() {
    for (Map.Entry<NFAState, Map<Character, Set<NFAState>>> entry : transitions.entrySet()) {
        NFAState state = entry.getKey();
        Map<Character, Set<NFAState>> stateTransitions = entry.getValue();

        // Check for epsilon transitions (DFA cannot have these)
        if (stateTransitions.containsKey('e')) {
            return false;
        }

        // Check if any symbol has multiple transitions
        for (Map.Entry<Character, Set<NFAState>> transition : stateTransitions.entrySet()) {
            char symbol = transition.getKey();
            Set<NFAState> destinations = transition.getValue();

            if (destinations.size() > 1) {
                return false;
            }
        }

        // Ensure every state has a transition for each symbol in sigma
        for (char symbol : sigma) {
            if (!stateTransitions.containsKey(symbol)) {
                return false;
            }
        }
    }

    return true;
}
}
