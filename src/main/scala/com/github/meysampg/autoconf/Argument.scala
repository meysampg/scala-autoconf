package com.github.meysampg.autoconf

import scala.annotation.StaticAnnotation

case class Argument
(
  arg: String,
  helpStr: String,
) extends StaticAnnotation
