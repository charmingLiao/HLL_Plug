package com.plug.hll

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.plug.hll.view.TrackerWindowManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


    }

    fun onViewClicked(view: View) {
        when (view) {
            btn_aces -> startActivityForResult(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 0x11)
            btn_open -> {
                if (AccessibilityHelper.isAccessibilitySettingsOn(this, AcesService::class.java)) {
                    startActivity(Intent().apply { component = ComponentName(HLL_PKG, HLL_CLZ) })
                } else {
                    Toast.makeText(this, "请先打开无障碍服务", Toast.LENGTH_LONG).show()
                }
            }
            btn_add -> {
                if (!Settings.canDrawOverlays(this)) {
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:$packageName"))
                    startActivityForResult(intent, 10)
                } else {
                    TrackerWindowManager.getInstance(this).addView()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        btn_aces.text = if (AccessibilityHelper.isAccessibilitySettingsOn(this, AcesService::class.java)) "关闭无障碍服务" else "开启无障碍服务"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            TrackerWindowManager.getInstance(this).addView()
        }
    }

}
