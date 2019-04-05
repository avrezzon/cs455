package cs455.hadoop.HW3;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;

public class Q3Helper implements Writable {
    private Double max = new Double(0);
    private String songId = new String();
    public Double getMax() {
        return max;
    }
    public void setMax(Double max) {
        this.max = max;
    }
    public String getSongId(){
        return songId;
    }
    public void setSongId(String id){
        this.songId = id;
    }
    public void readFields(DataInput in) throws IOException {
        songId = in.readLine();
        max = in.readDouble();
    }
    public void write(DataOutput out) throws IOException {
        out.writeBytes(songId);
        out.writeDouble(max);
    }
    public String toString() {
        return songId + "\t" + max;
    }
}