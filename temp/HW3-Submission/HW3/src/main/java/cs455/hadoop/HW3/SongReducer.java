package cs455.hadoop.HW3;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;
import java.math.BigDecimal;

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

	private HashMap<String, SongSegment> SongSegmentLookup;
	private SongSegmentAnalyzer ssa;

	private HashMap<String, Long> ArtistReference;
	private String maxArtist;

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
		SongSegmentLookup = new HashMap<>();
		ssa = new SongSegmentAnalyzer();
		maxArtist = null;
        ArtistReference = new HashMap<>();
    }


    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        String statusCode = key.toString().substring(0, 2);
        String MetaOrAnalysis = null;
        String[] dataSegment = null;

		//Q1 is complete
        if (statusCode.equals("Q1")) {
//SEGMENT
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
            double artistLoudness = 0.0;
            double artistAvg = 0.0;
            double maxLoudnessAvg = 0.0;
            HashSet<String> songList = null;
            HashSet<String> songs = null;
            String LoudestArtist = null;
            String LoudestArtistID = null;

            for (Text val : values) {
                MetaOrAnalysis = val.toString().substring(0, 1);
                dataSegment = val.toString().substring(1).split("\t");
                if (MetaOrAnalysis.equals("M")) {
                    ArtistNames.put(dataSegment[0], dataSegment[1]);
                    songs = ArtistSongs.getOrDefault(dataSegment[0], new HashSet<>());
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

            for (String artistId : ArtistSongs.keySet()) {
                songList = ArtistSongs.get(artistId);
                for (String songID : songList) {
                    artistLoudness += SongLoudness.getOrDefault(songID, 0.0);
                }

                artistAvg = artistLoudness / songList.size();

                if (maxLoudnessAvg < artistAvg) {
                    maxLoudnessAvg = artistAvg;
                    LoudestArtistID = artistId;
                }

                artistLoudness = 0.0;
            }

            LoudestArtist = ArtistNames.get(LoudestArtistID);
            mos.write("Q2", new Text("Artist with the average loudest songs"), new Text(" "));
            mos.write("Q2", new Text(LoudestArtist), new Text((new Double(maxLoudnessAvg).toString())));
        }

        //Q3 portion
        //Kevin said that there are multiple songs that have a score of one so i can either list all of those songs OR
        //just display the first value
		//Currently this only outputs the first instance of 1.0 Q3
        if (statusCode.equals("Q3")) {

            BigDecimal songPopScore = null;
            BigDecimal curMax = BigDecimal.valueOf(0.0);
            String MaxSongId = null;
            String HottestSong = null;

            for (Text val : values) {
                MetaOrAnalysis = val.toString().substring(0, 1);
                dataSegment = val.toString().substring(1).split("\t");
                if (MetaOrAnalysis.equals("M")) {
                    SongMapping.put(dataSegment[0], dataSegment[1]); //This sets the mapping from ID to title of the song
                }
                if (MetaOrAnalysis.equals("A")) {
                    try {
                        songPopScore = new BigDecimal(dataSegment[1]);

                        if (songPopScore.compareTo(curMax) == 1) {
                            curMax = new BigDecimal(dataSegment[1]);
                            MaxSongId = dataSegment[0];
                        }
                    } catch (NumberFormatException ne) {
                        //This is just a bad record
                    }
                }
            }

            HottestSong = SongMapping.getOrDefault(MaxSongId, "The title of the song with the highest value for loudness was not present within the metadata set, Song ID: " + MaxSongId);
            mos.write("Q3", new Text("Hottest Song"), new Text(" "));
            mos.write("Q3", new Text(HottestSong), new Text(curMax.toString()));
        }

		//Q4
        if(statusCode.equals("Q4")){

            double songFadeTime;
            HashSet<String> songs = null;

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

		//Q5 portion
        if(statusCode.equals("Q5")){
            double maxDuration = 0.0;
            double minDuration = 99999.0;

            double songDuration = 0.0;

			ArrayList<SongDuration> songList = new ArrayList<>();

            String title = null;
            String maxDurationID = null;
            String minDurationID = null;

            for(Text val : values) {
                MetaOrAnalysis = val.toString().substring(0, 1);
                dataSegment = val.toString().substring(1).split("\t");
                if (MetaOrAnalysis.equals("M")) {
                    SongMapping.put(dataSegment[0], dataSegment[1]);
                }
                if (MetaOrAnalysis.equals("A")) {
                    try{
                        songDuration = Double.parseDouble(dataSegment[1]);
                        if(songDuration > maxDuration){
                            maxDuration = songDuration;
                            maxDurationID = dataSegment[0];
                        }
                        if(songDuration < minDuration){
                            minDuration = songDuration;
                            minDurationID = dataSegment[0];
                        }
						songList.add(new SongDuration(dataSegment[0], songDuration));
                    }catch(NumberFormatException ne){
                        //Bad record
                    }
                }
            }

			//Finding the median of the dataset now
			SongDuration[] song_list = new SongDuration[songList.size()];
			song_list = songList.toArray(song_list);
			Arrays.sort(song_list, new Comparator<SongDuration>(){
				public int compare(SongDuration song1, SongDuration song2){
					if(song1.time < song2.time) return -1;
					if(song1.time == song2.time) return 0;
					return 1; 
				}
			});
			//Updates the value of songList to the sorted fashion
			songList = new ArrayList<SongDuration>(Arrays.asList(song_list));

			ArrayList<SongDuration> ShortestSongs = new ArrayList<>();
			ArrayList<SongDuration> LongestSongs = new ArrayList<>();
			ArrayList<SongDuration> MedianSongs = new ArrayList<>();
			double shortestTime = songList.get(0).time;
            double longestTime = songList.get(songList.size()-1).time;
			double medianTime = songList.get(songList.size() / 2).time;

			for(SongDuration song : songList){
			    if(song.time == shortestTime){
			        ShortestSongs.add(song);
                }
                if(song.time == longestTime){
			        LongestSongs.add(song);
                }
                if(song.time == medianTime){
                    MedianSongs.add(song);
                }
            }

            for(SongDuration song : ShortestSongs){
			    title = SongMapping.getOrDefault(song.songID, "Song title was not present in the metadata set, Song ID: " + song.songID);
			    mos.write("Q5", new Text("Shortest Songs: "), new Text(title + " " + shortestTime + " seconds"));
            }
            for(SongDuration song : LongestSongs){
                title = SongMapping.getOrDefault(song.songID, "Song title was not present in the metadata set, Song ID: " + song.songID);
                mos.write("Q5", new Text("Longest Songs: "), new Text(title + " " + longestTime + " seconds"));
            }
            for(SongDuration song : MedianSongs){
                title = SongMapping.getOrDefault(song.songID, "Song title was not present in the metadata set, Song ID: " + song.songID);
                mos.write("Q5", new Text("Median Songs: "), new Text(title + " " + medianTime + " seconds"));
            }

            mos.write("Q5", new Text("Number of songs in the set: "), new Text(Integer.toString(songList.size())));
        }

        if(statusCode.equals("Q7")){

            ArrayList<Text> values_cpy = new ArrayList<>();
            for(Text val : values){
                values_cpy.add(val);
            }

            String type = key.toString().substring(2);
			double[] result = AverageSongSegments.normalizeReducer(values_cpy);
            switch(type){
                case "0":
					ssa.updateValues(result, 0, values_cpy);
                    mos.write("Q7", new Text("Average start segment"), new Text( AverageSongSegments.toString(result)));
                    break;
                case "1":
					ssa.updateValues(result, 1, values_cpy);
                    mos.write("Q7", new Text("Average pitch segment"), new Text( AverageSongSegments.toString(result)));
                    break;
                case "2":
					ssa.updateValues(result, 2, values_cpy);
                    mos.write("Q7", new Text("Average timbre segment"), new Text(AverageSongSegments.toString(result)));
                    break;
                case "3":
					ssa.updateValues(result, 3, values_cpy);
                    mos.write("Q7", new Text("Average loudness max segment"), new Text( AverageSongSegments.toString(result)));
                    break;
                case "4":
					ssa.updateValues(result, 4, values_cpy);
                    mos.write("Q7", new Text("Average loudness max time segment"), new Text(AverageSongSegments.toString(result)));
                    break;
                case "5":
					ssa.updateValues(result, 5, values_cpy);
                    mos.write("Q7", new Text("Average loudness start segment"), new Text(AverageSongSegments.toString(result)));
                    break;
                default:
                    break;
            }
        }

        if(statusCode.equals("Q8")){
			for(Text val : values){
				dataSegment = val.toString().split("\t");
				SongSegmentLookup.put(dataSegment[0], new SongSegment(dataSegment[0], dataSegment[1], dataSegment[2], dataSegment[3], dataSegment[4], dataSegment[5], dataSegment[6]));
			}
        }

//        if(statusCode.equals("Q9")){
//
//        }


        //I received a GC overhead exception will need to revise FIXME
//        if(statusCode.equals("Q0")){
//            String[] similar_artists = null;
//            long maxCount = 0;
//
//            long count;
//            for(Text val : values){
//                similar_artists = val.toString().split(" ");
//                for(int i = 0; i < similar_artists.length; i++){
//                    count = ArtistReference.getOrDefault(similar_artists[i], new Long(0));
//                    count += 1;
//                    ArtistReference.put(similar_artists[i], count);
//                }
//            }
//            for(String artist : ArtistReference.keySet()){
//                if(maxCount < ArtistReference.get(artist)){
//                    maxCount = ArtistReference.get(artist);
//                    maxArtist = artist;
//                }
//            }
//            mos.write("Q10", new Text("Most influential artist: " + maxArtist), new Text("Score: " + maxCount));
//        }

        if(statusCode.equals("Q?")){
            long temp = 0;
            for(Text val : values){
                temp += 1;
            }
            mos.write("Debug", new Text("Song count in the analysis file: "), new Text(Long.toString(temp)));
        }
    }

    //NOTE that this is only invoked once and allows me to have reference to stuff from above
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

        //Q4 portion
        if(!SongFadeTime.isEmpty() && !ArtistSongs.isEmpty()){
            double artistMaxFade = 0.0;
            double artistFade = 0.0;
            HashSet<String> songList = null;
            String MaxFadeArtist = null;
            String MaxFadeArtistID = null;

            for (String artistId : ArtistNames.keySet()) {
                songList = ArtistSongs.get(artistId);
                for (String songID : songList) {
                    artistFade += SongFadeTime.getOrDefault(songID, 0.0);
                }
                if (artistMaxFade < artistFade) {
                    artistMaxFade = artistFade;
                    MaxFadeArtistID = artistId;
                }
                artistFade = 0;
            }

            MaxFadeArtist = ArtistNames.get(MaxFadeArtistID);

            mos.write("Q4", new Text("Artist that spent the most time fading"), new Text(" "));
            mos.write("Q4", new Text(MaxFadeArtist), new Text((new Double(artistMaxFade).toString())));
        }

		//Apr 10 --> I asked Kevin about whether or not I need to write out this question and he said I dont have to
        mos.write("Q6", new Text("There is no song that has a value for energy or dancability"), new Text("Question 6 ommited"));


        String uniqueArtist = null;
        String averageArtist = null;
        double uniqueVal = Double.MIN_VALUE;
        double averageVal = Double.MAX_VALUE;
        double score = 0.0;
        for(String artistID : ArtistSongs.keySet()){
            score = ssa.getArtistScore(SongSegmentLookup, ArtistSongs.get(artistID));
//            mos.write("Q8breakdown", new Text(artistID), new Text("Score: " + score));
            if(score < averageVal){
                averageArtist = artistID;
                averageVal = score;
            }
            if(score > uniqueVal){
                uniqueArtist = artistID;
                uniqueVal = score;
            }
        }
        mos.write("Q8", new Text("Most average artist: " + ArtistNames.get(averageArtist)), new Text("Score: " + averageVal));
        mos.write("Q8", new Text("Most unique artist: " + ArtistNames.get(uniqueArtist)), new Text("Score: " + uniqueVal));

//		String temp = AverageSongSegments.toString(ssa.segment_start_avg);
//		mos.write("Q8", new Text("start_avg"), new Text(temp));
//
//		temp = AverageSongSegments.toString(ssa.segment_start_stdev);
//		mos.write("Q8", new Text("start_stdev"), new Text(temp));
//
//		temp = AverageSongSegments.toString(ssa.segment_pitch_avg);
//        mos.write("Q8", new Text("pitch_avg"), new Text(temp));
//
//        temp = AverageSongSegments.toString(ssa.segment_pitch_stdev);
//        mos.write("Q8", new Text("pitch_stdev"), new Text(temp));
//
//        temp = AverageSongSegments.toString(ssa.segment_timbre_avg);
//        mos.write("Q8", new Text("timbre_avg"), new Text(temp));
//
//        temp = AverageSongSegments.toString(ssa.segment_timbre_stdev);
//        mos.write("Q8", new Text("timbre_stdev"), new Text(temp));
//
//        temp = AverageSongSegments.toString(ssa.segment_loudness_max_avg);
//        mos.write("Q8", new Text("loudness_max__avg"), new Text(temp));
//
//        temp = AverageSongSegments.toString(ssa.segment_loudness_max_stdev);
//        mos.write("Q8", new Text("start_stdev"), new Text(temp));
//
//        temp = AverageSongSegments.toString(ssa.segment_loudness_max_time_avg);
//        mos.write("Q8", new Text("loudness_max_time_avg"), new Text(temp));
//
//        temp = AverageSongSegments.toString(ssa.segment_loudness_max_time_stdev);
//        mos.write("Q8", new Text("loudness_max_time_stdev"), new Text(temp));
//
//        temp = AverageSongSegments.toString(ssa.segment_loudness_start_avg);
//        mos.write("Q8", new Text("loudness_start_avg"), new Text(temp));
//
//        temp = AverageSongSegments.toString(ssa.segment_loudness_start_stdev);
//        mos.write("Q8", new Text("loudness_start_stdev"), new Text(temp));

        //Sanity check
        mos.write("Debug", new Text("Map"), new Text("Reduce"));


        mos.close();
    }
}
