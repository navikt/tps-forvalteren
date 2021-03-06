package no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes;

import lombok.Getter;

@Getter
public enum FasitPropertyTypes {

    QUEUE(FasitQueue.class, "queue"),
    QUEUE_MANAGER(FasitMQManager.class, "queuemanager"),
    CHANNEL(FasitChannel.class, "channel"),
    UNKNOWN_PROPERTY(FasitProperty.class, "");

    private final Class<? extends FasitProperty> fasitPropertyClass;
    private final String propertyName;

    FasitPropertyTypes(Class<? extends FasitProperty> fasitPropertyClass, String propertyName){
        this.fasitPropertyClass = fasitPropertyClass;
        this.propertyName = propertyName;
    }

    public static FasitPropertyTypes getEnumByName(String propertyName){
        for(FasitPropertyTypes e : FasitPropertyTypes.values()){
            if(e.propertyName.equals(propertyName)){
                return e;
            }
        }
        return UNKNOWN_PROPERTY;
    }
}
