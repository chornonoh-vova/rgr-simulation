package rgr.model;

import process.Dispatcher;
import process.MultiActor;
import process.QueueForTransactions;
import rgr.MainWindow;
import stat.DiscretHisto;

public class Model {
    private Dispatcher dispatcher;
    //Посилання на візуальну частину
    private MainWindow mainWindow;

    ////////Актори\\\\\\\\\
    // Генератор транзакцій
    private Generator generator;
    // Митниця
    private Customs customs;
    private MultiActor multiCustom;
    // Бригади вантажників
    private WorkingTeams workingTeams;
    private MultiActor multiWorkingTeams;
    // Літаки
    private Plane plane;
    private MultiActor multiPlane;
    // TO
    private TechnicalService technicalService;

    private QueueForTransactions<WorkingTeams> queueWorkingTeams;
    private QueueForTransactions<Double> queueCustomsContainers;
    private QueueForTransactions<Double> queueNotLoadedContainers;
    private QueueForTransactions<Plane> queuePlanes;

    private DiscretHisto workingTeamsHisto;
    private DiscretHisto customsContainersHisto;
    private DiscretHisto notLoadedContainersHisto;
    private DiscretHisto planeQueueHisto;

    public Model(Dispatcher dispatcher, MainWindow mainWindow) {
        this.dispatcher = dispatcher;
        this.mainWindow = mainWindow;
        componentsToStartList();
    }

    private void componentsToStartList() {
        dispatcher.addStartingActor(getGenerator());
        dispatcher.addStartingActor(getMultiCustom());
        dispatcher.addStartingActor(getMultiWorkingTeams());
        dispatcher.addStartingActor(getMultiPlane());
    }

    public Generator getGenerator() {
        if (generator == null) {
            generator = new Generator("Generator", this.mainWindow, this);
        }
        return generator;
    }

    public QueueForTransactions<WorkingTeams> getQueueWorkingTeams() {
        if (queueWorkingTeams == null) {
            queueWorkingTeams = new QueueForTransactions<>("QueueWorkingTeams", dispatcher, getHistoNotLoadedQueue());
        }
        return queueWorkingTeams;
    }

    public QueueForTransactions<Double> getQueueCustomsContainers() {
        if (queueCustomsContainers == null) {
            queueCustomsContainers = new QueueForTransactions<>("QueueCustomsContainers", dispatcher, getHistoNotLoadedQueue());
        }
        return queueCustomsContainers;
    }

    public QueueForTransactions<Double> getQueueNotLoadedContainers() {
        if (queueNotLoadedContainers == null) {
            queueNotLoadedContainers = new QueueForTransactions<>("QueueNotLoaded", dispatcher, getHistoNotLoadedQueue());
        }
        return queueNotLoadedContainers;
    }

    private DiscretHisto getHistoNotLoadedQueue() {
        return null;
    }

    public QueueForTransactions<Plane> getQueuePlanes() {
        if (queuePlanes == null) {
            queuePlanes = new QueueForTransactions<>("QueuePlane", dispatcher, getHistoPlaneQueue());
        }
        return queuePlanes;
    }

    private DiscretHisto getHistoPlaneQueue() {
        return null;
    }

    public Customs getCustoms() {
        if (customs == null) {
            customs = new Customs("Customs", this.mainWindow, this);
//            customs.setHistoForActorWaitingTime(customsContainersHisto);
        }
        return customs;
    }

    public MultiActor getMultiCustom() {
        if (multiCustom == null) {
            multiCustom = new MultiActor();
            multiCustom.setNameForProtocol("MultiCustoms for protocol");
            multiCustom.setOriginal(getCustoms());
            multiCustom.setNumberOfClones(mainWindow.getNumCustoms().getInt());
        }
        return multiCustom;
    }

    public WorkingTeams getWorkingTeams() {
        if (workingTeams == null) {
            workingTeams = new WorkingTeams("WorkingTeams", this.mainWindow, this);
        }
        return workingTeams;
    }

    public MultiActor getMultiWorkingTeams() {
        if (multiWorkingTeams == null) {
            multiWorkingTeams = new MultiActor();
            multiWorkingTeams.setNameForProtocol("MultiWorkingTeams for protocol");
            multiWorkingTeams.setOriginal(getWorkingTeams());
            multiWorkingTeams.setNumberOfClones(this.mainWindow.getNumWorkingTeams().getInt());
        }
        return multiWorkingTeams;
    }

    public Plane getPlane() {
        if (plane == null) {
            plane = new Plane("Plane", this.mainWindow, this);
        }
        return plane;
    }

    public MultiActor getMultiPlane() {
        if (multiPlane == null) {
            multiPlane = new MultiActor();
            multiPlane.setNameForProtocol("MultiPlane for protocol");
            multiPlane.setOriginal(getPlane());
            multiPlane.setNumberOfClones(this.mainWindow.getNumPlane().getInt());
        }
        return multiPlane;
    }

    public TechnicalService getTechnicalService() {
        if (technicalService == null) {
            technicalService = new TechnicalService("TechnicalService", this.mainWindow, this);
        }
        return technicalService;
    }

    public void initForTest() {
        getQueueCustomsContainers().setPainter(this.mainWindow.getCustomsContainers().getPainter());
        getQueueNotLoadedContainers().setPainter(this.mainWindow.getNotLoadedContainers().getPainter());
        getQueueWorkingTeams().setPainter(this.mainWindow.getWorkingTeams().getPainter());
        getQueuePlanes().setPainter(this.mainWindow.getPlaneQueue().getPainter());

        if (this.mainWindow.getConsoleProtocol().isSelected()) {
            dispatcher.setProtocolFileName("Console");
        } else {
            dispatcher.setProtocolFileName("");
        }
    }
}
