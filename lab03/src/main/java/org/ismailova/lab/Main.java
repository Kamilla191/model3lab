package org.ismailova.lab;

import org.ismailova.lab.entity.*;
import org.ismailova.lab.utils.EntityMaps;
import org.ismailova.lab.utils.GenerationEntities;
import org.ismailova.lab.entity.*;

import java.util.*;



public class Main {
    public static void main(String[] args) {
        GenerationEntities.generation();
        main_menu();
    }

    private static void main_menu() {
        boolean flag = true;
        while (flag) {
            System.out.print("""
                    Главное меню:
                    1. Вывод всех данных по банку.
                    2. Вывод всей информации по клиенту.
                    3. Получение кредита.
                    0. Завершить программу.
                    """);
            System.out.print("Введите номер пункта: ");
            switch (inputInteger()) {
                case 0 -> flag = false;
                case 1 -> menuBank();
                case 2 -> menuClient();
                case 3 -> menuGetCredit();
                default -> errorMessage();
            }
        }
    }

    /*****************************************************************************************************************************************/
    private static void menuGetCredit(){
        boolean flag = true;
        while (flag) {
            Map<Long, Bank> banks = EntityMaps.bankMap;
            List<Long> bankIds = new ArrayList<>();
            List<Long> arr = new ArrayList<>();
            System.out.println("\nМеню вывода всех данных по банку:");
            System.out.println(" 0. Вернуться назад.");
            int id = 0;
            for (Bank bank : banks.values()) {
                bankIds.add(bank.getId());
                arr.add((long) bank.getInterestRate());
                System.out.println("[" + ++id + "] " + bank.getName() + ".");
            }
            System.out.print("Введите [порядковый номер] банка: ");


            int input = inputInteger() - 1;
            if (input == -1) flag = false;
            else {
                if (0 <= input && input < banks.size()) outputOfBankCredit(banks.get(bankIds.get(input)), arr);
                else errorMessage();
            }
        }
    }
    private static double calculateAverage(List<Long> marks) {
        Long sum = 0L;
        if(!marks.isEmpty()) {
            for (Long mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }
    private static void outputOfBankCredit(Bank bank, List arr) {
        Map<Long, BankOffice> bankOfficeMap = bank.getBankOfficeMap();
        Map<Long, BankAtm> bankAtmMap = new HashMap<>();
        Map<Long, Employee> employeeMap = bank.getEmployeeMap();
        System.out.println("Выбранный банк: " + bank.getName());
        /*OptionalDouble avarage_arr = arr.stream().mapToDouble(a -> (double) a).average();*/
        if ((bank.getBankOfficeMap().size() >= 2) && (bank.getEmployeeMap().size() >= 14) &&
                (bank.getBankOfficeMap().size() >= 3) && (bank.getInterestRate() <= calculateAverage(arr))){
            System.out.println("\nВыбранный банк соответствует условиям!\n\n");
            System.out.println("\nВведите сумму кредита:");

            double summ = inputInteger();

            System.out.println("Подходящие офисы: " + bankOfficeMap.size());
            for (BankOffice bankOffice : bankOfficeMap.values()) {
                if (summ < bankOffice.getRentCost()){
                    System.out.println(bankOffice.getName());
                    bankAtmMap.putAll(bankOffice.getBankAtmMap());
                }
                else {
                    System.out.println("Недостаточно средств для кредита");
                }
            }
            System.out.println("\nВыберете офис банка:");
            int chosed_office = inputInteger();
            chosen_office(bankOfficeMap.get(bankAtmMap.get(chosed_office)), bank);

            issueCredit(new User(), bank);

        }

    }
    private static void chosen_office(BankOffice bankOffice, Bank bank){
        Map<Long, Employee> employeeMap = bank.getEmployeeMap();
        for (Employee employee : employeeMap.values()) {
            if (employee.isCanIssueCredits()){
                System.out.print(employee.getLastName() + ' ');
                System.out.print(employee.getFirstName()+ ' ');
                System.out.print(employee.getPatronymic()+ ' ');
                break;
            }
        }

    }


        public static void issueCredit(User user, Bank bank) {
            Map<Long, CreditAccount> creditAccounts = user.getCreditAccounts();
            int clientCreditRating = user.getCreditRating();
            double bankRating = bank.getRating();
            // Проверяем наличие счета у клиента в банке
            if (creditAccounts.size() >= 0 && clientCreditRating < 5000 && bankRating > 50) {
                System.out.println("\nКредитный счет существует!\n");
                System.out.println("Кредит успешно выдан");
            }else {
                // Создаем счет для клиента в банке, если его нет
                System.out.println("\nСчет успешно создан для клиента в банке\n");
                System.out.println("\nОтказ в выдаче кредита: кредитный рейтинг клиента недостаточно высокий\n");
            }

        }

    public class CreditIssuingException extends Exception {
        public CreditIssuingException(String message) {
            super(message);
        }
    }




    /*****************************************************************************************************************************************/

    /**
     * Метод для работы с меню вывода данных по банку.
     */
    private static void menuBank() {
        boolean flag = true;
        while (flag) {
            Map<Long, Bank> banks = EntityMaps.bankMap;
            List<Long> bankIds = new ArrayList<>();
            System.out.println("\nМеню вывода всех данных по банку:");
            System.out.println(" 0. Вернуться назад.");
            int id = 0;
            for (Bank bank : banks.values()) {
                bankIds.add(bank.getId());
                System.out.println("[" + ++id + "] " + bank.getName() + ".");
            }
            System.out.print("Введите [порядковый номер] банка: ");

            int input = inputInteger() - 1;
            if (input == -1) flag = false;
            else {
                if (0 <= input && input < banks.size()) outputOfBank(banks.get(bankIds.get(input)));
                else errorMessage();
            }
        }
    }

    /**
     * Метод для вывода всех данных по банку.
     *
     * @param bank объект класса {@link Bank}
     */
    private static void outputOfBank(Bank bank) {
        Map<Long, BankOffice> bankOfficeMap = bank.getBankOfficeMap();
        Map<Long, BankAtm> bankAtmMap = new HashMap<>();
        System.out.println("Банк: " + bank);
        System.out.println("Офисы: " + bankOfficeMap.size());
        for (BankOffice bankOffice : bankOfficeMap.values()) {
            System.out.println(bankOffice);
            bankAtmMap.putAll(bankOffice.getBankAtmMap());
        }
        System.out.println("Банкоматы: " + bankAtmMap.size());
        for (BankAtm bankAtm : bankAtmMap.values()) System.out.println(bankAtm);

        Map<Long, Employee> employeeMap = bank.getEmployeeMap();
        System.out.println("Сотрудники: " + employeeMap.size());
        for (Employee employee : employeeMap.values()) System.out.println(employee);

        Map<Long, User> userMap = bank.getUserMap();
        System.out.println("Клиенты: " + userMap.size());
        for (User user : userMap.values()) System.out.println(user);
    }

    /**
     * Метод для работы с меню вывода информации по клиенту.
     */
    private static void menuClient() {
        boolean flag = true;
        while (flag) {
            System.out.println("\nМеню вывода всей информации по клиенту:");
            System.out.println(" 0. Вернуться назад.");
            Map<Long, User> userMap = new HashMap<>();
            List<Long> userIds = new ArrayList<>();
            int id = 0;
            for (Bank bank : EntityMaps.bankMap.values())
                for (User user : bank.getUserMap().values())
                    if (!userMap.containsKey(user.getId())) {
                        userIds.add(user.getId());
                        userMap.put(user.getId(), user);
                        System.out.println("[" + ++id + "] " + user);
                    }
            System.out.print("Введите [порядковый номер] клиента: ");
            int input = inputInteger() - 1;
            if (input == -1) flag = false;
            else {
                if (0 <= input && input < userMap.size()) outputOfClient(userMap.get(userIds.get(input)));
                else errorMessage();
            }
        }
    }

    /**
     * Метод для вывода всей информации по клиенту.
     *
     * @param user объект класса {@link User}
     */
    private static void outputOfClient(User user) {
        System.out.println("\nКлиент: " + user.getLastName() + " " + user.getFirstName() + (!user.getPatronymic().trim().isEmpty() ? " " + user.getPatronymic() : "") +
                ", Дата рождения: " + user.getBirthDate().getDayOfMonth() + "." + user.getBirthDate().getMonthValue() + "." + user.getBirthDate().getYear() +
                ", Место работы: " + user.getWorkplace() + ", Ежемесячный доход: " + user.getMonthlyIncome() +
                ", Кредитный рейтинг: " + user.getCreditRating()
        );
        for (PaymentAccount paymentAccount : user.getPaymentAccounts().values()) {
            System.out.println("Платёжный счёт: ID: " + paymentAccount.getId() +
                    ", Банк: " + paymentAccount.getBank().getName() +
                    ", Баланс: " + paymentAccount.getBalance());
        }
        for (CreditAccount creditAccount : user.getCreditAccounts().values()) {
            System.out.println("Кредитный счёт: ID: " + creditAccount.getId() +
                    ", Банк: " + creditAccount.getBank().getName() +
                    ", Дата начала: " + creditAccount.getStartDate().getDayOfMonth() + "." + creditAccount.getStartDate().getMonthValue() + "." + creditAccount.getStartDate().getYear() +
                    ", Кол-во месяцев: " + creditAccount.getMonths() +
                    ", Дата окончания: " + creditAccount.getEndDate().getDayOfMonth() + "." + creditAccount.getEndDate().getMonthValue() + "." + creditAccount.getEndDate().getYear() +
                    ", Сумма кредита: " + creditAccount.getLoanAmount() +
                    ", Сумма ежемесячного платежа: " + creditAccount.getMonthlyPayment() +
                    ", Процентная ставка: " + creditAccount.getInterestRate() + "% годовых" +
                    ", ID платёжного счёта: " + creditAccount.getPaymentAccount().getId());
        }
    }

    /**
     * Метод для ввода целочисленного значения с консоли.
     *
     * @return введенное целочисленное значение
     */
    public static int inputInteger() {
        Scanner in = new Scanner(System.in);
        String strInput = in.nextLine();
        int result;
        try {
            result = Integer.parseInt(strInput);
        } catch (NumberFormatException e) {
            result = -1;
        }
        return result;
    }

    /**
     * Метод для вывода сообщения об ошибке ввода.
     */
    public static void errorMessage() {
        System.out.println("Введено неверное значение!");
    }

    /**********************************************************************************************************************************************/

}