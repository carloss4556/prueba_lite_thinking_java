package com.example.demo.services;

import com.example.demo.models.Company;
import com.example.demo.repositories.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // ✅ Get all companies
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    // ✅ Get a company by ID (Returns Optional to prevent exceptions)
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    // ✅ Create a new company
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    // ✅ Update an existing company (If found)
    public Optional<Company> updateCompany(Long id, Company updatedCompany) {
        return companyRepository.findById(id).map(existingCompany -> {
            existingCompany.setName(updatedCompany.getName());
            existingCompany.setAddress(updatedCompany.getAddress());
            existingCompany.setPhone(updatedCompany.getPhone());
            return companyRepository.save(existingCompany);
        });
    }

    // ✅ Check if a company exists by ID
    public boolean companyExists(Long id) {
        return companyRepository.existsById(id);
    }

    // ✅ Delete a company (Only if it exists)
    public void deleteCompany(Long id) {
        if (!companyExists(id)) {
            throw new RuntimeException("Company with ID " + id + " does not exist");
        }
        companyRepository.deleteById(id);
    }
}
