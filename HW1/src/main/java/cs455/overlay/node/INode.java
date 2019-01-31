package cs455.overlay.node;

import cs455.overlay.wireformats.*;

public interface INode {
    public void onEvent(IEvent event);
}
