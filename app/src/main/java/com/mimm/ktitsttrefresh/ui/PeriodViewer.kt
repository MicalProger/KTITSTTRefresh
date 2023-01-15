package com.mimm.ktitsttrefresh.ui

import android.content.Context
import android.graphics.Canvas
import android.os.CountDownTimer
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

    public fun activate() {
        var actLT = findViewById<LinearLayout>(R.id.progressViewer)
        actLT.visibility = LinearLayout.VISIBLE
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
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

    constructor(context: Context) : super(context){
        inflate(getContext(), R.layout.preiod_viewer, this)
        lessonName = "Нет пары"
        classNum = "ХЗ"
        updateText()
    }

    public fun updateText(){
        var ls = findViewById<TextView>(R.id.lessonTV)
        ls.text = lessonName
        var cn = findViewById<TextView>(R.id.roomTV)
        cn.text = classNum
    }

    public fun updateProgress(){
        if(eventEnd != null){

            var delta = eventEnd!!.minusNanos(LocalTime.now().toNanoOfDay())
            var deltaText = findViewById<TextView>(R.id.remainsTV)
            deltaText.text = delta.toString().substring(0, 11)
            if(eventStart != null){
                var maxDelta = eventEnd!!.minusNanos(eventStart!!.toNanoOfDay())
                var progress = delta.toNanoOfDay().toDouble() / maxDelta.toNanoOfDay().toDouble()
                var progText = findViewById<TextView>(R.id.textProgressTV)
                progText.text = (progress * 100).toString().substring(0, 5) + "%"
                var progBar = findViewById<ProgressBar>(R.id.periodPB)
                progBar.progress = (progress * 1000).toInt()
            }
        }
    }
}
