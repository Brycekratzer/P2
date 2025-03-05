README - NFA Implementation

Description:

This program implements a Non-deterministic Finite Automaton (NFA) in Java. It includes methods to:

*Add states and transitions.

*Set start and final states.

*Process strings to determine acceptance.

*Compute epsilon-closures.

*Determine if the NFA is a DFA.

*Track the maximum number of NFA copies created during execution.

The implementation follows the standard definition of an NFA, allowing multiple transitions per symbol and epsilon ('e') transitions.

Authors:

Tristan Jones and Bryce Kratzer



Compilation Instructions:

1.Open a terminal in the root directory of the project.

2.Compile the Java files using this command:

    javac fa/nfa/*.java test/nfa/*.java

This compiles all source and test files.

Running the Program:

1. Navigate to the test/nfa directory
2. Type the following command to run the test suite:
    java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore test.nfa.NFATest

The test results will be displayed in the terminal, indicating which tests passed or failed.

Notes:
The program assumes valid input and proper state management.

The test suite ensures correctness of core functionalities.

For debugging, print transitions using System.out.println(nfa.getTransitions());

