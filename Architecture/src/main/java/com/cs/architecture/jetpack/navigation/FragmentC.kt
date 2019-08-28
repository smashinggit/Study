package com.cs.architecture.jetpack.navigation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cs.architecture.R
import kotlinx.android.synthetic.main.fragment_c.*


class FragmentC : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_c, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnPre.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentC_to_fragmentA)
        }
    }
}
