package info.jayharris.minimax;

import java.util.Comparator;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.function.DoubleSupplier;
import java.util.stream.Collectors;

public class Node2<S extends State<S, A>, A extends Action<S, A>> {

    private final S state;

    private final A action;

    private final int depth;

    private DoubleSupplier eval;

    public static Comparator<Node2> comparator = Comparator.comparingDouble(Node2::getHeuristicValue);

    public Node2(S state, A action, int depth, DoubleSupplier eval) {
        this.state = state;
        this.action = action;
        this.depth = depth;
        this.eval = new MemoizedDoubleSupplier(eval);
    }

    Set<Node2<S, A>> successors() {
        return state.actions().stream().map(this::apply).collect(Collectors.toSet());
    }

    Node2<S, A> apply(A successorAction) {
        return new Node2(successorAction.apply(state), successorAction, depth + 1, eval);
    }

    S getState() {
        return state;
    }

    A getAction() {
        return action;
    }

    double getHeuristicValue() {
        return eval.getAsDouble();
    }

    boolean terminalTest() {
        return state.terminalTest();
    }

    void setEvalAsConstant(double value) {
        this.eval = () -> value;
    }

    class MemoizedDoubleSupplier implements DoubleSupplier {

        OptionalDouble value = OptionalDouble.empty();

        final DoubleSupplier supplier;

        MemoizedDoubleSupplier(DoubleSupplier supplier) {
            this.supplier = supplier;
        }

        @Override
        public double getAsDouble() {
            if (!value.isPresent()) {
                value = OptionalDouble.of(supplier.getAsDouble());
            }

            return value.getAsDouble();
        }
    }
}
