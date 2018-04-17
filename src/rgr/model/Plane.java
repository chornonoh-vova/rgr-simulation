package rgr.model;

import process.Actor;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import rgr.MainWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class Plane extends Actor {
    private String plane;
    private MainWindow mainWindow;
    private Model model;
    boolean ready;

    public List<Double> getContainerList() {
        return containerList;
    }

    public void setContainerList(List<Double> containerList) {
        this.containerList = containerList;
    }

    private List<Double> containerList = new ArrayList<>();

    private int size = 10;
    private double modellingTime;
    private QueueForTransactions<Plane> queuePlane;

    public Plane(String plane, MainWindow mainWindow, Model model) {
        this.mainWindow = mainWindow;
        this.model = model;
        this.modellingTime = mainWindow.getModellingTime().getDouble();
        this.queuePlane = model.getQueuePlanes();
        this.setHistoForActorWaitingTime(model.getHistoPlaneWait());
    }

    @Override
    protected void rule() throws DispatcherFinishException {
        BooleanSupplier full = this::isFull;
        BooleanSupplier ready = this::isReady;
        while (getDispatcher().getCurrentTime() <= modellingTime) {
            queuePlane.add(this);
            waitForCondition(full, "****************\nplane loading...");
            getDispatcher().printToProtocol("plane starts");
            holdForTime(mainWindow.getFlyTime().next());
            //визначає час обслуговування
            getDispatcher().printToProtocol("*********************\nрозвантажується");
            while (!containerList.isEmpty()) {
                double container = containerList.remove(0);
                double serviceTime = dispatcher.getCurrentTime() - container;
//                holdForTime(serviceTime);
                //TODO: add to histo
            }
            holdForTime(mainWindow.getFlyTime().next());
            getDispatcher().printToProtocol("******************\nТО");
            this.ready = false;
            model.getQueueTO().add(this);
            waitForCondition(ready, "TO");
        }
    }

    public boolean isFull() {
        return containerList.size() < size;
    }

    public boolean isReady() {
        return ready;
    }
}
