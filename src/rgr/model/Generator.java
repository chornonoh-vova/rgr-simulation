package rgr.model;

import process.Actor;
import process.DispatcherFinishException;
import rgr.MainWindow;
import widgets.ChooseRandom;

public class Generator extends Actor {
    private String name;
    private MainWindow mainWindow;
    private Model model;
    private double modellingTime;
    private ChooseRandom containerInterval;

    public Generator(String name, MainWindow mainWindow, Model model) {
        this.name = name;
        this.mainWindow = mainWindow;
        this.model = model;
        this.modellingTime = mainWindow.getModellingTime().getDouble();
        this.containerInterval = mainWindow.getContainerInterval();
    }

    @Override
    protected void rule() throws DispatcherFinishException {
        while (getDispatcher().getCurrentTime() <= modellingTime) {
            holdForTime(containerInterval.next());
            model.getQueueCustomsContainers().add(getDispatcher().getCurrentTime());
            getDispatcher().printToProtocol(" " + getNameForProtocol() + " creates transaction");
        }
    }

    public void setFinishTime(double time) {
        modellingTime = time;
    }
}
