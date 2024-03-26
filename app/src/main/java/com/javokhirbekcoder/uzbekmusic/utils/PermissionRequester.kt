package com.javokhirbekcoder.uzbekmusic.utils

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs

/*
Created by Javokhirbek on 13/03/2024 at 14:45
*/

class PermissionRequester {
    fun openAppSettings(context: Context) {
        Toast.makeText(context, "Go to settings and Enable All Permissions", Toast.LENGTH_SHORT)
            .show()

        val settingsIntent = Intent(Settings.ACTION_APPLICATION_SETTINGS)
        settingsIntent.data = Uri.parse("package:${context.packageName}")
        context.startActivity(settingsIntent)

    }

    fun warningPermissionDialog(context: Context, listener: DialogInterface.OnClickListener) {
        MaterialAlertDialogBuilder(context)
            .setMessage("All Permission are Required for this app")
            .setCancelable(false)
            .setPositiveButton("Ok", listener)
            .create()
            .show()
    }
}