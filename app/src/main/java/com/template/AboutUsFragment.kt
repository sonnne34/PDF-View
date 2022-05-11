package com.template

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.template.databinding.FragmentAboutUsBinding
import java.io.File
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

class AboutUsFragment : Fragment() {

    private var _binding: FragmentAboutUsBinding? = null
    private val binding: FragmentAboutUsBinding
        get() = _binding ?: throw RuntimeException("FragmentAboutUsBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val versionName = getAppVersion(requireActivity())
        binding.tvAboutVersionApp.text = "version $versionName"

        val weightApp = readableFileSize(getApkSize(requireActivity()))
        binding.tvAboutWeightApp.text = "Weight App $weightApp"
    }

    private fun getAppVersion(context: Context): String {
        var version = ""
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return version
    }

    private fun getApkSize(context: Context): Long {
        var size: Long = 0
        try {
            size = File(
                context.packageManager.getApplicationInfo(
                    requireActivity().packageName!!,
                    0
                ).publicSourceDir
            ).length()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return size
    }

    private fun readableFileSize(size: Long): String {
        if (size <= 0) return "0"
        val units = arrayOf(
            "B",
            "kB",
            "MB",
            "GB",
            "TB"
        )
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble()))
            .toString() + " " + units[digitGroups]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}