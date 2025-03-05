package test.nfa;
import fa.nfa.NFA;
import java.util.Arrays;
import java.util.HashSet;

public class Test {
    public static void main(String[] args) {
        testIsDFA_TrueCase();
        testIsDFA_FalseCase_MultipleTransitionsForSameSymbol();
        
    }

    public static void testIsDFA_TrueCase() {
        NFA nfa = new NFA();

        // Create states
        nfa.addState("q0");
        nfa.addState("q1");

        // Set start and final state
        nfa.setStart("q0");
        nfa.setFinal("q1");

        // Add sigma (alphabet)
        nfa.addSigma('a');
        nfa.addSigma('b');

        // Add DFA-like transitions
        nfa.addTransition("q0", new HashSet<>(Arrays.asList("q1")), 'a'); // q0 -a-> q1
        nfa.addTransition("q1", new HashSet<>(Arrays.asList("q0")), 'b'); // q1 -b-> q0

        System.out.println("Test 1 (Should be DFA): " + (nfa.isDFA() ? "PASSED" : "FAILED"));
    }

    public static void testIsDFA_FalseCase_MultipleTransitionsForSameSymbol() {
        NFA nfa = new NFA();

        // Create states
        nfa.addState("q0");
        nfa.addState("q1");
        nfa.addState("q2");

        // Set start and final state
        nfa.setStart("q0");
        nfa.setFinal("q2");

        // Add sigma
        nfa.addSigma('a');

        // Add NFA-like transitions (q0 has multiple transitions on 'a')
        nfa.addTransition("q0", new HashSet<>(Arrays.asList("q1", "q2")), 'a'); // q0 -a-> {q1, q2}

        System.out.println("Test 2 (Should NOT be DFA): " + (!nfa.isDFA() ? "PASSED" : "FAILED"));
    }

    
}