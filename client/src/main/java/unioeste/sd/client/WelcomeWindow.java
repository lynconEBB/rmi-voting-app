package unioeste.sd.client;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import unioeste.sd.client.ui.Fonts;

import java.util.concurrent.CompletableFuture;

public class WelcomeWindow {
    float buttonSizeX;

    private final ImString newUsername;
    private final ImString newPassword;
    private final ImString newName;

    private final ImString username;
    private final ImString password;

    private boolean showErrorRegister;
    private boolean showErrorLogin;

    private String registerError;
    private String loginError;
    private CompletableFuture<Void> requestingFuture;
    public WelcomeWindow() {
        this.newPassword = new ImString();
        this.newUsername = new ImString();
        this.newName = new ImString();
        this.username = new ImString();
        this.password = new ImString();
        this.showErrorRegister = false;
        this.showErrorLogin = false;
        this.requestingFuture = CompletableFuture.completedFuture(null);
    }

    public void showLoginMessageError(String message) {
        showErrorRegister = false;
        showErrorLogin = true;
        loginError = message;
    }

    public void showRegisterMessageError(String message) {
        showErrorLogin = false;
        showErrorRegister = true;
        registerError = message;
    }

    public void draw(Client client) {
        boolean shouldDisable = false;
        buttonSizeX = ImGui.calcTextSize("Sign up").x + ImGui.getStyle().getFramePaddingX() * 2;

        ImGui.begin("Welcome", ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoDocking);
        {
            ImGui.pushFont(Fonts.titleFont);
            ImGui.textColored(255,255,0,255,"Sign new user");
            ImGui.popFont();
            ImGui.inputText("New Name", newName);
            ImGui.inputText("New Username", newUsername );
            ImGui.inputText("New Passowrd", newPassword, ImGuiInputTextFlags.Password);

            if (showErrorRegister) {
                ImGui.textColored(255,0,0,255, registerError);
            }

            ImGui.setCursorPos((ImGui.getWindowSizeX() - buttonSizeX) / 2, ImGui.getCursorPosY() + 10);

            if (!requestingFuture.isDone()) {
                ImGui.beginDisabled();
                shouldDisable = true;
            }
            if (ImGui.button("Sign up")) {
                requestingFuture = CompletableFuture.runAsync(() -> client.requestRegistration(newName.get(), newUsername.get(), newPassword.get()));
                requestingFuture.exceptionally(e -> {
                   showRegisterMessageError(e.getMessage());
                   return null;
                });
            }
            if (shouldDisable)
                ImGui.endDisabled();

            ImGui.separator();

            ImGui.pushFont(Fonts.titleFont);
            ImGui.textColored(255,255,0,255,"Sign up");
            ImGui.popFont();

            ImGui.inputText("Username", username);
            ImGui.inputText("Passowrd", password, ImGuiInputTextFlags.Password);


            if (showErrorLogin) {
                ImGui.textColored(255,0,0,255, loginError);
            }

            ImGui.setCursorPos((ImGui.getWindowSizeX() - buttonSizeX) / 2, ImGui.getCursorPosY() + 10);
            if (!requestingFuture.isDone()) {
                ImGui.beginDisabled();
                shouldDisable = true;
            }
            if (ImGui.button("Sign in")) {
                requestingFuture = CompletableFuture.runAsync(() -> client.requestLogin(username.get(), newPassword.get()));
                requestingFuture.exceptionally(e -> {
                    showLoginMessageError(e.getMessage());
                    return null;
                });
            }
            if (shouldDisable)
                ImGui.endDisabled();

            ImGui.setWindowSize(0,0);
            ImGui.setWindowPos((float)(ImGui.getIO().getDisplaySizeX() * 0.5) - ImGui.getWindowWidth()/2,
                    (float) (ImGui.getIO().getDisplaySizeY() * 0.5) - ImGui.getWindowHeight()/2);
        }
        ImGui.end();
    }
}
