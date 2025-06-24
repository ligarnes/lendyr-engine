package net.alteiar.lendyr.model.npc.behavior;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "behaviorType", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Patrol.class, name = Behavior.PATROL),
    @JsonSubTypes.Type(value = Static.class, name = Behavior.STATIC),
})
public interface Behavior {
  String PATROL = "PATROL";
  String PROTECT = "PROTECT";
  String STATIC = "STATIC";
  String RANDOM = "RANDOM";
}
