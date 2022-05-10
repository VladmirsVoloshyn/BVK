package com.example.bvk.model.sample

data class SampleCapParameters (var capVertexDiameter: Int, var capHeight : Int){
    override fun toString(): String {
        return "V=$capVertexDiameter, H=$capHeight"
    }
}
