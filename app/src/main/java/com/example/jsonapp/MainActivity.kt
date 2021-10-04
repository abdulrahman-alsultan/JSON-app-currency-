package com.example.jsonapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var curencyDetails: Datum? = null
    lateinit var amount: EditText
    lateinit var date: TextView
    lateinit var fromEURO: Button
    lateinit var toEURO: Button
    lateinit var result: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        amount = findViewById(R.id.etValue)
        date = findViewById(R.id.tvDate)
        fromEURO = findViewById(R.id.btnFromEURO)
        toEURO = findViewById(R.id.btnToEURO)
        result = findViewById(R.id.result)
        val spinner = findViewById<Spinner>(R.id.spinner)

        val cur = arrayListOf("inr", "usd", "aud", "sar", "cny", "jpy")
        var selected: Int = 0

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cur)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selected = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        fromEURO.setOnClickListener {
            var input = amount.text.toString()
            var currency = input!!.toDouble()

            fromEURO.setBackgroundColor(resources.getColor(R.color.green))
            toEURO.setBackgroundColor(resources.getColor(R.color.gray))

            getCurrency(onResult = {
                curencyDetails = it

                when (selected) {
                    0 -> disp(calcFrom(curencyDetails?.eur?.inr?.toDouble(), currency));
                    1 -> disp(calcFrom(curencyDetails?.eur?.usd?.toDouble(), currency));
                    2 -> disp(calcFrom(curencyDetails?.eur?.aud?.toDouble(), currency));
                    3 -> disp(calcFrom(curencyDetails?.eur?.sar?.toDouble(), currency));
                    4 -> disp(calcFrom(curencyDetails?.eur?.cny?.toDouble(), currency));
                    5 -> disp(calcFrom(curencyDetails?.eur?.jpy?.toDouble(), currency));
                }
            })
        }

        toEURO.setOnClickListener {
            var input = amount.text.toString()
            var currency = input!!.toDouble()

            toEURO.setBackgroundColor(resources.getColor(R.color.green))
            fromEURO.setBackgroundColor(resources.getColor(R.color.gray))

            getCurrency(onResult = {
                curencyDetails = it

                when (selected) {
                    0 -> disp(calcTo(curencyDetails?.eur?.inr?.toDouble(), currency));
                    1 -> disp(calcTo(curencyDetails?.eur?.usd?.toDouble(), currency));
                    2 -> disp(calcTo(curencyDetails?.eur?.aud?.toDouble(), currency));
                    3 -> disp(calcTo(curencyDetails?.eur?.sar?.toDouble(), currency));
                    4 -> disp(calcTo(curencyDetails?.eur?.cny?.toDouble(), currency));
                    5 -> disp(calcTo(curencyDetails?.eur?.jpy?.toDouble(), currency));
                }
            })
        }
    }

    private fun disp(res: Double) {
        result.text = "result $res"
    }

    private fun calcFrom(i: Double?, sel: Double): Double {
        var s = 0.0
        if (i != null) {
            s = i * sel
        }
        return s
    }

    private fun calcTo(i: Double?, sel: Double): Double {
        var s = 0.0
        if (i != null) {
            s = sel/i
        }
        return s
    }

    private fun getCurrency(onResult: (Datum?) -> Unit) {
        val apiInterface = RetroInstance().getClient()?.create(APIInterface::class.java)

        if (apiInterface != null) {
            apiInterface.getCurrency()?.enqueue(object : Callback<Datum> {
                override fun onResponse(
                        call: Call<Datum>,
                        response: Response<Datum>
                ) {
                    onResult(response.body())

                }

                override fun onFailure(call: Call<Datum>, t: Throwable) {
                    onResult(null)
                    Toast.makeText(applicationContext, "" + t.message, Toast.LENGTH_SHORT).show();
                }

            })
        }
    }
}