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
        String duration = null; //5
        String fadeInEnd = null; //6
        String energy = null; //7
        String loudness = null; //10
        String fadeOutStart = null; //13

		String segment_start = null; //18
		String segment_pitch = null; //20
		String segment_timbre = null; //21
		String segment_loudness_max = null; //22
		String segment_loudness_max_time = null; //23
		String segment_loudness_start = null; //24
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

            if(duration.length() != 0){
	    	    context.write(new Text("Q5"), new Text("A" + songID + "\t" + duration));
            }

            //For this segment I will be creating data average data segments of length 10
            //the segment has to be greater than 99
			//original thought was do a segment of 100 but when I do that, then I will lose values up to 1-99
			//by grouping them in 10% buckets i only lose upto 0-9 values
			if(true){
				segment_start = csvLine[18];
				segment_pitch = csvLine[20];
				segment_timbre = csvLine[21];
				segment_loudness_max = csvLine[22];
				segment_loudness_max_time = csvLine[23];
				segment_loudness_start = csvLine[24];
				if(segment_start.length() != 0 && segment_pitch.length() != 0 && segment_timbre.length() != 0 &&
				   segment_loudness_max.length() != 0 && segment_loudness_max_time.length() != 0 && segment_loudness_start.length() != 0){
					String[] temp = segment_start.split(" ");
					System.out.println(temp.length);

					context.write(new Text("Q7-0"), new Text(segment_start));
					context.write(new Text("Q7-1"), new Text(segment_pitch));
					context.write(new Text("Q7-2"), new Text(segment_timbre));
					context.write(new Text("Q7-3"), new Text(segment_loudness_max));
					context.write(new Text("Q7-4"), new Text(segment_loudness_max_time));
					context.write(new Text("Q7-5"), new Text(segment_loudness_start));
				}
			}
        }
    }
}
