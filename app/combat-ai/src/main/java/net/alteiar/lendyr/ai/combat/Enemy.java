package net.alteiar.lendyr.ai.combat;

import lombok.Builder;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.model.persona.AttackType;

@Builder
public record Enemy(PersonaEntity personaTarget, float distance, AttackType attackType) {
}
