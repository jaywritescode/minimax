package info.jayharris.minimax;

import java.util.Collection;
import java.util.Collections;
import java.util.OptionalLong;

public class TestState implements State<TestState, TestAction> {

    final String id;

    Collection<TestAction> actions;
    OptionalLong utility;
    boolean isTerminal;

    private TestState(String id, Collection<TestAction> actions, OptionalLong utility) {
        this.id = id;

        this.actions = actions;
        this.utility = utility;
        this.isTerminal = actions.isEmpty();
    }

    @Override
    public Collection<TestAction> actions() {
        return actions;
    }

    @Override
    public OptionalLong utility() {
        return utility;
    }

    @Override
    public boolean terminalTest() {
        return isTerminal;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TestState{");
        sb.append("id='").append(id).append('\'');
        sb.append('}');
        return sb.toString();
    }

    static TestState terminalState(String id, long utility) {
        return new TestState(id, Collections.emptyList(), OptionalLong.of(utility));
    }

    static TestState nonTerminalState(String id, Collection<TestAction> actions) {
        return new TestState(id, actions, OptionalLong.empty());
    }
}
