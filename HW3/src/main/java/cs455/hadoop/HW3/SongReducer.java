package cs455.hadoop.HW3;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.util.HashMap;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import java.io.IOException;


public class SongReducer extends Reducer<Text, Text, Text, Text> {

    //These are the data structures used for the first question
    private HashMap<String, String> ArtistMapping;
    private HashMap<String, Integer> SongCount;

    //These are the structures used for Q3
    private HashMap<String, String> SongMapping;
    private HashMap<String, Double> SongScore;
    //private Q3Helper q3Helper = new Q3Helper();

    private MultipleOutputs mos;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        mos = new MultipleOutputs(context);
        ArtistMapping = new HashMap<>();
        SongCount = new HashMap<>();
        SongMapping = new HashMap<>();
        SongScore = new HashMap<>();
    }


    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        String statusCode = key.toString().substring(0,2);
//        q3Helper.setSongId(" ");
//        q3Helper.setMax(0.0);

        if(statusCode.equals("Q1")){

            int count = 0;
            String artistName = null;
            String artistId = key.toString().substring(2);

            for(Text val : values){
                count += 1;
                artistName = val.toString();
            }

            ArtistMapping.put(artistId, artistName);
            SongCount.put(artistId, count);
        }

        if(statusCode.equals("Q3")){

            double songPopScore = 0.0;
            String MetaOrAnalysis = null;
            String[] dataSegment = null; //M --> songID and title     A --> songID and popularity



            for(Text val : values){
                MetaOrAnalysis = val.toString().substring(0,1);
                dataSegment = val.toString().substring(1).split("\t");
                if(MetaOrAnalysis.equals("M")){
                    SongMapping.put(dataSegment[0], dataSegment[1]); //This sets the mapping from ID to title of the song
                }
                if(MetaOrAnalysis.equals("A")){
                    songPopScore = Double.parseDouble(dataSegment[1]);
                    SongScore.put(dataSegment[0], songPopScore);
                }
            }
        }
    }

    protected void cleanup(Context context) throws IOException, InterruptedException{

        //TODO I might want to wrap this in gaurds to prevent other reducers to accidentally do something
        //Q1 portion
        if(!(ArtistMapping.isEmpty() && SongCount.isEmpty())){
            int max = 0;
            String artistId = null;
            String artistName = null;
            for (String key : SongCount.keySet()) {
                if (SongCount.get(key) > max) {
                    max = SongCount.get(key);
                    artistId = key;
                }
            }
            artistName = ArtistMapping.get(artistId);
            mos.write(new Text(artistName), new Text((new Integer(max)).toString()), "/cs455/debug/Q1");
        }

        //if(!(SongScore.isEmpty())){
            double Max = 0.0;
            String songID = "oof";
            String songName = "oof";
            for(String key : SongScore.keySet()){
                if(SongScore.get(key) > Max){
                    Max = SongScore.get(key);
                    songID = key;
                }
            }
            songName = SongMapping.get(songID);
        //, "/cs455/debug/Q3"
            context.write(new Text(songName), new Text((new Double(Max)).toString()));
        //context.write(new Text("Big"), new Text("Oof"));
        //}

        mos.close();
    }
}
