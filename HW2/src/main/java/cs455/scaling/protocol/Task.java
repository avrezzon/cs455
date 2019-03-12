package cs455.scaling.protocol;

import cs455.scaling.server.Server;
import cs455.scaling.server.ThreadPoolManager;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;

public class Task {

    private SelectionKey key;
    private boolean dispatch;

    //This is the task that is created for registering
    public Task(SelectionKey key) {
        this.key = key;
        this.dispatch = false;
    }

    public Task(boolean dispatch) {
        this.key = null;
        this.dispatch = dispatch;
    }

    //This method will take the key within the batch and will read the 8KB byte array
    //then it will calculate the hash of that message
    //Sends the hashed message back to the client as a response
    private void doWork(SelectionKey key) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(8000);
        SocketChannel client = (SocketChannel) key.channel();

        int bytesRead = client.read(buffer);
        if (bytesRead == -1) {
            client.close();
        } else {
            Payload payload = new Payload(buffer.array());
            try {
                payload.calculateMsgHash();
            } catch (NoSuchAlgorithmException ne) {
                System.err.println(ne.getMessage() + ne.getStackTrace());
            }
            buffer = ByteBuffer.wrap(payload.getHashBytes());
            buffer.flip();
            client.write(buffer);
            buffer.clear();
        }
        key.attach(null); //Only at this point should the client be able to be added to the queue
        Server.stats.sendMsg();
    }

    //This will do the function based upon the type of action so that the worker thread can just call the resolve fn
    public void resolve() {
        try {

            //FIXME focus on being able to add them correctly rather than removing yet
//            if (dispatch) {
//                Batch currentBatch = ThreadPoolManager.removeBatch();
//                Iterator<SelectionKey> keys = currentBatch.getBatchMessages();
//                while (keys.hasNext()) {
//                    SelectionKey key = keys.next();
//                    doWork(key);
//                    keys.remove();
//                }
//            }

            //Need to validate that we aren't trying to read from an already closed channel
            if (this.key.isValid()) {

                //An acceptable flag will show that we need to connect to a new client
                if (this.key.isAcceptable()) {
                    Server.register(this.key);
                }

                //This will extract the key from the task and pass it into the linked list of batches
                if (this.key.isReadable()) {
                    //FIXME something is wrong with the build that now all I can register is one key
                    ThreadPoolManager.addMsgKey(this.key);
                }
            }

        } catch (IOException ie) {
            System.err.println(ie.getMessage());
        }
    }


}
