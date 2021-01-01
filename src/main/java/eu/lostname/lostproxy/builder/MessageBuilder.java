package eu.lostname.lostproxy.builder;

import net.md_5.bungee.api.chat.*;

import java.util.List;

public class MessageBuilder {

    private final TextComponent textComponent;

    public MessageBuilder(String content) {
        this.textComponent = new TextComponent(TextComponent.fromLegacyText(content));
    }

    public MessageBuilder addClickEvent(ClickEvent.Action action, String content) {
        textComponent.setClickEvent(new ClickEvent(action, content));
        return this;
    }

    public MessageBuilder addHoverEvent(HoverEvent.Action action, String content) {
        textComponent.setHoverEvent(new HoverEvent(action, new Text(content)));
        return this;
    }

    public MessageBuilder addExtra(TextComponent textComponent) {
        textComponent.addExtra(textComponent);
        return this;
    }

    public MessageBuilder setExtra(List<BaseComponent> textComponents) {
        textComponent.setExtra(textComponents);
        return this;
    }

    public TextComponent build() {
        return textComponent;
    }
}
