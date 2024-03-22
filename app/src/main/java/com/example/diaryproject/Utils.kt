package com.example.diaryproject

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

class Utils {
    companion object {
        @SuppressLint("SimpleDateFormat")
        fun convertLongToDateString(systemTime: Long): String {
            return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
                .format(systemTime).toString()
        }
    }
}