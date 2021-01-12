package com.syn.domo.common;

public class ValidationErrorMessages {

    public static final String ID_NULL = "ID can not be null!";
    public static final String ID_EMPTY = "ID can not by empty!";

    public static final String BUILDING_NAME_NULL =
            "Building name should not be null!";
    public static final String BUILDING_NAME_EMPTY =
            "Building name should not be empty!";
    public static final String BUILDING_NAME_INVALID_LENGTH =
            "Building name should not be more than 40 symbols long!";

    public static final String BUILDING_NAME_EXISTS =
            "Building with name \"%s\" already exists in \"%s\"!";

    public static final String NEIGHBOURHOOD_NULL =
            "Neighbourhood should not be null!";
    public static final String NEIGHBOURHOOD_EMPTY =
            "Neighbourhood should not be empty!";
    public static final String NEIGHBOURHOOD_INVALID_LENGTH =
            "Neighbourhood should not be more than 40 symbols long!";

    public static final String ADDRESS_NULL =
            "Address should not be null!";
    public static final String ADDRESS_EMPTY =
            "Address should not be empty!";
    public static final String ADDRESS_OCCUPIED =
            "Address \"%s\" is already occupied by another building!";

    public static final String FLOOR_NUMBER_NULL =
            "Floor number can not be null!";
    public static final String FLOOR_MIN_INVALID =
            "Floor can not be a negative number!";
    public static final String FLOOR_MAX_INVALID =
            "Floor number can not be more than 100!";
    public static final String FLOOR_INVALID =
            "Invalid floor number!";

    public static final String PASSWORD_INVALID =
            "Password should be minimum 3 and 30 characters long!";
    public static final String PASSWORD_NULL =
            "Password can not be null!";
    public static final String PASSWORD_EMPTY =
            "Password can not be empty!";


    public static final String EMAIL_INVALID =
            "Please enter a valid email address!";
    public static final String EMAIL_NULL =
            "Email can not be null!";
    public static final String EMAIL_EMPTY =
            "Email can not be empty!";
    public static final String EMAIL_ALREADY_USED =
            "Email %s is already used by another user!";

    public static final String PHONE_NULL =
            "Phone number can not be null!";
    public static final String PHONE_EMPTY =
            "Phone number can not be empty!";
    public static final String PHONE_INVALID =
            "Phone number should be in the following format: '0888123456' or '+359888123456'";
    public static final String PHONE_LENGTH =
            "Phone number should be no more than 20 characters long";

    public static final String FIRST_NAME_INVALID =
            "First name should be between 2 and 55 characters long!";
    public static final String FIRST_NAME_NULL =
            "First name can not be null!";
    public static final String FIRST_NAME_EMPTY =
            "First name can not be empty!";

    public static final String LAST_NAME_INVALID =
            "Last name should be between 2 and 55 characters long!";
    public static final String LAST_NAME_NULL =
            "Last name can not be null!";
    public static final String LAST_NAME_EMPTY =
            "Last name can not be empty!";

    public static final String APARTMENT_LENGTH_INVALID =
            "Apartment number should be between 1 and 10 characters long!";
    public static final String APARTMENT_INVALID_SYMBOLS =
            "Apartment number should start with a digit and " +
                    "can contain only digits and letters! ex. (10a)";
    public static final String APARTMENT_NUMBER_NULL =
            "Apartment number can not be null!";
    public static final String APARTMENT_NUMBER_EMPTY =
            "Apartment number can not be empty!";

    public static final String DATE_FUTURE =
            "Date can not be in the future!";

    public static final String PETS_NULL =
            "Pets can not be null!";
    public static final String PETS_MIN =
            "Pets' count can not be a negative number!";
    public static final String PETS_MAX =
            "Maximum number of pets allowed per apartment is 5!";

    public static final String JOB_NULL = "Job can not be null!";
    public static final String JOB_EMPTY = "Job can not be empty!";

    public static final String SALARY_NULL =
            "Salary can not be null!";
    public static final String SALARY_MIN =
            "Salary can not be a negative number!";

    public static final String PARENTS_NULL =
            "Parents should not be null!";

    public static final String ACTIVE_STATUS_NULL =
            "Activation status can not be null!";
}
