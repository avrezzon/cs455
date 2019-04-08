package cs455.hadoop.HW3;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class ArtistMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String[] csvLine = (value.toString()).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        String artistName = null;
        String artistUid = null;
        String songId = null;
        String title = null;

        //throw out invalid data
        if(csvLine.length > 14){

            artistUid = csvLine[3];
            artistName = csvLine[7];
            songId = csvLine[8];
            title = csvLine[9];

            //This portion is used for question one.  Count the number of occurences that the artist is present in the data
            context.write(new Text("Q1"+artistUid), new Text(artistName));
	    	context.write(new Text("Q2"), new Text("M" + artistUid + "\t" + artistName + "\t" + songId));
            context.write(new Text("Q3"), new Text("M" + songId + "\t" + title));
            context.write(new Text("Q4"), new Text("M" + artistUid + "\t" + artistName + "\t" + songId));
            //context.write(new Text("Q6"), new Text("M" + songId + "\t" + title));
        }
    }
}
