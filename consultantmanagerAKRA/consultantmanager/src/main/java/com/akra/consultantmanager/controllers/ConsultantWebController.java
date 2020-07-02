package com.akra.consultantmanager.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.akra.consultantmanager.data.entities.Consultant;
import com.akra.consultantmanager.data.repositories.ConsultantRepository;
import com.akra.consultantmanager.services.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/consultant")
public class ConsultantWebController {

    @Autowired
    private final ConsultantRepository consultantRepository;

    @Autowired
    private final ConsultantService consultantService;

    public ConsultantWebController(ConsultantRepository consultantRepository, ConsultantService consultantService) {
        this.consultantRepository = consultantRepository;
        this.consultantService = consultantService;
    }


    @GetMapping
    public String index() {
        return "/consultant/index.html";
    }

    @RequestMapping(value = "/data_for_datatable", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        int length = params.containsKey("length") ? Integer.parseInt(params.get("length").toString()) : 30;
        int start = params.containsKey("start") ? Integer.parseInt(params.get("start").toString()) : 30;
        int currentPage = start / length;

        String sortName = "id";
        String dataTableOrderColumnIdx = params.get("order[0][column]").toString();
        String dataTableOrderColumnName = "columns[" + dataTableOrderColumnIdx + "][data]";
        if (params.containsKey(dataTableOrderColumnName))
            sortName = params.get(dataTableOrderColumnName).toString();
        String sortDir = params.containsKey("order[0][dir]") ? params.get("order[0][dir]").toString() : "asc";

        Sort.Order sortOrder = new Sort.Order((sortDir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC), sortName);
        Sort sort = Sort.by(sortOrder);

        Pageable pageRequest = PageRequest.of(currentPage,
                length,
                sort);

        String queryString = (String) (params.get("search[value]"));

        Page<Consultant> consultants = consultantService.getConsultantsForDatatable(queryString, pageRequest);

        long totalRecords = consultants.getTotalElements();

        List<Map<String, Object>> cells = new ArrayList<>();
        consultants.forEach(consultant -> {
            Map<String, Object> cellData = new HashMap<>();
            cellData.put("id", consultant.getId());
            cellData.put("firstName", consultant.getFirstName());
            cellData.put("lastName", consultant.getLastName());
            cellData.put("inProject", consultant.isInProject());
            cellData.put("technologies", consultant.getTechnologies());
            cells.add(cellData);
        });

        Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put("draw", draw);
        jsonMap.put("recordsTotal", totalRecords);
        jsonMap.put("recordsFiltered", totalRecords);
        jsonMap.put("data", cells);

        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        Consultant consultantInstance = consultantRepository.findById(Long.valueOf(id)).get();

        model.addAttribute("consultantInstance", consultantInstance);

        return "/consultant/edit.html";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("consultantInstance") Consultant consultantInstance,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes atts) {
        if (bindingResult.hasErrors()) {
            return "/consultant/edit.html";
        } else {
            if (consultantRepository.save(consultantInstance) != null)
                atts.addFlashAttribute("message", "Consultant updated successfully");
            else
                atts.addFlashAttribute("message", "Consultant update failed.");

            return "redirect:/consultant/";
        }
    }

    @GetMapping("/create")
    public String create(Model model)
    {
        model.addAttribute("consultantInstance", new Consultant());
        return "/consultant/create.html";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("consultantInstance") Consultant consultantInstance,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes atts) {
        if (bindingResult.hasErrors()) {
            return "/consultant/create.html";
        } else {
            if (consultantRepository.save(consultantInstance) != null)
                atts.addFlashAttribute("message", "Consultant created successfully");
            else
                atts.addFlashAttribute("message", "Consultant creation failed.");

            return "redirect:/consultant/";
        }
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes atts) {
        Consultant consultantInstance = consultantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consultant Not Found:" + id));

        consultantRepository.delete(consultantInstance);

        atts.addFlashAttribute("message", "Consultant deleted.");

        return "redirect:/consultant/";
    }

}
