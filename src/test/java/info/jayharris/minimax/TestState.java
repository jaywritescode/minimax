package info.jayharris.minimax;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.function.DoubleSupplier;

public class TestState implements State<TestState, TestAction> {

    private final String id;

    private Collection<TestAction> actions;
    private DoubleSupplier supplier;
    private boolean isTerminal;

    private int evalCount = 0;

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
    public double eval() {
        evalCount++;
        return supplier.getAsDouble();
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

    static TestState terminalState(String id, double utility) {
        return new TestState(id, Collections.emptyList(), () -> utility);
    }

    static TestState nonTerminalState(String id, Collection<TestAction> actions) {
        return new TestState(id, actions, () -> new Random().nextDouble() * 2 - 1);
    }
}
