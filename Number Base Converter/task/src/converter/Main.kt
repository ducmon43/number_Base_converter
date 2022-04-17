package converter
import java.math.BigInteger
import java.math.BigDecimal
import java.math.RoundingMode

fun toDecimal(sourceNum: String, sourceBase: BigInteger): BigInteger{
    val rangeList = ('0'..'9') + ('a'..'z')

    var decimalResult = 0.toBigInteger()
    for (i in sourceNum.indices) {
        val component = rangeList.indexOf(sourceNum[i]).toBigInteger()
        decimalResult = (component * sourceBase.pow(sourceNum.length - 1 - i)) + decimalResult
    }
    return decimalResult
}

fun fromDecimal(decimalResult: BigInteger, targetBase: BigInteger): String {
    val rangeList = ('0'..'9') + ('a'..'z')
    var convertResult = ""
    var decimalDivide = decimalResult
    do {
        val (quotient, remainder) = decimalDivide.divideAndRemainder(targetBase)
        decimalDivide = quotient
        convertResult = rangeList[remainder.toInt()].toString() + convertResult
    } while (quotient > BigInteger.ZERO)
    return convertResult
}

fun toDecimalFraction (sourceFraction: String, sourceBase: BigDecimal): BigDecimal{
    val rangeList = ('0'..'9') + ('a'..'z')

    var decimalFractionResult = BigDecimal.ZERO
    for (i in sourceFraction.indices) {
        val component = rangeList.indexOf(sourceFraction[i]).toBigDecimal()
        decimalFractionResult = component.setScale(37, RoundingMode.CEILING) / sourceBase.pow(i + 1) + decimalFractionResult
    }
    return decimalFractionResult
}

fun fromDecimalFraction(decimalFractionResult: BigDecimal, targetBase: BigDecimal): String {
    val rangeList = ('0'..'9') + ('a'..'z')
    var convertResult = ""
    var newDecimal = decimalFractionResult

    repeat (5) {
        val component = newDecimal * targetBase
        convertResult += rangeList[component.toInt()]
        newDecimal = component.remainder(BigDecimal.ONE)
    }
    return convertResult
}

fun main() {
    do {
        println("Enter two numbers in format: {source base} {target base} (To quit type /exit)")
        val answer = readLine()!!
        if (answer != "/exit") {
            val inputList = answer.split(" ").map { it.toInt() }.toMutableList()
            do {
                println("Enter number in base ${inputList[0]} to convert to base ${inputList[1]} (To go back type /back),")
                val answer2 = readLine()!!.toString()
                if (answer2 != "/back") {
                    if (answer2.indexOf('.') == - 1) {
                        val result = fromDecimal(toDecimal(answer2, inputList[0].toBigInteger()), inputList[1].toBigInteger())
                        println("Conversion result: $result")
                    } else {
                        val numberParts = answer2.split(".")
                        val integerComponent = fromDecimal(toDecimal(numberParts[0], inputList[0].toBigInteger()), inputList[1].toBigInteger())
                        val fractionComponent = fromDecimalFraction(decimalFractionResult = toDecimalFraction(sourceFraction = numberParts[1],
                            sourceBase = inputList[0].toBigDecimal()), targetBase = inputList[1].toBigDecimal())
                        println("Conversion result: ${integerComponent}.${fractionComponent}")
                    }
                }
            } while (answer2 != "/back")
        }
    } while (answer != "/exit")

}