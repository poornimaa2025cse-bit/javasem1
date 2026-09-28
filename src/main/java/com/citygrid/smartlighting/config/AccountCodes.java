package com.citygrid.smartlighting.config;

/**
 * Fixed account codes used by the seed data and by the services when they
 * need to look up "the Bank account" or "the Accounts Payable account" etc.
 * Keeping these as constants (instead of hard-coded strings scattered around)
 * makes the whole ledger logic easy to find and change in one place.
 */
public final class AccountCodes {

    private AccountCodes() {}

    public static final String CASH_BANK = "1000";
    public static final String ACCOUNTS_RECEIVABLE = "1010";
    public static final String LIGHTING_GRID_INFRASTRUCTURE = "1500";
    public static final String ACCOUNTS_PAYABLE = "2000";   // "Power Utility Payables" in the spec
    public static final String MUNICIPAL_GRID_SERVICE_FEE = "4000";
    public static final String ELECTRICITY_CONSUMPTION_EXPENSE = "5000";
    public static final String REPAIR_EXPENSES = "5100";
}
