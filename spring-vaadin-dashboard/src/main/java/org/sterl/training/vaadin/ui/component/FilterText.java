package org.sterl.training.vaadin.ui.component;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class FilterText extends TextField {

    public FilterText() {
        super();
        this.setIcon(FontAwesome.SEARCH);
        this.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        this.addShortcutListener(
                new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
                    @Override
                    public void handleAction(final Object sender,
                            final Object target) {
                        FilterText.this.setValue("");
                    }
                });
    }
}
