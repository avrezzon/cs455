package cs455.hadoop.Sample;

class NumberManager{
	public Double MaxSalary;
	public Double MinSalary;
	public Double AverageSalary;
	public long count;
	public double sum;

	public NumberManager(){
		MaxSalary = new Double(0.0);
		MinSalary = new Double(9999999.0);
		AverageSalary = new Double(0.0);
		count = 0;
		sum = 0;
	}

	public void updateValues(double val){
		//Check to see if max
		if(val > MaxSalary){
			MaxSalary = val;
		}
		if(val < MinSalary){
			MinSalary = val;
		}
		count += 1;
		sum += val;
	}

	public Double getMin(){	return MinSalary;	}
	public Double getMax(){	return MaxSalary;	}
	public Double getAvg(){	return new Double(sum/count);	}

	public String toString(){
		return "$MAX: " + MaxSalary + " $MIN: " + MinSalary + " $AVG: " + getAvg(); 
	}
}
