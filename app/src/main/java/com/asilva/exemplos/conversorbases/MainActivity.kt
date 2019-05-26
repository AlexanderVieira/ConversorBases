package com.asilva.exemplos.conversorbases

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    val INVALID_ID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        conversor_button.setOnClickListener{

            if (number_edittext.text.isNotEmpty()){

                val inputNumber = number_edittext.text.toString().toLong()
                val inputId = input_radiogroup.checkedRadioButtonId
                val outputId = output_radiogroup.checkedRadioButtonId

                if (inputId != INVALID_ID && outputId != INVALID_ID){

                    val inputBase = getBase(inputId)
                    val outputBase = getBase(outputId)

                    val result = convertBase(inputNumber, inputBase, outputBase)
                    result_textview.text = result
                }
                else{
                    Toast.makeText(this, "Selecione as base!", Toast.LENGTH_LONG).show()
                }
            }
            else{
                number_edittext.error = "Campo vazio!"
            }
        }
    }

    fun getBase(id: Int) = when(id){
        R.id.in_bin, R.id.out_bin -> 2
        R.id.in_oct, R.id.out_oct -> 8
        R.id.in_dec, R.id.out_dec -> 10
        R.id.in_hex, R.id.out_hex -> 16
        else -> 0
    }

    fun convertBase(number: Long, from: Int, to: Int):String{

        if (from == 10 && to <= 16){

            if (to == 16){

                val binaryBase = to/8
                val binTemp = decimalToOther(number, binaryBase)
                val hex = binaryToHex(binTemp.toLong(), to)
                return String.format("(" + hex + ")" + to)

            }

            val result = decimalToOther(number, to)
            return result
        }
        else if (from < 10 && to == 10){

            val result = otherToDecimal(number, from)
            return String.format("(" + result + ")10")
        }
        else if (from < 10 && to == 8 ){

            val result = binaryToOctal(number, to)
            return String.format("(" + result + ")8")
        }
        else if(from < 10 && to == 16){

            val result = binaryToHex(number, to)
            return String.format("(" + result + ")16")
        }

        return number.toString()
    }

    fun decimalToOther(number: Long, base: Int) :String{

        val binDigits = mutableListOf<Long>()
        var result = number

        if (base == 8){

            var numOctal = 0
            var numDecimal = number.toInt()

            var i = 1
            while (numDecimal != 0){

                numOctal += (numDecimal % base) * i
                numDecimal /= base
                i *= 10
            }
            return String.format("(" + numOctal + ")" + base)
        }

        do{
            var rest = result % base
            result = result/base
            binDigits.add(rest)
        }while (result > 0)
        binDigits.reverse()
        //return String.format("(" + binDigits.joinToString("") + ")" + base)
        return binDigits.joinToString("")
    }

    fun otherToDecimal(number: Long, base: Int): Int{

        val numberStr = number.toString()
        var result = 0.0

        for ((index, value) in numberStr.withIndex()) {

            val n = value.toString().toInt()
            val tam = numberStr.length - index
            result = result + n * (base.toDouble().pow(tam - 1))
        }
        return result.toInt()
    }

    fun binaryToOctal(number: Long, base: Int): String{

        if (base == 8){

            return number.toString()
        }
        var binaryBase = base/4
        var numOctal = 0
        var numDecimal = otherToDecimal(number, binaryBase)

        var i = 1
        while (numDecimal != 0){

            numOctal += (numDecimal % base) * i
            numDecimal /= base
            i *= 10
        }
        return numOctal.toString()
    }

    fun binaryToHex(number: Long, base: Int): String{

        var binTemp = completeNumber(number.toString())
        val q = binTemp.length/4
        var hex = ""
        //println("$q")

        for (i in 0..q-1){

            //println("$i")

            val block = binTemp.substring(4*i, 4*i+4)
            val blockToLong = block.toLong()
            val binaryBase = base/8
            val decTemp = otherToDecimal(blockToLong, binaryBase)

            //println("$block $decTemp")

            hex += when(decTemp){
                10 -> "A"
                11 -> "B"
                12 -> "C"
                13 -> "D"
                14 -> "E"
                15 -> "F"
                else -> decTemp
            }
        }
        return hex
    }

    fun completeNumber(value: String): String {

        val rest = value.length % 4
        var binTemp = value

        if(rest == 1){

            for (i in 0..rest + 1) {
                binTemp = "0" + binTemp
            }
            return binTemp

        }

        if(rest == 3){

            for (i in 0..rest - 3) {

                binTemp = "0" + binTemp

            }
            return binTemp
        }

        for (i in 0..rest - 1) {

            binTemp = "0" + binTemp

        }
        return binTemp
    }
}
