package org.imesense.dynamicspawncontrol.core.script.initializer;

import org.imesense.dynamicspawncontrol.core.debug.Timer;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.actioncollector.*;

public class ConceptScriptProcessor
{
    public ConceptScriptProcessor()
    {
        Timer timer = new Timer();

        timer.start();
        Equip equip = Equip.getInstance();
        double equipTime = timer.stop();
        Log.writeDataToLogFile(0, "Create Action Collector object 'Equip': " + equip.hashCode() + " (Time: " + equipTime + " ms)");

        timer.start();
        Potion potion = Potion.getInstance();
        double potionTime = timer.stop();
        Log.writeDataToLogFile(0, "Create Action Collector object 'Potion': " + potion.hashCode() + " (Time: " + potionTime + " ms)");

        timer.start();
        Priority priority = Priority.getInstance();
        double priorityTime = timer.stop();
        Log.writeDataToLogFile(0, "Create Action Collector object 'Priority': " + priority.hashCode() + " (Time: " + priorityTime + " ms)");

        timer.start();
        CommandNBT commandNBT = CommandNBT.getInstance();
        double commandNBTTime = timer.stop();
        Log.writeDataToLogFile(0, "Create Action Collector object 'CommandNBT': " + commandNBT.hashCode() + " (Time: " + commandNBTTime + " ms)");

        timer.start();
        World world = World.getInstance();
        double worldTime = timer.stop();
        Log.writeDataToLogFile(0, "Create Action Collector object 'World': " + world.hashCode() + " (Time: " + worldTime + " ms)");
    }
}
