package net.bootBootique.springboot.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import net.bootBootique.springboot.entities.Boot;
import net.bootBootique.springboot.enums.BootType;
import net.bootBootique.springboot.exceptions.QueryNotSupportedException;
import net.bootBootique.springboot.repositories.BootRepository;

@RestController
@RequestMapping("api/v1/boots")
public class BootController {

    private final BootRepository bootRepository;

    public BootController(BootRepository bootRepository) {
        this.bootRepository = bootRepository;
    }
    
    @PostMapping("/")
    public Boot addBoot(@RequestBody Boot boot) {
        return this.bootRepository.save(boot);
    }
    
    @GetMapping("/")
    public Iterable<Boot> getAllBoots() {
        return this.bootRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Optional<Boot> getBootId(@PathVariable("id") Integer id) {
      return this.bootRepository.findById(id);
    }
  	
    
    @DeleteMapping("/{id}")
    public Boot deleteBoot(@PathVariable("id") Integer id) {
        Optional<Boot> optionalBoot = this.bootRepository.findById(id);

        if(optionalBoot.isEmpty()) {
            return null;
        }

        Boot bootToDelete = optionalBoot.get();
        this.bootRepository.delete(bootToDelete);
        return bootToDelete;
    }
    
    @PutMapping("/{id}")
    public Boot updateBoot(@PathVariable("id") Integer id, @RequestBody Boot b) {
      Optional<Boot> bootToUpdateOptional = this.bootRepository.findById(id);
      if (!bootToUpdateOptional.isPresent()) {
   	  throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found");
      }
      Boot bootToUpdate = bootToUpdateOptional.get();
      
     if (b.getType() != null) {
 	 bootToUpdate.setType(b.getType());
     }
      if (b.getSize() != null) {
   	   bootToUpdate.setSize(b.getSize());
      }
     if (b.getQuantity() != null) {
    	 bootToUpdate.setQuantity(b.getQuantity());
     }
     
      if (b.getMaterial() != null) {
    	  bootToUpdate.setMaterial(b.getMaterial());
      }
    
      Boot updatedBoot = this.bootRepository.save(bootToUpdate);
      return updatedBoot;
    }
    
    @PutMapping("/{id}/quantity/increment")
    public Boot incrementQuantity(@PathVariable("id") Integer id) {
        Optional<Boot> bootOptional = this.bootRepository.findById(id);

        if(bootOptional.isEmpty()) {
            return null;
        }

        Boot bootToUpdate = bootOptional.get();
        bootToUpdate.setQuantity(bootToUpdate.getQuantity() + 1);
        return this.bootRepository.save(bootToUpdate);
    }
    

    @PutMapping("/{id}/quantity/decrement")
    public Boot decrementQuantity(@PathVariable("id") Integer id) {
        Optional<Boot> bootOptional = this.bootRepository.findById(id);

        if(bootOptional.isEmpty()) {
            return null;
        }

        Boot bootToUpdate = bootOptional.get();
        if(bootToUpdate.getQuantity() > 0) {
            bootToUpdate.setQuantity(bootToUpdate.getQuantity() - 1);
            return this.bootRepository.save(bootToUpdate);
        } else {
            return bootToUpdate;
        }
    }

    @GetMapping("/search")
    public List<Boot> searchBoots(
            @RequestParam(required = false) BootType type,
            @RequestParam(required = false) Float size,
            @RequestParam(required = false) String material,
            @RequestParam(name = "quantity", required = false) Integer minQuantity
            ) throws QueryNotSupportedException {
        if(Objects.nonNull(type)) {
            if(Objects.nonNull(size) && Objects.nonNull(material)
                    && Objects.nonNull(minQuantity)) {
                // call the repository method that accepts a type, a size, a material and a minimum quantity
                throw new QueryNotSupportedException(
                        "This query is not supported! Try a different " +
                                "combination of search parameters.");
            } else if(Objects.nonNull(size) && Objects.nonNull(material)) {
                // call the repository method that accepts a type, a size, a material
                throw new QueryNotSupportedException(
                        "This query is not supported! Try a different " +
                                "combination of search parameters.");
            } else if(Objects.nonNull(size) && Objects.nonNull(minQuantity)) {
                // call the repository method that accepts a type, a size and a minimum quantity
                throw new QueryNotSupportedException(
                        "This query is not supported! Try a different " +
                                "combination of search parameters.");
            } else if(Objects.nonNull(size)) {
                // call the repository method that accepts a type and size
                throw new QueryNotSupportedException(
                        "This query is not supported! Try a different " +
                                "combination of search parameters.");
            } else {
                // call the repository method that accepts a type
                return this.bootRepository.findBootsByType(type);
            }
        } else if(Objects.nonNull(size)) {
            if(Objects.nonNull(material) && Objects.nonNull(minQuantity)) {
                // call the repository method that accepts a size, a material and a minimum quantity
                throw new QueryNotSupportedException(
                        "This query is not supported! Try a different " +
                                "combination of search parameters.");
            } else if(Objects.nonNull(material)) {
                // call the repository method that accepts a size and material
                throw new QueryNotSupportedException(
                        "This query is not supported! Try a different " +
                                "combination of search parameters.");
            } else if(Objects.nonNull(minQuantity)) {
                // call the repository method that accepts a size and a minimum quantity
                throw new QueryNotSupportedException(
                        "This query is not supported! Try a different " +
                                "combination of search parameters.");
            } else {
                // call the repository method that accepts a size
                return this.bootRepository.findBootsBySize(size);
            }
        } else if(Objects.nonNull(material)) {
            if(Objects.nonNull(minQuantity)) {
                // call the repository method that accepts a material and a minimum quantity
                throw new QueryNotSupportedException(
                        "This query is not supported! Try a different " +
                                "combination of search parameters.");
            } else {
                // call the repository method that accepts a material
                return this.bootRepository.findBootsByMaterial(material);
            }
        } else if(Objects.nonNull(minQuantity)) {
            // call the repository method that accepts a minimum quantity
            return this.bootRepository.findBootsByQuantityGreaterThan(minQuantity);
        } else {
            throw new QueryNotSupportedException(
                    "This query is not supported! Try a different " +
                            "combination of search parameters.");
        }
    }

}
