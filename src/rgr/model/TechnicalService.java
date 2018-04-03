package rgr.model;

import process.Actor;
import process.DispatcherFinishException;
import rgr.MainWindow;

import java.util.function.BooleanSupplier;

public class TechnicalService extends Actor {
    private String technicalService;
    private MainWindow mainWindow;
    private Model model;
    private double modellingTime;

    public TechnicalService(String technicalService, MainWindow mainWindow, Model model) {
        this.technicalService = technicalService;
        this.mainWindow = mainWindow;
        this.model = model;
        this.modellingTime = mainWindow.getModellingTime().getDouble();
    }

    @Override
    protected void rule() throws DispatcherFinishException {
        System.out.println(modellingTime);
        BooleanSupplier queueTONotEmpty = () -> model.getQueueTO().size() > 0;
        System.out.println(modellingTime);
        while (getDispatcher().getCurrentTime() <= modellingTime) {
            waitForCondition(queueTONotEmpty, "wait for planes");
            Plane plane = model.getQueueTO().removeFirst();
            holdForTime(mainWindow.getTechnicalServiceTime().next());
            plane.ready = true;
        }
    }
}
