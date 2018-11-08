package com.snt.phoney.ui.dating.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.snt.phoney.R

/**
 * A fragment representing a list of Items.
 */
class OthersDatingFragment : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_othersdating_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager =   LinearLayoutManager(context)

                adapter = OthersDatingRecyclerViewAdapter( )
            }
        }
        return view
    }



    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = DatingListFragment().apply {
            this.arguments = arguments
        }
    }
}
