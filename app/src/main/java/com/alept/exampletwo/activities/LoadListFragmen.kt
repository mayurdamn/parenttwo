package com.alept.exampletwo.activities

import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.alept.exampletwo.adapter.AllAppsAdapter
import com.alept.exampletwo.databinding.LoadlistFragmentBinding
import com.alept.exampletwo.util.runOnUiThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread


class LoadListFragmen :Fragment() {
    lateinit var binding: LoadlistFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoadlistFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.allAppRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        GlobalScope.launch(Dispatchers.IO) {
            val strings = ArrayList<String>()
            val drawables = ArrayList<Drawable>()
            val packages = ArrayList<String>()
            var i = 0
            val apps: List<ApplicationInfo> = requireContext().packageManager.getInstalledApplications(0)
            for (app in apps) {
                if (requireContext().packageManager
                        .getLaunchIntentForPackage(app.packageName) != null
                ) {
                    strings.add(i, requireContext().packageManager.getApplicationLabel(app).toString())
                    drawables.add(i,requireContext(). packageManager.getApplicationIcon(app))
                    packages.add(app.packageName)
                    i++
                }
            }
            // making three arrays for passing to the adapter
            for (i1 in strings.indices) for (j1 in i1 + 1 until strings.size) {
                var tempDrw: Drawable
                var tempPck: String
                var tempNm: String
                if (strings[i1].compareTo(strings[j1], ignoreCase = true) > 0) {
                    tempNm = strings[i1]
                    tempDrw = drawables[i1]
                    tempPck = packages[i1]
                    strings[i1] = strings[j1]
                    drawables[i1] = drawables[j1]
                    packages[i1] = packages[j1]
                    strings[j1] = tempNm
                    drawables[j1] = tempDrw
                    packages[j1] = tempPck
                }
            }
            launch(Dispatchers.Main) {
                binding.allAppRecyclerView.adapter =
                    AllAppsAdapter(requireContext(), strings, drawables, packages)
            }
        }

    }
}