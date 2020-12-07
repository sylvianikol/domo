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
    private static final String BASE_FEE = "5";

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
            if (this.feeRepository.findByStartDateAndApartment_Number(
                    LocalDate.now(), apartment.getNumber()) != null) {
                continue;
            }

            Fee fee = new Fee();
            fee.setStartDate(LocalDate.now());
            fee.setApartment(this.apartmentService.getByNumber(apartment.getNumber()));

            fee.setDueDate(LocalDate.now().plusMonths(1));
            fee.setPaid(false);

            BigDecimal total = new BigDecimal(BASE_FEE);

            int residentsCount = apartment.getResidents().size();

            if (residentsCount > 0) {
                total = total.multiply(BigDecimal.valueOf(residentsCount));
            }

            if (apartment.getPets() > 0) {
                total = total.add(total.multiply(BigDecimal.valueOf(apartment.getPets())));
            }

            fee.setTotal(total);

            this.feeRepository.saveAndFlush(fee);

            FeeViewModel feeViewModel = this.modelMapper.map(fee, FeeViewModel.class);
            feeViewModels.add(feeViewModel);
        }

        return feeViewModels;
    }
}
