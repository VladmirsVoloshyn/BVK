package com.example.bvk.model.sample

import com.example.bvk.model.Mandrel
import com.example.bvk.model.MandrelParametersCalculator

class SampleCreator {

    companion object{
        fun crate(inputMandrelsList : ArrayList<Mandrel>, sampleCapParameters: SampleCapParameters) : ArrayList<Mandrel> {
            val mandrelsList = ArrayList<Mandrel>()
            for (mandrel in inputMandrelsList) {
                if (mandrel.mandrelName.substring(0..1) == sampleCapParameters.capVertexDiameter.toString()) {
                    mandrelsList.add(
                        MandrelParametersCalculator.calculateDataForMandrel(
                            mandrel,
                            sampleCapParameters
                        )
                    )
                }
            }
            return mandrelsList
        }
    }
}