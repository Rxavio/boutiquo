package net.bootBootique.springboot.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.bootBootique.springboot.entities.Boot;
import net.bootBootique.springboot.enums.BootType;

public interface BootRepository extends CrudRepository<Boot, Integer> {

    List<Boot> findBootsBySize(Float size);

    List<Boot> findBootsByMaterial(String material);

    List<Boot> findBootsByType(BootType type);

    List<Boot> findBootsByQuantityGreaterThan(Integer minQuantity);
}