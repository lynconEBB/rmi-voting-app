package unioeste.sd.client;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiConfigFlags;
import imgui.type.ImBoolean;
import unioeste.sd.client.ui.Fonts;

public class MainWindow extends Application {

    public final ImBoolean showWelcomeWindow;
    public final WelcomeWindow welcomeWindow;

    private final Client client;

    public MainWindow() {
        this.showWelcomeWindow = new ImBoolean(true);
        this.welcomeWindow = new WelcomeWindow();
        this.client = new Client(this);
    }

    @Override
    public void process() {
        ImGui.dockSpaceOverViewport(ImGui.getMainViewport());

        if (showWelcomeWindow.get()) {
            welcomeWindow.draw(client);
        }

    }

    @Override
    protected void initImGui(Configuration config) {
        super.initImGui(config);

        ImGui.getIO().addConfigFlags(ImGuiConfigFlags.DockingEnable);
        Fonts.InitFonts();
    }

    @Override
    protected void configure(Configuration config) {
        config.setTitle("RMI Voting");
    }

    public static void main(String[] args) {
        launch(new MainWindow());
    }
}
