package info.jayharris.minimax;

import java.util.Collection;
import java.util.OptionalDouble;

/**
 * Represents a state in the game.
 *
 * @param <S> the type of State
 * @param <A> the type of Action
 */
public interface State<S extends State<S, A>, A extends Action<S, A>> {

    /**
     * Get all legal moves from this state.
     *
     * @return a collection of legal moves
     */
    Collection<A> actions();

    /**
     * Get the utility of this state, if this is a terminal state.
     *
     * @return the utility of this state, or {@code OptionalDouble.empty()} if this is not a terminal state.
     */
    OptionalDouble utility();

    /**
     * Evaluate this state.
     *
     * @return a heuristic value for this state, or a utility value if this is a terminal state
     */
    double eval();

    /**
     * Test if this is a terminal state.
     *
     * @return true iff this is a terminal state.
     */
    boolean terminalTest();
}
