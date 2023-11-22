package unioeste.sd.client;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiConfigFlags;
import imgui.type.ImBoolean;
import unioeste.sd.client.ui.Fonts;
import unioeste.sd.client.ui.ImGuiUtils;
import unioeste.sd.common.VotingSimpleDTO;


public class MainWindow extends Application {
    public final WelcomeWindow welcomeWindow;
    public VotingFormWindow votingFormWindow;
    private final VotingDetailWindow votingDetailWindow;
    private final ImBoolean showFormWindow;

    public final Client client;

    public MainWindow() {
        this.votingDetailWindow = new VotingDetailWindow();
        this.welcomeWindow = new WelcomeWindow();
        this.votingFormWindow = new VotingFormWindow();

        this.showFormWindow = new ImBoolean(false);
        this.client = new Client();
    }

    @Override
    public void process() {
        ImGui.dockSpaceOverViewport(ImGui.getMainViewport());

        if (!client.isLogged()) {
            welcomeWindow.draw(client);
            return;
        }

        ImGui.begin("Votings Sidebar");
        {
            ImGuiUtils.centerHorizontally(ImGui.calcTextSize("New Voting").x);
            if (ImGui.button("New Voting")) {
                votingFormWindow = new VotingFormWindow();
                showFormWindow.set(true);
            }
            for (VotingSimpleDTO voting : client.availableVotings) {
                VotingItemWidget.draw(voting, client);
            }
        }
        ImGui.end();

        if (showFormWindow.get()) {
            votingFormWindow.draw(showFormWindow, client);
        }

        votingDetailWindow.draw(client);

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
        config.setWidth(1920);
        config.setHeight(1080);
    }

    public static void main(String[] args) {
        launch(new MainWindow());
    }
}
