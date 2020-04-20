package com.michael.fakeorfact.misc

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.michael.fakeorfact.R


class Dialog {
    fun showDialogBox(mTitle: String, mMsg: String, mFrom: Context){
        // Set Up Dialog box
        val build = AlertDialog.Builder(mFrom)
        val inflater = LayoutInflater.from(mFrom)
        val dialV: View = inflater.inflate(R.layout.dialog_view, null)
        build.setView(dialV)
        val close = dialV.findViewById<Button>(R.id.btn_ok)
        val title = dialV.findViewById<TextView>(R.id.txt_dialog_title)
        val msg = dialV.findViewById<TextView>(R.id.txt_dialog)
        title.text = mTitle
        msg.text = mMsg
        val box: AlertDialog = build.create()
        box.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        close.setOnClickListener { box.dismiss() }
        box.show()
    }
}