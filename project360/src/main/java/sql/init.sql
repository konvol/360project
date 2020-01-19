    CREATE DATABASE HY360;

    USE HY360;

    CREATE TABLE HY360.EMPLOYEE(
        EmpID int NOT NULL,
        Lastname varchar(20) NOT NULL,
        Firstname varchar(20) NOT NULL,
        Beg_date date NOT NULL,
        Addr varchar(50) NOT NULL,
        Bank_number varchar(20) NOT NULL,
        Bank_name varchar(20) NOT NULL,
        Phone varchar(20) NOT NULL,
        Department_name varchar(50) NOT NULL,
        Emp_type varchar(20) NOT NULL,
        Family_bonus float(10, 2) NOT NULL,
        PRIMARY KEY (EmpID)
    );

    CREATE TABLE HY360.PERM_ADMIN(
        EmpID int NOT NULL,
        Basic_income float(10, 2) not NULL,
        PRIMARY KEY (EmpID)
    );

    CREATE TABLE HY360.TEMP_ADMIN(
        EmpID int NOT NULL,
        End_date date NOT NULL,
        PRIMARY KEY (EmpID)
    );

    CREATE TABLE HY360.PERM_TEACHING(
        EmpID int NOT NULL,
        Basic_income float(10, 2) not NULL,
        Research_bonus float(10, 2) not NULL,
        PRIMARY KEY (EmpID)
    );

    CREATE TABLE HY360.TEMP_TEACHING(
        EmpID int NOT NULL,
        Library_bonus float(10, 2) not NULL,
        End_date date NOT NULL,
        PRIMARY KEY (EmpID)
    );

    CREATE TABLE HY360.TEMP_CONTRACT(
        ContractID int NOT NULL,
        EmpID int NOT NULL,
        Beg_date date NOT NULL,
        End_date date NOT NULL,
        Basic_income float(10, 2) not NULL,
        PRIMARY KEY (ContractID)
    );

    CREATE TABLE HY360.FAMILY_STATUS(
        EmpID int NOT NULL,
        Marriage_status boolean NOT NULL,
        Kids_number int NOT NULL,
        PRIMARY KEY (EmpID)
    );

    CREATE TABLE HY360.KIDS(
        KidID int NOT NULL,
        EmpID int NOT NULL,
        Age int NOT NULL,
        PRIMARY KEY (KidID)
    );

    CREATE TABLE HY360.PAYROLL(
        PayrollID int NOT NULL AUTO_INCREMENT,
        EmpID int NOT NULL,
        Basic_income float(10, 2) NOT NULL,
        Family_bonus float(10, 2) NOT NULL,
        Teaching_bonus float(10, 2) NOT NULL,
        Total float(10, 2) NOT NULL,
        Payment_date date NOT NULL,
        Bank_number varchar(20) NOT NULL,
        Bank_name varchar(20) NOT NULL,
        PRIMARY KEY (PayrollID)
    );

    CREATE TABLE HY360.DEPARTMENT(
        Department_name varchar(50) NOT NULL,
        Addr varchar(50) NOT NULL,
        EmpID int NOT NULL
    );
