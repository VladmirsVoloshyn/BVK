package com.example.bvk.model.sample

import android.content.Context
import com.example.bvk.model.Mandrel
import com.example.bvk.model.MandrelParametersCalculator

class SampleCreator(val context: Context) {

    fun crate(
        inputMandrelsList: ArrayList<Mandrel>,
        sampleCapParameters: SampleCapParameters
    ): ArrayList<Mandrel> {
        val mpc = MandrelParametersCalculator(context)
        val mandrelsList = ArrayList<Mandrel>()
        for (mandrel in inputMandrelsList) {
            if (mandrel.mandrelName.substring(0..1) == sampleCapParameters.capVertexDiameter.toString()) {
                mandrelsList.add(
                    mpc.calculateDataForMandrel(
                        mandrel,
                        sampleCapParameters
                    )
                )
            }
        }
        return mandrelsList
    }

}