package cs455.hadoop.HW3;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;


public class SongReducer extends Reducer<Text, Text, Text, Text> {

    //TODO for all of these questions try to wrap it in a way where I am not releiant only on the cleanup method.

    //These are the data structures used for the first question
    private HashMap<String, String> ArtistMapping;
    private HashMap<String, Integer> SongCount;

    //These are the data structures used for the second question
    //This will be: Artist name : {set of all the songs that the artist has done}
    private HashMap<String, HashSet<String>> ArtistSongs;
    private HashMap<String, String> ArtistNames;
    private HashMap<String, Double> SongLoudness;

    //These are the structures used for Q3
    private HashMap<String, String> SongMapping;

    private HashMap<String, Double> SongFadeTime;

    private MultipleOutputs mos;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        mos = new MultipleOutputs(context);

        ArtistMapping = new HashMap<>();
        SongCount = new HashMap<>();

        ArtistSongs = new HashMap<>();
        ArtistNames = new HashMap<>();
        SongLoudness = new HashMap<>();

        SongMapping = new HashMap<>();

        SongFadeTime = new HashMap<>();
    }


    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        String statusCode = key.toString().substring(0, 2);
        String MetaOrAnalysis = null;
        String[] dataSegment = null;

        if (statusCode.equals("Q1")) {

            int count = 0;
            String artistName = null;
            String artistId = key.toString().substring(2);

            for (Text val : values) {
                count += 1;
                artistName = val.toString();
            }

            ArtistMapping.put(artistId, artistName);
            SongCount.put(artistId, count);
        }

        if (statusCode.equals("Q2")) {
            double Loudness;
            HashSet<String> songs = null;

            for (Text val : values) {
                MetaOrAnalysis = val.toString().substring(0, 1);
                dataSegment = val.toString().substring(1).split("\t");
                if (MetaOrAnalysis.equals("M")) {
                    ArtistNames.put(dataSegment[0], dataSegment[1]);
                    songs = ArtistSongs.getOrDefault(dataSegment[0], new HashSet<String>());
                    songs.add(dataSegment[2]);
                    ArtistSongs.put(dataSegment[0], songs);
                }
                if (MetaOrAnalysis.equals("A")) {
                    try {
                        Loudness = Double.parseDouble(dataSegment[1]);
                        SongLoudness.put(dataSegment[0], Loudness);
                    } catch (NumberFormatException ne) {
                        //This is a bad record
                    }
                }
            }
        }

        if (statusCode.equals("Q3")) {

            double songPopScore = 0.0;
            double curMax = 0.0;
            String MaxSongId = null;
            String LoudestSong = null;

            for (Text val : values) {
                MetaOrAnalysis = val.toString().substring(0, 1);
                dataSegment = val.toString().substring(1).split("\t");
                if (MetaOrAnalysis.equals("M")) {
                    SongMapping.put(dataSegment[0], dataSegment[1]); //This sets the mapping from ID to title of the song
                }
                if (MetaOrAnalysis.equals("A")) {
                    try {
                        songPopScore = Double.parseDouble(dataSegment[1]);
                        if (songPopScore > curMax) {
                            curMax = songPopScore;
                            MaxSongId = dataSegment[0];
                        }
                        //SongScore.put(dataSegment[0], songPopScore);
                    } catch (NumberFormatException ne) {
                        //This is just a bad record
                    }
                }
            }

            LoudestSong = SongMapping.getOrDefault(MaxSongId, "N/A");
            if (LoudestSong.equals("N/A")) {
                LoudestSong = "The title of the song with the highest value for loudness was not present within the metadata set, Song ID: " + MaxSongId;
            }
            mos.write("Q3", new Text(LoudestSong), new Text((new Double(curMax).toString())));
        }

        if(statusCode.equals("Q4")){

            double songFadeTime;

            for(Text val : values){
                MetaOrAnalysis = val.toString().substring(0, 1);
                dataSegment = val.toString().substring(1).split("\t");
                if(MetaOrAnalysis.equals("M")){
                    ArtistNames.put(dataSegment[0], dataSegment[1]);
                    songs = ArtistSongs.getOrDefault(dataSegment[0], new HashSet<String>());
                    songs.add(dataSegment[2]);
                    ArtistSongs.put(dataSegment[0], songs);
                }
                if(MetaOrAnalysis.equals("A")){
                    songFadeTime = Double.parseDouble(dataSegment[1]);
                    SongFadeTime.put(dataSegment[0], songFadeTime);
                }
            }
        }

    }

    protected void cleanup(Context context) throws IOException, InterruptedException {

        //Q1 portion
        if (!(ArtistMapping.isEmpty() && SongCount.isEmpty())) {
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
            mos.write("Q1", new Text(artistName), new Text((new Integer(max)).toString()));
        }

        //Q2 portion
        if (!(ArtistSongs.isEmpty() && ArtistNames.isEmpty() && SongLoudness.isEmpty())) {

            double artistLoudness = 0;
            double artistAvg = 0.0;
            double maxLoudnessAvg = 0.0;
            HashSet<String> songList = null;
            String LoudestArtist = null;
            String LoudestArtistID = null;

            for (String artistId : ArtistSongs.keySet()) {
                songList = ArtistSongs.get(artistId);
                for (String songID : songList) {
                    artistLoudness += SongLoudness.getOrDefault(songID, 0.0);
                    System.out.println("ArtistLoudness: " + artistLoudness);
                }
                System.out.println("\n\n\n\n");
                artistAvg = artistLoudness / songList.size();
                System.out.println("ArtistAverage: " + artistAvg);
                if (maxLoudnessAvg < artistAvg) {
                    maxLoudnessAvg = artistAvg;
                    LoudestArtistID = artistId;
                }
                artistLoudness = 0;
            }

            LoudestArtist = ArtistNames.get(LoudestArtistID);
            mos.write("Q2", new Text(LoudestArtistID), new Text((new Double(maxLoudnessAvg).toString())));
        }

        //TODO finish this
        //Q4 portion
        if(!SongFadeTime.isEmpty() && !ArtistSongs.isEmpty()){
            double artistMaxFade = 0.0;
            double artistFade = 0.0;
            HashSet<String> songList = null;
            String MaxFadeArtist = null;
            String MaxFadeArtistID = null;

            for (String artistId : ArtistSongs.keySet()) {
                songList = ArtistSongs.get(artistId);
                for (String songID : songList) {
                    artistLoudness += SongLoudness.getOrDefault(songID, 0.0);
                    System.out.println("ArtistLoudness: " + artistLoudness);
                }
                System.out.println("\n\n\n\n");
                artistAvg = artistLoudness / songList.size();
                System.out.println("ArtistAverage: " + artistAvg);
                if (maxLoudnessAvg < artistAvg) {
                    maxLoudnessAvg = artistAvg;
                    LoudestArtistID = artistId;
                }
                artistLoudness = 0;
            }

            LoudestArtist = ArtistNames.get(LoudestArtistID);
            mos.write("Q2", new Text(LoudestArtistID), new Text((new Double(maxLoudnessAvg).toString())));

        }

        mos.close();
    }
}
