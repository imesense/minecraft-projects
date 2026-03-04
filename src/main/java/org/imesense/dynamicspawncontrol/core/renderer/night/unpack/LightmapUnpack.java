package org.imesense.dynamicspawncontrol.core.renderer.night.unpack;

public abstract class LightmapUnpack
{
    public static int unpackSkyIndex(int packedLight)
    {
        return packedLight >> 4;
    }

    public static int unpackBlockIndex(int packedLight)
    {
        return packedLight & 15;
    }
}
