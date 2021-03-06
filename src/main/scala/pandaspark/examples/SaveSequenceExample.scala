package spark.examples

import spark.SparkContext
import spark.SparkContext._
import spark.SparkFiles;

import au.com.bytecode.opencsv.CSVReader

import java.io.StringReader

object SaveSeqeunceExample {
  def main(args: Array[String]) {
    if (args.length != 2) {
      System.err.println("Usage: LoadCsvExample <master> <inputfile> <sequenceout> <csvout>")
      System.exit(1)
    }
    val master = args(0)
    val inputFile = args(1)
    val sc = new SparkContext(master, "Load CSV Example",
			      System.getenv("SPARK_HOME"),
			      Seq(System.getenv("JARS")))
    sc.addFile(inputFile)
    val inFile = sc.textFile(inputFile)
    val splitLines = inFile.map(line => {
      val reader = new CSVReader(new StringReader(line))
      reader.readNext()
    }
			      )
    val numericData = splitLines.map(line => line.tail.map(_.toDouble))
    val summedData = numericData.map(row => Pair(row.head,row.tail.sum))
    summedData.saveAsSequenceFile(args(2));
    val formattedData = summedData.map(row => row.toString)
    formattedData.saveAsTextFile(args(3))
  }

}
