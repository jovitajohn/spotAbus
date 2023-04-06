package com.jovita.spotabus.ui.busdetail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jovita.mycustomarimagelabeling.files.AppRenderer
import com.jovita.spotabus.R

class ModelBottomSheetBusDetails : BottomSheetDialogFragment() {
    var isFav: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val name = arguments?.getString("name")
        var view = inflater.inflate(R.layout.bottom_sheet_detail_view, container, false)
        val title = view.findViewById<TextView>(R.id.lbl_bus_stop)
        val fav = view.findViewById<ImageView>(R.id.img_fav)
        val direction = view.findViewById<TextView>(R.id.lbl_direction)
        val bus1 = view.findViewById<Button>(R.id.bus1)
        val bus2 = view.findViewById<Button>(R.id.bus2)
        val bus3 = view.findViewById<Button>(R.id.bus3)
        bus1.setOnClickListener {
            var details = Intent(this.context, ActivityBusDetail::class.java)
            startActivity(details)
        }
        title.text = name
        fav.setOnClickListener {
            if (isFav) {
                isFav = false
                fav.setImageResource(R.drawable.ic_fav_outline)
            } else {
                isFav = true
                fav.setImageResource(R.drawable.ic_fav_fill)
            }
        }
        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // Do something for new state.
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Do something for slide offset.
            }
        }

        // To add the callback:
       // bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}