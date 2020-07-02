package com.akra.consultantmanager.services;

import com.akra.consultantmanager.data.entities.Consultant;
import com.akra.consultantmanager.data.repositories.ConsultantRepository;
import com.akra.consultantmanager.data.specifications.ConsultantDatatableFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ConsultantService {

    private final ConsultantRepository consultantRepository;

    @Autowired
    public ConsultantService(ConsultantRepository consultantRepository) {
        this.consultantRepository = consultantRepository;
    }

    public Page<Consultant> getConsultantsForDatatable(String queryString, Pageable pageable) {

        ConsultantDatatableFilter consultantDatatableFilter = new ConsultantDatatableFilter(queryString);

        return consultantRepository.findAll(consultantDatatableFilter, pageable);
    }
}
