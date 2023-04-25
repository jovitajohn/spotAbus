package com.jovita.spotabus.ui.home

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.jovita.spotabus.R
import com.jovita.spotabus.databinding.FragmentHomeBinding
import com.jovita.spotabus.ui.busdetail.ModelBottomSheetBusDetails
import java.io.IOException

class HomeFragment : Fragment() {

    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private var scannedValue = ""
    private var value: String = ""
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        if (ContextCompat.checkSelfPermission(
                requireActivity(), android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        } else {
            setupControls()
        }
        val aniSlide: Animation =
            AnimationUtils.loadAnimation(requireActivity(), R.anim.scanner_animation)
        binding.barcodeLine.startAnimation(aniSlide)

        //restarting camera and scan animation on refresh button press
        binding.btnRefresh.setOnClickListener(View.OnClickListener {
            value = ""
            cameraSource.start(binding.cameraSurfaceView.holder)
            binding.barcodeLine.startAnimation(aniSlide)
        })

        return root
    }

    private fun setupControls() { // QR/Bar code detection
        barcodeDetector =
            BarcodeDetector.Builder(activity).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(activity, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()

        binding.cameraSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    //Start preview after 1s delay
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            @SuppressLint("MissingPermission")
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })


        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(activity, "Scanner has been closed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
                    scannedValue = barcodes.valueAt(0).rawValue


                    //printing value must run on main thread
                    activity?.runOnUiThread {
                        if (scannedValue != value) {
                            value = scannedValue
                            Toast.makeText(
                                activity,
                                "value- $scannedValue",
                                Toast.LENGTH_SHORT
                            ).show()
                            showBottomSheet(scannedValue)
                            cameraSource.stop()
                            binding.barcodeLine.clearAnimation()

                        }

                    }

                } else {
                    // more bar codes detected
                }
            }
        })
    }

    fun showBottomSheet(name: String) {

        val modalBottomSheet = ModelBottomSheetBusDetails()
        val bundle = Bundle()
        bundle.putString("name", name)
        modalBottomSheet.arguments = bundle
        activity?.let {
            modalBottomSheet.show(
                it.supportFragmentManager,
                ModelBottomSheetBusDetails.TAG
            )
        }

        /*   activity.activity?.runOnUiThread(Runnable {
               val dialog = BottomSheetDialog(activity.requireContext())
               val view = activity.layoutInflater.inflate(R.layout.bottom_sheet_detail_view, null)
               val title = view.findViewById<TextView>(R.id.lbl_bus_stop)
               val fav = view.findViewById<ImageView>(R.id.img_fav)
               val direction = view.findViewById<TextView>(R.id.lbl_direction)
               val bus1 =view.findViewById<Button>(R.id.bus1)
               val bus2 =view.findViewById<Button>(R.id.bus2)
               val bus3 =view.findViewById<Button>(R.id.bus3)
               bus1.setOnClickListener {
                   var details = Intent(activity.context, ActivityBusDetail::class.java)
                   activity.startActivity(details)
               }
               title.text = name
               dialog.setCancelable(true)
               fav.setOnClickListener {
                   if (isFav) {
                       isFav = false
                       fav.setImageResource(R.drawable.ic_fav_outline)
                   } else {
                       isFav = true
                       fav.setImageResource(R.drawable.ic_fav_fill)
                   }
               }
               dialog.setOnCancelListener {
                   stopName = ""
               }
               dialog.setContentView(view)
               dialog.show()
           })*/


    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupControls()
            } else {
                Toast.makeText(activity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (cameraSource != null) {
            cameraSource.stop()
        }
        _binding = null
    }
}