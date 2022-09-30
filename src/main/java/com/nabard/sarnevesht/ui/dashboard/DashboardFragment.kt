package com.nabard.sarnevesht.ui.dashboard

import RetrofitApi
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.example.messaging.kotlin.com.nabard.sarnevesht.Content
import com.google.firebase.example.messaging.kotlin.com.nabard.sarnevesht.RecyclerViewAdapter
import com.nabard.sarnevesht.MyHttpClient.REWRITE_CACHE_CONTROL_INTERCEPTOR
import com.nabard.sarnevesht.databinding.FragmentDashboardBinding
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val sharedPreference = requireActivity().getSharedPreferences("content", Context.MODE_PRIVATE)

        val url= sharedPreference.getString("url","https://cdn.rasm.io/")
        val httpCacheDirectory = File(context!!.cacheDir, "responses")


        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MiB
        val cache = Cache(httpCacheDirectory, cacheSize)
        val client = OkHttpClient.Builder().addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR).cache(cache).build()


        val retrofit: Retrofit = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        val retrofitAPI: RetrofitApi = retrofit.create(RetrofitApi::class.java)
        val call: Call<ArrayList<Content>> = retrofitAPI.all
        call.enqueue(object : Callback<ArrayList<Content>>{
           override fun onResponse(
                call: Call<ArrayList<Content>>,
                response: Response<ArrayList<Content>>
            ) {
                // inside on response method we are checking
                // if the response is success or not.
                if (response.isSuccessful()) {

                    try {
                        //val sharedPreference = requireActivity().getSharedPreferences("content", Context.MODE_PRIVATE)
                        //val editor=sharedPreference.edit()
                        //editor.putString("contentData",response.body().)
                        binding.textDashboard.visibility = View.GONE
                        binding.Recycler.visibility = View.VISIBLE
                        val recyclerDataArrayList = response.body()
                        val recyclerViewAdapter =
                            RecyclerViewAdapter(recyclerDataArrayList!!, requireContext())
                        val manager = LinearLayoutManager(requireContext())
                        binding.Recycler.setLayoutManager(manager)
                        binding.Recycler.setAdapter(recyclerViewAdapter)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

           override fun onFailure(call: Call<ArrayList<Content>>, t: Throwable?) {
                // in the method of on failure we are displaying a
                // toast message for fail to get data.
               try {
                   Toast.makeText(
                       requireContext(),
                       "خطا در دریافت محتوای قسمت آموزشی",
                       Toast.LENGTH_SHORT
                   ).show()
               }catch(e:Exception){}
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}