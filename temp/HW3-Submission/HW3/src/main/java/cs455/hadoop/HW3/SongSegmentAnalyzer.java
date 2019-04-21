package cs455.hadoop.HW3;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.hadoop.io.Text;
class SongSegmentAnalyzer{

	public double[] segment_start_avg;
	public double[] segment_start_stdev;

	public double[] segment_pitch_avg;
	public double[] segment_pitch_stdev;

	public double[] segment_timbre_avg;
	public double[] segment_timbre_stdev;

	public double[] segment_loudness_max_avg;
	public double[] segment_loudness_max_stdev;

	public double[] segment_loudness_max_time_avg;
	public double[] segment_loudness_max_time_stdev;

	public double[] segment_loudness_start_avg;
	public double[] segment_loudness_start_stdev; 

	public SongSegmentAnalyzer(){
		//this.SongScore = new HashMap<>();
		//this.uniqueSongs = new ArrayList<>();
		//this.averageSongs = new ArrayList<>();
	}

	private double[] createStdev(ArrayList<Text> values, double[] avgs){

		long count = 0;
		double[] temp = new double[10];
		Double parsed = 0.0;
		String[] temp_str = null;

		for(int i = 0; i < 10; i++){
			temp[i] = 0.0;
		}

		for(Text val : values){
			temp_str = val.toString().split(" ");
			for(int i = 0; i < 10; i++){
				try {
					parsed = Double.parseDouble(temp_str[i]);
					temp[i] += Math.pow(parsed.doubleValue() - avgs[i], 2.0);
				}catch(NumberFormatException nte){

				}
			}
			count++;
		}

		for(int i = 0; i < 10; i++){
			temp[i] = Math.sqrt(temp[i]/(double)(count-1));
		}

		return Arrays.copyOf(temp, temp.length);
	}

	//values would be the average, type is what data segment, and then an Iterable of all the songs at that value
	public void updateValues(double[] avg, int type, ArrayList<Text> values ){

		switch(type){
			case 0:
				this.segment_start_avg = Arrays.copyOf(avg, avg.length);
				this.segment_start_stdev = createStdev(values, avg);
				break;
			case 1:
				this.segment_pitch_avg = Arrays.copyOf(avg, avg.length);
				this.segment_pitch_stdev = createStdev(values, avg);
				break;
			case 2:
				this.segment_timbre_avg = Arrays.copyOf(avg, avg.length);
				this.segment_timbre_stdev = createStdev(values, avg);
				break;
			case 3:
				this.segment_loudness_max_avg = Arrays.copyOf(avg, avg.length);
				this.segment_loudness_max_stdev = createStdev(values, avg);
				break;
			case 4:
				this.segment_loudness_max_time_avg = Arrays.copyOf(avg, avg.length);
				this.segment_loudness_max_time_stdev = createStdev(values, avg);
				break;
			case 5:
				this.segment_loudness_start_avg = Arrays.copyOf(avg, avg.length);
				this.segment_loudness_start_stdev = createStdev(values, avg);
				break;
		}
	}

	public double getArtistScore(HashMap<String, SongSegment> songData, HashSet<String> artistSongs){
		SongSegment song = null;
		double totalScore = 0;
		for(String songID : artistSongs){
			song = songData.getOrDefault(songID,null);
			if(song != null) {
				totalScore += getScore(song.segment_start, this.segment_start_avg, this.segment_start_stdev);
				totalScore += getScore(song.segment_pitch, this.segment_pitch_avg, this.segment_pitch_stdev);
				totalScore += getScore(song.segment_timbre, this.segment_timbre_avg, this.segment_timbre_stdev);
				totalScore += getScore(song.segment_loudness_max, this.segment_loudness_max_avg, this.segment_loudness_max_stdev);
				totalScore += getScore(song.segment_loudness_max_time, this.segment_loudness_max_time_avg, this.segment_loudness_max_time_stdev);
				totalScore += getScore(song.segment_loudness_start, this.segment_loudness_start_avg, this.segment_loudness_start_stdev);
			}
		}

		return Math.abs(totalScore);
	}

	private double getScore(double[] segment, double[] avg, double[] stdev){
		double points = 0;
		for(int i = 0; i < 10; i++){
			points += (avg[i] - segment[i])/stdev[i];
		}
		return points;
	}

}
