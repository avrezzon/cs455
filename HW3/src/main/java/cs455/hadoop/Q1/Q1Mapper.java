package cs455.hadoop.Q1;

//Goal: Which artist has the most songs in the data set?

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Mapper: Reads line by line, split them into words. Emit <word, 1> pairs.
 */
public class Q1Mapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String line = value.toString();
        String[] song_values = line.split(",+");
        try {
            String artist = song_values[6];
            context.write(new Text(), new IntWritable(1));
        }catch(ArrayIndexOutOfBoundsException ae){

        }
        //TODO note that in the assignment, the artist name is not garunteed to be unique

    }
}
