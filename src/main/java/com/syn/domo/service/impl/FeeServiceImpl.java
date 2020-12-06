package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Fee;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.view.FeeViewModel;
import com.syn.domo.repository.FeeRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.FeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FeeServiceImpl implements FeeService  {

    private final FeeRepository feeRepository;
    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;

    @Autowired
    public FeeServiceImpl(FeeRepository feeRepository, ApartmentService apartmentService, ModelMapper modelMapper) {
        this.feeRepository = feeRepository;
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<FeeViewModel> generateMonthlyFees() {

        Set<ApartmentServiceModel> apartments =
                this.apartmentService.getAllApartments();

        List<FeeViewModel> feeViewModels = new ArrayList<>();

        for (ApartmentServiceModel apartment : apartments) {
            Fee fee = new Fee();
            fee.setStartDate(LocalDate.now());
            fee.setDueDate(LocalDate.now().plusMonths(1));
            fee.setApartment(this.apartmentService.getByNumber(apartment.getNumber()));
            fee.setPaid(false);

            BigDecimal base = new BigDecimal(5);
            BigDecimal total = new BigDecimal(0);

            total = base.multiply(BigDecimal.valueOf(apartment.getResidents()));

            if (apartment.getPets() > 0) {
                total = total.add(base.multiply(BigDecimal.valueOf(apartment.getPets())));
            }

            fee.setTotal(total);

            System.out.println();
//            this.feeRepository.saveAndFlush(fee);

            FeeViewModel feeViewModel = this.modelMapper.map(fee, FeeViewModel.class);
            feeViewModels.add(feeViewModel);
        }

        return feeViewModels;
    }
}
