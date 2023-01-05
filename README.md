Scala CLI AutoConf
==================
```scala
package com.github.meysampg.sample

import com.github.meysampg.autoconf.Parser
import com.github.meysampg.autoconf.{Argument, Config}

@Config
class SampleConfig
(
  @Argument("name", "name to show")
  val name: String,
  @Argument("iteration", "count of iteration")
  val iteration: Int,
  @Argument("only-even-numbers", "print name only for even numbers")
  val onlyEvenNumbers: Option[Boolean], // make optional argument with Option type
  @Argument("pre-line-char", "characters to print before each line")
  val preLine: String = ">>> ", // make optional with default value
  @Argument("post-line-seq", "show characters after the line")
  val postLine: Seq[String],
)

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
```

```shell
$ java target/autoconf.jar com.github.meysampg.sample.Main --name Meysam --iteration 5 --only-even-numbers --pre-line-char !!!
!!!0 Meysam
!!!2 Meysam
!!!4 Meysam
```