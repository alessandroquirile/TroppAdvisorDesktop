package dao_interfaces;

import models.Account;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface AccountDAO {
    boolean login(Account account);
}
