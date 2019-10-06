package info.jayharris.minimax;

import java.util.Collection;

/**
 * Represents a state in the game.
 *
 * Note: We assume that the game has two players who always alternate turns.
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
     * Test if this is a terminal state.
     *
     * @return true iff this is a terminal state.
     */
    boolean terminalTest();
}
