package com.github.meysampg.sample

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
) {
	override def toString: String =
		s"SampleConfig {name: $name, iteration: $iteration, oen: $onlyEvenNumbers, pl: $preLine}"
}
