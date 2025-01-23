package com.assessment;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class BorrowerController {

    private final BorrowerRepository repository;

    BorrowerController(BorrowerRepository repository) {
        this.repository = repository;
    }


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/borrowers")
    List<Borrower> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/borrowers")
    Borrower newBorrower(@RequestBody Borrower new_borrower) {
        return repository.save(new_borrower);
    }

    // Single item

    @GetMapping("/borrowers/{id}")
    Borrower one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new BorrowerNotFoundException(id));
    }
}
