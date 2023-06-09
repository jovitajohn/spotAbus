/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jovita.mycustomarimagelabeling.files

import android.opengl.GLSurfaceView
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.jovita.spotabus.R
import com.jovita.spotabus.common.SnackbarHelper
import com.jovita.spotabus.common.samplerender.SampleRender
import com.jovita.spotabus.ui.ar.ArFragment

/** Wraps [R.layout.activity_main] and controls lifecycle operations for [GLSurfaceView]. */
class ArFragmentView(val _officeFrament: ArFragment, renderer: AppRenderer) :
    DefaultLifecycleObserver {
    val root = View.inflate(_officeFrament.activity, R.layout.fragment_ar, null)
    val surfaceView =
        root.findViewById<GLSurfaceView>(R.id.surfaceview).apply {
            SampleRender(this, renderer, _officeFrament.activity?.assets)
        }
    val scanButton = root.findViewById<AppCompatButton>(R.id.scanButton)
    val resetButton = root.findViewById<AppCompatButton>(R.id.clearButton)
    val snackbarHelper =
        SnackbarHelper().apply {
            setParentView(root.findViewById(R.id.coordinatorLayout))
            setMaxLines(6)
        }

    override fun onResume(owner: LifecycleOwner) {
        surfaceView.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        surfaceView.onPause()
    }

    fun post(action: Runnable) = root.post(action)

    /** Toggles the scan button depending on if scanning is in progress. */
    fun setScanningActive(active: Boolean) =
        when (active) {
            true -> {
                scanButton.isEnabled = false
                scanButton.setText(_officeFrament.getString(R.string.scan_busy))
            }
            false -> {
                scanButton.isEnabled = true
                scanButton.setText(_officeFrament.getString(R.string.scan_available))
            }
        }
}
