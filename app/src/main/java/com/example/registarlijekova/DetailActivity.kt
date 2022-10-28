package com.example.registarlijekova

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity() : AppCompatActivity() {

    private lateinit var name: TextView
    private lateinit var atc: TextView
    private lateinit var desc: TextView
    private lateinit var detail: TextView
    private lateinit var activeSubstance: TextView
    private lateinit var maxDose: TextView
    private lateinit var measurementSub: TextView
    private lateinit var category: TextView
    private lateinit var active: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            title = "Back"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        name = findViewById(R.id.detailName)
        atc = findViewById(R.id.detailAtc)
        desc = findViewById(R.id.shortDescription)
        detail = findViewById(R.id.longDescription)
        activeSubstance = findViewById(R.id.activSubstance)
        maxDose = findViewById(R.id.recomDose)
        measurementSub = findViewById(R.id.ratioActiveSub)
        category = findViewById(R.id.detailDescription)
        active = findViewById(R.id.treciApi)
        getData()
        val intent = intent.extras
        val medicamentId = intent?.getInt("id", 0)
        getActivityDetails(medicamentId)
    }

    private fun getData() {

        val intent = intent.extras
        val medicamentName = intent?.getString("name")
        val medicamtentAtc = intent?.getString("atc")
        val medicamtenrDesc = intent?.getString("description")
        val medicamentDetail = intent?.getString("longDesc")
        var medicamentsActiveSubstance = intent?.getInt("activeSubstance", 0).toString()
        if (medicamentsActiveSubstance == "0") medicamentsActiveSubstance = "/"
        var medicamentsDailyDose = intent?.getInt("maxDailyDose", 0).toString()
        if (medicamentsDailyDose == "0") medicamentsDailyDose = "/"
        var medicamentsMeasur = intent?.getString("measurementSubstance")
        if (medicamentsMeasur.isNullOrEmpty()) medicamentsMeasur = "/"
        val medicamentsCategory = intent?.getString("category")

        name.text = medicamentName
        atc.text = medicamtentAtc
        desc.text = medicamtenrDesc
        detail.text = medicamentDetail
        detail.movementMethod = ScrollingMovementMethod()
        activeSubstance.text = medicamentsActiveSubstance
        maxDose.text = medicamentsDailyDose
        measurementSub.text = medicamentsMeasur
        category.text = medicamentsCategory
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getActivityDetails(medicamentId: Int?) {
        val serviceGenerator = ServiceGenerator.buildService(ApiInterface::class.java)
        val call = serviceGenerator.getData3(medicamentId)

        call.enqueue(object : Callback<ArrayList<ActiveSubstanceItem>> {
            override fun onResponse(
                call: Call<ArrayList<ActiveSubstanceItem>>,
                response: Response<ArrayList<ActiveSubstanceItem>>
            ) {
                if (response.isSuccessful) {

                    val activeResponse = response.body()!!
                    active.text = activeResponse[0].name

                }
            }

            override fun onFailure(call: Call<ArrayList<ActiveSubstanceItem>>, t: Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }
        })
    }
}
