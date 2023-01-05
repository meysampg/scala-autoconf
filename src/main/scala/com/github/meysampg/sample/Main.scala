package com.github.meysampg.sample

import com.github.meysampg.autoconf.Parser

object Main {
	def print(pr: String, i: Int, name: String, po: Seq[String]): Unit =
		println(s"${pr}$i ${name} ${po.mkString(" ")}")

	def main(args: Array[String]): Unit = {
		val config: SampleConfig = Parser(args).as[SampleConfig]
		if (config.onlyEvenNumbers.isDefined)
			Range(0, config.iteration).withFilter(_ % 2 == 0)
			  .foreach(i => print(config.preLine, i, config.name, config.postLine))
		else
			Range(0, config.iteration)
			  .foreach(i => print(config.preLine, i, config.name, config.postLine))
	}
}
