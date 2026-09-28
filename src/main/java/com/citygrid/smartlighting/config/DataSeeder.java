package com.citygrid.smartlighting.config;

import com.citygrid.smartlighting.entity.*;
import com.citygrid.smartlighting.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Runs automatically every time the app starts and loads demo master data,
 * so you can hit the API immediately without manually creating contacts,
 * products, accounts, zones and poles first.
 *
 * Because application.properties uses an in-memory H2 database, this data
 * resets every time you restart the app - which is exactly what you want
 * for a clean, repeatable viva/demo run.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final ContactRepository contactRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final JournalRepository journalRepository;
    private final AnalyticAccountRepository analyticAccountRepository;
    private final StreetLightRepository streetLightRepository;

    public DataSeeder(ContactRepository contactRepository,
                       ProductRepository productRepository,
                       AccountRepository accountRepository,
                       JournalRepository journalRepository,
                       AnalyticAccountRepository analyticAccountRepository,
                       StreetLightRepository streetLightRepository) {
        this.contactRepository = contactRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
        this.journalRepository = journalRepository;
        this.analyticAccountRepository = analyticAccountRepository;
        this.streetLightRepository = streetLightRepository;
    }

    @Override
    public void run(String... args) {

        // ---------- Chart of Accounts ----------
        accountRepository.save(new Account(AccountCodes.CASH_BANK, "Cash / Bank", AccountType.ASSET));
        accountRepository.save(new Account(AccountCodes.ACCOUNTS_RECEIVABLE, "Accounts Receivable", AccountType.ASSET));
        accountRepository.save(new Account(AccountCodes.LIGHTING_GRID_INFRASTRUCTURE, "Lighting Grid Infrastructure", AccountType.ASSET));
        accountRepository.save(new Account(AccountCodes.ACCOUNTS_PAYABLE, "Power Utility Payables", AccountType.LIABILITY));
        accountRepository.save(new Account(AccountCodes.MUNICIPAL_GRID_SERVICE_FEE, "Municipal Grid Service Fee", AccountType.INCOME));
        accountRepository.save(new Account(AccountCodes.ELECTRICITY_CONSUMPTION_EXPENSE, "Electricity Consumption Expense", AccountType.EXPENSE));
        accountRepository.save(new Account(AccountCodes.REPAIR_EXPENSES, "Repair Expenses", AccountType.EXPENSE));

        // ---------- Journals ----------
        journalRepository.save(new Journal("Purchase Journal", JournalType.PURCHASE));
        journalRepository.save(new Journal("Sales Journal", JournalType.SALES));
        journalRepository.save(new Journal("Bank Journal", JournalType.BANK));

        // ---------- Contacts ----------
        contactRepository.save(new Contact("City Power Utility Co.", ContactType.VENDOR,
                "billing@citypower.example", "+91-9000000001", "12 Grid Road"));
        contactRepository.save(new Contact("BrightFix Maintenance Contractors", ContactType.VENDOR,
                "ops@brightfix.example", "+91-9000000002", "45 Service Lane"));
        contactRepository.save(new Contact("City Municipal Corporation", ContactType.CUSTOMER,
                "accounts@citymunicipal.example", "+91-9000000003", "1 Civic Center"));

        // ---------- Products ----------
        productRepository.save(new Product("Smart LED Pole Assembly", ProductType.GOODS, 15000.0));
        productRepository.save(new Product("Grid Electricity kWh", ProductType.GOODS, 8.0));
        productRepository.save(new Product("Repair Maintenance Service", ProductType.SERVICE, 2500.0));

        // ---------- Zones (Analytic Accounts) ----------
        AnalyticAccount zone7 = analyticAccountRepository.save(new AnalyticAccount("Highway Zone 7 Smart Grid"));
        AnalyticAccount zone3 = analyticAccountRepository.save(new AnalyticAccount("Downtown Zone 3 Smart Grid"));

        // ---------- Sample poles ----------
        streetLightRepository.save(buildPole("HZ7-POLE-001", zone7));
        streetLightRepository.save(buildPole("HZ7-POLE-002", zone7));
        streetLightRepository.save(buildPole("DZ3-POLE-001", zone3));

        System.out.println("=================================================================");
        System.out.println(" Smart Lighting ERP demo data loaded.");
        System.out.println(" Try: GET http://localhost:8080/api/contacts");
        System.out.println("      GET http://localhost:8080/api/street-lights");
        System.out.println("      GET http://localhost:8080/api/street-lights/power-summary");
        System.out.println(" See README.md for a full step-by-step demo walkthrough.");
        System.out.println("=================================================================");
    }

    private StreetLight buildPole(String code, AnalyticAccount zone) {
        StreetLight light = new StreetLight();
        light.setPoleCode(code);
        light.setAnalyticAccount(zone);
        light.setDimmingPercent(100);
        light.setPowerDrawWatts(120.0);
        light.setAmbientLightLux(5.0);
        light.setNightActive(true);
        return light;
    }
}
