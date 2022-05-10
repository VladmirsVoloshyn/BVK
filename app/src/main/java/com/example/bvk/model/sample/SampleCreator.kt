package com.example.bvk.model.sample

import com.example.bvk.model.Mandrel
import com.example.bvk.model.MandrelProcessor

class SampleCreator {

    companion object{
        fun crate(inputMandrelsList : ArrayList<Mandrel>, vertexDiameter: Int, heightSoughtFor: Int) : ArrayList<Mandrel> {
            val mandrelsList = ArrayList<Mandrel>()
            for (mandrel in inputMandrelsList) {
                if (mandrel.vertexDiameter.toInt() == vertexDiameter) {
                    mandrelsList.add(
                        MandrelProcessor.calculateDataForMandrel(
                            mandrel,
                            heightSoughtFor
                        )
                    )
                }
            }
            return mandrelsList
        }
    }
}