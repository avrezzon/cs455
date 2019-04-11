package cs455.hadoop.HW3;


//This will be used to evaluate all of the logic of combining the data

class AverageSongSegments{

    private Double[] summedValues;
    private Double[] avgValues;
    private long count;

    public AverageSongSegments(){
        summedValues = new Double[10];
        avgValues = new Double[10];
        for(int i = 0; i < 10; i++){
            summedValues[i] = 0.0;
        }
        count = 0;
    }

    public void appendNewSongSegment(String unparsedValues){
        String[] seperatedValues = unparsedValues.split(" ");
        double temp = 0.0;
        Double parsedVal = 0.0;
        int inc = seperatedValues.length / 10;

        if(seperatedValues.length >= 100) {
            for (int idx = 0; idx < 10; idx++) {
                for (int list_idx = idx * 10; list_idx < ((idx * 10) + inc); list_idx++) {
                    try{
                        parsedVal = Double.parseDouble(seperatedValues[list_idx]);
                        temp += parsedVal;
                    }catch(NumberFormatException ne){
                        //bad data
                    }
                }
                summedValues[idx] += temp;
            }
            this.count += 1;
        }
    }

    private void normalizeBuckets(){
        for(int i = 0; i < 10; i++){
            this.avgValues[i] = this.summedValues[i] / double(this.count);
        }
    }

    public String toString(){
        String res = "";

        normalizeBuckets();

        for(int i = 0; i < 10; i++){
            res += this.avgValues[i] + " ";
        }

        return res;
    }

}