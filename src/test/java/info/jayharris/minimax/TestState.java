package info.jayharris.minimax;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class TestState implements State<TestState, TestAction> {

    private final String id;
    private final Collection<TestAction> actions;
    private final double utility;

    private TestState(String id, double utility) {
        this.id = id;
        this.actions = Collections.emptySet();
        this.utility = utility;
    }

    private TestState(String id, Collection<TestAction> actions) {
        this.id = id;
        this.actions = actions;
        this.utility = Double.NaN;
    }

    @Override
    public Collection<TestAction> actions() {
        return actions;
    }

    @Override
    public boolean terminalTest() {
        return actions().isEmpty();
    }

    public String getId() {
        return id;
    }

    public double getUtility() {
        return utility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestState testState = (TestState) o;
        return Objects.equals(id, testState.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static TestState terminalState(String id, double utility) {
        return new TestState(id, utility);
    }

    public static TestState nonTerminalState(String id, Collection<TestAction> actions) {
        return new TestState(id, actions);
    }

    public static TestState nonTerminalState(String id, TestState... successors) {
        Collection<TestAction> actions = Arrays.stream(successors).map(TestAction::new).collect(Collectors.toList());
        return nonTerminalState(id, actions);
    }
}
