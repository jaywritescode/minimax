package info.jayharris.minimax;

import java.util.Collection;
import java.util.OptionalLong;

public interface State<S extends State<S, A>, A extends Action<S, A>> {

    Collection<A> actions();

    OptionalLong utility();

    boolean terminalTest();
}
