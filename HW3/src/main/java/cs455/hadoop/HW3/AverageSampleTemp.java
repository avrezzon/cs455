//package cs455.hadoop.HW3;
//
//
////This will be used to evaluate all of the logic of combining the data
//
//class AverageSongSegments{
//
//    private Double[] summedValues;
//    private Double[] avgValues;
//    private long count;
//    private boolean empty;
//    private boolean containsArrays;
//
//    public AverageSongSegments(boolean is2D){
//        summedValues = new Double[10];
//        avgValues = new Double[10];
//        for(int i = 0; i < 10; i++){
//            summedValues[i] = 0.0;
//        }
//        count = 0;
//        empty = true;
//        containsArrays = is2D;
//    }
//
//    public void appendNewSongSegment(String unparsedValues) {
//        if(containsArrays){
//            containsArraysAppend(unparsedValues);
//        }else{
//            containsValuesAppend(unparsedValues);
//        }
//    }
//
//    private void containsValuesAppend(String unparsedValues){
//        String[] seperatedValues = unparsedValues.split(" ");
//        double temp = 0.0;
//        Double parsedVal = 0.0;
//        int inc = seperatedValues.length / 10;
//
//        if(seperatedValues.length >= 100) {
//            for (int idx = 0; idx < 10; idx++) {
//                for (int list_idx = idx * 10; list_idx < ((idx * 10) + inc); list_idx++) {
//                    try{
//                        parsedVal = Double.parseDouble(seperatedValues[list_idx]);
//                        temp += parsedVal;
//                    }catch(NumberFormatException ne){
//                        //bad data
//                    }
//                }
//                summedValues[idx] += (temp/(double)inc);
//            }
//            this.count += 1;
//            empty = false;
//        }
//    }
//
//    private void containsArraysAppend(String unparsedValues){
//        String[] seperatedValues = unparsedValues.split(" ");
//        double temp = 0.0;
//        Double parsedVal = 0.0;
//        int inc = seperatedValues.length / 10;
//
//        if(seperatedValues.length >= 100) {
//            for (int idx = 0; idx < 10; idx++) {
//                for (int list_idx = idx * 10; list_idx < ((idx * 10) + inc); list_idx++) {
//
//                    try{
//                        parsedVal = Double.parseDouble(seperatedValues[list_idx]);
//                        temp += parsedVal;
//                    }catch(NumberFormatException ne){
//                        //bad data
//                    }
//                }
//                summedValues[idx] += (temp/(double)inc);
//            }
//            this.count += 1;
//            empty = false;
//        }
//    }
//
//    private void normalizeBuckets(){
//        for(int i = 0; i < 10; i++){
//            this.avgValues[i] = this.summedValues[i] / (double)count;
//        }
//    }
//
//    public boolean isEmpty(){
//        return empty;
//    }
//
//    public String toString(){
//        String res = "";
//
//        normalizeBuckets();
//
//        for(int i = 0; i < 10; i++){
//            res += this.avgValues[i] + " ";
//        }
//
//        return res;
//    }
//
//}




/**
 * package cs455.hadoop.HW3;


 //This will be used to evaluate all of the logic of combining the data

 class AverageSongSegments{

 private Double[] summedValues;
 private Double[] avgValues;
 private long count;
 private boolean empty;

 public AverageSongSegments(){
 summedValues = new Double[10];
 avgValues = new Double[10];
 for(int i = 0; i < 10; i++){
 summedValues[i] = 0.0;
 }
 count = 0;
 empty = true;
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
 summedValues[idx] += (temp/(double)inc);
 }
 this.count += 1;
 empty = false;
 }
 }

 public String normalizeMapper(String unparsedValues){

 }

 private void normalizeBuckets(){
 for(int i = 0; i < 10; i++){
 this.avgValues[i] = this.summedValues[i] / (double)this.count;
 }
 }

 public boolean isEmpty(){
 return empty;
 }


 public String toString(){
 String res = "";

 normalizeBuckets();

 for(int i = 0; i < 10; i++){
 res += this.avgValues[i] + " ";
 }

 return res;
 }

 }*/