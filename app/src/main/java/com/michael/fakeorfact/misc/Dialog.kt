package com.michael.fakeorfact.misc

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.michael.fakeorfact.Egg
import com.michael.fakeorfact.MainActivity
import com.michael.fakeorfact.R


class Dialog {
    // Basic Template Dialog Box
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
    // Dialog Box for About Icon w/ Easter Egg
    fun showAboutDialog(mFrom: Context){
        val build = AlertDialog.Builder(mFrom)
        val inflater = LayoutInflater.from(mFrom)
        val count = intArrayOf(0)
        val dialogV = inflater.inflate(R.layout.dialog_about_view, null)
        build.setView(dialogV)
        // Show dialog box
        val dialog = build.create()
        val close = dialogV.findViewById<Button>(R.id.btn_ok)
        val secret = dialogV.findViewById<TextView>(R.id.txt_dialog3)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        close.setOnClickListener {
            dialog.dismiss()
            count[0] = 0
        }
        secret.setOnClickListener {
            if (count[0] == 5){
                secret.isClickable = false
                mFrom.startActivity(Intent(mFrom, Egg::class.java))
            }
            else{
                Toast.makeText(mFrom, "You have " + (5 - count[0]) + " left",
                        Toast.LENGTH_SHORT).show()
                count[0]++
            }
        }
        dialog.show()
    }
    // Back Button Pressed Dialog. Leave if "Yes" is clicked
    fun showLeavingDialogBox(mFrom: Context){
        val builder = AlertDialog.Builder(mFrom)
        val inflater = LayoutInflater.from(mFrom)
        val dialV = inflater.inflate(R.layout.leave_view, null)
        builder.setView(dialV)
        val yes: Button = dialV.findViewById(R.id.btn_yes)
        val no: Button = dialV.findViewById(R.id.btn_no)
        val dialog: AlertDialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.show()
        Log.d(mFrom.javaClass.name, "back button pressed")
        yes.setOnClickListener {
            val intent = Intent(mFrom, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            mFrom.startActivity(intent)
        }
        no.setOnClickListener { dialog.dismiss() }
    }
}