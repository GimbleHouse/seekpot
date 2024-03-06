package com.gimble.seekpot.ui.frags

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.gimble.seekpot.databinding.FragmentFoundItemBinding
import com.gimble.seekpot.feature.enquiry.domain.model.FoundItemData
import com.gimble.seekpot.feature.enquiry.presentation.EnquiryAdapter
import com.gimble.seekpot.feature.enquiry.presentation.ProgShowOne
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FoundItemFrag : Fragment() {
    private lateinit var fireref : DatabaseReference
    private lateinit var itemlist: ArrayList<FoundItemData>
    private lateinit var binding : FragmentFoundItemBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoundItemBinding.inflate(inflater,container,false)
        setUp()
        return binding.root
    }

    private fun setUp() {
        binding.recycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
        }
        fireref = FirebaseDatabase.getInstance().getReference("founditems")
        itemlist = arrayListOf()
       fetchDataForFound()
    }


    private fun fetchDataForFound() {
        val sendingDialog = ProgShowOne()
        sendingDialog.show(requireActivity().supportFragmentManager, "sendingDialog")

        fireref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemlistB = mutableListOf<FoundItemData>()
                for (contactSnap in snapshot.children) {
                    val item = contactSnap.getValue(FoundItemData::class.java)
                    item?.let {

                        itemlistB.add(it)
                    }
                }
                sendingDialog.dismiss()
                val adapters = EnquiryAdapter(itemlistB, requireContext())
                binding.recycler.adapter = adapters
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                sendingDialog.dismiss()
            }
        })

}
}