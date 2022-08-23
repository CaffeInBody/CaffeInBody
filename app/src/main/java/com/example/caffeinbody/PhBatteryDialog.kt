package com.example.caffeinbody

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.DialogFragment


class PhBatteryDialog : DialogFragment() {
    override fun onCreateDialog(a_savedInstanceState: Bundle?): Dialog {
        val yesListener: DialogInterface.OnClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                //val bundle: Bundle = getArguments() ?: return@OnClickListener
                val intent: Intent
                intent = selfIntent
                startActivity(intent)
            }
        val noListener: DialogInterface.OnClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                this.dismiss()
                System.exit(0)
            }
        val message = "스마트워치의 실시간 카페인 데이터 확인을 위해 \"배터리 사용량 최적화\" 목록에서 \"제외\"해야 합니다."
        val builder: AlertDialog.Builder = AlertDialog.Builder(getActivity())
        builder.setMessage(message)
            .setPositiveButton("네", yesListener)
            .setNegativeButton("아니오", noListener)
        return builder.create()
    }

    /*private val permissionIntent: Intent
        private get() {
            val intent = Intent()
            intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
            return intent
        }*/
    private val selfIntent: Intent//직접등록
        private get() {
        Log.e("Dialog", "self")
            val intent = Intent()
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.parse("package:" + requireContext().getPackageName())
            return intent
        }
}