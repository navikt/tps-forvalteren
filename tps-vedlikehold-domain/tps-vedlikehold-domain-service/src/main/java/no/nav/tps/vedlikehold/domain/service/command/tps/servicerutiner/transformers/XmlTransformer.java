package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers;

import org.codehaus.jackson.annotate.JsonIgnoreType;

@JsonIgnoreType
public class XmlTransformer {

    private Type type;

    private XmlTransformStrategy strategy;


    public XmlTransformer(XmlTransformStrategy strategy, Type type) {
        this.strategy = strategy;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public XmlTransformStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(XmlTransformStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean isPreSend(){
        return type == Type.PRE_SEND;
    }

    public boolean isPostSend() {
        return type == Type.POST_SEND;
    }

    public enum Type {
        PRE_SEND, POST_SEND
    }
}
