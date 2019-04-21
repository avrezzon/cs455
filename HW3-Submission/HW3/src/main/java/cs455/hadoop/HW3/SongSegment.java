package cs455.hadoop.HW3;

import java.util.Arrays;

//This class will take in the string values for the segment data and parse them into double[] for the fields
class SongSegment{

	public String id;
	public double[] segment_start;
	public double[] segment_pitch;
	public double[] segment_timbre;
	public double[] segment_loudness_max;
	public double[] segment_loudness_max_time;
	public double[] segment_loudness_start;

	public SongSegment(String id, String start, String pitch, String timbre, String loudness_max, String loudness_max_time, String loudness_start){
		this.id = id;
		this.segment_start = parseSegmentData(start.split(" "));
		this.segment_pitch = parseSegmentData(pitch.split(" "));
		this.segment_timbre = parseSegmentData(timbre.split(" "));
		this.segment_loudness_max = parseSegmentData(loudness_max.split(" "));
		this.segment_loudness_max_time = parseSegmentData(loudness_max_time.split(" "));
		this.segment_loudness_start = parseSegmentData(loudness_start.split(" "));
	}

	private double[] parseSegmentData(String[] unparsed){

		double[] parsedValues = new double[10];
		Double parsedNum = null;

		for(int i = 0; i < 10; i++){
			try{
				parsedNum = Double.parseDouble(unparsed[i]);
				parsedValues[i] = parsedNum.doubleValue();
			}catch(NumberFormatException nfe){
				parsedValues[i] = -1.0;
			}
		}

		return Arrays.copyOf(parsedValues, 10);
	}

}
