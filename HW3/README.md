# HW3-PC README

## *View the project description CS455-Spring19-HW3-PC and the written components CS455-Spring19-HW3-WC*

Descriptions of classes:
* ArtistMapper.java
        This class is the mapper for the metadata files. This mapper will emit values that pertain to the artist
        including their name, unique Id, and the songs they've produced.

* SongMapper.java
        This class was responsible for emitting the values for the analysis files. This will map all of the necessary
        fields that include song Id, popularity, loudness, fading time and their segment data.  Due to the nature of how big the segment data
        is, I had to reduce the information down so that I could use the data in an effective way without running out of memory.

* AverageSongSegments.java
        This class helped out tremendously when it came to analyzing the segment data for the songs.  this class would take the original
        segment data and compress it into an array of 10 floating point values.  Each of the elements in this array will correspond
        to 10% of the information for the song. The normalizeMapper function does this reduction before writing it to context.
        The normalizeReducer function is what is later used within the SongReducer to find the averages of the segmented data.

* SongDuration.java
        This was a simple class that was used to just wrangle the song's duration and songID into a "tuple".

* SongSegment.java
        This class is very similar to the SongDuration class in a sense that it just wraps the data together.  This class
        was used primarily for Q8.

* SongSegmentAnalyzer.java
        This class was used to maintain the average values for the data segments that needed to be observed.  Since the AverageSongSegment.normalizeReducer()
        finds the average for the segment, this class will update its local copy of the average and derive the standard deviation for the songs for the segments.
        This class will find the score for uniqueness by determining how many standard deviations the song data is.

* SongReducer.java
        The main reducer class for the job.  This breaks down alot of the problems based upon the keys Q1-10 and solves them as accordingly.

NOTE: The Sample directory was just another thing that I did on the side and does not pertain to this assignment
