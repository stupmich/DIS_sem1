package Observer;

import SimulationClasses.SimulationCore;

public interface ISimDelegate {
    // Metóda určená pre aktualizáciu GUI
    void refresh(SimulationCore simulation);
}
