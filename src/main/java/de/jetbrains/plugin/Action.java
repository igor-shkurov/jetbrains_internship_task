package de.jetbrains.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class Action extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Main.launchWindow();
    }
}
