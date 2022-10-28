package com.example.registarlijekova


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), MyAdapter.ClickListener {

    private lateinit var recyclerView: RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerview_medicamentos)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        getMedicalItems()

    }

    private fun getMedicalItems() {
        val serviceGenerator = ServiceGenerator.buildService(ApiInterface::class.java)
        val call = serviceGenerator.getData()
        call.enqueue(object : Callback<ArrayList<MedicamentDataItem>> {
            override fun onResponse(
                call: Call<ArrayList<MedicamentDataItem>>,
                response: Response<ArrayList<MedicamentDataItem>>
            ) {
                if (response.isSuccessful) {
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                    }
                    var responseBody1 = response.body()!!
                    getCategoryItems(responseBody1)
                }
            }

            override fun onFailure(call: Call<ArrayList<MedicamentDataItem>>, t: Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }
        })
    }

    private fun getCategoryItems(medicamentsCategory: ArrayList<MedicamentDataItem>) {
        val serviceGenerator = ServiceGenerator.buildService(ApiInterface::class.java)
        val call = serviceGenerator.getData2()

        call.enqueue(object : Callback<ArrayList<MedicamentsCategoryDataItem>> {
            override fun onResponse(
                call: Call<ArrayList<MedicamentsCategoryDataItem>>,
                response: Response<ArrayList<MedicamentsCategoryDataItem>>
            ) {
                if (response.isSuccessful) {

                    val responseCategory = response.body()!!
                    val categoryList = responseCategory
                    val medicamentList = medicamentsCategory


                    val categoryById: Map<Int, MedicamentsCategoryDataItem> =
                        categoryList.associateBy { it.id }
                    val result =
                        medicamentList.filter { categoryById[it.categoryId] != null }.map { value ->
                            categoryById[value.categoryId]?.let { category ->
                                Info(
                                    value.id,
                                    value.atc,
                                    value.name,
                                    category.name,
                                    category.color,
                                    value.description,
                                    value.activeSubstanceValue,
                                    value.shortDescription,
                                    value.maximumDailyDose,
                                    value.activeSubstanceMeasurementUnit,
                                    category.mark
                                )
                            }
                        }

                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = MyAdapter(result, this@MainActivity)
                    }
                    val myAdapter = MyAdapter(result, this@MainActivity)
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    recyclerView.adapter = myAdapter

                    val search = findViewById<SearchView>(R.id.searchView)

                    search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            myAdapter.filter.filter(newText)
                            return false
                        }
                    })
                }
            }

            override fun onFailure(
                call: Call<ArrayList<MedicamentsCategoryDataItem>>,
                t: Throwable
            ) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }
        })
    }

    override fun clickeditem(userModel: Info?) {
        startActivity(
            Intent(this, DetailActivity::class.java)
                .putExtra("id", userModel?.id)
                .putExtra("name", userModel?.nameInfo)
                .putExtra("atc", userModel?.atcInfo)
                .putExtra("category", userModel?.categoryNameInfo)
                .putExtra("longDesc", userModel?.longDescription)
                .putExtra("description", userModel?.shortDescription)
                .putExtra("activeSubstance", userModel?.activeSubstanceValue)
                .putExtra("measurementSubstance", userModel?.activeSubstanceMeasurementUnit)
                .putExtra("maxDailyDose", userModel?.maximumDailyDose)
        )
    }


}






