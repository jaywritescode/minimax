package info.jayharris.minimax;

/**
 * An action is a function that transitions one state to a successor state.
 *
 * @param <S> the type of State
 * @param <A> the type of Action
 */
public interface Action<S extends State<S, A>, A extends Action<S, A>> {

    /**
     * Apply this action to the given state.
     *
     * @param initialState the game state
     * @return a successor state
     */
    S perform(S initialState);
}
