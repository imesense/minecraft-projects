package org.imesense.dynamicspawncontrol.technical.customlibrary.registrationhelpers;

@FunctionalInterface
public interface IProcedure
{
    void run();

    default IProcedure andThen(IProcedure after)
    {
        return () ->
        {
            this.run();
            after.run();
        };
    }

    default IProcedure compose(IProcedure before)
    {
        return () ->
        {
            before.run();
            this.run();
        };
    }
}
