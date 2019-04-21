package cs455.hadoop.Sample;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class SalaryMapper extends Mapper<LongWritable, Text, Text, Text> {


    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		//Unit,Department,LastName,FirstInitial,JobTitle,PosNum,Contract,ApptTime,FTE,AnnualSalary_str,AnnualSalary_str,AnnualSalary_Decimal

        String[] csvLine = (value.toString()).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		String unit = csvLine[0];
		String department = csvLine[1];
		String name = csvLine[3] + ". " +  csvLine[2];
		String jobTitle = csvLine[4];
		String salary = csvLine[11];

		context.write(new Text(department), new Text(salary));
		//context.wrtie(new Text(unit), new Text(salary + "\t" + department));

    }
}
