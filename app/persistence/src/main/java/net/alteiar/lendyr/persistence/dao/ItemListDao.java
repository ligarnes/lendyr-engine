package net.alteiar.lendyr.persistence.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.alteiar.lendyr.model.items.Item;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemListDao {
  private List<Item> items;
}
