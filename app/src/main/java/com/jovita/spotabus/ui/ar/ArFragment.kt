package com.jovita.spotabus.ui.ar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.ar.core.CameraConfig
import com.google.ar.core.CameraConfigFilter
import com.google.ar.core.Config
import com.google.ar.core.exceptions.*
import com.jovita.mycustomarimagelabeling.files.ARCoreSessionLifecycleHelper
import com.jovita.mycustomarimagelabeling.files.AppRenderer
import com.jovita.mycustomarimagelabeling.files.ArFragmentView
import com.jovita.spotabus.databinding.FragmentArBinding

class ArFragment : Fragment() {

    private var _binding: FragmentArBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val TAG = "Ar View"
    lateinit var arCoreSessionHelper: ARCoreSessionLifecycleHelper
    lateinit var renderer: AppRenderer
    lateinit var view: ArFragmentView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       /* val OfficeViewModel =
            ViewModelProvider(this).get(OfficeViewModel::class.java)

        _binding = FragmentOfficeBinding.inflate(inflater, container, false)
        val root: View = binding.root*/

       /* val textView: TextView = binding.textGallery
        OfficeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
*/
        arCoreSessionHelper = ARCoreSessionLifecycleHelper(this.requireActivity())
        // When session creation or session.resume fails, we display a message and log detailed
        // information.
        arCoreSessionHelper.exceptionCallback =
            { exception ->
                val message =
                    when (exception) {
                        is UnavailableArcoreNotInstalledException,
                        is UnavailableUserDeclinedInstallationException -> "Please install ARCore"
                        is UnavailableApkTooOldException -> "Please update ARCore"
                        is UnavailableSdkTooOldException -> "Please update this app"
                        is UnavailableDeviceNotCompatibleException -> "This device does not support AR"
                        is CameraNotAvailableException -> "Camera not available. Try restarting the app."
                        else -> "Failed to create AR session: $exception"
                    }
                Log.e(TAG, message, exception)
                Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
            }

        arCoreSessionHelper.beforeSessionResume =
            { session ->
                session.configure(
                    session.config.apply {
                        // To get the best image of the object in question, enable autofocus.
                        focusMode = Config.FocusMode.AUTO
                        if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                            depthMode = Config.DepthMode.AUTOMATIC
                        }
                    }
                )

                val filter =
                    CameraConfigFilter(session).setFacingDirection(CameraConfig.FacingDirection.BACK)
                val configs = session.getSupportedCameraConfigs(filter)
                val sort =
                    compareByDescending<CameraConfig> { it.imageSize.width }.thenByDescending {
                        it.imageSize.height
                    }
                session.cameraConfig = configs.sortedWith(sort)[0]
            }
        lifecycle.addObserver(arCoreSessionHelper)

        renderer = AppRenderer(this)
        lifecycle.addObserver(renderer)
        view = ArFragmentView(this, renderer)
        renderer.bindView(view)
        lifecycle.addObserver(view)

        return view.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}