package org.imesense.dynamicspawncontrol.core.script.syntax;

public final class CheckScript
{
    public static class MissingRequiredFieldException extends RuntimeException
    {
        public MissingRequiredFieldException(String message)
        {
            super(message);
        }
    }
}
