package com.cs.architecture.jetpack.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cs.architecture.R
import kotlinx.android.synthetic.main.fragment_a.*


class FragmentA : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_a, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnNext.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("name", "chensen")
            findNavController().navigate(R.id.action_fragmentA_to_fragmentB, bundle)
        }
    }
}
