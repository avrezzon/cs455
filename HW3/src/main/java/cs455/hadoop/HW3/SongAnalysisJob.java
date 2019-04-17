package cs455.hadoop.HW3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;

import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

//FIXMe I normally am getting one reducer buy default and I have the ability to change this def. value

public class SongAnalysisJob {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();

            Job job = Job.getInstance(conf, "Million song analysis");
            job.setJarByClass(SongAnalysisJob.class);

            job.setMapperClass(SongMapper.class);
            job.setMapperClass(ArtistMapper.class);

            job.setReducerClass(SongReducer.class);

            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, ArtistMapper.class);
            MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, SongMapper.class);

            FileOutputFormat.setOutputPath(job, new Path(args[2]));

            MultipleOutputs.addNamedOutput(job, "Debug", TextOutputFormat.class, Text.class, Text.class);
            MultipleOutputs.addNamedOutput(job, "Q1", TextOutputFormat.class, Text.class, Text.class);
	        MultipleOutputs.addNamedOutput(job, "Q2", TextOutputFormat.class, Text.class, Text.class);
	        MultipleOutputs.addNamedOutput(job, "Q3", TextOutputFormat.class, Text.class, Text.class);
            MultipleOutputs.addNamedOutput(job, "Q4", TextOutputFormat.class, Text.class, Text.class);
            MultipleOutputs.addNamedOutput(job, "Q5", TextOutputFormat.class, Text.class, Text.class);
            MultipleOutputs.addNamedOutput(job, "Q6", TextOutputFormat.class, Text.class, Text.class);
            MultipleOutputs.addNamedOutput(job, "Q7", TextOutputFormat.class, Text.class, Text.class);
            MultipleOutputs.addNamedOutput(job, "Q8", TextOutputFormat.class, Text.class, Text.class);
            MultipleOutputs.addNamedOutput(job, "Q9", TextOutputFormat.class, Text.class, Text.class);
            MultipleOutputs.addNamedOutput(job, "Q10", TextOutputFormat.class, Text.class, Text.class);

            System.exit(job.waitForCompletion(true) ? 0 : 1);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

    }
}
