package mic.projectjavafxml.sample;

import mic.projectjavafxml.backend.CPU;
import mic.projectjavafxml.backend.CodeParser;
import mic.projectjavafxml.backend.CodeParserException;
import mic.projectjavafxml.backend.FileParser;
import mic.projectjavafxml.backend.InstructionParser;
import mic.projectjavafxml.backend.MemoryLine;
import mic.projectjavafxml.backend.NumericFactory;
import mic.projectjavafxml.backend.ObservableResourceFactory;
import mic.projectjavafxml.backend.Register;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class Controller {
    public TableView<Map<String, String>> supportedInstructionsTable;
    public TableColumn<Map, String> instrMnemonic;
    public TableColumn<Map, String> instrInstruction;
    public TableColumn<Map, String> instrMeaning;
    public TableColumn<Map, String> instrBinaryCode;

    public TableView<Map<String, String>> controlMemoryTable;
    public TableColumn<Map, String> cmAddressCol;
    public TableColumn<Map, String> cmValueCol;
    public TableColumn<Map, String> cmAmuxCol;
    public TableColumn<Map, String> cmCondCol;
    public TableColumn<Map, String> cmAluCol;
    public TableColumn<Map, String> cmShCol;
    public TableColumn<Map, String> cmMbrCol;
    public TableColumn<Map, String> cmMarCol;
    public TableColumn<Map, String> cmRdCol;
    public TableColumn<Map, String>cmWrCol;
    public TableColumn<Map, String> cmEncCol;
    public TableColumn<Map, String> cmCCol;
    public TableColumn<Map, String> cmBCol;
    public TableColumn<Map, String> cmACol;
    public TableColumn<Map, String> cmAddrCol;

    public TableView<Register> registersTable;
    public TableColumn<Register, String> regName;
    public TableColumn<Register, String> regValue;

    public TableView<MemoryLine> memoryTable;
    public TableColumn<MemoryLine, String> memAddress;
    public TableColumn<MemoryLine, String> memValue;

    public TextArea codeArea;
    public TextArea microcodeArea;
    public TextArea console;

    public Label MARField;
    public Label MBRField;
    public Label MPCField;
    public Label MIRField;

    public TextField memorySearchField;
    public TextField searchedAddressValueField;

    public Label clockLab;
    public Label subcycleLab;
    public Label instructionStatusLabel;

    public Button btnRun;
    public Button btnNextSubClock;
    public Button btnNextClock;
    public Button btnEndProgram;

    public TabPane tabPane;
    public Tab codeTab;
    public Tab memoryTab;
    public Tab controlTab;
    public Tab datapathTab;

    public Menu fileMenu;
    public Menu examplesMenu;
    public Menu executeMenu;
    public Menu helpMenu;
    public MenuItem newMenuitem;
    public MenuItem openMenuItem;
    public MenuItem saveMenuItem;
    public MenuItem simpleAdderExampleMenuItem;
    public MenuItem nthFibNumExampleMenuItem;
    public MenuItem firstNToStackMenuItem;
    public MenuItem exitMenuItem;
    public MenuItem aboutMenuItem;
    public MenuItem helpMenuItem;
    public MenuItem contactMenuItem;
    public MenuItem menuItemRun;
    public MenuItem menuItemNextSubClk;
    public MenuItem menuItemNextClk;
    public MenuItem menuItemEndProgram;

    public AnchorPane circuitPane;
    public ImageView registersImg;

    private final Map<ImageView, Pair<Tooltip, Function<String, String>>> toolTips = new HashMap<>();
    private final Map<ImageView, Image[]> circuitPaneImages = new HashMap<>();
    private final CodeParser codeParser = CodeParser.getInstance();
    private final InstructionParser instructionParser = InstructionParser.getInstance();

    public Tooltip newFileTooltip;
    public Tooltip loadFileTooltip;
    public Tooltip saveFileTooltip;
    public Tooltip runCodeTooltip;
    public Tooltip nextSubClockTooltip;
    public Tooltip nextClockTooltip;
    public Tooltip endProgramTooltip;

    public Label controlMemoryLabel;
    public Label clockTitleLabel;
    public Label subClockTitleLabel;
    public Label languageLab;
    public Label supportedInstructionsLab;

    public MenuButton radixChoiceMenu;
    public MenuItem decimalRadixItem;
    public MenuItem binaryRadixItem;

    private final CPU cpu = new CPU();

    private final int[][] microCodeLinesLengths = new int[256][2];

    private final FileChooser fileChooser = new FileChooser();

    private final SimpleBooleanProperty activeExecutionState = new SimpleBooleanProperty(false);

    private final ObservableResourceFactory resourceFactory = ObservableResourceFactory.getInstance();

    private String errorKey = null;
    private String errorLineNumber = null;

    ObservableList<Map<String, String>> supportedInstructionsList;
    
    @FXML
    private ComboBox<String> comboBox;
    
    @FXML
    private VBox VBoxDropdown;
    
    @FXML
    private ImageView backgroundImg;   
    
    @FXML
    private ImageView circuitTop; 
    
    @FXML
    private ImageView circuitWithin; 
    
    @FXML
    private ImageView circuitBottom; 

    private final double ZOOM_AMOUNT = 1.1;
    private final double[] childrenDefaultLayoutXY = {
        361.17, 195.7,
        366.17, 189.7,
        363.17, 191.7,
        360.17, 193.7,
        360.17, 193.7,
        361.17, 195.7,
        361.17, 195.7,
        367.07, 188.5,
        360.17, 191.7,
        364.17, 189.7,
        362.17, 191.7,
        350.17, 192.7,
        360.17, 192.7,
        360.17, 192.7,
        360.17, 192.7,
        361.17, 190.7,
        361.17, 191.7,
        362.17, 190.7
    };

    @FXML
    private void zoomIn() {
        double newFitWidth = backgroundImg.getFitWidth() * ZOOM_AMOUNT;
        double newFitHeight = backgroundImg.getFitHeight() * ZOOM_AMOUNT;
        backgroundImg.setFitWidth(Math.min(newFitWidth, 1280 * 2.5));
        backgroundImg.setFitHeight(Math.min(newFitHeight, 720 * 2.5));

        if (Math.min(newFitWidth, 1280 * 2.5) != 1280 * 2.5) {
            circuitTop.setFitWidth(circuitTop.getFitWidth() * ZOOM_AMOUNT);
            circuitTop.setFitHeight(circuitTop.getFitHeight() * ZOOM_AMOUNT);
            circuitTop.setLayoutX(circuitTop.getLayoutX() * ZOOM_AMOUNT);
            circuitTop.setLayoutY(circuitTop.getLayoutY() * ZOOM_AMOUNT);
        } else {
            circuitTop.setFitWidth(835 * 2.5);
            circuitTop.setFitHeight(480 * 2.5);
            circuitTop.setLayoutX(270 * 2.5);
            circuitTop.setLayoutY(95 * 2.5);
        }

        int i = 0; 
        for (Node img : circuitPane.getChildren()) {
            if (img.getId().equals("backgroundImg")) continue;
            if (img.getId().equals("circuitTop")) continue;
            if (img.getId().equals("circuitWithin")) continue;
            if (img.getId().equals("circuitBottom")) continue;

            ImageView imgV = (ImageView) img; 
            if (Math.min(newFitWidth, 1280 * 2.5) != 1280 * 2.5) {
                imgV.setFitWidth(imgV.getFitWidth() * ZOOM_AMOUNT);
                imgV.setFitHeight(imgV.getFitHeight() * ZOOM_AMOUNT);
                imgV.setLayoutX(imgV.getLayoutX() * ZOOM_AMOUNT);
                imgV.setLayoutY(imgV.getLayoutY() * ZOOM_AMOUNT);
            } else {
                imgV.setFitWidth(439.3 * 2.5);
                imgV.setFitHeight(281.81 * 2.5);
                imgV.setLayoutX(childrenDefaultLayoutXY[2 * i] * 2.5);
                imgV.setLayoutY(childrenDefaultLayoutXY[2 * i + 1] * 2.5);
            }

            i++; 
        }

        if (Math.min(newFitWidth, 1280 * 2.5) != 1280 * 2.5) {
            circuitWithin.setFitWidth(circuitWithin.getFitWidth() * ZOOM_AMOUNT);
            circuitWithin.setFitHeight(circuitWithin.getFitHeight() * ZOOM_AMOUNT);
            circuitWithin.setLayoutX(circuitWithin.getLayoutX() * ZOOM_AMOUNT);
            circuitWithin.setLayoutY(circuitWithin.getLayoutY() * ZOOM_AMOUNT);
        } else {
            circuitWithin.setFitWidth(558 * 2.5);
            circuitWithin.setFitHeight(310 * 2.5);
            circuitWithin.setLayoutX(302.5 * 2.5);
            circuitWithin.setLayoutY(187 * 2.5);
        }

        if (Math.min(newFitWidth, 1280 * 2.5) != 1280 * 2.5) {
            circuitBottom.setFitWidth(circuitBottom.getFitWidth() * ZOOM_AMOUNT);
            circuitBottom.setFitHeight(circuitBottom.getFitHeight() * ZOOM_AMOUNT);
            circuitBottom.setLayoutX(circuitBottom.getLayoutX() * ZOOM_AMOUNT);
            circuitBottom.setLayoutY(circuitBottom.getLayoutY() * ZOOM_AMOUNT);
        } else {
            circuitBottom.setFitWidth(1280 * 2.5);
            circuitBottom.setFitHeight(720 * 2.5);
            circuitBottom.setLayoutX(150 * 2.5);
            circuitBottom.setLayoutY(0 * 2.5);
        }
    }

    @FXML
    private void zoomOut() {
        double newFitWidth = backgroundImg.getFitWidth() / ZOOM_AMOUNT;
        double newFitHeight = backgroundImg.getFitHeight() / ZOOM_AMOUNT;
        backgroundImg.setFitWidth(Math.max(newFitWidth, 1280));
        backgroundImg.setFitHeight(Math.max(newFitHeight, 720));

        if (Math.max(newFitWidth, 1280) != 1280) {
            circuitTop.setFitWidth(circuitTop.getFitWidth() / ZOOM_AMOUNT);
            circuitTop.setFitHeight(circuitTop.getFitHeight() / ZOOM_AMOUNT);
            circuitTop.setLayoutX(circuitTop.getLayoutX() / ZOOM_AMOUNT);
            circuitTop.setLayoutY(circuitTop.getLayoutY() / ZOOM_AMOUNT);
        } else {
            circuitTop.setFitWidth(835);
            circuitTop.setFitHeight(480);
            circuitTop.setLayoutX(270);
            circuitTop.setLayoutY(95);
        }

        int i = 0; 
        for (Node img : circuitPane.getChildren()) {
            if (img.getId().equals("backgroundImg")) continue;
            if (img.getId().equals("circuitTop")) continue;
            if (img.getId().equals("circuitWithin")) continue;
            if (img.getId().equals("circuitBottom")) continue;

            ImageView imgV = (ImageView) img; 
            if (Math.max(newFitWidth, 1280) != 1280) {
                imgV.setFitWidth(imgV.getFitWidth() / ZOOM_AMOUNT);
                imgV.setFitHeight(imgV.getFitHeight() / ZOOM_AMOUNT);
                imgV.setLayoutX(imgV.getLayoutX() / ZOOM_AMOUNT);
                imgV.setLayoutY(imgV.getLayoutY() / ZOOM_AMOUNT);
            } else {
                imgV.setFitWidth(439.3);
                imgV.setFitHeight(281.81);
                imgV.setLayoutX(childrenDefaultLayoutXY[2 * i]);
                imgV.setLayoutY(childrenDefaultLayoutXY[2 * i + 1]);
            }

            i++;
        }

        if (Math.max(newFitWidth, 1280) != 1280) {
            circuitWithin.setFitWidth(circuitWithin.getFitWidth() / ZOOM_AMOUNT);
            circuitWithin.setFitHeight(circuitWithin.getFitHeight() / ZOOM_AMOUNT);
            circuitWithin.setLayoutX(circuitWithin.getLayoutX() / ZOOM_AMOUNT);
            circuitWithin.setLayoutY(circuitWithin.getLayoutY() / ZOOM_AMOUNT);
        } else {
            circuitWithin.setFitWidth(558);
            circuitWithin.setFitHeight(310);
            circuitWithin.setLayoutX(302.5);
            circuitWithin.setLayoutY(187);
        }

        if (Math.max(newFitWidth, 1280) != 1280) {
            circuitBottom.setFitWidth(circuitBottom.getFitWidth() / ZOOM_AMOUNT);
            circuitBottom.setFitHeight(circuitBottom.getFitHeight() / ZOOM_AMOUNT);
            circuitBottom.setLayoutX(circuitBottom.getLayoutX() / ZOOM_AMOUNT);
            circuitBottom.setLayoutY(circuitBottom.getLayoutY() / ZOOM_AMOUNT);
        } else {
            circuitBottom.setFitWidth(1280);
            circuitBottom.setFitHeight(720);
            circuitBottom.setLayoutX(150);
            circuitBottom.setLayoutY(0);
        }
    }

    @FXML
    private void toggleCircuit() {
        for (Node img : circuitPane.getChildren()) {
            if (img.getId().equals("backgroundImg")) continue;
            if (img.getId().equals("circuitBottom")) continue;
            if (img.isVisible()) {
                img.setVisible(true);
                img.setVisible(false);
            } else {
                img.setVisible(false);
                img.setVisible(true);
            }
        }
    }

    @FXML
    public void initialize() {
        initializeSupportedInstructionsTable();
        initializeControlMemoryTable();
        initializeClockGrid();
        initializeConsole();
        bindImageViews();
        initializeFileChooser();
        initializeExecutionState();
        initializeDropdown(); 
    }

    private void initializeDropdown() {
        comboBox.getItems().addAll("Registers", "Memory Registers", "Memory Addresses", "Microprogram");
        comboBox.setValue("Memory Registers"); 
        addMemoryRegister("MPC", "MPCField");
        addMemoryRegister("MIR", "MIRField");
        addMemoryRegister("MAR", "MARField");
        addMemoryRegister("MBR", "MBRField");
        removeDropdownChildren();
        comboBox.setValue("Microprogram");
        addMicroprogram();
        comboBox.setOnAction(event -> {
            String selectedItem = comboBox.getSelectionModel().getSelectedItem();
            removeDropdownChildren();
            if (selectedItem == "Registers") {
                addTableView();
            }
            if (selectedItem == "Memory Registers") {
                addMemoryRegister("MPC", "MPCField");
                addMemoryRegister("MIR", "MIRField");
                addMemoryRegister("MAR", "MARField");
                addMemoryRegister("MBR", "MBRField");
            }
            if (selectedItem == "Memory Addresses") {
                addMemoryTable();
            }
            if (selectedItem == "Microprogram") {
                addMicroprogram();
            }
        });
    }

    private void removeDropdownChildren() {        
        int size = VBoxDropdown.getChildren().size(); 
        var lastChild = (VBoxDropdown.getChildren().get(size - 1));
        String lastChildId = ((lastChild.getId()==null) ? "none": lastChild.getId() ); 

        while (!lastChildId.equals("comboVBox")) {
            VBoxDropdown.getChildren().remove(size - 1);
            size = VBoxDropdown.getChildren().size(); 
            lastChild = (VBoxDropdown.getChildren().get(size - 1));
            lastChildId = ((lastChild.getId()==null) ? "none": lastChild.getId() ); 
        } 
    }
    
    private void addTableView() {
        TableView<Register> tableView = new TableView<>();
        tableView.setId("registersTable");
        tableView.setEditable(true);
        tableView.setFixedCellSize(24.0);
        tableView.setPrefHeight(380.0);
        tableView.setPrefWidth(320.0);
        tableView.getStyleClass().add("center-align");
        tableView.setVisible(true);

        Rectangle clipRect = new Rectangle();
        clipRect.setWidth(320);
        clipRect.setHeight(367);
        tableView.setClip(clipRect);

        TableColumn<Register, String> regName = new TableColumn<>();
        regName.setId("regName");
        regName.setEditable(false);
        regName.setPrefWidth(74.0);
        regName.setResizable(false);
        regName.setSortable(false);
        regName.setText("Register");
        regName.setStyle("-fx-font-weight: bold;");
        regName.setStyle("-fx-font-size: 9.5pt;");

        TableColumn<Register, String> regValue = new TableColumn<>();
        regValue.setId("regValue");
        regValue.setPrefWidth(244.0);
        regValue.setResizable(false);
        regValue.setSortable(false);
        regValue.getStyleClass().add("center-align");
        regValue.setText("Value");
        regValue.setStyle("-fx-font-weight: bold;");
        regValue.setStyle("-fx-font-size: 9.5pt;");

        tableView.getColumns().addAll(regName, regValue);
        VBoxDropdown.getChildren().add(tableView);

        initializeRegistersTable(tableView, regName, regValue);
    }

    private void addMemoryRegister(String textWithin, String id) {
        VBox newVBox = new VBox();
        newVBox.getStyleClass().add("default-border");
        newVBox.setVisible(true);

        Label label = new Label(textWithin);
        label.setAlignment(javafx.geometry.Pos.CENTER);
        label.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
        label.setPrefHeight(22.0);
        label.setPrefWidth(318.0);
        label.getStyleClass().add("table-label");
        label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        label.setFont(new javafx.scene.text.Font("System Bold", 13.0));

        Label MField = new Label();
        MField.setId(id);
        MField.setAlignment(javafx.geometry.Pos.CENTER);
        MField.setPrefHeight(40.0);
        MField.setPrefWidth(318.0);
        MField.getStyleClass().add("status-bar");

        newVBox.getChildren().addAll(label, MField);

        VBoxDropdown.getChildren().add(newVBox);

        if (id.equals("MPCField")) {
            this.MPCField = MField; 
            initializeMPCField(MField);
        }
        if (id.equals("MIRField")) {
            this.MIRField = MField; 
            initializeMIRField(MField);
        }
        if (id.equals("MARField")) {
            this.MARField = MField; 
            initializeMARField(MField);
        }
        if (id.equals("MBRField")) {
            this.MBRField = MField; 
            initializeMBRField(MField); 
        }
    }

    private void addMicroprogram() {
        HBox hBox = new HBox();
        hBox.setPrefHeight(300.0);
        hBox.setPrefWidth(1280.0);
        hBox.setSpacing(5.0);

        VBox vBox = new VBox();
        vBox.setMinHeight(300.0);

        TextArea microcodeArea = new TextArea();
        microcodeArea.setEditable(false);
        microcodeArea.setMinHeight(450.0);
        microcodeArea.setPrefWidth(376.0);
        microcodeArea.setStyle("-fx-text-fill: white;");

        vBox.getChildren().add(microcodeArea);

        hBox.getChildren().add(vBox);

        hBox.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));

        VBoxDropdown.getChildren().add(hBox);

        this.microcodeArea = microcodeArea; 

        initializeMicrocodeArea();
    }

    private void addMemoryTable() {
        TextField memorySearchField = new TextField();
        memorySearchField.setId("memorySearchField"); 
        memorySearchField.setPrefSize(251, 26);
        memorySearchField.setPromptText("Search by address");
        memorySearchField.setOnKeyPressed(event -> searchKeyPressedAction(event));

        Button btnSearchMemory = new Button();
        btnSearchMemory.setId("btnSearchMemory"); 
        btnSearchMemory.setMnemonicParsing(false);
        btnSearchMemory.setPrefSize(24, 24);
        btnSearchMemory.getStyleClass().add("buttons");
        btnSearchMemory.setOnAction(event -> searchMemoryAction());

        TextField searchedAddressValueField = new TextField();
        searchedAddressValueField.setId("searchedAddressValueField"); 
        searchedAddressValueField.setPrefSize(251, 26);
        searchedAddressValueField.setEditable(false);

        HBox hbox = new HBox(5, memorySearchField, btnSearchMemory, searchedAddressValueField);

        TableView<MemoryLine> memoryTable = new TableView<>();
        memoryTable.setId("memoryTable");
        memoryTable.setEditable(true);
        memoryTable.setFixedCellSize(24.0);
        memoryTable.setPrefSize(320, 380);

        TableColumn<MemoryLine, String> memAddress = new TableColumn<>();
        memAddress.setId("memAddress"); 
        memAddress.setEditable(false);
        memAddress.setPrefWidth(74);
        memAddress.setResizable(false);
        memAddress.setSortable(false);
        memAddress.setText("Address");

        TableColumn<MemoryLine, String> memValue = new TableColumn<>();
        memValue.setId("memValue");
        memValue.setPrefWidth(244);
        memValue.setResizable(false);
        memValue.setSortable(false);
        memValue.setText("Value");

        memoryTable.getColumns().addAll(memAddress, memValue);

        Rectangle clipRect = new Rectangle();
        clipRect.setWidth(320);
        clipRect.setHeight(364);
        memoryTable.setClip(clipRect);

        VBox vbox = new VBox(5, hbox, memoryTable);
        vbox.setPrefHeight(380 + 26); 

        VBoxDropdown.getChildren().add(vbox);

        initializeMemoryTable(memAddress, memValue, memoryTable); 
        initializeMemoryTab(memorySearchField, searchedAddressValueField);
        
        this.memAddress = memAddress; 
        this.memValue = memValue; 
        this.memorySearchField = memorySearchField; 
        this.searchedAddressValueField = searchedAddressValueField; 
        this.memoryTable = memoryTable; 
    }

    private void initializeExecutionState() {
        activeExecutionState.addListener((o, oldVal, newVal) -> {
            if (newVal) {
                btnRun.setDisable(true);
                btnNextSubClock.setDisable(false);
                btnNextClock.setDisable(false);
                btnEndProgram.setDisable(false);
            } else {
                btnRun.setDisable(false);
                btnNextSubClock.setDisable(true);
                btnNextClock.setDisable(true);
                btnEndProgram.setDisable(true);
            }
        });
    }

    private void initializeFileChooser() {
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Supported", "*.txt", "*.mic1"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("MIC-1 Specific", "*.mic1")
        );
    }

    private void initializeMemoryTab(TextField memorySearchField, TextField searchedAddressValueField) {
        memorySearchField.setText("");
        searchedAddressValueField.setText("");
        memorySearchField.setStyle(null);
    }

    private void initializeConsole() {
        console.addEventFilter(MouseEvent.ANY, Event::consume);
    }

    private void initializeMIRField(Label MIRField) {
        MIRField.setText(NumericFactory.getStringValue32(cpu.MIRProperty().get()));
        cpu.MIRProperty().addListener(
                (o, oldVal, newVal) -> MIRField.setText(NumericFactory.getStringValue32(newVal.intValue())));
    }

    private void initializeMPCField(Label MPCField) {
        MPCField.setText(NumericFactory.getStringValue8(cpu.MPCProperty().getValue().shortValue()));
        cpu.MPCProperty().addListener((o, oldVal, newVal) -> {
            MPCField.setText(NumericFactory.getStringValue8(newVal.shortValue()));
            microcodeArea.selectRange(
                    microCodeLinesLengths[newVal.intValue()][0], microCodeLinesLengths[newVal.intValue()][1]);
            if (newVal.intValue() <= 2) {
                instructionStatusLabel.textProperty().bind(resourceFactory.getStringBinding("instr-fetch"));
            } else if (newVal.intValue() == 3) {
                instructionStatusLabel.textProperty().unbind();
                instructionStatusLabel.
                        setText(resourceFactory.getResources().getString("instr-exec") + ": " +
                                instructionParser.getInstructionString((short) cpu.MBRProperty().get()));
            } else {
                instructionStatusLabel.textProperty().unbind();
                instructionStatusLabel.
                        setText(resourceFactory.getResources().getString("instr-exec") + ": " +
                                instructionParser.getInstructionString(cpu.getRegisters().get(3).getValue()));
            }
        });
    }

    private void initializeClockGrid() {
        clockLab.setText(String.valueOf(cpu.clockCounterProperty().get() + 1));
        subcycleLab.setText(String.valueOf(cpu.clockProperty().get() + 1));
        cpu.clockCounterProperty().addListener(
                (o, oldVal, newVal) -> clockLab.setText(String.valueOf((Integer) newVal + 1)));
        cpu.clockProperty().addListener(
                (o, oldVal, newVal) -> subcycleLab.setText(String.valueOf((Integer) newVal + 1)));
    }

    private void initializeMARField(Label MARField) {
        MARField.setText(NumericFactory.getStringValue16(cpu.MARProperty().getValue().shortValue()));
        cpu.MARProperty().addListener(
                (o, oldVal, newVal) -> MARField.setText(NumericFactory.getStringValue16(newVal.shortValue())));
        }

    private void initializeMBRField(Label MBRField) {
        MBRField.setText(NumericFactory.getStringValue16(cpu.MBRProperty().getValue().shortValue()));
        cpu.MBRProperty().addListener(
                (o, oldVal, newVal) -> MBRField.setText(NumericFactory.getStringValue16(newVal.shortValue())));
    }

    private void initializeMemoryTable(TableColumn<MemoryLine, String> memAddress, TableColumn<MemoryLine, String> memValue, TableView<MemoryLine> memoryTable) {
        memAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        memValue.setCellValueFactory(new PropertyValueFactory<>("stringValue"));
        memValue.setCellFactory(TextFieldTableCell.forTableColumn());
        memValue.setOnEditCommit(event -> changeMemoryLineValue(event.getRowValue(), event.getNewValue()));
        memoryTable.setItems(cpu.getMemory().getMemory());
    }

    private void changeMemoryLineValue(MemoryLine memLine, String newValue) {
        try {
            int value = Integer.parseInt(newValue);
            if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
                throw new NumberFormatException();
            }
            memLine.setValue((short) value);
            memoryTable.refresh();
        } catch (NumberFormatException e) {
            showErrorAlert("memory");
            memoryTable.refresh();
        }
    }

    private void initializeRegistersTable(TableView<Register> registersTable, TableColumn<Register, String> regName, TableColumn<Register, String> regValue) {
        regName.setCellValueFactory(new PropertyValueFactory<>("name"));
        regValue.setCellValueFactory(new PropertyValueFactory<>("stringValue"));
        regValue.setCellFactory(TextFieldTableCell.forTableColumn());
        regValue.setOnEditCommit(event -> changeRegisterValue(event.getRowValue(), event.getNewValue()));
        registersTable.setItems(cpu.getRegisters());
    }

    private void changeRegisterValue(Register register, String newValue) {
        try {
            int value = Integer.parseInt(newValue);
            validateValue(register.getName(), value);
            register.setValue((short) value);
            registersTable.refresh();
        } catch (NumberFormatException e) {
            showErrorAlert(register.getName());
            registersTable.refresh();
        }
    }

    private void showErrorAlert(String identifier) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceFactory.getResources().getString("error"));
        alert.setHeaderText(resourceFactory.getResources().getString("invalid-new-val"));
        alert.setContentText((identifier.equals("memory") ? "mem" : identifier) + getBoundaryMsg(identifier));
        alert.showAndWait();
    }

    private String getBoundaryMsg(String identifier) {
        if (identifier.equalsIgnoreCase("memory")) {
            return ": " + resourceFactory.getResources().getString("valid-range")
                    + " [" + Short.MIN_VALUE + ", " + Short.MAX_VALUE + "].";
        }
        if (isImmutableRegister(identifier)) {
            return " " + resourceFactory.getResources().getString("immutable-reg") + ".";
        }
        return ": " + resourceFactory.getResources().getString("valid-range")
                + " [" + getRegisterLowerBound(identifier) + ", " + getRegisterUpperBound(identifier) + "].";
    }

    private void validateValue(String regName, int value) {
        if (isImmutableRegister(regName)) {
            throw new NumberFormatException();
        }

        int lower = getRegisterLowerBound(regName);
        int upper = getRegisterUpperBound(regName);

        if (value < lower || value > upper) {
            throw new NumberFormatException();
        }
    }

    private int getRegisterUpperBound(String regName) {
        int res = Short.MAX_VALUE;
        if (regName.equalsIgnoreCase("PC") || regName.equalsIgnoreCase("SP")) {
            res = 4095;
        }
        return res;
    }

    private int getRegisterLowerBound(String regName) {
        int res = Short.MIN_VALUE;
        if (regName.equalsIgnoreCase("PC") || regName.equalsIgnoreCase("SP")) {
            res = 0;
        }
        return res;
    }

    private boolean isImmutableRegister(String regName) {
        return regName.equals("0") || regName.equals("+1") || regName.equals("-1")
                || regName.equalsIgnoreCase("AMASK")
                || regName.equalsIgnoreCase("SMASK");
    }

    private void initializeMicrocodeArea() {
        String code = FileParser.loadMicroCode();
        String[] codeLines = code.split("\n");
        setMicroCodeLinesLengths(codeLines);
        microcodeArea.setText(FileParser.loadMicroCode());
        microcodeArea.setStyle(null);
        microcodeArea.addEventFilter(MouseEvent.MOUSE_RELEASED, t -> {
            int line = cpu.MPCProperty().get();
            if (activeExecutionState.get())
                microcodeArea.selectRange(microCodeLinesLengths[line][0], microCodeLinesLengths[line][1]);
        });
    }

    private void setMicroCodeLinesLengths(String[] codeLines) {
        for (int i = 0; i < codeLines.length; i++) {
            microCodeLinesLengths[i][0] = i > 0 ? microCodeLinesLengths[i-1][1] + 1 : 0;
            microCodeLinesLengths[i][1] = microCodeLinesLengths[i][0] + codeLines[i].length();
        }
    }


    private void initializeControlMemoryTable() {
        cmAddressCol.setCellValueFactory(new MapValueFactory<>("address"));
        cmAmuxCol.setCellValueFactory(new MapValueFactory<>("amux"));
        cmCondCol.setCellValueFactory(new MapValueFactory<>("cond"));
        cmAluCol.setCellValueFactory(new MapValueFactory<>("alu"));
        cmShCol.setCellValueFactory(new MapValueFactory<>("sh"));
        cmMbrCol.setCellValueFactory(new MapValueFactory<>("mbr"));
        cmMarCol.setCellValueFactory(new MapValueFactory<>("mar"));
        cmRdCol.setCellValueFactory(new MapValueFactory<>("rd"));
        cmWrCol.setCellValueFactory(new MapValueFactory<>("wr"));
        cmEncCol.setCellValueFactory(new MapValueFactory<>("enc"));
        cmCCol.setCellValueFactory(new MapValueFactory<>("c"));
        cmBCol.setCellValueFactory(new MapValueFactory<>("b"));
        cmACol.setCellValueFactory(new MapValueFactory<>("a"));
        cmAddrCol.setCellValueFactory(new MapValueFactory<>("addr"));
        controlMemoryTable.getItems().addAll(FileParser.loadControlMemoryTableData());
    }

    private void initializeSupportedInstructionsTable() {
        instrMnemonic.setCellValueFactory(new MapValueFactory<>("mnemonic"));
        instrInstruction.setCellValueFactory(new MapValueFactory<>("instruction"));
        instrMeaning.setCellValueFactory(new MapValueFactory<>("meaning"));
        instrBinaryCode.setCellValueFactory(new MapValueFactory<>("binaryCode"));
        supportedInstructionsList = FileParser.loadSupportedInstructionsTableData();
        supportedInstructionsTable.setItems(supportedInstructionsList);
    }

    private void bindImageViews() {
        try {
            for (Node img : circuitPane.getChildren()) {
                if (img.getId().equals("registersImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("registers.png", "registers-active.png"));
                } else if (img.getId().equals("aluImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("alu.png", "alu-active.png"));
                } else if (img.getId().equals("amuxImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("amux.png", "amux-active.png"));
                } else if (img.getId().equals("aLatchImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("a-latch.png", "a-latch-active.png"));
                } else if (img.getId().equals("bLatchImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("b-latch.png", "b-latch-active.png"));
                } else if (img.getId().equals("aDecImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("a-decoder.png", "a-decoder-active.png"));
                } else if (img.getId().equals("bDecImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("b-decoder.png", "b-decoder-active.png"));
                } else if (img.getId().equals("cDecImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("c-decoder.png", "c-decoder-active.png"));
                } else if (img.getId().equals("clockImg")) {
                    circuitPaneImages.put((ImageView) img,
                            getImgArray("4-phase-clock.png", "4-phase-clock-active-2.png",
                                    "4-phase-clock-active-3.png", "4-phase-clock-active-4.png", "4-phase-clock-active-1.png"));
                } else if (img.getId().equals("circuitWithin")) {
                    circuitPaneImages.put((ImageView) img,
                            getImgArray("circuit-within.png", "circuit-clock-2-active.png",
                                    "circuit-clock-3-active.png", "circuit-clock-4-active.png", "circuit-clock-1-active.png"));
                } else if (img.getId().equals("shifterImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("shifter.png", "shifter-active.png"));
                } else if (img.getId().equals("marImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("mar.png", "mar-active.png"));
                } else if (img.getId().equals("mbrImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("mbr.png", "mbr-active.png"));
                } else if (img.getId().equals("mMuxImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("mmux.png", "mmux-active.png"));
                } else if (img.getId().equals("mpcImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("mpc.png", "mpc-active.png"));
                } else if (img.getId().equals("incImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("increment.png", "increment-active.png"));
                } else if (img.getId().equals("controlImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("control-store.png", "control-store-active.png"));
                } else if (img.getId().equals("mirImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("mir.png", "mir-active.png"));
                } else if (img.getId().equals("mSeqLogicImg")) {
                    circuitPaneImages.put((ImageView) img, getImgArray("micro-seq-logic.png", "micro-seq-logic-active.png"));
                }
            }
        
        } catch (NullPointerException e) {
            System.out.println("NullPointerException in bindImageViews method");
            e.printStackTrace();
        }
    }

    private Image[] getImgArray(String ... args) {
        Image[] result = new Image[args.length];
        String imgPath = "/img/datapath/";
        for (int i = 0; i < args.length; i++) {
            result[i] = new Image(getClass().getResourceAsStream(imgPath + args[i]));
        }
        return result;
    }

    private void changeRadix(int radix) {
        int oldRadix = NumericFactory.getRadix();
        NumericFactory.setRadix(radix);
        changeRadixInTables();
        changeRadixInStaticFields(oldRadix);
        updateToolTips();
    }

    private void changeRadixInStaticFields(int oldRadix) {
        MPCField.setText(NumericFactory.getStringValue8(cpu.MPCProperty().getValue().shortValue()));
        MIRField.setText(NumericFactory.getStringValue32(cpu.MIRProperty().get()));
        MARField.setText(NumericFactory.getStringValue16(cpu.MARProperty().getValue().shortValue()));
        MBRField.setText(NumericFactory.getStringValue16(cpu.MBRProperty().getValue().shortValue()));
        String memValue = (searchedAddressValueField == null) ? "null" : searchedAddressValueField.getText();
        if (!memValue.equals("null")) {
            searchedAddressValueField.setText(
                    NumericFactory.getStringValue16(NumericFactory.getShortValue(memValue, oldRadix)));
        }
    }

    private void changeRadixInTables() {
        for (Register r : cpu.getRegisters()) {
            r.setValue(r.getValue());
        }
        for (MemoryLine m : cpu.getMemory().getMemory()) {
            m.setValue(m.getValue());
        }
    }

    public void runCodeAction() {
        try {
            short[] machineCode = codeParser.parseCode(codeArea.getText());
            console.textProperty().bind(resourceFactory.getStringBinding("successful-assemble"));
            cpu.setCPUInitial();
            cpu.getMemory().write(machineCode);
            codeArea.setEditable(false);
            activeExecutionState.set(true);
            microcodeArea.setScrollTop(0);
            microcodeArea.setStyle("-fx-highlight-fill: #f84e4e; -fx-highlight-text-fill: #000000");
            microcodeArea.selectRange(microCodeLinesLengths[0][0], microCodeLinesLengths[0][1]);
            updateToolTips();
            updateImgColors();
        } catch (CodeParserException e) {
            String[] errorInfo = e.getMessage().split("=");
            if (errorInfo.length > 1) {
                errorLineNumber = errorInfo[0];
                errorKey = errorInfo[1];
            } else {
                errorLineNumber = null;
                errorKey = errorInfo[0];
            }
            setConsoleErrorText();
        }
    }

    private void setConsoleErrorText() {
        console.textProperty().unbind();
        if (errorLineNumber != null) {
            console.setText(resourceFactory.getResources().getString("line-num-err-message")
                    + errorLineNumber + resourceFactory.getResources().getString(errorKey));
        } else {
            console.setText(resourceFactory.getResources().getString(errorKey));
        }
    }

    public void runClockCycleAction() {
        cpu.runCycle();
        updateToolTips();
        updateImgColors();
    }

    public void runSubClockCycleAction() {
        cpu.runSubCycle();
        updateToolTips();
        updateImgColors();
    }

    private void updateImgColors() {
        for (ImageView img : circuitPaneImages.keySet()) {
            img.setImage(circuitPaneImages.get(img)[cpu.getComponentImg(img.getId())]);
            if (img.getId().equals("clockImg")) {
                circuitWithin.setImage(circuitPaneImages.get(circuitWithin)[cpu.getComponentImg(circuitWithin.getId())]);
            } 
        }
    }

    private void updateToolTips() {
        for (ImageView img : toolTips.keySet()) {
            toolTips.get(img).getKey().setText(toolTips.get(img).getValue().apply(img.getId()));
        }
    }

    public void searchMemoryAction() {
        try {
            String addressString = memorySearchField.getText();
            if (addressString.isEmpty())
                return;
            int address = Integer.parseInt(addressString);
            if (address < 0 || address > 4095)
                throw new NumberFormatException("out of bounds");
            searchedAddressValueField.setText(NumericFactory.getStringValue16(cpu.getMemory().read((short) address)));
            memorySearchField.setStyle(null);
            memoryTable.scrollTo(cpu.getMemory().getMemory().get(address));
            memoryTable.getSelectionModel().select(cpu.getMemory().getMemory().get(address));
        } catch (NumberFormatException e) {
            memorySearchField.setStyle("-fx-border-color: red;");
        }
    }

    public void newFileAction() {
        if (codeArea.getText().isEmpty()) {
            clearConsole();
            return;
        }

        Optional<ButtonType> selectedOption = confirmationAlertShowAndWait(
                resourceFactory.getResources().getString("new-code-file"),
                resourceFactory.getResources().getString("curr-progress")
        );
        if (!activeExecutionState.get()) {
            if (selectedOption.isPresent() && selectedOption.get() == ButtonType.OK) {
                codeArea.clear();
                clearConsole();
                tabPane.getSelectionModel().select(codeTab);
            }
        } else {
            if (selectedOption.isPresent() && selectedOption.get() == ButtonType.OK)
                reinitialiseAppState();
        }
    }

    private Optional<ButtonType> confirmationAlertShowAndWait(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resourceFactory.getResources().getString("confirmation"));
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    private void reinitialiseAppState() {
        cpu.setCPUInitial();
        codeArea.setEditable(true);
        codeArea.clear();
        microcodeArea.setScrollTop(0);
        microcodeArea.selectRange(0, 0);
        microcodeArea.setStyle(null);
        clearConsole();
        activeExecutionState.set(false);
        clearInstructionStatusLabel();
        updateToolTips();
        updateImgColors();
        tabPane.getSelectionModel().select(codeTab);
    }

    private void clearInstructionStatusLabel() {
        instructionStatusLabel.textProperty().unbind();
        instructionStatusLabel.setText("");
    }

    private void clearConsole() {
        console.textProperty().unbind();
        console.setText("");
    }

    public void loadFileAction() {
        File selectedFile = fileChooser.showOpenDialog(btnRun.getScene().getWindow());
        if (selectedFile != null) {
            Optional<ButtonType> selectedOption = Optional.of(ButtonType.OK);
            if (!codeArea.getText().isEmpty()) {
                selectedOption = confirmationAlertShowAndWait(
                        resourceFactory.getResources().getString("load-code-file") + "?",
                        resourceFactory.getResources().getString("curr-progress") + "!"
                );
            }
            if (selectedOption.isPresent() && selectedOption.get() == ButtonType.OK) {
                reinitialiseAppState();
                String content = FileParser.readFile(selectedFile);
                codeArea.setText(content);
            }
        }
    }

    public void saveFileAction() {
        File selectedFile = fileChooser.showSaveDialog(btnRun.getScene().getWindow());
        if (selectedFile != null) {
            FileParser.writeFile(selectedFile, codeArea.getText());
        }
    }

    public void exitAction() {
        Stage currStage = (Stage) btnRun.getScene().getWindow();
        currStage.close();
    }

    public void setDecimalRadix() {
        if (NumericFactory.getRadix() != 10) {
            changeRadix(10);
            radixChoiceMenu.setText(resourceFactory.getResources().getString("decimal"));
        }
    }

    public void setBinaryRadix() {
        if (NumericFactory.getRadix() != 2) {
            changeRadix(2);
            radixChoiceMenu.setText(resourceFactory.getResources().getString("binary"));
        }
    }

    public void endProgramExecutionAction() {
        Optional<ButtonType> selectedOption = confirmationAlertShowAndWait(
                resourceFactory.getResources().getString("end-program-exec-warn"),
                resourceFactory.getResources().getString("curr-progress")
        );
        if (!activeExecutionState.get()) {
            if (selectedOption.isPresent() && selectedOption.get() == ButtonType.OK) {
                console.textProperty().bind(resourceFactory.getStringBinding("program-exec-stop"));
                tabPane.getSelectionModel().select(codeTab);
            }
        } else {
            if (selectedOption.isPresent() && selectedOption.get() == ButtonType.OK) {
                String currentCode = codeArea.getText();
                reinitialiseAppState();
                codeArea.setText(currentCode);
                console.textProperty().bind(resourceFactory.getStringBinding("program-exec-stop"));
            }
        }
    }

    public void onKeyPressedAction(KeyEvent keyEvent) {
        String key = keyEvent.getCode().getName();
        if (!activeExecutionState.get()) {
            if (key.equals("F1")) {
                runCodeAction();
            }
        } else {
            switch (key) {
                case "F2":
                    runSubClockCycleAction();
                    break;
                case "F3":
                    runClockCycleAction();
                    break;
                case "F4":
                    endProgramExecutionAction();
                    break;
            }
        }
    }

    public void searchKeyPressedAction(KeyEvent keyEvent) {
        if (keyEvent.getCode().getName().equalsIgnoreCase("enter")) {
            searchMemoryAction();
        }
    }
}