package rgr.model;

import process.Actor;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import rgr.MainWindow;
import widgets.ChooseRandom;

import java.util.function.BooleanSupplier;

public class WorkingTeams extends Actor {
    private String workingTeams;
    private MainWindow mainWindow;
    private Model model;
    private QueueForTransactions<Double> queueNotLoaded;
    private QueueForTransactions<Plane> queuePlane;
    private ChooseRandom workersProductivity;
    private double modellingTime;

    public WorkingTeams(String workingTeams, MainWindow mainWindow, Model model) {
        this.workingTeams = workingTeams;
        this.mainWindow = mainWindow;
        this.model = model;
        this.modellingTime = mainWindow.getModellingTime().getDouble();
        this.queueNotLoaded = this.model.getQueueNotLoadedContainers();
        this.queuePlane = this.model.getQueuePlanes();
        this.workersProductivity = this.mainWindow.getWorkerProductivity();
    }

    @Override
    protected void rule() throws DispatcherFinishException {
        BooleanSupplier queueSizePlane = () -> model.getQueuePlanes().size() > 0;
        BooleanSupplier queueSizeContainer = () -> model.getQueueNotLoadedContainers().size() > 0;
        while (getDispatcher().getCurrentTime() <= modellingTime) {
            waitForCondition(queueSizePlane, "wait for plane");
            Plane plane = queuePlane.removeFirst();
            while (plane.isFull()) {
                waitForCondition(queueSizeContainer, "waiting for container");
                model.getQueueWorkingTeams().add(this);
                double container = model.getQueueNotLoadedContainers().removeFirst();
                holdForTime(mainWindow.getWorkerProductivity().next());
                plane.getContainerList().add(container);
                model.getQueueWorkingTeams().remove(this);
            }
        }
    }
}
