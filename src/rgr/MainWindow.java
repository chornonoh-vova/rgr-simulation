package rgr;

import process.Dispatcher;
import process.IModelFactory;
import rgr.model.Model;
import rnd.Erlang;
import rnd.Negexp;
import rnd.Norm;
import rnd.Uniform;
import widgets.ChooseData;
import widgets.ChooseRandom;
import widgets.Diagram;

import javax.swing.*;
import java.awt.event.*;

public class MainWindow {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;

    public ChooseRandom getContainerInterval() {
        return containerInterval;
    }

    public ChooseRandom getCustomsProductivity() {
        return customsProductivity;
    }

    public ChooseRandom getWorkerProductivity() {
        return workerProductivity;
    }

    public ChooseRandom getFlyTime() {
        return flyTime;
    }

    public ChooseRandom getTechnicalServiceTime() {
        return technicalServiceTime;
    }

    private ChooseRandom containerInterval;
    private ChooseRandom customsProductivity;
    private ChooseRandom workerProductivity;
    private ChooseRandom flyTime;
    private ChooseRandom technicalServiceTime;
    private ChooseData numCustoms;
    private ChooseData numCargo;
    private ChooseData numPlane;
    private ChooseData modellingTime;
    private JCheckBox consoleProtocol;
    private JButton start;
    public Diagram workingTeams;
    private Diagram customsContainers;
    private Diagram notLoadedContainers;
    private Diagram planeQueue;

    public Diagram getWorkingTeams() {
        return workingTeams;
    }

    public Diagram getCustomsContainers() {
        return customsContainers;
    }

    public Diagram getNotLoadedContainers() {
        return notLoadedContainers;
    }

    public Diagram getPlaneQueue() {
        return planeQueue;
    }

    public ChooseData getNumWorkingTeams() {

        return numWorkingTeams;
    }

    private ChooseData numWorkingTeams;

    public Diagram getTechnicalService() {
        return technicalService;
    }

    private Diagram technicalService;

    public MainWindow() {
        numWorkingTeams.setTitle("Кількість бригад");
        containerInterval.setTitle("Інтервал появи контейнерів");
        customsProductivity.setTitle("Продуктивність таможні");
        workerProductivity.setTitle("Продуктивність робітників");
        flyTime.setTitle("Час польоту");
        technicalServiceTime.setTitle("Час ТО");

        containerInterval.setRandom(new Negexp(1));
        customsProductivity.setRandom(new Norm(10, 2));
        workerProductivity.setRandom(new Erlang(1, 3));
        flyTime.setRandom(new Uniform(19, 25));
        technicalServiceTime.setRandom(new Uniform(16, 65));


        modellingTime.addActionListener(e -> {
            changeDiagramText();
        });

        start.addActionListener(e -> {
            testStart();
        });

        tabbedPane1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //super.mouseClicked(e);
                changeDiagramText();
            }
        });
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Моделювання авіаперевезень");
        frame.setContentPane(new MainWindow().panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void changeDiagramText() {
        if (workingTeams.isShowing()) {
            workingTeams.setHorizontalMaxText(modellingTime.getText());
        }
        if (customsContainers.isShowing()) {
            customsContainers.setHorizontalMaxText(modellingTime.getText());
        }
        if (notLoadedContainers.isShowing()) {
            notLoadedContainers.setHorizontalMaxText(modellingTime.getText());
        }
        if (planeQueue.isShowing()) {
            planeQueue.setHorizontalMaxText(modellingTime.getText());
        }
    }

    private void testStart() {
        workingTeams.clear();
        customsContainers.clear();
        notLoadedContainers.clear();
        planeQueue.clear();
        technicalService.clear();
        //Створюємо диспетчера
        Dispatcher dispatcher = new Dispatcher();
        //Створюємо модель за допомогою фабрики
        IModelFactory factory = (d)-> new Model(d, this);
        Model model =(Model) factory.createModel(dispatcher);
        // Робимо кнопку «Старт» недосяжною на період роботи моделі
        start.setEnabled(false);
        dispatcher.addDispatcherFinishListener(
                ()->start.setEnabled(true));
        //Готуємо модель до роботи у режимі тестування
        model.initForTest();
        //Запускаємо модель
        dispatcher.start();
    }

    public ChooseData getNumCustoms() {
        return numCustoms;
    }

    public ChooseData getNumCargo() {
        return numCargo;
    }

    public ChooseData getNumPlane() {
        return numPlane;
    }

    public ChooseData getModellingTime() {
        return modellingTime;
    }

    public JCheckBox getConsoleProtocol() {
        return consoleProtocol;
    }
}
