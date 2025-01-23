package com.assessment;

import org.springframework.data.jpa.repository.JpaRepository;

interface BorrowerRepository extends JpaRepository<Borrower, Long> {

}