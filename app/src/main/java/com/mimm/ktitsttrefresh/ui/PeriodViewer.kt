package com.mimm.ktitsttrefresh.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.mimm.ktitsttrefresh.R
import java.time.LocalTime

class PeriodViewer : FrameLayout {
    private var _lessonName: String = "Нет пары"
    private var _classNum: String = ""
    public var eventStart: LocalTime? = null
    public var eventEnd: LocalTime? = null


    var lessonName: String
        get() = _lessonName
        set(value) {
            _lessonName = value
        }

    var classNum: String
        get() = _classNum
        set(value) {
            _classNum = value
        }

    fun activate() {
        var actLT = findViewById<LinearLayout>(R.id.timeViewer)
        actLT.visibility = LinearLayout.VISIBLE
        if(eventStart != null)
            findViewById<LinearLayout>(R.id.progressViewer).visibility = LinearLayout.VISIBLE
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        inflate(getContext(), R.layout.preiod_viewer, this)
        context.theme.obtainStyledAttributes(attrs, R.styleable.PeriodViewer, 0, 0).apply {
            lessonName = getString(R.styleable.PeriodViewer_lessonName) ?: "Нет пары"
            classNum = getString(R.styleable.PeriodViewer_classNo) ?: "ХЗ"
            try {
            } finally {
                recycle()
            }
        }
        updateText()
    }

    constructor(context: Context) : super(context) {
        inflate(getContext(), R.layout.preiod_viewer, this)
        lessonName = "Нет пары"
        classNum = "ХЗ"
        updateText()
    }
    fun updateText() {
        var ls = findViewById<TextView>(R.id.lessonTV)
        ls.text = lessonName
        var cn = findViewById<TextView>(R.id.roomTV)
        cn.text = classNum
        if(eventStart != null){
            var startTV = findViewById<TextView>(R.id.startTimeTV)
            startTV.text = eventStart.toString()
            startTV.visibility = LinearLayout.VISIBLE
        }
    }

    fun updateProgress() {
        if (eventEnd != null) {

            var delta = eventEnd!!.minusNanos(LocalTime.now().toNanoOfDay())
            var deltaText = findViewById<TextView>(R.id.remainsTV)
            deltaText.text = delta.toString().substring(0, 11)
            if (eventStart != null) {
                var maxDelta = eventEnd!!.minusNanos(eventStart!!.toNanoOfDay())
                var progress = (maxDelta.toNanoOfDay() - delta.toNanoOfDay()).toDouble() / maxDelta.toNanoOfDay().toDouble()
                var progressAsText = findViewById<TextView>(R.id.textProgressTV)
                progressAsText.text = (progress * 100).toString().substring(0, 5) + "%"
                var progressBar = findViewById<ProgressBar>(R.id.periodPB)
                progressBar.progress = (progress * 1000).toInt()
            }
        }
    }
}
