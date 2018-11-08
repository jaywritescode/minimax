package info.jayharris.minimax;

import java.util.Collection;
import java.util.Collections;

public class TestState implements State<TestState, TestAction> {

    private final String id;

    private Collection<TestAction> actions;
    private double heuristicValue;
    private boolean isTerminal;

    private int evalCount = 0;

    public TestState(String id, Collection<TestAction> actions, double heuristicValue) {
        this.id = id;

        this.actions = actions;
        this.heuristicValue = heuristicValue;
        this.isTerminal = actions.isEmpty();
    }

    @Override
    public Collection<TestAction> actions() {
        return actions;
    }

    @Override
    public double eval() {
        evalCount++;
        return heuristicValue;
    }

    @Override
    public boolean terminalTest() {
        return isTerminal;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TestState{");
        sb.append("id='").append(id).append('\'');
        sb.append('}');
        return sb.toString();
    }

    static TestState terminalState(String id, double heuristicValue) {
        return new TestState(id, Collections.emptyList(), heuristicValue);
    }

    static TestState nonTerminalState(String id, Collection<TestAction> actions) {
        return new TestState(id, actions, Double.NaN);
    }
}
