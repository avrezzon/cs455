package cs455.hadoop.Sample;

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

public class SalaryReducer extends Reducer<Text, Text, Text, Text> {

	HashMap<String, NumberManager> Department_salary = new HashMap<>();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		NumberManager nm = new NumberManager();
		for(Text val : values){
			nm.updateValues(Double.parseDouble(val.toString()));
		}
		Department_salary.put(key.toString(), nm);
    }

	protected void cleanup(Context context) throws IOException, InterruptedException{
		NumberManager nm = null;
		Double MaxSalary = new Double(0.0);
		Double MinSalary = new Double(9999999.0);
		String MaxSalDep = "N/A";
 		String MinSalDep = "N/A";
		for(String dep : Department_salary.keySet()){
			nm = Department_salary.get(dep);
			if(MaxSalary < nm.getMax()){
				MaxSalDep = dep;
				MaxSalary = nm.getMax();
			}
			if(MinSalary > nm.getMin()){
				MinSalDep = dep;
				MinSalary = nm.getMin();
			}
			context.write(new Text(dep), new Text(nm.toString()));
		}

		context.write(new Text("Max Salary in dataset: "), new Text(MaxSalDep + " $" + MaxSalary));
		context.write(new Text("Min Salary in dataset: "), new Text(MinSalDep + " $" + MinSalary));
	}
}
