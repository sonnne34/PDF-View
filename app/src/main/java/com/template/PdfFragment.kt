package com.template

import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.template.databinding.FragmentPdfBinding
import java.io.IOException


class PdfFragment : Fragment() {

    private val listAsset: ArrayList<String> = ArrayList()
    private var pos: Int = 0
    private var lastPositionListAsset: Int = 0

    private var _binding: FragmentPdfBinding? = null
    private val binding: FragmentPdfBinding
        get() = _binding ?: throw RuntimeException("FragmentPdfBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPdfBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAssetList()
        lastPositionListAsset = listAsset.size - 1
        checkingFileAssets()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.aboutUsFragment -> {
                findNavController().navigate(R.id.action_pdfFragment_to_aboutUsFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkingFileAssets() {
        if (lastPositionListAsset <= 0) {
            Toast.makeText(requireActivity(), "Error: no files", Toast.LENGTH_LONG).show()
        } else {
            openPdf(pos)
            onClickListeners()
        }
    }

    private fun onClickListeners() {
        binding.buttonBack.setOnClickListener {
            if (pos > 0) {
                pos--
            } else {
                pos = lastPositionListAsset
            }
            openPdf(pos)
        }
        binding.buttonNext.setOnClickListener {
            if (pos < lastPositionListAsset) {
                pos++
            } else {
                pos = 0
            }
            openPdf(pos)
        }
    }

    private fun openPdf(position: Int) {
        binding.pdfView.fromAsset(getPdfNameFromAssets(position))
            .defaultPage(1)
            .showMinimap(false)
            .enableSwipe(true)
            .swipeVertical(true)
            .load()

    }

    private fun getPdfNameFromAssets(position: Int): String {
        return listAsset[position]
    }

    private fun getAssetList() {
        val mgr: AssetManager = requireActivity().assets
        displayFiles(mgr, "")
    }

    private fun displayFiles(mgr: AssetManager, path: String) {
        try {
            val list = mgr.list(path)
            if (list != null) for (i in list.indices) {
                Log.v("Assets:", path + "/" + list[i])
                displayFiles(mgr, path + "/" + list[i])
                if (list[i].endsWith(".pdf")) {
                    listAsset.add(list[i])
                }
            }
        } catch (e: IOException) {
            Log.v("List error:", "can't list$path")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}