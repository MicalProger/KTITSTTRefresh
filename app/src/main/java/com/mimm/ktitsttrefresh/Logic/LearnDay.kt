package com.mimm.ktitsttrefresh.Logic

import java.time.LocalTime

class LearnDay {
    lateinit var CallsTimetable: Array<LocalTime>
    lateinit var LessonsTimetable: Array<Lesson?>

    constructor(CallsTimetable: Array<LocalTime>, LessonsTimetable: Array<Lesson?>) {
        this.CallsTimetable = CallsTimetable
        this.LessonsTimetable = LessonsTimetable
    }

    constructor() {
        CallsTimetable = Array(11) { LocalTime.MIN }
        LessonsTimetable = Array(8) { Lesson("Нет пары", "нет") }
    }

}

class Lesson {
    var LessonName: String?
    var ClassNo: String?

    constructor(lesson: String, room: String) {
        LessonName = lesson
        ClassNo = room
    }
}
