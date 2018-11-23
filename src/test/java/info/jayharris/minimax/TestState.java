package info.jayharris.minimax;

import java.util.Collection;
import java.util.Collections;

public class TestState implements State<TestState, TestAction> {

    private final String id;

    private Collection<TestAction> actions;
    public Double heuristicValue;
    private boolean isCutoff;
    private boolean isTerminal;

    public TestState(String id, Collection<TestAction> actions, Double heuristicValue) {
        this(id, actions, heuristicValue, false);
    }

    public TestState(String id, Collection<TestAction> actions, Double heuristicValue, boolean isCutoff) {
        this.id = id;

        this.actions = actions;
        this.heuristicValue = heuristicValue;
        this.isCutoff = isCutoff;
        this.isTerminal = actions.isEmpty();
    }

    @Override
    public Collection<TestAction> actions() {
        return actions;
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
        return new TestState(id, actions, null);
    }

    static TestState cutoffTestState(String id, Collection<TestAction> actions, double heuristicValue) {
        return new TestState(id, actions, heuristicValue, true);
    }
}
