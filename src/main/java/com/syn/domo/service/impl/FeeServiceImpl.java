package com.syn.domo.service.impl;

import com.syn.domo.model.binding.FeeAddBindingModel;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Fee;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.FeeServiceModel;
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
    public List<FeeServiceModel> generateMonthlyFees(FeeAddBindingModel feeAddBindingModel) {

        Set<ApartmentServiceModel> apartments =
                this.apartmentService.getAllApartmentsByBuildingId();

        List<FeeServiceModel> feeServiceModels = new ArrayList<>();

        for (ApartmentServiceModel apartment : apartments) {
            if (this.feeRepository.findByStartDateAndApartment_Number(
                    LocalDate.now(), apartment.getNumber()) != null) {
                continue;
            }

            Fee fee = this.modelMapper.map(feeAddBindingModel, Fee.class);
            fee.setStartDate(LocalDate.now());

            ApartmentServiceModel apartmentServiceModel =
                    this.apartmentService.getByNumber(apartment.getNumber());

            fee.setApartment(this.modelMapper.map(apartmentServiceModel, Apartment.class));

            fee.setDueDate(LocalDate.now().plusMonths(1));
            fee.setPaid(false);

            BigDecimal total = new BigDecimal(0);

            int residentsCount = apartment.getResidents().size();
            BigDecimal baseFee = fee.getBase();

            if (residentsCount > 0) {
                total = total.add(baseFee.multiply(BigDecimal.valueOf(residentsCount)));
            }

            int childrenCount = apartment.getChildren().size();

            if (childrenCount > 0) {
                total = total.add(baseFee.multiply(BigDecimal.valueOf(childrenCount)));
            }

            if (apartment.getPets() > 0) {
                total = total.add(total.multiply(BigDecimal.valueOf(apartment.getPets())));
            }

            fee.setTotal(total.add(baseFee));

            this.feeRepository.saveAndFlush(fee);

            FeeServiceModel feeServiceModel = this.modelMapper.map(fee, FeeServiceModel.class);
            feeServiceModels.add(feeServiceModel);
        }

        return feeServiceModels;
    }
}
