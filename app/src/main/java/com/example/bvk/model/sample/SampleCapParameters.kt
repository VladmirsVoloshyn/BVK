package com.example.bvk.model.sample

data class SampleCapParameters (val capVertexDiameter: Int, val capHeight : Int){
    override fun toString(): String {
        return "V=$capVertexDiameter, H=$capHeight"
    }
}
