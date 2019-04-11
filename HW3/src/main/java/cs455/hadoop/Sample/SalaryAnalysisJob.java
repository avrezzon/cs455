package cs455.hadoop.Sample;

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

public class SalaryAnalysisJob {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();

            Job job = Job.getInstance(conf, "Salary analysis");
            job.setJarByClass(SalaryAnalysisJob.class);

            job.setMapperClass(SalaryMapper.class);
            job.setReducerClass(SalaryReducer.class);

            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

			FileInputFormat.addInputPath(job, new Path(args[0]));

            //MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, ArtistMapper.class);
            //MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, SongMapper.class);

            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            //MultipleOutputs.addNamedOutput(job, "Q1", TextOutputFormat.class, Text.class, Text.class);
	        //MultipleOutputs.addNamedOutput(job, "Q2", TextOutputFormat.class, Text.class, Text.class);
	        //MultipleOutputs.addNamedOutput(job, "Q3", TextOutputFormat.class, Text.class, Text.class);
	        //MultipleOutputs.addNamedOutput(job, "Q3Debug", TextOutputFormat.class, Text.class, Text.class);
            //MultipleOutputs.addNamedOutput(job, "Q4", TextOutputFormat.class, Text.class, Text.class);
            //MultipleOutputs.addNamedOutput(job, "Q5", TextOutputFormat.class, Text.class, Text.class);

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
