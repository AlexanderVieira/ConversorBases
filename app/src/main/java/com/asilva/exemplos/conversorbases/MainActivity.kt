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

                val inputNumber = number_edittext.text.toString().toInt()
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

    fun convertBase(number: Int, from: Int, to: Int):String{

        if (from == 10 && to < 10){

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

        return number.toString()
    }

    fun decimalToOther(number: Int, base: Int) :String{

        val binDigits = mutableListOf<Int>()
        var result = number

        if (base == 8){

            var numOctal = 0
            var numDecimal = number

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
        return String.format("(" + binDigits.joinToString("") + ")" + base)
    }

    fun otherToDecimal(number: Int, base: Int): Int{

        val numberStr = number.toString()
        var result = 0.0

        for ((index, value) in numberStr.withIndex()) {

            val n = value.toString().toInt()
            val tam = numberStr.length - index
            result = result + n * (base.toDouble().pow(tam - 1))
        }
        return result.toInt()
    }

    fun binaryToOctal(number: Int, base: Int): String{

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
}
