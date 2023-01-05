package com.github.meysampg.autoconf

import scala.annotation.tailrec
import scala.reflect.runtime.{universe => ru}

class Parser private(private val args: Array[String]) {
	private val map = getOptionsAcc(Map(), args.toList)

	def as[T: ru.TypeTag]: T = {
		val constructor = getConstructor
		val constructorArgs = getConstructorArgs
		val params = getParamsFromMap(map, constructorArgs)

		params.map(p => constructor(p: _*)).asInstanceOf[T]
	}

	private def getConstructor[T: ru.TypeTag]: ru.MethodMirror = {
		val t = ru.typeOf[T]
		val constructor = t.decl(ru.termNames.CONSTRUCTOR).asMethod
		val classMirror = ru.runtimeMirror(getClass.getClassLoader).reflectClass(t.typeSymbol.asClass)

		classMirror.reflectConstructor(constructor)
	}

	private def getConstructorArgs[T: ru.TypeTag]: List[List[(List[String], ru.Type)]] = {
		ru.typeOf[T].decl(ru.termNames.CONSTRUCTOR)
		  .asMethod
		  .paramLists
		  .map { o =>
			  o.map { i =>
				  (
					i.annotations.filter(_.tree.tpe == ru.typeOf[Argument]).flatMap(_.tree.children.tail.map { case ru.Literal(ru.Constant(k: String)) => k }),
					i.typeSignature
				  )
			  }
		  }
	}

	private def getParamsFromMap(args: Map[String, Any], constructorArgs: List[List[(List[String], ru.Type)]]): List[List[Any]] = {
		val argsKey = args.keySet

		constructorArgs.map { o =>
			o.zipWithIndex.map { i =>
				if (argsKey.contains(i._1._1(0))) getParamValue(args.get(i._1._1(0)), i._1._2)
				else getParamValue(args.get(i._1._1(2)), i._1._2)
			}
		}
	}

	private def getParamValue(value: Option[Any], desiredType: ru.Type): Any = {
		val td = ru.definitions

		if (desiredType =:= td.IntClass.toType) value.map(_.toString.toInt).getOrElse(0)
		else if (desiredType =:= td.LongClass.toType) value.map(_.toString.toLong).getOrElse(0.0)
		else if (desiredType =:= td.StringClass.toType) value.map(_.toString).getOrElse("")
		else if (desiredType =:= td.BooleanClass.toType) value.exists(_.toString.toBoolean)
		else if (desiredType <:< ru.typeOf[Option[_]]) Some(getParamValue(value, desiredType.typeArgs.head))
		else Some(false)
	}

	@tailrec
	private def getOptionsAcc(map: Map[String, String], argv: List[String]): Map[String, String] = argv match {
		case Nil => map
		case key :: value :: tail if key.startsWith("--") && !value.startsWith("--") =>
			val pureKey: String = key.replace("--", "").toLowerCase
			getOptionsAcc(map ++ Map(pureKey -> value.trim), tail)
		case key :: value :: tail if key.startsWith("--") && value.startsWith("--") =>
			val pureKey: String = key.replace("--", "").trim.toLowerCase
			getOptionsAcc(map ++ Map(pureKey -> "true"), value :: tail)
		case value :: tail if value.startsWith("--") =>
			val pureKey: String = value.replace("--", "").trim.toLowerCase
			getOptionsAcc(map ++ Map(pureKey -> "true"), tail)
		case value :: tail if map.nonEmpty =>
			getOptionsAcc(map ++ Map(map.last._1 -> s"${map.last._2},${value.trim}"), tail)
	}
}

object Parser {
	def apply(args: Array[String]): Parser = new Parser(args)
}