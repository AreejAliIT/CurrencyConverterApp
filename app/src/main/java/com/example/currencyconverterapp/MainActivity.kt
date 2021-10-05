package com.example.currencyconverterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() , AdapterView.OnItemSelectedListener {
    var currencies = arrayOf("inr", "usd", "aud","sar","cny","jpy")
    lateinit var spinner : Spinner
    lateinit var btn : Button
    lateinit var input : EditText
    lateinit var tvResult: TextView
    lateinit var tvDate: TextView
    private var curencyDetails: PriceData? = null
    var selected: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn=findViewById(R.id.btn)
        input=findViewById(R.id.input)
        tvResult=findViewById(R.id.result)
        tvDate=findViewById(R.id.tvDate)

        //  ------------ SPINNER------------------
        spinner = findViewById(R.id.spinner)
        spinner!!.setOnItemSelectedListener(this)
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val array_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        // Set layout to use when the list of choices appear
        array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner!!.setAdapter(array_adapter)

        //  ------------ API CALL------------------

         btn.setOnClickListener{
                callAPI()
            }
        }

    private fun disp(calc: Double) {
        tvResult.text = "result " + calc
    }

    private fun calc(cur: Double?, num: Double): Double {
        var r = 0.0
        if ( cur != null) {
            r = (cur * num)
        }
        return r
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selected = position
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
    fun callAPI(){

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        val call: Call<PriceData?>? = apiInterface!!.getPrices()

        call?.enqueue(object : Callback<PriceData?> {
            override fun onResponse(call: Call<PriceData?>?, response: Response<PriceData?>
            ) {
                Log.d("TAG", response.code().toString() + "")
                val resource: PriceData? = response.body()
                val datumDate = resource?.date
                val datumCur = resource?.eur
                tvDate.text = "Date:"+datumDate
                var num = input.text.toString()
                var currency: Double = num.toDouble()
                    when (selected) {
                        0 -> disp(calc(datumCur?.inr?.toDouble(), currency));
                        1 -> disp(calc(datumCur?.usd?.toDouble(), currency));
                        2 -> disp(calc(datumCur?.aud?.toDouble(), currency));
                        3 -> disp(calc(datumCur?.sar?.toDouble(), currency));
                        4 -> disp(calc(datumCur?.cny?.toDouble(), currency));
                        5 -> disp(calc(datumCur?.jpy?.toDouble(), currency));
                    }
            }
            override fun onFailure(call: Call<PriceData?>, t: Throwable?) {
                call.cancel()
            }
        })

    }
}