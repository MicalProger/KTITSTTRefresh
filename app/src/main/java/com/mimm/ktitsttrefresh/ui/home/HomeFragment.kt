package com.mimm.ktitsttrefresh.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mimm.ktitsttrefresh.Logic.LearnDay
import com.mimm.ktitsttrefresh.MainActivity
import com.mimm.ktitsttrefresh.R
import com.mimm.ktitsttrefresh.databinding.FragmentHomeBinding
import com.mimm.ktitsttrefresh.ui.PeriodViewer
import java.time.LocalTime

class HomeFragment : Fragment() {

    var checkTime: Boolean = true
    var today: LearnDay? = null
    private var _binding: FragmentHomeBinding? = null
    var handler: Handler = Handler()

    var uiUpdater = object : Runnable {
        override fun run() {
            updateTimings()
            handler.postDelayed(this, 50)

        }
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        today = (activity as MainActivity).today
        val root: View = binding.root
        return root
    }


    fun LoadLessons(activity: MainActivity) {
        var panel = activity.findViewById<LinearLayout>(R.id.MainLL)
        panel.removeAllViews()
        if (today != null) {
            var start = today!!.CallsTimetable.indexOfLast { i ->
                i.toNanoOfDay() < LocalTime.now().toNanoOfDay()
            }
            if (start < today!!.LessonsTimetable.size * 2 - 1) {
                if (start == -1) {
                    start = 0
                    val callTime = TextView(activity)
                    callTime.text = "Утро"
                    callTime.setPadding(10)
                    panel.addView(callTime)
                    var beforeLs = PeriodViewer(activity)
                    beforeLs.lessonName = "Техникум связи"
                    beforeLs.classNum = "Бари Галеева 3А"
                    beforeLs.eventEnd = today!!.CallsTimetable[0]
                    beforeLs.updateText()

                    panel.addView(beforeLs)
                }
                for (i in start until today!!.LessonsTimetable.size * 2) {
                    val callTime = TextView(activity)
                    callTime.text = today!!.CallsTimetable[i].toString()
                    callTime.setPadding(10)
                    panel.addView(callTime)
                    if (i == today!!.LessonsTimetable.size * 2 - 1)
                        continue
                    val lsPV = PeriodViewer(activity)
                    lsPV.eventStart = today!!.CallsTimetable[i]
                    lsPV.eventEnd = today!!.CallsTimetable[i + 1]
                    if (i % 2 == 1) {
                        lsPV.lessonName = "Перемена"
                        lsPV.classNum =
                            (LocalTime.ofNanoOfDay(lsPV.eventEnd!!.toNanoOfDay() - lsPV.eventStart!!.toNanoOfDay())).minute.toString() + "минут"
                    } else {
                        if (today!!.LessonsTimetable[i / 2] != null) {
                            lsPV.lessonName = today!!.LessonsTimetable[i / 2]?.LessonName.toString()
                            lsPV.classNum = today!!.LessonsTimetable[i / 2]?.ClassNo.toString()
                        }
                    }
                    lsPV.setPadding(10)
                    lsPV.updateText()
                    panel.addView(lsPV)
                    (panel[1] as PeriodViewer).activate()
                }
            } else {
                checkTime = false
                var noLesson = TextView(activity)
                noLesson.text = "Сегодня пар уже нет"
                noLesson.textSize = 30f
                noLesson.setPadding(10)
                panel.addView(noLesson)
            }
        } else {
            checkTime = false
            var noLesson = TextView(activity)
            noLesson.text = "Сегодня пар еще нет"
            noLesson.textSize = 30f
            noLesson.setPadding(10)
            panel.addView(noLesson)
        }
    }

    override fun onStart() {
        super.onStart()
        var act = (activity as MainActivity)
        LoadLessons(act)
        handler.postDelayed(uiUpdater, 50)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(uiUpdater, 50)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(uiUpdater)
    }
    fun updateTimings() {

        var act = activity!!
        var now = LocalTime.now()
        var nowText = now.toString()
        if (nowText.length >= 10)
            nowText = nowText.substring(0, 10)
        act.findViewById<TextView>(R.id.nowTime).text = nowText
        if (!checkTime)
            return
        var container = act.findViewById<LinearLayout>(R.id.MainLL)
        if ((container[1] as PeriodViewer).eventEnd!!.toNanoOfDay() < now.toNanoOfDay()) {
            container.removeViewAt(0)
            container.removeViewAt(0)
            if (container.childCount < 2){
                checkTime = false
                return
            }
            (container[1] as PeriodViewer).activate()
        }
        if (container.childCount >= 2)
            (container[1] as PeriodViewer).updateProgress()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}