package info.jayharris.minimax;

import java.util.Collection;
import java.util.Collections;
import java.util.OptionalDouble;
import java.util.function.DoubleSupplier;

public class TestState implements State<TestState, TestAction> {

    final String id;

    Collection<TestAction> actions;
    DoubleSupplier supplier;
    boolean isTerminal;

    private TestState(String id, Collection<TestAction> actions, DoubleSupplier supplier) {
        this.id = id;

        this.actions = actions;
        this.supplier = supplier;
        this.isTerminal = actions.isEmpty();
    }

    @Override
    public Collection<TestAction> actions() {
        return actions;
    }

    @Override
    public OptionalDouble utility() {
        return OptionalDouble.of(supplier.getAsDouble());
    }

    @Override
    public double eval() {
        return utility().getAsDouble();
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

    static TestState terminalState(String id, double utility) {
        return new TestState(id, Collections.emptyList(), () -> utility);
    }

    static TestState nonTerminalState(String id, Collection<TestAction> actions) {
        return new TestState(id, actions, () -> { throw new RuntimeException(); });
    }
}
