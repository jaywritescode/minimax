package info.jayharris.minimax.search;

import info.jayharris.minimax.TestAction;
import info.jayharris.minimax.TestState;

import java.lang.reflect.Field;

public class TestNodeBuilder {

    private final Builder builder;

    // TODO: implement other fields in Node class
    private final Field depthField;

    public TestNodeBuilder() throws NoSuchFieldException {
        builder = new Builder();

        depthField = Node.class.getDeclaredField("depth");
        depthField.setAccessible(true);
    }

    public TestNodeBuilder setDepth(int depth) {
        builder.setDepth(depth);
        return this;
    }

    public Node<TestState, TestAction> build() throws IllegalAccessException {
        Node node = Node.createRootNode(null, null);

        depthField.set(node, builder.depth);

        return node;
    }

    public static TestNodeBuilder create() throws NoSuchFieldException{
        return new TestNodeBuilder();
    }

    class Builder {
        int depth = 0;

        void setDepth(int depth) {
            this.depth = depth;
        }
    }
}
