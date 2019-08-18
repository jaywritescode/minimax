package info.jayharris.minimax;

public class TestAction implements Action<TestState, TestAction> {

    private TestState successor;

    public TestAction(TestState successor) {
        this.successor = successor;
    }

    @Override
    public TestState apply(TestState initialState) {
        return successor;
    }
}
