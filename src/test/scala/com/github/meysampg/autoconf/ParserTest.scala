package com.github.meysampg.autoconf

import org.scalatest.wordspec.AnyWordSpec

@Config
case class SampleConfig
(
  @Argument("name", "name to show")
  name: String,
  @Argument("iteration", "count of iteration")
  iteration: Int,
  @Argument("only-even-numbers", "print name only for even numbers")
  onlyEvenNumbers: Option[Boolean], // optional argument with default value
  @Argument("pre-line-char", "characters to print before each line") // optional but has default
  preLine: String = ">>> ",
)

class ParserTest extends AnyWordSpec {
	"Parser Class" when {
		"it receives an empty args array" must {
			"throw an exception if one of arguments does not have default value" in {
				val args = Array[String]() // --name or -n with a given value is required on args
				assertThrows(() => Parser(args).as[SampleConfig])
			}
		}

		"it receives a non-empty args array" must {
			"return default values if some arguments have default but no argument passed to them" in {
				val args = Array("--name", "Meysam", "-i", "5")
				assert(new SampleConfig("Meysam", 5, None, ">>> ") == Parser(args).as[SampleConfig])
			}

			"throw an exception if one of required argument does not have value in args" in {
				val args = Array("--iteration", "13", "-o") // --name or -n with a given value is required on args
				assertThrows(() => Parser(args).as[SampleConfig])
			}

			"return proper result for all given arguments" in {
				val args = Array("--name", "Meysam", "--iteration", "5", "--only-even-numbers", "--pre-line-char", "!!! ")
				assert(new SampleConfig("Meysam", 5, Some(true), "!!!") == Parser(args).as[SampleConfig])
			}

			"ignore non-related args that doesn't have equivalence argument" in {
				val args = Array("--name", "Meysam", "--iteration", "5", "-o", "-g", "--type", "foo")
				assert(new SampleConfig("Meysam", 5, Some(true), ">>> ") == Parser(args).as[SampleConfig])
			}
		}
	}
}
