package info.jayharris.minimax;

class TestAction implements Action<TestState, TestAction> {

    TestState successor;

    public TestAction(TestState successor) {
        this.successor = successor;
    }

    @Override
    public TestState apply(TestState initialState) {
        return successor;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TestAction{");
        sb.append("successor=").append(successor);
        sb.append('}');
        return sb.toString();
    }
}
