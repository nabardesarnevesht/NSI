package com.nabard.sarnevesht.ui.home

import Cities
import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.nabard.sarnevesht.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    var Provinces = ArrayList<String>()
    var ProvinceIds = ArrayList<Int>()
    var CitiesList = ArrayList<String>()
    var CityIdsList = ArrayList<Int>()

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

val context = this.context!!
        val dbManager = Cities(context)
        dbManager.open()
        val cursor: Cursor = dbManager.fetchProvinces()!!

        val provinceSpinner = binding.Province
        val citySpinner = binding.Cities

        do{
            Provinces.add(cursor.getString(1))
            ProvinceIds.add(cursor.getInt(0))
        }while(cursor.moveToNext())
        //now you have options with all values of your database

        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,Provinces);
        provinceSpinner.setAdapter(adapter); // this will set list of values to spinner

        provinceSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if(i<0)
                    return;
                binding.W.isEnabled=ProvinceIds[i]!=0
                binding.C.isEnabled=ProvinceIds[i]!=0
                val ccursor: Cursor = dbManager.fetchCities(ProvinceIds[i])!!
                CitiesList.clear()
                CityIdsList.clear()
                do{
                    CitiesList.add(ccursor.getString(1));
                    CityIdsList.add(ccursor.getInt(0));
                }while(ccursor.moveToNext())
                //now you have options with all values of your database

                val adapter2 = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,CitiesList);
                citySpinner.setAdapter(adapter2); // this will set list of values to spinner
                  }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }

        //provinceSpinner.setSelection(options.indexOf(<value you want to show selected>)));//set selected value in spinner


        binding.setAbilitiesButton.setOnClickListener {
            binding.setAbilitiesButton.isEnabled=false;
            binding.setAbilitiesButton.text="در حال ثبت..."
            Toast.makeText(context, "درخواست عضویت ارسال شد. منتظر نتیجه بمانید.", Toast.LENGTH_SHORT).show()
            val bundle = Bundle()
            bundle.putInt(FirebaseAnalytics.Param.ITEM_ID,ProvinceIds[provinceSpinner.selectedItemPosition])
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Provinces[provinceSpinner.selectedItemPosition])

            Firebase.analytics.logEvent("Province",bundle)
            Firebase.messaging.subscribeToTopic(ProvinceIds[provinceSpinner.selectedItemPosition].toString())
                .addOnCompleteListener { task ->
                        if (!task.isSuccessful)
                            Toast.makeText(context, "خطا در عضویت. دوباره سعی کنید", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { it->
                    Toast.makeText(context,"خطا در عضویت اینترنت خود را بررسی کرده و دوباره سعی کنید",Toast.LENGTH_LONG)
                }
            if(binding.W.isChecked)
                Firebase.messaging.subscribeToTopic(
                "W" +ProvinceIds[provinceSpinner.selectedItemPosition]+"-" +CityIdsList[citySpinner.selectedItemPosition])
                .addOnCompleteListener { task ->
                    var msg = "عضو ارتش پیاده شدید"
                    if (!task.isSuccessful)
                        msg = "خطا در عضویت. دوباره سعی کنید"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }.addOnCanceledListener {
                    Toast.makeText(context,"خطا در عضویت اینترنت خود را بررسی کرده و دوباره سعی کنید",Toast.LENGTH_SHORT)
                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "خطا در عضویت اینترنت خود را بررسی کرده و دوباره سعی کنید",
                        Toast.LENGTH_SHORT
                    )
                }

            if(binding.C.isChecked)
                Firebase.messaging.subscribeToTopic(
                    "C" +ProvinceIds[provinceSpinner.selectedItemPosition]+"-" +CityIdsList[citySpinner.selectedItemPosition])
                    .addOnCompleteListener { task ->
                        var msg = "عضو ارتش سواره شدید"
                        if (!task.isSuccessful)
                            msg = "خطا در عضویت. دوباره سعی کنید"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
            if(binding.V.isChecked)
                Firebase.messaging.subscribeToTopic("V")
                    .addOnCompleteListener { task ->
                        var msg = "عضو ارتش فضای مجازی شدید"
                        if (!task.isSuccessful)
                            msg = "خطا در عضویت. دوباره سعی کنید"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
            if(binding.D.isChecked)
                Firebase.messaging.subscribeToTopic("D")
                    .addOnCompleteListener { task ->
                        var msg = "عضو ارتش طراحی شدید"
                        if (!task.isSuccessful)
                            msg = "خطا در عضویت. دوباره سعی کنید"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
            if(binding.A.isChecked)
                Firebase.messaging.subscribeToTopic("A")
                    .addOnCompleteListener { task ->
                        var msg = "عضو ارتش تبلیغات شدید"
                        if (!task.isSuccessful)
                            msg = "خطا در عضویت. دوباره سعی کنید"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
            if(binding.F.isChecked)
                Firebase.messaging.subscribeToTopic("F")
                    .addOnCompleteListener { task ->
                        var msg = "عضو ارتش مالی شدید"
                        if (!task.isSuccessful)
                            msg = "خطا در عضویت. دوباره سعی کنید"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
            binding.setAbilitiesButton.text="ثبت مشخصات"
            binding.setAbilitiesButton.isEnabled=true;

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}