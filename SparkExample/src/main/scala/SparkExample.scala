import scala.util.{Try, Success, Failure}
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row

object SparkExample{

	def main(args: Array[String]){
		val spark = SparkSession.builder.appName("SparkExample").getOrCreate()
		val sc = SparkContext.getOrCreate()
		import spark.implicits._

		val exampleData = spark.read.textFile("hdfs://salt-lake-city:30110/ExampleData").rdd.map(x => 
			x.split(",|  |\t")).map(
			x => (x(0), x.drop(1).map(s => s.toDouble))).filter(
			x => x._2.count(_.isNaN) == 0 && x._2.sum != 0 && x._2.length == 23
		).groupByKey()
	}
}
