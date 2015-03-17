package ru.extas.web.commons.component;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.jouni.animator.Animator;
import org.vaadin.jouni.animator.client.CssAnimation;
import org.vaadin.jouni.dom.Dom;
import org.vaadin.jouni.dom.client.Css;

public class Disclosure extends CssLayout {

    public static final String STYLE = "v-disclosure";
    public static final String STYLE_CAPTION = STYLE + "-caption";
    public static final String STYLE_CAPTION_OPEN = STYLE_CAPTION + "-open";

    protected AbstractComponent content;
    protected Button caption = new Button();
    protected boolean open = false;

    private String openCaption;
    private String closeCaption;

    public Disclosure(String openCaption, String closeCaption) {
        this.openCaption = openCaption;
        this.closeCaption = closeCaption;
        this.caption.setCaption(openCaption);
        setPrimaryStyleName(STYLE);
        this.caption.addStyleName(ValoTheme.BUTTON_LINK);
        this.caption.setIcon(FontAwesome.CHEVRON_RIGHT);
        this.caption.addStyleName(STYLE_CAPTION);
        super.addComponent(this.caption);
        this.caption.addClickListener(e -> {
                    if (open) {
                        close();
                    } else {
                        open();
                    }
                });
    }

    public Disclosure(String openCaption, String closeCaption, AbstractComponent content) {
        this(openCaption, closeCaption);
        setContent(content);
    }

    public String getDisclosureCaption() {
        return caption.getCaption();
    }

    public void setDisclosureCaption(String caption) {
        this.caption.setCaption(caption);
    }

    public Disclosure setContent(AbstractComponent newContent) {
        if (content != newContent) {
            if (content != null && content.getParent() != null) {
                removeComponent(content);
            }
            if (open && newContent != null) {
                super.addComponent(newContent);
            }
            content = newContent;
            new Dom(content).getStyle().setProperty("overflow", "hidden");
        }
        return this;
    }

    public Component getContent() {
        return content;
    }

    public Disclosure open() {
        if (content != null) {
            if (content.getParent() == null || content.getParent() != this) {
                super.addComponent(content);
            }
            Animator.animate(new CssAnimation(content, new Css()
                    .setProperty("max-height", getMaxHeight() + "px")));
            caption.addStyleName(STYLE_CAPTION_OPEN);
            open = true;
            caption.setIcon(FontAwesome.CHEVRON_DOWN);
            caption.setCaption(closeCaption);
        }
        return this;
    }

    public boolean isOpen() {
        return open;
    }

    public Disclosure close() {
        if (content != null) {
            Animator.animate(new CssAnimation(content, new Css()
                    .setProperty("max-height", "0")));
            caption.removeStyleName(STYLE_CAPTION_OPEN);
            open = false;
            caption.setIcon(FontAwesome.CHEVRON_RIGHT);
            caption.setCaption(openCaption);
        }
        return this;
    }

    @Override
    public void addComponent(Component c) {
        if (content == null) {
            setContent((AbstractComponent) c);
        } else {
            throw new UnsupportedOperationException(
                    "You can only add one component to the Disclosure. Use Disclosure.setContent() method instead.");
        }
    }

    @Override
    public void removeAllComponents() {
        setContent(null);
    }

    /**
     * @return The expected maximum height for the disclosure content
     * (pixels)
     */
    protected int getMaxHeight() {
        return 800;
    }
}