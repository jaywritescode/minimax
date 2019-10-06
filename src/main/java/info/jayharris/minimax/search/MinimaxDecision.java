package info.jayharris.minimax.search;

import info.jayharris.minimax.Action;
import info.jayharris.minimax.Node;
import info.jayharris.minimax.State;

import java.util.Comparator;

/**
 * Minimax search algorithm implementation.
 *
 * @param <S> the type of State
 * @param <A> the type of Action
 */
public abstract class MinimaxDecision<S extends State<S, A>, A extends Action<S, A>> implements Search<S, A> {

    @Override
    public A perform(S initialState) {
        return perform(Node.createRootNode(initialState, this::maxValue));
    }

    private A perform(Node<S, A> root) {
        double v = root.getValue();

        return root.getKnownSuccessors().stream()
                .filter(node -> node.getValue() == v)
                .findFirst()
                .map(Node::getAction)
                .orElseThrow(RuntimeException::new);
    }

    private double maxValue(Node<S, A> node) {
        S state = node.getState();

        if (state.terminalTest()) {
            return utility(state);
        }

        return state.actions().stream()
                .map(action -> Node.createSuccessor(node, action, this::minValue))
                .max(Comparator.comparingDouble(Node::getValue))
                .map(Node::getValue)
                .get();
    }

    private double minValue(Node<S, A> node) {
        S state = node.getState();

        if (state.terminalTest()) {
            return utility(state);
        }

        return state.actions().stream()
                .map(action -> Node.createSuccessor(node, action, this::maxValue))
                .min(Comparator.comparingDouble(Node::getValue))
                .map(Node::getValue)
                .get();
    }
}
