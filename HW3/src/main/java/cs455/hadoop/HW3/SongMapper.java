package cs455.hadoop.HW3;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class SongMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String[] csvLine = (value.toString()).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        String songID = null; //1
        String popularity = null; //2
        String danceability; //4
        String duration; //5
        String loudness; //10

        //throw out invalid data
        if(csvLine.length > 30){

            songID = csvLine[1];
            popularity = csvLine[2];
            danceability = csvLine[4];
            duration = csvLine[5];
            loudness = csvLine[10];

            if(popularity.matches("[^.0-9]")){
                if(songID.length() != 0 && popularity.length() != 0) {
                    context.write(new Text("Q3"), new Text("A" + songID + "\t" + popularity));
                }
            }
        }
    }
}
