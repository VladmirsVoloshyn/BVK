package com.example.bvk.model.sample

import com.example.bvk.model.Mandrel
import com.example.bvk.model.MandrelProcessor

class SampleCreator {

    companion object{
        fun crate(inputMandrelsList : ArrayList<Mandrel>, sampleCapParameters: SampleCapParameters) : ArrayList<Mandrel> {
            val mandrelsList = ArrayList<Mandrel>()
            for (mandrel in inputMandrelsList) {
                if (mandrel.mandrelName.substring(0..1).toInt() == sampleCapParameters.capVertexDiameter) {
                    mandrelsList.add(
                        MandrelProcessor.calculateDataForMandrel(
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