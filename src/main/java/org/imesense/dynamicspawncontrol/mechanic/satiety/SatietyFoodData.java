package org.imesense.dynamicspawncontrol.mechanic.satiety;

public class SatietyFoodData
{
    private final int satiety;
    private final int spanTime;

    public SatietyFoodData(int satiety, int spanTime)
    {
        this.satiety = satiety;
        this.spanTime = Math.max(1, spanTime);
    }

    public int getSatiety()
    {
        return satiety;
    }

    public int getSpanTime()
    {
        return spanTime;
    }

    public boolean isPositive()
    {
        return satiety > 0;
    }
}
