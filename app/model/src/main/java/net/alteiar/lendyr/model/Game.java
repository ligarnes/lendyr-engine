package net.alteiar.lendyr.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.alteiar.lendyr.model.encounter.Encounter;
import net.alteiar.lendyr.model.items.Item;
import net.alteiar.lendyr.model.persona.Persona;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {
  private List<Persona> personas;
  private List<Item> items;
  private Encounter encounter;
}
