package cs455.hadoop.HW3;

import java.io.IOException;

public class SongDuration{
	public double time;
	public String songID;

	public SongDuration(String songID, double time){
		this.songID = songID;
		this.time = time;
	}
}
