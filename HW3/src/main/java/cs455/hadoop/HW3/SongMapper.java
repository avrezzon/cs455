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
        String danceability = null; //4
        String duration; //5
        String fadeInEnd = null; //6
        String energy = null; //7
        String loudness = null; //10
        String fadeOutStart = null; //13

        //throw out invalid data
        if(csvLine.length > 30){

            songID = csvLine[1];
            popularity = csvLine[2];
            danceability = csvLine[4];
            duration = csvLine[5];
            fadeInEnd = csvLine[6];
            loudness = csvLine[10];
            fadeOutStart = csvLine[13];

			if(loudness.length() != 0){
				//System.out.println("SongID: " + songID + " Loudness: " + loudness);
				context.write(new Text("Q2"), new Text("A"+songID+"\t"+loudness));
			}

	    	if(popularity.length()!=0){
	        	context.write(new Text("Q3"), new Text("A"+songID+"\t"+popularity));
	    	}

	    	if(fadeInEnd.length() != 0 && fadeOutStart.length() != 0){
	    	    try {
                    double fadeInTime = Double.parseDouble(fadeInEnd);
                    double fadeOutTime = Double.parseDouble(fadeOutStart);
                    double total = fadeInTime + fadeOutTime;
                    context.write(new Text("Q4"), new Text("A"+ songID + "\t" + (new Double(total)).toString()));
                }catch(NumberFormatException ne){
	    	        //This is a bad record
                }
            }
        }
    }
}
