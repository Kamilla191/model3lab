package org.ismailova.lab.service.impl;

import org.ismailova.lab.entity.Bank;
import org.ismailova.lab.service.BankService;
import org.ismailova.lab.utils.EntityMaps;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Реализация интерфейса {@link BankService} для управления банками.
 * <p>Этот класс предоставляет методы для создания, чтения, обновления и удаления информации о банке.</p>
 * <p>Все методы этого класса предполагается использовать для манипуляции данными о банке.</p>
 *
 * @see BankService
 */
public class BankServiceImpl implements BankService {
    private static final Random RANDOM = new Random();

    @Override
    public void create(long id, String name) {
        if (read(id) == null) {
            Bank bank = new Bank();
            bank.setId(id);
            bank.setName(name);
            int rating = RANDOM.nextInt(101);
            double totalMoney = (double) (Math.round((RANDOM.nextDouble() * 1_000_000) * 100)) / 100;
            bank.setRating(rating);
            bank.setTotalMoney(totalMoney);
            bank.setInterestRate(BigDecimal.valueOf(20).subtract(BigDecimal.valueOf(rating).multiply(BigDecimal.valueOf(0.2))).doubleValue());
            EntityMaps.bankMap.put(id, bank);
        }
    }

    @Override
    public Bank read(long id) {
        return EntityMaps.bankMap.get(id);
    }

    @Override
    public void update(long id, Bank bank) {
        if (read(id) != null && bank != null){
            EntityMaps.bankMap.replace(id, bank);
        }
    }

    @Override
    public void delete(long id) {
        Bank bank = read(id);
        if (bank != null) {
            EntityMaps.bankMap.remove(id);
        }
    }
}
