package com.akra.consultantmanager.data.repositories;

import com.akra.consultantmanager.data.entities.Consultant;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultantRepository extends PagingAndSortingRepository<Consultant, Long>, JpaSpecificationExecutor<Consultant> {
}
