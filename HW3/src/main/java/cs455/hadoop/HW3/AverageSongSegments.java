package cs455.hadoop.HW3;


//This will be used to evaluate all of the logic of combining the data

class AverageSongSegments{

    public static String normalizeMapper(String unparsedValues, boolean isFlattened){
        String[] seperatedValues = unparsedValues.split(" ");
        String result = null;
        double[] summedValues = new double[10];
        double temp = 0.0;
        Double parsedVal = 0.0;
        int inc = seperatedValues.length / 10;

        if(seperatedValues.length >= 100) {

            if(!isFlattened) {
                for (int idx = 0; idx < 10; idx++) {
                    for (int list_idx = idx * 10; list_idx < ((idx * 10) + inc); list_idx++) {
                        try {
                            parsedVal = Double.parseDouble(seperatedValues[list_idx]);
                            temp += parsedVal;
                        } catch (NumberFormatException ne) {
                            //bad data
                        }
                    }
                    summedValues[idx] = (temp / (double) inc);
                }
            }else{
                inc = seperatedValues.length/22;
                for (int idx = 0; idx < 10; idx++) {
                    for (int list_idx = idx * 10; list_idx < ((idx * 10) + inc); list_idx++) {
                        try {
                            parsedVal = Double.parseDouble(seperatedValues[list_idx]);
                            temp += parsedVal;
                        } catch (NumberFormatException ne) {
                            //bad data
                        }
                    }
                    summedValues[idx] = (temp / (double) inc);
                }
            }

            for(int i = 0; i < 10; i++){
                if(i == 9){
                    result += summedValues[i];
                }else{
                    result += summedValues[i] + " ";
                }
            }
            return result;
        }
        return "N/A";
    }

    //PRECONDITION: values when split will be of length of 10
    public static String normalizeReducer(Iterable<String> values){

        long count = 0;
        Double[] buckets = new Double[10];
        Double parsedValue = 0.0;
        String[] seperatedValues = null;
        String result = null;

        //Init the buckets
        for(int i = 0; i < 10; i++){
            buckets[i] = 0.0;
        }

        for(String val : values){
            seperatedValues = val.split(" ");
            for(int i = 0; i < 10; i++){
                try{
                    parsedValue = Double.parseDouble(seperatedValues[i]);
                    buckets[i] += parsedValue;
                }catch(NumberFormatException ne){
                    //bad data
                }
            }
            count++;
        }

        for(int i = 0; i < 10; i++){
            buckets[i] = buckets[i] / count;
        }

        for(int i = 0; i < 10; i++){
            if(i == 9){
                result += buckets[i];
            }else{
                result += buckets[i] + " ";
            }
        }

        return result;
    }
}