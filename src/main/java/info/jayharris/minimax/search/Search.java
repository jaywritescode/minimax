package info.jayharris.minimax.search;

import info.jayharris.minimax.Action;
import info.jayharris.minimax.State;

/**
 * An algorithm that searches for the best move ({@code Action}) across a
 * universe of legal moves.
 *
 * @param <S> the type of State
 * @param <A> the type of Action
 */
public interface Search<S extends State<S, A>, A extends Action<S, A>> {

    /**
     * Search for the optimal action given the initial state.
     *
     * @param initialState a game state
     * @return an action
     */
    A perform(S initialState);

    /**
     * Get the utility of the given state.
     *
     * This should be implemented in the game's project.
     *
     * @param state a state
     * @return the utility of the state
     */
    double utility(S state);
}
