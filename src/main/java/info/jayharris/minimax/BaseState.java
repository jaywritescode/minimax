package info.jayharris.minimax;

public abstract class BaseState<S extends State<S, A>, A extends Action<S, A>> implements State<S, A> {
    @Override
    public boolean terminalTest() {
        return actions().isEmpty();
    }
}
