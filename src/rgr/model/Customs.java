package rgr.model;

import process.Actor;
import process.DispatcherFinishException;
import rgr.MainWindow;
import widgets.ChooseRandom;

import java.util.function.BooleanSupplier;

public class Customs extends Actor {
    private String customs;
    private MainWindow mainWindow;
    private Model model;
    private double modellingTime;
    private ChooseRandom customsProductivity;

    public Customs(String customs, MainWindow mainWindow, Model model) {
        this.customs = customs;
        this.mainWindow = mainWindow;
        this.model = model;
        this.modellingTime = mainWindow.getModellingTime().getDouble();
        this.customsProductivity = mainWindow.getCustomsProductivity();
    }

    @Override
    protected void rule() throws DispatcherFinishException {
        BooleanSupplier queueSize = () -> model.getQueueCustomsContainers().size() > 0;
        while (getDispatcher().getCurrentTime() <= modellingTime) {
            waitForCondition(queueSize, "waiting for container to appear");
            model.getQueueCustomsContainers().removeFirst();
            holdForTime(customsProductivity.next());
            model.getQueueNotLoadedContainers().add(getDispatcher().getCurrentTime());
        }
    }
}
