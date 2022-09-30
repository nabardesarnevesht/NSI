package com.nabard.sarnevesht.ui.notifications

import DBManager
import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.journaldev.sqlite.DatabaseHelper
import com.nabard.sarnevesht.MainActivity
import com.nabard.sarnevesht.MyNotificationPublisher
import com.nabard.sarnevesht.R
import com.nabard.sarnevesht.databinding.FragmentNotificationsBinding
import com.nabard.sarnevesht.ui.NotificationDetails


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    val from = arrayOf(
        DatabaseHelper.SUBJECT,
        DatabaseHelper.DESC,

        DatabaseHelper.TAG
    )
    val to = intArrayOf(R.id.title,R.id.subtitle,R.id.newbadge)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                NotificationsViewModel::class.java
            )

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)


        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        val dbManager = DBManager(this.requireContext())
        dbManager.open()
        val cursor: Cursor = dbManager.fetch()!!

        val listView = binding.listView
        listView.setEmptyView(textView)

        val adapter = SimpleCursorAdapter(this.context,
           R.layout.notification_item, cursor, from, to, 0)

        adapter.notifyDataSetChanged()

        listView.setAdapter(adapter)






        listView.setOnItemClickListener(OnItemClickListener { parent, view, position, viewId ->
            //val idTextView = view.findViewById<View>(R.id.id) as TextView
           val item= adapter.getItem(position);

            //.val titleTextView = (view as TwoLineListItem).text1
           // val descTextView = view.findViewById<View>(R.id.desc) as TextView

            //val id = idTextView.text.toString()
            //val title = titleTextView.text.toString()
            val cursor=(item as SQLiteCursor)
            val intent = Intent(this.context, NotificationDetails::class.java)
            intent.putExtra("title", cursor.getString(1))
            intent.putExtra("desc", cursor.getString(2))
            startActivity(intent)

            dbManager.setClicked((item as SQLiteCursor).getInt(0))
           // val desc = descTextView.text.toString()
//            val modify_intent = Intent(
//                ApplicationProvider.getApplicationContext<Context>(),
//                ModifyCountryActivity::class.java
//            )
//            modify_intent.putExtra("title", title)
//            modify_intent.putExtra("desc", desc)
//            modify_intent.putExtra("id", id)
//            startActivity(modify_intent)
        })



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}